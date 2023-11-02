import 'package:mobile/model/response_models/general_response.dart';


class getProfiles extends generalResponse {
  List<Profile>? profiles; 

  getProfiles(String? code, String? status, List<Profile> profiles)
      : super(code, status) {
    this.profiles = profiles;
  }

  getProfiles.fromJson(Map<String, dynamic> json)
      : super(json['code'], json['status']) {
    if (json['profiles'] != null) {
      profiles = <Profile>[];
      json['profiles'].forEach((v) {
        profiles!.add(new Profile.fromJson(v));
      });
    }
  }
}

class Profile {
  String? name;
  String? profileImage;

  Profile({this.name, this.profileImage});

  Profile.fromJson(Map<String, dynamic> json) {
    name = json['name'];
    profileImage = json['profileImage'];
  }
}
