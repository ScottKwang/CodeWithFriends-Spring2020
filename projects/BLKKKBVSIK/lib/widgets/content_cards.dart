import 'dart:math';
import 'package:audioplayers/audio_cache.dart';
import 'package:audioplayers/audioplayers.dart';
import 'package:flutter/material.dart';
import 'package:relaxing_rain/kConstant.dart';
import 'dart:io';
import 'package:relaxing_rain/widgets/custom_slider_thumb.dart';
import 'package:wiredash/wiredash.dart';

class ContentCards extends StatefulWidget {
  int selectedIndex;
  Color bgColor;
  String assetName;
  AudioPlayer fixedPlayer;
  AudioCache audioCache;

  ContentCards({this.selectedIndex, this.bgColor, this.assetName, this.audioCache, this.fixedPlayer});

  @override
  _ContentCardsState createState() => _ContentCardsState();
}

class _ContentCardsState extends State<ContentCards> {
  double volumeValue = 70.0;

  @override
  Widget build(BuildContext context) {

    return Expanded(
      child: Column(
        children: <Widget>[
          Container(
            width: MediaQuery.of(context).size.width,
            padding: EdgeInsets.all(20),
            child: Hero(
              tag: 'picto',
              child: Image.asset(
                'assets/picto.png',
                height: MediaQuery.of(context).size.height * .25,
                width: MediaQuery.of(context).size.width * .25,
              ),
            ),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.end,
            children: [
              InkWell(
                onTap: () {
                  Wiredash.of(context).show();
                },
                child: Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 20),
                  child: Text("Report a bug", style: TextStyle(color: widget.bgColor.withOpacity(1)),),
                ),
              ),
            ],
          ),
          Expanded(
            child: Container(
              height: MediaQuery.of(context).size.height,
              width: MediaQuery.of(context).size.width,
              decoration: BoxDecoration(
                borderRadius: BorderRadius.only(topLeft: Radius.circular(30)),
                color: widget.bgColor,
              ),
              child: SingleChildScrollView(
                child: Container(
                  height: MediaQuery.of(context).size.height * .64,
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: <Widget>[
                      Text(
                        finalSentences[widget.selectedIndex],
                        style: TextStyle(color: Colors.white, fontSize: 30),
                      ),
                      InkWell(
                        onTap: () {
                          widget.fixedPlayer.state == AudioPlayerState.PLAYING
                              ? pauseLocal()
                              : playLocal();
                        },
                        child: widget.fixedPlayer.state == AudioPlayerState.PLAYING
                            ? Icon(
                                Icons.pause_circle_outline,
                                color: Colors.white,
                                size: iconRightSize(),
                              )
                            : Icon(
                                Icons.play_circle_outline,
                                color: Colors.white,
                                size: iconRightSize(),
                              ),
                      ),
                      volumeSlider(),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  double iconRightSize() {
    if (MediaQuery.of(context).orientation == Orientation.landscape ||
        Platform.isMacOS ||
        Platform.isWindows ||
        Platform.isLinux) {
      return MediaQuery.of(context).size.width * .1;
    } else {
      return MediaQuery.of(context).size.width * .4;
    }
  }

  playLocal() async {
    if (widget.fixedPlayer.state == AudioPlayerState.PAUSED) {
      widget.fixedPlayer.resume();
    } else {
      widget.fixedPlayer =
          await widget.audioCache.loop(widget.assetName, volume: 0.7, stayAwake: true);
    }
  }

  pauseLocal() async {
    widget.fixedPlayer.pause();
  }

  Widget volumeSlider() {
    return Container(
      width: (48) * 5.5,
      height: (48),
      decoration: new BoxDecoration(
        borderRadius: new BorderRadius.all(
          Radius.circular((48 * .3)),
        ),
        gradient: new LinearGradient(
            colors: [
              const Color(0xFF00c6ff),
              const Color(0xFF0072ff),
            ],
            begin: const FractionalOffset(0.0, 0.0),
            end: const FractionalOffset(1.0, 1.00),
            stops: [0.0, 1.0],
            tileMode: TileMode.clamp),
      ),
      child: Padding(
        padding: EdgeInsets.fromLTRB(48 * .2, 2, 48 * .2, 2),
        child: Row(
          children: <Widget>[
            Text(
              '0',
              textAlign: TextAlign.center,
              style: TextStyle(
                fontSize: 48 * .3,
                fontWeight: FontWeight.w700,
                color: Colors.white,
              ),
            ),
            SizedBox(
              width: 48 * .1,
            ),
            Expanded(
              child: Center(
                child: SliderTheme(
                  data: SliderTheme.of(context).copyWith(
                    activeTrackColor: Colors.white.withOpacity(1),
                    inactiveTrackColor: Colors.white.withOpacity(.5),
                    trackHeight: 4.0,
                    thumbShape: CustomSliderThumbCircle(
                      thumbRadius: 48 * .4,
                      min: 0,
                      max: 100,
                    ),
                    overlayColor: Colors.white.withOpacity(.4),
                    //valueIndicatorColor: Colors.white,
                    activeTickMarkColor: Colors.white,
                    inactiveTickMarkColor: Colors.red.withOpacity(.7),
                  ),
                  child: Slider(
                      value: volumeValue,
                      max: 100,
                      min: 0,
                      onChanged: (value) {
                        setState(() {
                          volumeValue = value;
                        });
                        widget.fixedPlayer.setVolume(value / 100);
                      }),
                ),
              ),
            ),
            SizedBox(
              width: 48 * .1,
            ),
            Text(
              '100',
              textAlign: TextAlign.center,
              style: TextStyle(
                fontSize: 48 * .3,
                fontWeight: FontWeight.w700,
                color: Colors.white,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
