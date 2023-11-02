import 'dart:convert';

import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/model/response_models/get_followings.dart';
import 'package:http/http.dart' as http;

class Apiprofile {
  Future<GetFollowings> getFollowingsAPI() async {
    String url = 'https://k9c103.p.ssafy.io/api/v1/follows';
    const storage = FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    final response = await http.get(
      Uri.parse(url),
      headers: <String, String>{
        'Authorization': 'Bearer $authKey',
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );
    GetFollowings followings = GetFollowings.fromJson(
      json.decode(
        response.body,
      ),
    );
    print(response.body);
    print(followings);
    return followings;
  }
}
