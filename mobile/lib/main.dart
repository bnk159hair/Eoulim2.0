import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:get/get.dart';
import 'package:mobile/controller/profile_select.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:mobile/screen/session_screen.dart';
import 'package:permission_handler/permission_handler.dart';

import 'package:mobile/screen/splash_screen.dart';

@pragma('vm:entry-point')
Future<void> _firebaseMessagingBackgroundHandler(RemoteMessage message) async {
  await Firebase.initializeApp();
  initializeNotification();
  print('백그라운드입니다.');
  // showFlutterNotification(message);
}

@pragma('vm:entry-point')
void backgroundHandler(NotificationResponse details) {
  print('현재 백그라운드에서 초대 메시지를 받았습니다.');
  final payload = details.payload!.split(' ');
  final sessionId = payload[payload.length - 1];
  Get.to(() => SessionPage(),
      arguments: {'sessionId': sessionId, 'sessionToken': ''});
}

void initializeNotification() async {
  final flutterLocalNotificationsPlugin = FlutterLocalNotificationsPlugin();
  await flutterLocalNotificationsPlugin
      .resolvePlatformSpecificImplementation<
          AndroidFlutterLocalNotificationsPlugin>()
      ?.createNotificationChannel(const AndroidNotificationChannel(
          'high_importance_channel', 'high_importance_notification',
          importance: Importance.max));

  await flutterLocalNotificationsPlugin.initialize(
    const InitializationSettings(
      android: AndroidInitializationSettings("@mipmap/ic_launcher"),
      iOS: DarwinInitializationSettings(),
    ),
    onDidReceiveNotificationResponse: (NotificationResponse details) {
      final payload = details.payload!.split(' ');
      final sessionId = payload[payload.length - 1];
      Get.to(() => SessionPage(),
          arguments: {'sessionId': sessionId, 'sessionToken': ''});
    },
    onDidReceiveBackgroundNotificationResponse: backgroundHandler,
  );

  await FirebaseMessaging.instance.requestPermission(
    alert: true,
    badge: true,
    sound: true,
  );

  FirebaseMessaging.onMessage.listen((RemoteMessage message) async {
    RemoteNotification? notification = message.notification;

    if (notification != null) {
      Future.delayed(const Duration(milliseconds: 1000), () {
        flutterLocalNotificationsPlugin.show(
            notification.hashCode,
            notification.title,
            notification.body,
            const NotificationDetails(
              android: AndroidNotificationDetails(
                'high_importance_channel',
                'high_importance_notification',
                importance: Importance.max,
              ),
              iOS: DarwinNotificationDetails(),
            ),
            payload: notification.body);
        print(notification.body);
        print("수신자 측 메시지 수신");
      });
    }
  });

  FirebaseMessaging.onMessageOpenedApp.listen((RemoteMessage message) async {
    RemoteNotification? notification = message.notification;
    if (notification != null) {
      final payload = notification.body!.split(' ');
      final sessionId = payload[payload.length - 1];
      Get.to(() => SessionPage(),
          arguments: {'sessionId': sessionId, 'sessionToken': ''});
    }
  });

  RemoteMessage? message = await FirebaseMessaging.instance.getInitialMessage();
  if (message != null) {
    // 액션 부분 -> 파라미터는 message.data['test_parameter1'] 이런 방식으로...
  }
}

Future<String?> getMyDeviceToken() async {
  final token = await FirebaseMessaging.instance.getToken();
  if (token != null) {
    const storage = FlutterSecureStorage();
    await storage.write(key: 'fcmToken', value: token);
    print("내 디바이스 토큰: $token");
  }
  return token;
}

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  await Permission.audio.request();
  await Permission.camera.request();
  await Permission.microphone.request();
  FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);
  initializeNotification();

  await getMyDeviceToken();

  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    SystemChrome.setSystemUIOverlayStyle(
      const SystemUiOverlayStyle(
        statusBarColor: Colors.transparent,
        statusBarIconBrightness: Brightness.light,
      ),
    );

    return GetMaterialApp(
      localizationsDelegates: const [
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
      ],
      supportedLocales: const [
        Locale('ko', 'KR'),
      ],
      debugShowCheckedModeBanner: false,
      debugShowMaterialGrid: false,
      title: 'Eoullim',
      home: const Splash(),
      initialBinding: BindingsBuilder(() {
        Get.put(ProfileController());
      }),
    );
  }
}
