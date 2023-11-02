import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

import '../model/request_models/put_login.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:mobile/model/response_models/post_login.dart';

const apiUrl = "https://k9c103.p.ssafy.io/api/v1/users/login";

class ApiLogin {
  Future<generalResponse> login(LoginRequestModel requestModel) async {
    final response = await http.post(
      Uri.parse(apiUrl),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(
        requestModel.toJson(),
      ),
    );
    print(response.body);
    loginPost loginInfo = loginPost.fromJson(json.decode(response.body));
    print(loginPost.fromJson(json.decode(response.body)));
    const storage = FlutterSecureStorage();
    await storage.write(key: 'Authkey', value: loginInfo.data);
    print("++++++++++++++++++++++++++++++++++++++++");
    print(loginInfo.data);
    String? authKey = await storage.read(key: 'Authkey');
    print("$authKey");
    return generalResponse(loginInfo.code, loginInfo.status);
  }
}
