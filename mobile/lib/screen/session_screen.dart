import 'dart:async';
import 'dart:convert';

import "package:flutter/material.dart";
import 'package:get/get.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:webview_flutter_android/webview_flutter_android.dart';
import 'package:webview_flutter_wkwebview/webview_flutter_wkwebview.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class SessionPage extends StatefulWidget {
  const SessionPage({super.key});

  @override
  State<SessionPage> createState() => _SessionPageState();
}

class _SessionPageState extends State<SessionPage> {
  late final WebViewController _controller;
  final sessionId = Get.arguments['sessionId'];
  final sessionToken = Get.arguments['sessionToken'];

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
      await _controller.runJavaScript("changePage('$message')");
    }
  }

  @override
  void initState() {
    super.initState();
    print(sessionId);
    late final PlatformWebViewControllerCreationParams params;
    if (WebViewPlatform.instance is WebKitWebViewPlatform) {
      params = WebKitWebViewControllerCreationParams(
        allowsInlineMediaPlayback: true,
        mediaTypesRequiringUserAction: const <PlaybackMediaTypes>{},
      );
    } else {
      params = const PlatformWebViewControllerCreationParams();
    }

    final WebViewController controller =
        WebViewController.fromPlatformCreationParams(
      params,
      onPermissionRequest: (WebViewPermissionRequest request) {
        request.grant();
      },
    );

    controller
      ..setJavaScriptMode(JavaScriptMode.unrestricted)
      ..setBackgroundColor(const Color(0x00000000))
      ..setNavigationDelegate(
        NavigationDelegate(
          onProgress: (int progress) {
            debugPrint('WebView is loading (progress : $progress%)');
          },
          onPageStarted: (String url) {
            debugPrint('Page started loading: $url');
            Future.delayed(const Duration(seconds: 1), () {
              getChildInfo();
            });
          },
          onPageFinished: (String url) {
            debugPrint('Page finished loading: $url');
          },
          onWebResourceError: (WebResourceError error) {
            debugPrint('''
              Page resource error:
                code: ${error.errorCode}
                description: ${error.description}
                errorType: ${error.errorType}
                isForMainFrame: ${error.isForMainFrame}
          ''');
          },
        ),
      )
      ..addJavaScriptChannel(
        'Toaster',
        onMessageReceived: (JavaScriptMessage message) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text(message.message)),
          );
        },
      )
      ..loadRequest(Uri.parse('https://k9c103.p.ssafy.io/mobile'));

    if (controller.platform is AndroidWebViewController) {
      AndroidWebViewController.enableDebugging(true);
      (controller.platform as AndroidWebViewController)
          .setMediaPlaybackRequiresUserGesture(false);
    }

    _controller = controller;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: SafeArea(
        bottom: false,
        child: WebViewWidget(controller: _controller),
      ),
    );
  }
}
