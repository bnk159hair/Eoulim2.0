// import 'package:mobile/model/response_models/general_response.dart';


class loginPost {
  String? result;
  String? resultCode;

  loginPost({this.result, this.resultCode});

  factory loginPost.fromJson(Map<String, dynamic> json) {
    return loginPost(
      result: json['result'],
      resultCode: json['resultCode'],
    );
}
}
