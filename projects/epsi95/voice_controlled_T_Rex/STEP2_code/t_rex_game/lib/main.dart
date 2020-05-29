import 'package:flutter/material.dart';
import 'package:flame/util.dart';
import 'package:flutter/services.dart';
import 't_rex_game.dart';
import 'package:flame/flame.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await Flame.images.loadAll(<String>[
    "bg/ground.png",
    "bg/grass_1.png",
    "bg/grass_2.png",
    "bg/grass_3.png",
    "bg/grass_4.png",
    "bg/grass_5.png",
    "bg/mouth_closed.png",
    "bg/mouth_open.png",
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
  Flame.audio.loadAll(["button-press.mp3", "hit.mp3", "score-reached.mp3"]);
  Util gameUtil = Util();
//  await gameUtil.setOrientation(DeviceOrientation.landscapeLeft);
//  await gameUtil.fullScreen();
  await Flame.util.fullScreen();
  await Flame.util.setLandscape();
  Size size = await Flame.util.initialDimensions();
  print("size first time-->>" + size.toString());
  while (size.width < size.height) {
    size = await Flame.util.initialDimensions();
  }
  print("size-->>" + size.toString());
  SharedPreferences score = await SharedPreferences.getInstance();

  TRexGame game =
      TRexGame(screenSize: size, tileSize: size.height / 9, score: score);
  runApp(game.widget);
}
