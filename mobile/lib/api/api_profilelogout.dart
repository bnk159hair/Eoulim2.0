import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/model/response_models/general_response.dart';

class ApiprofileLogout {
  static Future<generalResponse> postProfileLogout() async {
    String url = "https://k9c103.p.ssafy.io/api/v1/children/logout";
    const storage = FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    String? childId = await storage.read(key: 'childId');
    String? fcmToken = await storage.read(key: 'fcmToken');
    final Map<String, String> requestData = {
      'childId': childId ?? '',
      'fcmToken': fcmToken ?? ''
    };
    final response = await http.post(Uri.parse(url),
        headers: <String, String>{
          'Authorization': "Bearer $authKey",
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(requestData));
    if (response.statusCode == 401) {
      return generalResponse(
          response.statusCode.toString(), response.reasonPhrase);
    } else {
      generalResponse Logoutprofile = generalResponse(
          response.statusCode.toString(), response.reasonPhrase);
      print("++++++++++++++++++++++++++++++++++++++++");
      print(requestData);
      print(Logoutprofile.status);
      return Logoutprofile;
    }
  }
}
