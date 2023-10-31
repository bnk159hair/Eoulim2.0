import 'package:mobile/model/response_models/general_response.dart';


class loginPost extends generalResponse {
  String? content; 

  loginPost(String? status, String? message, String? content)
      : super(status, message) {
    this.content = content;
  }

  loginPost.fromJson(Map<String, dynamic> json)
      : super(json['status'], json['message']) {
    content = json['content'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['status'] = this.status;
    data['message'] = this.message;
    data['content'] = this.content; // 추가
    return data;
  }
}
