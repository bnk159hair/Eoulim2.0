
import 'package:mobile/model/response_models/general_response.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/model/response_models/get_alarm.dart';


class Apialarm {
  Future<GetAlarm>  getalarmAPI() async {
    String url = "https://k9c103.p.ssafy.io/api/v1/notifications";
    final storage = new FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    final response = await http.get(Uri.parse(url), headers: <String, String>{
      'Authorization': "Bearer ${authKey}",
      'Content-Type': 'application/json; charset=UTF-8',
    });
    if (response.statusCode == 401) {
      return GetAlarm(response.statusCode.toString(), response.reasonPhrase,[]); 
    }else{
      String responseBody = utf8.decode(response.bodyBytes);
      GetAlarm alarmInfo= GetAlarm.fromJson(json.decode(responseBody));
      print(response.body);
      print("++++++++++++++++++++++++++++++++++++++++");
      print(alarmInfo);
      return alarmInfo;
    }
  }
}