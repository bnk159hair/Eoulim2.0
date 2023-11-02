import 'package:mobile/model/request_models/put_createprofile.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';


class ApiCreateprofile {
  Future<generalResponse>  createprofile(CreateprofileRequestModel requestModel) async {
    String url = "https://k9c103.p.ssafy.io/api/v1/children";
    final storage = new FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    final response = await http.post(Uri.parse(url), headers: <String, String>{
      'Authorization': "Bearer ${authKey}",
      'Content-Type': 'application/json; charset=UTF-8',
    }, body: jsonEncode(requestModel.toJson()));
      generalResponse profileInfo= generalResponse.fromJson(json.decode(response.body));
      print(response.body);
      print("++++++++++++++++++++++++++++++++++++++++");
      return generalResponse(profileInfo.code, profileInfo.status);
  }
}