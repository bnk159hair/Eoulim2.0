
import 'package:mobile/model/request_models/put_profilelogin.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';



class ApiprofileLogin {
  Future<generalResponse>  postProfileLoginAPI(ProfileLoginRequestModel requestModel) async {
    String url = "https://k9c103.p.ssafy.io/api/v1/children/login";
    final storage = new FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    await storage.write(key: 'childId', value: requestModel.childId?.toString());
    final response = await http.post(Uri.parse(url), headers: <String, String>{
      'Authorization': "Bearer ${authKey}",
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(
        requestModel.toJson(),
      ));
      String responseBody = utf8.decode(response.bodyBytes);
      generalResponse Loginprofile = generalResponse.fromJson(json.decode(responseBody));
      print("++++++++++++++++++++++++++++++++++++++++");
      print(Loginprofile);
      print(Loginprofile.status);
      return Loginprofile;
  }
}