import 'dart:ui';
import 'package:flutter/cupertino.dart';
import 'package:t_rex_game/t_rex_game.dart';
import 'package:flame/sprite.dart';
import 'dart:math';

class Grass {
  TRexGame game;
  Sprite grassSprite;
  Rect grassRect;

  Grass({this.game}) {
    List<String> grasses = [
      'bg/grass_1.png',
      'bg/grass_2.png',
      'bg/grass_3.png',
      'bg/grass_4.png',
      "bg/grass_5.png"
    ];
    grassSprite = Sprite(grasses[Random().nextInt(grasses.length)]);
    grassRect = Rect.fromLTWH(
        game.screenSize.width,
        game.screenSize.height - 2.36 * game.tileSize,
        game.tileSize * 0.4,
        game.tileSize * 0.4);
  }

  void update(double t, double speed) {
    //print("grass.dart: update method gets called with speed $speed");
    grassRect = grassRect.translate(-speed, 0);
//    print(grassRect.right);
  }

  void render(Canvas canvas) {
    grassSprite.renderRect(canvas, grassRect);
  }
}
