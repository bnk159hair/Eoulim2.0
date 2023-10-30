import 'package:mobile/model/response_models/general_response.dart';

class GuideResponse extends generalResponse {
  Response? response;

  GuideResponse(String statusCode, Response response) : super(statusCode) {
    this.response = response;
  }
}

class Response {
  String? childId;
  bool? isNextGuideOn;

  Response({
    this.childId,
    this.isNextGuideOn,
  });

  Response.fromJson(Map<String, dynamic> json) {
    childId = json['childId'];
    isNextGuideOn = json['isNextGuideOn'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['childId'] = this.childId;
    data['isNextGuideOn'] = this.isNextGuideOn;

    return data;
  }
}
