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

class TRexGame extends Game with TapDetector {
  Size screenSize;
  double tileSize;
  Sprite bgSprite;
  double gameSpeed = 4.5;
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

  TRexGame() {
    bgSprite = Sprite("bg/ground.png");
    initialize();
  }

  void initialize() async {
    resize(await Flame.util.initialDimensions());
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

  @override
  void resize(Size size) {
    screenSize = size;
    tileSize = size.height / 9;
  }

  @override
  void update(double t) {
    gameSpeed += 0.001;
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
    if (animationLoaded) {
      tRex.update((gameSpeed / 2) * t);
    }
    if (jumpUp) {
      if (dinoY < referenceY - 3 * tileSize) {
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
  }

  void onTapDown(TapDownDetails details) {
    print(details.globalPosition);
    if (!tapped) {
      tapped = true;
      tRex.updateAnimation("jump-up");
      jumpUp = true;
    }
  }
}
