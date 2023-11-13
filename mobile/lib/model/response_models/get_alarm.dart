import 'package:mobile/model/response_models/general_response.dart';

class GetAlarm extends generalResponse {
  List<Alarm>? alarms;

  GetAlarm(String? code, String? status, List<Alarm> data)
      : super(code, status) {
    this.alarms = data;
  }

  GetAlarm.fromJson(Map<String, dynamic> json)
      : super(json['code'], json['status']) {
    if (json['data'] != null) {
      alarms = <Alarm>[];
      json['data'].forEach((v) {
        alarms!.add(Alarm.fromJson(v));
      });
    }
  }
}


class Alarm {
  String? createTime;
  String? text;

  Alarm({this.createTime, this.text});

  Alarm.fromJson(Map<String, dynamic> json) {
    createTime = json['createTime'];
    text = json['text'];
  }
}
