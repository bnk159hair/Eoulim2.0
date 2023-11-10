// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// ignore_for_file: public_member_api_docs

import 'dart:ffi';
import 'dart:convert';

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
  late final WebViewController _controller;
  late String childId = '';
  late String token = '';
  final bool invitation = false;

  Future<void> getChildInfo() async {
    const storage = FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    String? profileId = await storage.read(key: 'childId');
    setState(() {
      if (authKey != null) {
        token = authKey;
      } else {
        print('토큰이 없습니다.');
        token = '';
      }

      if (profileId != null) {
        childId = profileId;
      } else {
        print('아이디가 없습니다.');
        childId = '';
      }
    });
  }

  void sendChildId() async {
    if (childId != '') {
      print('웹으로 childId 보내는 중입니다.');
      String message = json
          .encode({"childId": childId, "invitation": false, "token": token});
      await _controller.runJavaScript("changePage('$message')");
    }
  }

  @override
  void initState() {
    super.initState();
    getChildInfo();

    // #docregion webview_controller
    _controller = WebViewController()
      ..setJavaScriptMode(JavaScriptMode.unrestricted)
      ..setBackgroundColor(const Color(0x00000000))
      ..setNavigationDelegate(
        NavigationDelegate(
          onProgress: (int progress) {
            _controller.currentUrl().then((result) => {print(result)});
          },
          onPageStarted: (String url) {},
          onPageFinished: (String url) {
            sendChildId();
            debugPrint('Page Finished');
          },
          onWebResourceError: (WebResourceError error) {
            print(error);
          },
        ),
      )
      ..loadRequest(Uri.parse('https://k9c103.p.ssafy.io/mobile'));
    // #enddocregion webview_controller
  }

  // #docregion webview_widget
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Flutter Simple Example')),
      body: WebViewWidget(controller: _controller),
    );
  }
  // #enddocregion webview_widget
}
