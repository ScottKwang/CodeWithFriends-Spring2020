import 'package:flutter/material.dart';
import 'package:flame/util.dart';
import 'package:flutter/services.dart';
import 't_rex_game.dart';
import 'package:flame/flame.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await Flame.images.loadAll(<String>[
    "bg/ground.png",
    "bg/grass_1.png",
    "bg/grass_2.png",
    "bg/grass_3.png",
    "bg/grass_4.png",
    "bg/grass_5.png",
    "cloud/cloud_1.png",
    "cloud/cloud_2.png",
    "cloud/cloud_3.png",
    "cloud/cloud_4.png",
    "tree/tree_1.png",
    "tree/tree_2.png",
    "tree/tree_3.png",
    "tree/tree_4.png",
    "tree/tree_5.png",
    "tree/tree_6.png",
    "tree/tree_7.png",
    "tree/tree_8.png",
    "tree/tree_9.png",
  ]);
  Util gameUtil = Util();
  await gameUtil.setOrientation(DeviceOrientation.landscapeLeft);
  await gameUtil.fullScreen();

  TRexGame game = TRexGame();
  runApp(game.widget);
}
