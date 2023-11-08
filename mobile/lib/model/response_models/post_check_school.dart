import 'package:mobile/model/response_models/general_response.dart';

class PostCheckSchool extends generalResponse {
  final bool data;

  PostCheckSchool.fromJson(Map<String, dynamic> json)
      : data = json['data'],
        super(json['code'], json['status']);
}
