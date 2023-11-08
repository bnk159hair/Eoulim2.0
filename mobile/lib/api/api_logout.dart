import 'package:mobile/model/response_models/general_response.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';



class ApiLogout {
  Future<generalResponse>  postLogoutAPI() async {
    String url = "https://k9c103.p.ssafy.io/api/v1/users/logout";
    final storage = new FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    String? fcmToken = await storage.read(key: 'fcmToken');
    final Map<String, String> requestData = {
      'fcmToken': fcmToken ?? '',
    };
    final response = await http.post(Uri.parse(url), headers: <String, String>{
      'Authorization': "Bearer ${authKey}",
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(
        requestData
      ));
    if (response.statusCode == 401) {
      return generalResponse(response.statusCode.toString(), response.reasonPhrase); 
    }else{
      generalResponse Logout = generalResponse(response.statusCode.toString(), response.reasonPhrase);
      print("++++++++++++++++++++++++++++++++++++++++");
      print(requestData);
      print(Logout.status);
      return Logout;
    }
  }
}