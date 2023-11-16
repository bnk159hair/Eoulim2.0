import 'package:mobile/model/response_models/general_response.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';


class ApiDeletefollowing {
  Future<generalResponse>  deletefollowing(followingid) async {
    String url = "https://k9c103.p.ssafy.io/api/v1/follows";
    final storage = new FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    String? childId = await storage.read(key: 'childId');
    final Map<String, String> requestData = {
      'childId': childId ?? '',
      'followingChildId': followingid.toString()
    };
    final response = await http.delete(Uri.parse(url), headers: <String, String>{
      'Authorization': "Bearer ${authKey}",
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(
      requestData
    )
    );
    if (response.statusCode == 401) {
      return generalResponse(response.statusCode.toString(), response.reasonPhrase); // 에러 메시지를 원하는 내용으로 수정
    }else{
      print(response);
      print("++++++++++++++++++++++++++++++++++++++++");
      return generalResponse(response.statusCode.toString(), response.reasonPhrase);
    }
  }
}