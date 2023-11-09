import 'package:mobile/model/response_models/general_response.dart';

class GetAnimons extends generalResponse {
  List<Animon>? animons;

  GetAnimons(String? code, String? status, List<Animon> data)
      : super(code, status) {
    this.animons = data;
  }

  GetAnimons.fromJson(Map<String, dynamic> json)
      : super(json['code'], json['status']) {
    if (json['data'] != null) {
      animons = <Animon>[];
      json['data'].forEach((v) {
        animons!.add(Animon.fromJson(v));
      });
    }
  }
}


class Animon {
  late int id;
  String? maskImagePath;
  String? bodyImagePath;
  String? name;

  Animon({required this.id,this.maskImagePath, this.bodyImagePath, this.name});

  Animon.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    maskImagePath = json['headImagePath'];
    bodyImagePath = json['bodyImagePath'];
    name = json['name'];
  }
}
