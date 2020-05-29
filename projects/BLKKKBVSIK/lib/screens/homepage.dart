import 'dart:math';

import 'package:audioplayers/audio_cache.dart';
import 'package:flutter/material.dart';
import 'package:flutter/scheduler.dart';
import 'package:relaxing_rain/widgets/content_cards.dart';
import 'package:relaxing_rain/widgets/custom_slider_thumb.dart';
import 'package:relaxing_rain/kConstant.dart';
import 'package:audioplayers/audioplayers.dart';

class Homepage extends StatefulWidget {
  static const routeName = "/homepage";

  @override
  _HomepageState createState() => _HomepageState();
}

class _HomepageState extends State<Homepage> {
  int _selectedIndex = 0;
  Ticker _ticker;
  static AudioPlayer fixedPlayer = AudioPlayer(mode: PlayerMode.MEDIA_PLAYER);
  AudioCache audioCache = AudioCache(fixedPlayer: fixedPlayer);
  bool playState = false;

  @override
  void dispose() {
    _ticker.dispose();
    super.dispose();
  }

  @override
  void initState() {
    _ticker = Ticker((d) {
      setState(() {});
    })
      ..start();
  }

  @override
  void didChangeDependencies() {
    //precacheImage(_blueImage.image, context);
    super.didChangeDependencies();
  }

  @override
  Widget build(BuildContext context) {
    var time = DateTime.now().millisecondsSinceEpoch / 2000;
    var scaleX = 1.2 + sin(time) * .11;
    var scaleY = 1.2 + cos(time) * .1;
    var offsetY = 20 + cos(time) * 20;

    return Scaffold(
      backgroundColor: kBlueishDye,
      body: Stack(
        alignment: Alignment.center,
        fit: StackFit.expand,
        children: <Widget>[
          Transform(
            transform: Matrix4.diagonal3Values(scaleX, scaleY, 1),
            child: Transform.translate(
              offset: Offset(
                  -(scaleX - 1) / 2 * MediaQuery.of(context).size.width,
                  -(scaleY - 1) / 2 * MediaQuery.of(context).size.height +
                      offsetY),
              child: Image.asset(
                'assets/background.png',
                fit: BoxFit.cover,
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(top: 30),
            child: Row(
              children: <Widget>[
                NavigationRail(
                  backgroundColor: Colors.transparent,
                  selectedIndex: _selectedIndex,
                  onDestinationSelected: (int index) {
                    fixedPlayer.stop();
                    fixedPlayer.state = AudioPlayerState.COMPLETED;
                    playState = false;
                    setState(() {
                      _selectedIndex = index;
                    });
                  },
                  labelType: NavigationRailLabelType.selected,
                  destinations: [
                    NavigationRailDestination(
                      icon: Icon(Icons.grain, color: Colors.white),
                      selectedIcon: Icon(Icons.grain, color: Colors.white),
                      label: Text(
                        'Rain',
                        style: TextStyle(color: Colors.white),
                      ),
                    ),
                    NavigationRailDestination(
                      icon: Icon(Icons.cloud, color: Colors.white),
                      selectedIcon: Icon(Icons.cloud, color: Colors.white),
                      label: Text(
                        'Thunder',
                        style: TextStyle(color: Colors.white),
                      ),
                    ),

                    NavigationRailDestination(
                      icon: Icon(Icons.clear_all, color: Colors.white),
                      selectedIcon: Icon(Icons.clear_all, color: Colors.white),
                      label: Text(
                        'Wind',
                        style: TextStyle(color: Colors.white),
                      ),
                    ),
                  ],
                ),
                getRightContentCards(_selectedIndex),
                //ContentCards(selectedIndex: _selectedIndex,),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget getRightContentCards(int selectedIndex) {
    switch (selectedIndex) {
      case 0:
        return ContentCards(
          selectedIndex: _selectedIndex,
          bgColor: kBlueCardBackground,
          audioCache: audioCache,
          fixedPlayer: fixedPlayer,
          assetName: "rain.mp3",
        );
        break;
      case 1:
        return ContentCards(
          selectedIndex: _selectedIndex,
          bgColor: kBlueishDye,
          audioCache: audioCache,
          fixedPlayer: fixedPlayer,
          assetName: "thunder.mp3",
        );
        break;
      case 2:
        return ContentCards(
          selectedIndex: _selectedIndex,
          bgColor: kGreenAlgua,
          audioCache: audioCache,
          fixedPlayer: fixedPlayer,
          assetName: "wind.mp3",
        );
        break;
      default:
        return Container();
        break;
    }
  }
}
