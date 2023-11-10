import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

import 'package:mobile/model/response_models/general_response.dart';

const baseUrl = 'https://k9c103.p.ssafy.io/api/v1';
const changePasswordPath = 'users/password';

class ApiChangePassword {
  static Future<generalResponse> patchChangePassword(
    String curPassword,
    String newPassword,
  ) async {
    const storage = FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    final response = await http.patch(
      Uri.parse(
        '$baseUrl/$changePasswordPath',
      ),
      headers: <String, String>{
        'Authorization': "Bearer $authKey",
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(
        {
          "curPassword": curPassword,
          "newPassword": newPassword,
        },
      ),
    );

    if (response.statusCode == 401) {
      return generalResponse(
        response.statusCode.toString(),
        response.reasonPhrase,
      );
    } else if (response.statusCode == 204) {
      print(curPassword);
      print(newPassword);
      return generalResponse('204', 'No Content');
    } else {
      final data = generalResponse.fromJson(json.decode(response.body));
      final statusCode = data.status!.split(' ');
      return generalResponse(statusCode[0], statusCode[1]);
    }
  }
}
