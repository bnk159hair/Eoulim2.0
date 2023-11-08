import 'package:mobile/model/response_models/general_response.dart';

class getProfiles extends generalResponse {
  List<Profile>? profiles;

  getProfiles(String? code, String? status, List<Profile> data)
      : super(code, status) {
    this.profiles = data;
  }

  getProfiles.fromJson(Map<String, dynamic> json)
      : super(json['code'], json['status']) {
    if (json['data'] != null) {
      profiles = <Profile>[];
      json['data'].forEach((v) {
        profiles!.add(Profile.fromJson(v));
      });
    }
  }
}

class Profile {
  late int id;
  String? name;
  List<int>? birth;
  String? gender;
  String? school;
  int? grade;
  User? user;
  ProfileAnimon? profileAnimon;

  Profile({
    required this.id,
    this.name,
    this.birth,
    this.gender,
    this.school,
    this.grade,
    this.user,
    this.profileAnimon,
  });

  Profile.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    name = json['name'];
    birth = List<int>.from(json['birth']);
    gender = json['gender'];
    school = json['school'];
    grade = json['grade'];
    user = json['user'] != null ? User.fromJson(json['user']) : null;
    profileAnimon = json['profileAnimon'] != null
        ? ProfileAnimon.fromJson(json['profileAnimon'])
        : null;
  }
}

class User {
  late int id;
  String? name;
  String? phoneNumber;
  String? username;
  String? password;
  String? role;

  User({
    required this.id,
    this.name,
    this.phoneNumber,
    this.username,
    this.password,
    this.role,
  });

  User.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    name = json['name'];
    phoneNumber = json['phoneNumber'];
    username = json['username'];
    password = json['password'];
    role = json['role'];
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
    maskImagePath = json['headImagePath'];
    bodyImagePath = json['bodyImagePath'];
    name = json['name'];
  }
}
