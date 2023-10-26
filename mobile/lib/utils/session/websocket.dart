import 'dart:async';
import 'dart:convert';

import 'package:stomp_dart_client/stomp.dart';
import 'package:stomp_dart_client/stomp_config.dart';
import 'package:stomp_dart_client/stomp_frame.dart';

void onConnect(StompFrame frame) {
  print("연결시도");
  subscribe(sessionId, "animon");
  subscribe(sessionId, "guide");
  subscribe(sessionId, "leave-session");
}

final int childId = 2;
final String sessionId = "1_20231026071532";

final stompClient = StompClient(
  config: StompConfig(
    url: 'wss://k9c103.p.ssafy.io/ws',
    onConnect: onConnect,
    beforeConnect: () async {
      print('waiting to connect...');
      await Future.delayed(const Duration(milliseconds: 200));
      print('connecting...');
    },
    onWebSocketError: (dynamic error) => print(error.toString()),
    onStompError: (d) => print('error stomp'),
  ),
);

void subscribe(String sessionId, String topic) {
  stompClient.subscribe(
      destination: '/topic/$sessionId/$topic',
      callback: (response) {
        Map<String, dynamic> result = json.decode(response.body!);
        if (topic == "animon") {
          print("animon");
          print(result);
        } else if (topic == "guide") {
          print("guide");
          print(result);
        } else if (topic == "leave-session") {
          print("leave-session");
          print(result);
        } else {
          print("nothing");
        }
      });
}

void send(
  String sessionId,
  String topic,
  Map<String, dynamic> message,
) {
  stompClient.send(
      destination: '/app/$sessionId/$topic',
      body: json.encode(message),
      headers: {});
  print("successfully send message");
}
