import 'package:mobile/model/request_models/put_signup.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';



class ApiSignup {
  Future<generalResponse> signup(SignupRequestModel requestModel) async {
    String url = "https://k9c103.p.ssafy.io/api/v1/join";

    final response = await http.post(Uri.parse(url), headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
    }, body: jsonEncode(requestModel.toJson()));
      generalResponse signupInfo= generalResponse.fromJson(json.decode(response.body));
      print(response.body);
      print("++++++++++++++++++++++++++++++++++++++++");
      return generalResponse(signupInfo.status, signupInfo.message);
  }
}