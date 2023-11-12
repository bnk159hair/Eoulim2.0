import 'package:flutter/material.dart';

class RecordForm extends StatefulWidget {
  const RecordForm({super.key});

  @override
  State<RecordForm> createState() => _RecordFormState();
}

class _RecordFormState extends State<RecordForm> {
  final recordList = [
    {
      "name": '김성준',
      "animon":"assets/bear.png",
      "school":"금동초등학교",
      "date": "2023-11-23",
      "video_path":"https://k9c103.p.ssafy.io/openvidu/recordings/53_20231109153207/str_CAM_Dthp_con_BhPrhkiXm9.webm"
    },
    {
      "name": '김성준',
      "animon":"assets/bear.png",
      "school":"금동초등학교",
      "date": "2023-11-23",
      "video_path":"https://k9c103.p.ssafy.io/openvidu/recordings/53_20231109153207/str_CAM_Dthp_con_BhPrhkiXm9.webm"
    },
    {
      "name": '김성준',
      "animon":"assets/bear.png",
      "school":"금동초등학교",
      "date": "2023-11-23",
      "video_path":"https://k9c103.p.ssafy.io/openvidu/recordings/53_20231109153207/str_CAM_Dthp_con_BhPrhkiXm9.webm"
    },
  ];

  @override
  Widget build(BuildContext context) {
    return Container();
  }
}