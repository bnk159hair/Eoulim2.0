import 'package:mobile/model/response_models/general_response.dart';

class GetFollowings extends generalResponse {
  List<Profile>? followings;

  GetFollowings(String? code, String? status, List<Profile> data)
      : super(code, status) {
    this.followings = data;
  }

  GetFollowings.fromJson(Map<String, dynamic> json)
      : super(json['code'], json['status']) {
    if (json['data'] != null) {
      followings = <Profile>[];
      json['data'].forEach((v) {
        followings!.add(Profile.fromJson(v));
      });
    }
  }
}
class Profile {
  late int id;
  String? name;
  String? gender;
  String? school;
  int? grade;
  ProfileAnimon? profileAnimon;

  Profile({
    required this.id,
    this.name,
    this.gender,
    this.school,
    this.grade,
    this.profileAnimon,
  });

  Profile.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    name = json['name'];
    gender = json['gender'];
    school = json['school'];
    grade = json['grade'];
    profileAnimon = json['profileAnimon'] != null
        ? ProfileAnimon.fromJson(json['profileAnimon'])
        : null;
  }
}


class ProfileAnimon {
  late int id;
  String? maskImagePath;
  String? bodyImagePath;
  String? name;

  ProfileAnimon({required this.id,this.maskImagePath, this.bodyImagePath, this.name});

  ProfileAnimon.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    maskImagePath = json['maskImagePath'];
    bodyImagePath = json['bodyImagePath'];
    name = json['name'];
  }
}
