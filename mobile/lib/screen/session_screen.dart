import 'dart:async';
import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_inappwebview/flutter_inappwebview.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';

void _permission() async {
  Map<Permission, PermissionStatus> requestStatuses = await [
    Permission.camera,
    Permission.audio,
    Permission.microphone,
  ].request();
  // 각 권한에 대한 상태를 확인하고 처리할 수 있습니다.
  if (requestStatuses[Permission.camera] == PermissionStatus.granted) {
    // Camera 권한이 허용된 경우
    print('Camera permission granted');
  } else {
    // Camera 권한이 거부된 경우
    print('Camera permission denied');
  }

  if (requestStatuses[Permission.audio] == PermissionStatus.granted) {
    // Audio 권한이 허용된 경우
    print('Audio permission granted');
  } else {
    // Audio 권한이 거부된 경우
    print('Audio permission denied');
  }

  if (requestStatuses[Permission.microphone] == PermissionStatus.granted) {
    // Microphone 권한이 허용된 경우
    print('Microphone permission granted');
  } else {
    // Microphone 권한이 거부된 경우
    print('Microphone permission denied');
  }
}

class SessionPage extends StatefulWidget {
  SessionPage({super.key});
  final ChromeSafariBrowser browser = ChromeSafariBrowser();

  @override
  State<SessionPage> createState() => _SessionPageState();
}

class _SessionPageState extends State<SessionPage> {
  final GlobalKey webViewKey = GlobalKey();
  final sessionId = Get.arguments['sessionId'];
  Uri myUrl = Uri.parse("https://k9c103.p.ssafy.io/mobile");
  late final InAppWebViewController webViewController;
  late final PullToRefreshController pullToRefreshController;
  double progress = 0;

  Future<void> getChildInfo(InAppWebViewController controller) async {
    const storage = FlutterSecureStorage();

    String? authKey = await storage.read(key: 'Authkey');
    String? profileId = await storage.read(key: 'childId');

    sendChildId(controller, profileId, authKey, sessionId);
  }

  void sendChildId(controller, childId, token, sessionId) async {
    if (childId != '') {
      print('웹으로 childId 보내는 중입니다.');
      final bool invitation;
      if (sessionId != '') {
        invitation = true;
      } else {
        invitation = false;
      }
      String message = json.encode(
          {"childId": childId, "invitation": invitation, "token": token});
      print('$childId, $invitation, $token');
      await controller.evaluateJavascript(source: "changePage('$message')");
    }
  }

  @override
  void initState() {
    super.initState();

    _permission();

    pullToRefreshController = (kIsWeb
        ? null
        : PullToRefreshController(
            options: PullToRefreshOptions(
              color: Colors.blue,
            ),
            onRefresh: () async {
              if (defaultTargetPlatform == TargetPlatform.android) {
                webViewController.reload();
              } else if (defaultTargetPlatform == TargetPlatform.iOS ||
                  defaultTargetPlatform == TargetPlatform.macOS) {
                webViewController.loadUrl(
                    urlRequest:
                        URLRequest(url: await webViewController.getUrl()));
              }
            },
          ))!;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: SafeArea(
            child: WillPopScope(
                onWillPop: () => _goBack(context),
                child: Column(children: <Widget>[
                  progress < 1.0
                      ? LinearProgressIndicator(
                          value: progress, color: Colors.blue)
                      : Container(),
                  Expanded(
                      child: Stack(children: [
                    InAppWebView(
                      key: webViewKey,
                      initialUrlRequest: URLRequest(url: myUrl),
                      initialOptions: InAppWebViewGroupOptions(
                        crossPlatform: InAppWebViewOptions(
                          javaScriptCanOpenWindowsAutomatically: true,
                          javaScriptEnabled: true,
                          useOnDownloadStart: true,
                          useOnLoadResource: true,
                          useShouldOverrideUrlLoading: true,
                          mediaPlaybackRequiresUserGesture: false,
                          allowFileAccessFromFileURLs: true,
                          allowUniversalAccessFromFileURLs: true,
                          verticalScrollBarEnabled: true,
                          userAgent: 'random',
                        ),
                        android: AndroidInAppWebViewOptions(
                            useHybridComposition: true,
                            allowContentAccess: true,
                            builtInZoomControls: true,
                            thirdPartyCookiesEnabled: true,
                            allowFileAccess: true,
                            supportMultipleWindows: true),
                        ios: IOSInAppWebViewOptions(
                          allowsInlineMediaPlayback: true,
                          allowsBackForwardNavigationGestures: true,
                        ),
                      ),
                      pullToRefreshController: pullToRefreshController,
                      onConsoleMessage: (InAppWebViewController controller,
                          ConsoleMessage consoleMessage) {
                        print("Console: ${consoleMessage.message}");
                      },
                      onLoadStart: (InAppWebViewController controller, uri) {
                        setState(() {
                          myUrl = uri!;
                        });
                      },
                      onLoadStop: (InAppWebViewController controller, uri) {},
                      onProgressChanged: (controller, progress) {
                        if (progress == 100) {
                          pullToRefreshController.endRefreshing();
                        }
                        setState(() {
                          this.progress = progress / 100;
                        });
                      },
                      androidOnPermissionRequest:
                          (controller, origin, resources) async {
                        return PermissionRequestResponse(
                            resources: resources,
                            action: PermissionRequestResponseAction.GRANT);
                      },
                      onWebViewCreated:
                          (InAppWebViewController controller) async {
                        webViewController = controller;
                        await Future.delayed(const Duration(seconds: 3));
                        getChildInfo(webViewController);
                      },
                      onCreateWindow: (controller, createWindowRequest) async {
                        showDialog(
                          context: context,
                          builder: (context) {
                            return AlertDialog(
                              content: SizedBox(
                                width: MediaQuery.of(context).size.width,
                                height: 400,
                                child: InAppWebView(
                                  // Setting the windowId property is important here!
                                  windowId: createWindowRequest.windowId,
                                  initialOptions: InAppWebViewGroupOptions(
                                    android: AndroidInAppWebViewOptions(
                                      builtInZoomControls: true,
                                      thirdPartyCookiesEnabled: true,
                                    ),
                                    crossPlatform: InAppWebViewOptions(
                                      cacheEnabled: true,
                                      javaScriptEnabled: true,
                                      userAgent: 'random',
                                    ),
                                    ios: IOSInAppWebViewOptions(
                                      allowsInlineMediaPlayback: true,
                                      allowsBackForwardNavigationGestures: true,
                                    ),
                                  ),
                                  onCloseWindow: (controller) async {
                                    if (Navigator.canPop(context)) {
                                      Navigator.pop(context);
                                    }
                                  },
                                ),
                              ),
                            );
                          },
                        );

                        return true;
                      },
                    )
                  ]))
                ]))));
  }

  Future<bool> _goBack(BuildContext context) async {
    if (await webViewController.canGoBack()) {
      webViewController.goBack();
      return Future.value(false);
    } else {
      return Future.value(true);
    }
  }
}
