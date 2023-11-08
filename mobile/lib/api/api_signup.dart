import 'dart:convert';

import 'package:http/http.dart' as http;

import 'package:mobile/model/request_models/put_signup.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:mobile/model/response_models/get_check_username.dart';

const baseUrl = 'https://k9c103.p.ssafy.io/api/v1';
const signupPath = 'users/join';
const checkUsernamePath = 'users/check-username';
const headers = <String, String>{
  'Content-Type': 'application/json; charset=UTF-8',
};

class ApiSignup {
  Future<generalResponse> signup(SignupRequestModel requestModel) async {
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
    print(response.body);
    return generalResponse(signupInfo.code, signupInfo.status);
  }

  Future<GetCheckUsername> checkUsername(String username) async {
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
    return checkUsernameInfo;
  }
}
