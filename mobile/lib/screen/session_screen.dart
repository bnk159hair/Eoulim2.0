// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// ignore_for_file: public_member_api_docs

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:webview_flutter_android/webview_flutter_android.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class Session extends StatefulWidget {
  const Session({super.key});

  @override
  State<Session> createState() => _SessionState();
}

class _SessionState extends State<Session> {
  late final WebViewController controller;
  late String token = '';

  Future<void> getToken() async {
    final storage = new FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    setState(() {
      if (authKey != null) {
        token = authKey;
      } else {
        token = '';
      }
    });
  }

  void sendToken() {
    if (token != '') {
      print('웹으로 보내는 중....');
      Map<String, String> message = {'token': token};
      controller.runJavaScript('getTokenFromApp("$message")');
    }
  }

  @override
  void initState() {
    super.initState();
    getToken();

    // #docregion webview_controller
    controller = WebViewController()
      ..setJavaScriptMode(JavaScriptMode.unrestricted)
      ..setBackgroundColor(const Color(0x00000000))
      ..setNavigationDelegate(
        NavigationDelegate(
          onProgress: (int progress) {
            print('token: $token');
            sendToken();
          },
          onPageStarted: (String url) {},
          onPageFinished: (String url) {
            debugPrint('Page Finished');
          },
          onWebResourceError: (WebResourceError error) {},
        ),
      )
      ..loadRequest(Uri.parse('https://k9c103.p.ssafy.io/session'));
    // #enddocregion webview_controller
  }

  // #docregion webview_widget
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Flutter Simple Example')),
      body: WebViewWidget(controller: controller),
    );
  }
  // #enddocregion webview_widget
}
