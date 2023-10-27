import 'package:mobile/model/response_models/general_response.dart';
import 'package:mobile/model/response_models/post_login.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import '../model/request_models/put_login.dart';

import 'package:flutter_secure_storage/flutter_secure_storage.dart';


class ApiLogin {
  Future<generalResponse> login(LoginRequestModel requestModel) async {
    String url = "https://k9c103.p.ssafy.io/api/v1/users/login";

    final response = await http.post(Uri.parse(url), headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
    }, body: jsonEncode(requestModel.toJson()));
      
      loginPost loginInfo = loginPost.fromJson(json.decode(response.body));
      print(loginPost.fromJson(json.decode(response.body)));
      final storage = new FlutterSecureStorage();
      
      await storage.write(key: 'Authkey', value: loginInfo.result);
      print("++++++++++++++++++++++++++++++++++++++++");
      print(response.body);
      print(loginInfo);
      String? authKey = await storage.read(key: 'Authkey');
      print("${authKey}");
      return generalResponse(loginInfo.resultCode);
  }
}