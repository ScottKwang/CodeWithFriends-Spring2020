import 'dart:ui';
import 'package:flutter/cupertino.dart';
import 'package:t_rex_game/t_rex_game.dart';
import 'package:flame/sprite.dart';
import 'dart:math';

class Tree {
  TRexGame game;
  Sprite treeSprite;
  Rect treeRect;

  Tree({this.game}) {
    List<String> trees = [
      "tree/tree_1.png",
      "tree/tree_2.png",
      "tree/tree_3.png",
      "tree/tree_4.png",
      "tree/tree_5.png",
      "tree/tree_6.png",
      "tree/tree_7.png",
      "tree/tree_8.png",
      "tree/tree_9.png"
    ];
    double tileSize = game.tileSize;
    Map<String, dynamic> treeDimension = {
      "tree/tree_1.png": [2 * tileSize, (2 * tileSize) * (201 / 386)],
      "tree/tree_2.png": [2 * tileSize, (2 * tileSize) * (307 / 446)],
      "tree/tree_3.png": [2 * tileSize, (2 * tileSize) * (307 / 446)],
      "tree/tree_4.png": [2 * tileSize, (2 * tileSize) * (307 / 446)],
      "tree/tree_5.png": [2 * tileSize, (2 * tileSize) * (307 / 446)],
      "tree/tree_6.png": [2 * tileSize, (2 * tileSize) * (205 / 495)],
      "tree/tree_7.png": [2 * tileSize, (2 * tileSize) * (307 / 513)],
      "tree/tree_8.png": [2 * tileSize, (2 * tileSize) * (603 / 518)],
      "tree/tree_9.png": [2 * tileSize, (2 * tileSize) * (285 / 495)],
    };
    String randomTreeName = trees[Random().nextInt(trees.length)];
    treeSprite = Sprite(randomTreeName);
    treeRect = Rect.fromLTWH(
        game.screenSize.width,
        game.screenSize.height - 3.5 * game.tileSize,
        treeDimension[randomTreeName][1],
        treeDimension[randomTreeName][0]);
  }

  void update(double t, speed) {
    treeRect = treeRect.translate(-speed, 0);
  }

  void render(Canvas canvas) {
    treeSprite.renderRect(canvas, treeRect);
  }
}
