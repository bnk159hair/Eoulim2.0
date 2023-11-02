import 'package:mobile/model/response_models/general_response.dart';


class loginPost extends generalResponse {
  String? data; 

  loginPost(String? code, String? status, String? data)
      : super(code, status) {
    this.data = data;
  }

  loginPost.fromJson(Map<String, dynamic> json)
      : super(json['code'], json['status']) {
    data = json['data'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['code'] = this.code;
    data['status'] = this.status;
    data['data'] = this.data; // 추가
    return data;
  }
}
