import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:http/http.dart' as http;
import 'package:mobile/model/response_models/post_check_info.dart';


class ApiCheckPassword {
  
  Future<PostCheckInfo> postCheckPasswordAPI(password) async {
    final storage = FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    String url = 'https://k9c103.p.ssafy.io/api/v1/users/check-password';
   final Map<String, String> requestData = {
      'password': password.toString(),
    };
    final response = await http.post(
      Uri.parse(url),
      headers: <String, String>{
        'Authorization': 'Bearer $authKey',
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(
        requestData
      )
    );
    PostCheckInfo checkinfo= PostCheckInfo.fromJson(jsonDecode(response.body));
    
    print(response.body);
    print(checkinfo);
    return checkinfo;}
  }
