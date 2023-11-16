import 'dart:convert';

import 'package:http/http.dart' as http;

import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:mobile/model/request_models/put_signup.dart';
import 'package:mobile/model/response_models/get_check_username.dart';
import 'package:mobile/model/request_models/put_login.dart';
import 'package:mobile/model/response_models/post_login.dart';

class ApiUser {
  static const baseUrl = 'https://k9c103.p.ssafy.io/api/v1';
  static const headers = <String, String>{
    'Content-Type': 'application/json; charset=UTF-8',
  };
  static const signupPath = 'users/join';
  static const checkUsernamePath = 'users/check-username';
  static const loginPath = 'users/login';
  static const logoutPath = 'users/logout';
  static const storage = FlutterSecureStorage();

  static Future<generalResponse> postSignup(
      SignupRequestModel requestModel) async {
    final response = await http.post(
      Uri.parse('$baseUrl/$signupPath'),
      headers: headers,
      body: jsonEncode(
        requestModel.toJson(),
      ),
    );
    generalResponse signupInfo = generalResponse.fromJson(
      json.decode(
        response.body,
      ),
    );
    print("++++++++++++++++++++++++++++++++++++++++");
    print('회원가입 API');
    print(response.body);
    return generalResponse(signupInfo.code, signupInfo.status);
  }

  static Future<GetCheckUsername> getCheckUsername(String username) async {
    final response = await http.get(
      Uri.parse(
        '$baseUrl/$checkUsernamePath/$username',
      ),
      headers: headers,
    );
    GetCheckUsername checkUsernameInfo = GetCheckUsername.fromJson(
      json.decode(
        response.body,
      ),
    );
    print("++++++++++++++++++++++++++++++++++++++++");
    print('중복확인 API');
    print(response.body);
    return checkUsernameInfo;
  }

  static Future<generalResponse> postLogin(
      LoginRequestModel requestModel) async {
    final response = await http.post(
      Uri.parse('$baseUrl/$loginPath'),
      headers: headers,
      body: jsonEncode(
        requestModel.toJson(),
      ),
    );
    loginPost loginInfo = loginPost.fromJson(json.decode(response.body));
    await storage.write(key: 'Authkey', value: loginInfo.data);
    print("++++++++++++++++++++++++++++++++++++++++");
    print('로그인 API');
    print(response.body);
    print("++++++++++++++++++++++++++++++++++++++++");
    print(loginInfo.data);
    return generalResponse(loginInfo.code, loginInfo.status);
  }

  static Future<generalResponse> postLogout() async {
    String? authKey = await storage.read(key: 'Authkey');
    String? fcmToken = await storage.read(key: 'fcmToken');
    final response = await http.post(
      Uri.parse('$baseUrl/$logoutPath'),
      headers: <String, String>{
        'Authorization': "Bearer $authKey",
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode({
        'fcmToken': fcmToken ?? '',
      }),
    );
    print("++++++++++++++++++++++++++++++++++++++++");
    print('로그아웃 API');
    print(response.body);
    if (response.statusCode == 401) {
      return generalResponse(
        response.statusCode.toString(),
        response.reasonPhrase,
      );
    } else {
      final logoutResponse = generalResponse(
        response.statusCode.toString(),
        response.reasonPhrase,
      );
      print("++++++++++++++++++++++++++++++++++++++++");
      print(logoutResponse.status);
      return logoutResponse;
    }
  }
}
