import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class Menu extends StatelessWidget {
  const Menu(
      {required this.controller,
      required this.sessionId,
      required this.sessionToken,
      Key? key})
      : super(key: key);

  final WebViewController controller;
  final String sessionId;
  final String sessionToken;

  Future<void> getChildInfo() async {
    const storage = FlutterSecureStorage();

    String? authKey = await storage.read(key: 'Authkey');
    String? profileId = await storage.read(key: 'childId');

    sendChildId(profileId, authKey, sessionId, sessionToken);
  }

  void sendChildId(childId, token, sessionId, sessionToken) async {
    if (childId != '') {
      print('웹으로 childId 보내는 중입니다.');
      final bool invitation;
      if (sessionId != '') {
        invitation = true;
      } else {
        invitation = false;
      }
      String message = json.encode({
        "childId": childId,
        "invitation": invitation,
        "sessionToken": sessionToken,
        "token": token
      });
      await controller.runJavaScript("changePage('$message')");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        IconButton(
          icon: const Icon(Icons.arrow_back_ios),
          onPressed: () async {
            if (await controller.canGoBack()) {
              await controller.goBack();
            } else {
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(content: Text('No back history item')),
              );
              return;
            }
          },
        ),
        IconButton(
          icon: const Icon(Icons.arrow_forward_ios),
          onPressed: () async {
            if (await controller.canGoForward()) {
              await controller.goForward();
            } else {
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(content: Text('No forward history item')),
              );
              return;
            }
          },
        ),
        IconButton(
          icon: const Icon(Icons.replay),
          onPressed: () {
            controller.reload();
          },
        ),
        IconButton(
          icon: const Icon(Icons.queue_play_next),
          onPressed: () {
            getChildInfo();
          },
        )
      ],
    );
  }
}
