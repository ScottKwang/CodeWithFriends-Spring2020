import 'dart:async';

import 'package:flame/components/text_component.dart';
import 'package:flame/game.dart';
import 'dart:ui';
import 'package:flame/flame.dart';
import 'package:flame/sprite.dart';
import 'package:flutter/cupertino.dart';
import 'package:flame/gestures.dart';
import 'components/grass.dart';
import 'dart:math';
import 'package:flame/flare_animation.dart';
import 'components/cloud.dart';
import 'components/tree.dart';
import 'package:flame/text_config.dart';
import 'package:flutter/material.dart';
import 'package:flame/position.dart';
import 'package:flame/flame.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:noise_meter/noise_meter.dart';

class TRexGame extends Game with TapDetector {
  Size screenSize;
  double tileSize;
  Sprite bgSprite;
  Sprite mouthOpen;
  Sprite mouthClosed;
  bool isVoiceEnabled = false;
  double gameSpeed = 5.5;
  List<Grass> grasses = [];
  List<Cloud> clouds = [];
  List<Tree> trees = [];
  FlareAnimation tRex;
  bool animationLoaded = false;
  double dinoX;
  double dinoY;
  double referenceY;
  bool jumpUp = false;
  bool jumpDown = false;
  double mulFactor;
  bool tapped = false;
  TextConfig config;
  int highScore;
  int currentScore = 0;
  bool isCollisionHappened = false;
  double relaxation;
  bool isHitSoundPlayable = true;
  SharedPreferences score;
  bool _isRecording = false;
  StreamSubscription<NoiseReading> _noiseSubscription;
  NoiseMeter _noiseMeter = NoiseMeter();

  TRexGame({this.screenSize, this.tileSize, this.score}) {
    bgSprite = Sprite("bg/ground.png");
    mouthOpen = Sprite("bg/mouth_open.png");
    mouthClosed = Sprite("bg/mouth_closed.png");
    initialize();
  }

  void start() async {
    try {
      _noiseSubscription = _noiseMeter.noiseStream.listen(onData);
    } catch (exception) {
      print(exception);
    }
  }

  void onData(NoiseReading noiseReading) {
    if (!_isRecording) {
      _isRecording = true;
    }

    /// Do someting with the noiseReading object
    //print(noiseReading.toString());
    if (noiseReading.maxDecibel >= 85.0) {
      tapOccurred();
    }
  }

  void stopRecorder() async {
    try {
      if (_noiseSubscription != null) {
        _noiseSubscription.cancel();
        _noiseSubscription = null;
      }
      _isRecording = false;
    } catch (err) {
      print('stopRecorder error: $err');
    }
  }

  void initialize() async {
    //score = await SharedPreferences.getInstance();
    highScore = score.getInt('dinoScore') ?? 0;
    //resize(await Flame.util.initialDimensions());
    print(screenSize);
    relaxation = tileSize * 0.6;
    dinoX = tileSize;
    dinoY = screenSize.height - tileSize * 4;
    referenceY = dinoY;
    mulFactor = tileSize * 0.2;
    grasses.add(Grass(game: this));
    clouds.add(Cloud(game: this));
    spawnTree();
    tRex = await FlareAnimation.load("assets/flare_files/t_rex_game.flr");
    // animation names
    //t-rex-run
    //jump-up
    //jump-down
    //sad-face
    tRex.updateAnimation("t-rex-run");
    tRex.width = tileSize * 2;
    tRex.height = tileSize * 2;
    animationLoaded = true;
  }

  void spawnGrass() {
    int rnd = Random().nextInt(50);
    if (rnd == 2) {
      grasses.add(Grass(game: this));
    }
  }

  void spawnCloud() {
    int rnd = Random().nextInt(50);
    if (rnd == 2) {
      clouds.add(Cloud(game: this));
    }
  }

  void spawnTree() {
    int rnd = Random().nextInt(40);
    if (rnd == 2) {
      trees.add(Tree(game: this));
    }
  }

  void resetGame() {
    trees.clear();
    clouds.clear();
    grasses.clear();
    currentScore = 0;
    gameSpeed = 4.5;
    tRex.updateAnimation("t-rex-run");
  }

  @override
  void resize(Size size) {
    print("resize gets called. After resize: " + size.toString());
    screenSize = size;
    tileSize = size.height / 9;
  }

  @override
  void update(double t) {
    // score handling
    if (currentScore > highScore) {
      highScore = currentScore;
    }
    if (currentScore == 999999) {
      currentScore = 0;
      gameSpeed = 5.5;
    }
    //speed handling
    //print("game speed " + gameSpeed.toString());
    // check for multiple for 100 score
    if (currentScore != 0 && currentScore % 1000 == 0) {
      if (!isVoiceEnabled) {
        Flame.audio.play('score-reached.mp3');
      }
    }
    // check for collision detection
    trees.forEach((element) {
      if ((element.treeRect.left > dinoX + relaxation &&
              element.treeRect.left < dinoX + 2 * tileSize - relaxation) ||
          (element.treeRect.right > dinoX + relaxation &&
              element.treeRect.left < dinoX + 2 * tileSize - relaxation)) {
        if (dinoY + 2 * tileSize > screenSize.height - 3.5 * tileSize) {
          //collision happened
          if (isHitSoundPlayable) {
            Flame.audio.play('hit.mp3');
            isHitSoundPlayable = false;
          }
          //print("collision happened");
          if (currentScore >= highScore) {
            score.setInt("dinoScore", currentScore);
          }
          isCollisionHappened = true;
        } else {
          //print("no collision");
          //print(dinoY);
          //print(screenSize.height - 3.5 * tileSize);
        }
      }
    });
    if (!isCollisionHappened) {
      currentScore += 1;
      gameSpeed = [gameSpeed + 0.0005, 13].reduce(min);
      // handling grass
      if (grasses.length > 0) {
        // step-1: update the grass position
        grasses.forEach((element) {
          element.update(t, gameSpeed);
        });
        // check if the last grass the more than 1/3 left to the screen width
        // then spawn new one
        if (grasses.last.grassRect.right < screenSize.width * (2 / 3)) {
          spawnGrass();
        }
        // delete grass if it left the screen
        grasses.removeWhere((element) {
          if (element.grassRect.right < 0) {
            //print("t-rex-game.dart: removing grass, releasing memory");
            return true;
          } else {
            return false;
          }
        });
      } else {
        spawnGrass();
      }

      // handling cloud
      if (clouds.length > 0) {
        // step-1: update the cloud position
        clouds.forEach((element) {
          element.update(t);
        });
        // check if the last grass the more than 1/3 left to the screen width
        // then spawn new one
        if (clouds.last.cloudRect.right < screenSize.width * (2 / 3)) {
          spawnCloud();
        }
        // delete cloud if it left the screen
        clouds.removeWhere((element) {
          if (element.cloudRect.right < 0) {
            //print("t-rex-game.dart: removing grass, releasing memory");
            return true;
          } else {
            return false;
          }
        });
      } else {
        spawnCloud();
      }

      // handling trees
      if (trees.length > 0) {
        // step-1: update the cloud position
        trees.forEach((element) {
          element.update(t, gameSpeed);
        });
        // check if the last grass the more than 1/3 left to the screen width
        // then spawn new one
        if (trees.last.treeRect.right < screenSize.width * (1 / 2)) {
          spawnTree();
        }
        // delete cloud if it left the screen
        trees.removeWhere((element) {
          if (element.treeRect.right < 0) {
            //print("t-rex-game.dart: removing grass, releasing memory");
            return true;
          } else {
            return false;
          }
        });
      } else {
        spawnTree();
      }

      // handling dino
//      tRex.width = tileSize * 2;
//      tRex.height = tileSize * 2;
      if (animationLoaded) {
        tRex.update((gameSpeed / 2) * t);
      }
      if (jumpUp) {
        if (dinoY < referenceY - 3.5 * tileSize) {
          // get the dino down
          jumpUp = false;
          jumpDown = true;
          mulFactor = 0.2;
          tRex.updateAnimation("jump-down");
        } else {
          // push the dino up
          dinoY -= mulFactor;
          if (mulFactor > tileSize * 0.2) {
            mulFactor *= 0.7;
          }
        }
      }
      if (jumpDown) {
        if (dinoY > referenceY) {
          dinoY = referenceY;
          jumpDown = false;
          mulFactor = tileSize * 0.2;
          tRex.updateAnimation("t-rex-run");
          tapped = false;
        } else {
          // push the dino down
          dinoY += mulFactor;
          mulFactor *= 1.2;
        }
      }
    } else {
      // handle collision scenario
      // handling dino
      if (animationLoaded) {
        tRex.update((gameSpeed / 2) * t);
      }
      if (jumpUp) {
        if (dinoY < referenceY - 3.5 * tileSize) {
          // get the dino down
          jumpUp = false;
          jumpDown = true;
          mulFactor = 0.2;
          tRex.updateAnimation("jump-down");
        } else {
          // push the dino up
          dinoY -= mulFactor;
          if (mulFactor > tileSize * 0.2) {
            mulFactor *= 0.7;
          }
        }
      }
      if (jumpDown) {
        if (dinoY > referenceY) {
          dinoY = referenceY;
          jumpDown = false;
          mulFactor = tileSize * 0.2;
          tRex.updateAnimation("sad-face");
          tapped = false;
        } else {
          // push the dino down
          dinoY += mulFactor;
          mulFactor *= 1.2;
        }
      } else {
        tRex.updateAnimation("sad-face");
      }
    }
  }

  @override
  void render(Canvas canvas) {
    // step-1: Draw the sky
    Rect sky = Rect.fromLTWH(0, 0, screenSize.width, screenSize.height);
    Paint skyPaint = Paint()..color = Color(0xffb4cefe);
    canvas.drawRect(sky, skyPaint);

    // step-2: Draw the grass
    grasses.forEach((element) {
      element.render(canvas);
    });
    // Step-3: Draw the ground for T-REX
    // We will be drawing the ground 2 tiles up from the base of mobile
    // in landscape mode
    Rect bgRect = Rect.fromLTWH(
        0, (screenSize.height - 2 * tileSize), 2500, tileSize * 2);
    bgSprite.renderRect(canvas, bgRect);

    // drawing the cloud
    clouds.forEach((element) {
      element.render(canvas);
    });

    // drawing trees
    trees.forEach((element) {
      element.render(canvas);
    });

    // handling dino
    if (animationLoaded) {
      tRex.render(canvas, x: dinoX, y: dinoY);
    }

    // showing score
    config = TextConfig(fontSize: tileSize, fontFamily: 'pizzadudedotdk');
    if (config != null) {
      config.render(
          canvas,
          "HI ${highScore.toString().padLeft(6, "0")}  ${currentScore.toString().padLeft(6, "0")}",
          Position(screenSize.width / 2, 10));
    }

    // drawing mouth
    Rect mouth = Rect.fromLTWH(10, 10, 40, 40);
    if (isVoiceEnabled) {
      mouthOpen.renderRect(canvas, mouth);
    } else {
      mouthClosed.renderRect(canvas, mouth);
    }

    // handle collision scenario
    if (isCollisionHappened) {
      Rect collisionRect =
          Rect.fromLTWH(0, 0, screenSize.width, screenSize.height);
      Paint collisionPaint = Paint()..color = Colors.blueGrey.withOpacity(0.5);
      canvas.drawRect(collisionRect, collisionPaint);
      config.render(
          canvas,
          "GAME OVER",
          Position(screenSize.width / 2 - 2.25 * tileSize,
              screenSize.height / 2 - tileSize / 2));
    }
  }

  void tapOccurred() {
    if (!tapped && !isCollisionHappened) {
      Flame.audio.play('button-press.mp3');
      tapped = true;
      tRex.updateAnimation("jump-up");
      jumpUp = true;
    }
    if (isCollisionHappened) {
      isCollisionHappened = false;
      isHitSoundPlayable = true;
      resetGame();
    }
  }

  void onTapDown(TapDownDetails details) {
    double x = details.globalPosition.dx;
    double y = details.globalPosition.dy;

    if (x >= 10 && x <= 50 && y >= 10 && y <= 50) {
      if (isVoiceEnabled) {
        stopRecorder();
      } else {
        start();
      }
      isVoiceEnabled = !isVoiceEnabled;
      return;
    }
    tapOccurred();
  }
}
