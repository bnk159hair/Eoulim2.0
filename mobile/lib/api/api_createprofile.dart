import 'package:mobile/model/request_models/put_createprofile.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/model/response_models/post_check_info.dart';

const baseUrl = 'https://k9c103.p.ssafy.io/api/v1';
const createChildPath = 'children';
const checkSchoolPath = 'open-api/schools';

class ApiCreateprofile {
  static Future<generalResponse> createprofile(
      CreateprofileRequestModel requestModel) async {
    const storage = FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    final response = await http.post(
      Uri.parse(
        '$baseUrl/$createChildPath',
      ),
      headers: <String, String>{
        'Authorization': "Bearer $authKey",
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(
        requestModel.toJson(),
      ),
    );
    if (response.statusCode == 401) {
      return generalResponse(
        response.statusCode.toString(),
        response.reasonPhrase,
      );
    } else {
      generalResponse profileInfo = generalResponse.fromJson(
        json.decode(
          response.body,
        ),
      );
      print(response.body);
      print("++++++++++++++++++++++++++++++++++++++++");
      return generalResponse(profileInfo.code, profileInfo.status);
    }
  }

  static Future<PostCheckInfo> postCheckShool(String school) async {
    const storage = FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    final response = await http.post(
      Uri.parse('$baseUrl/$checkSchoolPath'),
      headers: <String, String>{
        'Authorization': "Bearer $authKey",
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(
        {"keyword": school},
      ),
    );
    return PostCheckInfo.fromJson(jsonDecode(response.body));
  }
}
