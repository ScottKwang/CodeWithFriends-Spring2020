import 'dart:ui';
import 'package:flutter/cupertino.dart';
import 'package:t_rex_game/t_rex_game.dart';
import 'package:flame/sprite.dart';
import 'dart:math';

class Cloud {
  TRexGame game;
  Sprite cloudSprite;
  Rect cloudRect;

  Cloud({this.game}) {
    List<String> clouds = [
      "cloud/cloud_1.png",
      "cloud/cloud_2.png",
      "cloud/cloud_3.png",
      "cloud/cloud_4.png",
    ];
    double tileSize = game.tileSize;
    Map<String, dynamic> cloudDimension = {
      "cloud/cloud_1.png": [1.5 * tileSize, (1.5 * tileSize) * (420 / 140)],
      "cloud/cloud_2.png": [1.5 * tileSize, (1.5 * tileSize) * (311 / 71)],
      "cloud/cloud_3.png": [1.5 * tileSize, (1.5 * tileSize) * (455 / 119)],
      "cloud/cloud_4.png": [1.5 * tileSize, (1.5 * tileSize) * (300 / 100)]
    };
    String randomCloudName = clouds[Random().nextInt(clouds.length)];
    cloudSprite = Sprite(randomCloudName);
    cloudRect = Rect.fromLTWH(
        game.screenSize.width,
        game.screenSize.height - 8 * game.tileSize - Random().nextDouble() * 10,
        cloudDimension[randomCloudName][1],
        cloudDimension[randomCloudName][0]);
  }

  void update(double t) {
    cloudRect = cloudRect.translate(-1, 0);
  }

  void render(Canvas canvas) {
    cloudSprite.renderRect(canvas, cloudRect);
  }
}
