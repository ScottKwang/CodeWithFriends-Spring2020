import 'package:device_preview/device_preview.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:relaxing_rain/kConstant.dart';
import 'package:relaxing_rain/kSecrets.dart';
import 'package:relaxing_rain/screens/homepage.dart';
import 'package:wiredash/wiredash.dart';

void main() => runApp(
      DevicePreview(
        enabled: false, //!kReleaseMode,
        builder: (context) => MyApp(),
      ),
    );

class MyApp extends StatelessWidget {
  final _navigatorKey = GlobalKey<NavigatorState>();
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    var routeName;
    return Wiredash(
      projectId: kProjectId,
      secret: kSecretId,
      navigatorKey: _navigatorKey,
      theme: WiredashThemeData(
        primaryColor: kBlueCardBackground.withOpacity(.6),
        secondaryColor: kBlueCardBackground.withOpacity(.9),
        backgroundColor: kBlueCardBackground.withOpacity(1),
      ),
      options: WiredashOptionsData(
        showDebugFloatingEntryPoint: false,
      ),
      child: MaterialApp(
        navigatorKey: _navigatorKey,
        locale: DevicePreview.of(context).locale, // <--- Add the locale
        builder: DevicePreview.appBuilder, // <--- Add the builder
        title: 'Relaxing Rain',
        debugShowCheckedModeBanner: false,
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        home: MyHomePage(),
        routes: {
          Homepage.routeName: (BuildContext ctx) => Homepage(),
        },
      ),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage();

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        padding: EdgeInsets.all(20),
        color: kBlueBackground,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            Flexible(
              child: Container(
                height: MediaQuery.of(context).size.width * 0.60,
                child: Hero(
                  tag: 'picto',
                  child: Image.asset('assets/picto.png'),
                ),
              ),
            ),
            Center(
                child: RichText(
              text: TextSpan(
                children: [
                  TextSpan(
                      text: "Relaxing",
                      style: TextStyle(color: Colors.white, fontSize: 40.0)),
                  TextSpan(
                      text: "Rain.",
                      style: TextStyle(
                          color: Colors.white,
                          fontSize: 40.0,
                          fontWeight: FontWeight.bold))
                ],
              ),
            )),
            SizedBox(
              height: 20,
            ),
            InkWell(
              onTap: () {
                for(int i = 0; i < 3; i++) {
                  kSentences.shuffle();
                  finalSentences.add(kSentences.first);
                  kSentences.removeAt(0);
                }
                Navigator.of(context).pushNamed(Homepage.routeName);
              },
              child: Container(
                decoration: BoxDecoration(
                  border: Border.all(color: Colors.white, width: 3),
                  borderRadius: BorderRadius.all(Radius.circular(30)),
                ),
                padding: EdgeInsets.all(15),
                child: Text(
                  "I want to relax",
                  style: TextStyle(fontSize: 20, color: Colors.white),
                ),
              ),
            )
          ],
        ),
      ),
    );
  }
}
