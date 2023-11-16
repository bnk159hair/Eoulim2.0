import 'package:mobile/model/response_models/general_response.dart';

class GetCheckUsername extends generalResponse {
  late bool data;

  // GetCheckUsername(String? code, String? status, bool data)
  //     : super(code, status) {
  //   this.data;
  // }

  GetCheckUsername.fromJson(Map<String, dynamic> json)
      : super(json['code'], json['status']) {
    data = json['data'];
  }
}
