import 'package:mobile/model/response_models/general_response.dart';

class PostCheckInfo extends generalResponse {
  final bool data;

  PostCheckInfo.fromJson(Map<String, dynamic> json)
      : data = json['data'],
        super(json['code'], json['status']);
}
