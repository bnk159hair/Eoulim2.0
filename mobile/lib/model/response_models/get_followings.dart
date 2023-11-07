import 'package:mobile/model/response_models/general_response.dart';

class GetFollowings extends generalResponse {
  List<Followings>? followings;

  GetFollowings(String? code, String? status, List<Followings> followings)
      : super(code, status) {
    this.followings;
  }

  GetFollowings.fromJson(Map<String, dynamic> json)
      : super(json['code'], json['status']) {
    if (json['followings'] != null) {
      followings = <Followings>[];
      json['followings'].forEach((v) {
        followings!.add(Followings.fromJson(v));
      });
    }
  }
}

class Followings {
  String? name;
  String? followingImage;

  Followings({this.name, this.followingImage});

  Followings.fromJson(Map<String, dynamic> json) {
    name = json['name'];
    followingImage = json['followingImage'];
  }
}
