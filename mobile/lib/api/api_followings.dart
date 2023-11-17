import 'dart:convert';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/model/response_models/get_followings.dart';
import 'package:http/http.dart' as http;

class Apifollowing {
  
  Future<GetFollowings> getFollowingsAPI() async {
    final storage = FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    String? childId = await storage.read(key: 'childId');
    String url = 'https://k9c103.p.ssafy.io/api/v1/children/$childId/follows';
    final response = await http.get(
      Uri.parse(url),
      headers: <String, String>{
        'Authorization': 'Bearer $authKey',
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );
    if (response.statusCode == 401) {
      return GetFollowings(response.statusCode.toString(), response.reasonPhrase,[]); 
    }else{
    String responseBody = utf8.decode(response.bodyBytes);
    GetFollowings followings = GetFollowings.fromJson(
      json.decode(
        responseBody,
      ),
    );
    print(response.body);
    print(followings);
    return followings;}
  }
}
