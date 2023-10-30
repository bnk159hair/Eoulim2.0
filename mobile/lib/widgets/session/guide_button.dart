import 'package:flutter/material.dart';
import 'package:mobile/utils/websocket.dart' as websocket;

class GuideButton extends StatefulWidget {
  const GuideButton({Key? key}) : super(key: key);

  @override
  _GuideButtonState createState() => _GuideButtonState();
}

class _GuideButtonState extends State<GuideButton> {
  @override
  Widget build(BuildContext context) {
    return Container(
        child: Center(child: FloatingActionButton(onPressed: nextGuide)));
  }

  void nextGuide() {
    final message = {
      'childId': websocket.childId,
      'isNextGuideOn': true,
    };
    websocket.send(websocket.sessionId, 'guide', message);
  }
}
