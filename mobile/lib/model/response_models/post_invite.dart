import 'package:mobile/model/response_models/general_response.dart';

class invitePost extends generalResponse {
  Invitation? invitation;

  invitePost(String? code, String? status, Invitation? data)
      : super(code, status) {
    this.invitation = data;
  }

  invitePost.fromJson(Map<String, dynamic> json)
      : super(json['code'], json['status']) {
    if (json['data'] != null) {
      invitation = Invitation.fromJson(json['data']);
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['code'] = this.code;
    data['status'] = this.status;
    data['data'] = this.invitation; // 추가
    return data;
  }
}

class Invitation {
  String? sessionId;
  String? token;

  Invitation({
    this.sessionId,
    this.token,
  });

  Invitation.fromJson(Map<String, dynamic> json) {
    sessionId = json['sessionId'];
    token = json['token'];
  }
}
