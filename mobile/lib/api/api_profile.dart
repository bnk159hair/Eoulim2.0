import 'dart:convert';
import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/controller/profile_select.dart';
import 'package:mobile/model/request_models/put_createprofile.dart';
import 'package:mobile/model/request_models/put_profilelogin.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:mobile/model/response_models/get_profile.dart';
import 'package:mobile/model/response_models/get_profiles.dart';
import 'package:mobile/model/response_models/post_check_info.dart';

class ApiProfile {
  static const storage = FlutterSecureStorage();
  static const baseUrl = 'https://k9c103.p.ssafy.io/api/v1/children';
  static const profileLoginPath = 'login';
  static const profileLogoutPath = 'logout';
  static const checkSchoolPath = 'open-api/schools';

  static Future<ProfilesModel> getProfiles() async {
    String? authKey = await storage.read(key: 'Authkey');
    final response = await http.get(
      Uri.parse(baseUrl),
      headers: <String, String>{
        'Authorization': "Bearer $authKey",
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );
    if (response.statusCode == 401) {
      return ProfilesModel(
          response.statusCode.toString(), response.reasonPhrase, []);
    } else {
      String responseBody = utf8.decode(response.bodyBytes);
      ProfilesModel profileInfo =
          ProfilesModel.fromJson(json.decode(responseBody));
      print("++++++++++++++++++++++++++++++++++++++++");
      print('get 프로필 목록 API');
      print(response.body);
      print(profileInfo);
      print("++++++++++++++++++++++++++++++++++++++++");
      return profileInfo;
    }
  }

  static Future<ProfileInfoModel> getProfileInfo() async {
    String? authKey = await storage.read(key: 'Authkey');
    String? childId = await storage.read(key: 'childId');
    final response = await http.get(
      Uri.parse('$baseUrl/$childId'),
      headers: <String, String>{
        'Authorization': "Bearer $authKey",
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );
    if (response.statusCode == 401) {
      return ProfileInfoModel(
          response.statusCode.toString(), response.reasonPhrase, null);
    } else {
      String responseBody = utf8.decode(response.bodyBytes);
      ProfileInfoModel profileInfo = ProfileInfoModel.fromJson(
        json.decode(
          responseBody,
        ),
      );
      print("++++++++++++++++++++++++++++++++++++++++");
      print('get 프로필 정보 API');
      print(response.body);
      print(profileInfo);
      print("++++++++++++++++++++++++++++++++++++++++");
      ProfileController profileController = Get.find();
      // 응답값을 ProfileController에 저장
      await profileController.updateSelectedProfile(profileInfo.profile);
      return profileInfo;
    }
  }

  static Future<generalResponse> postProfileLogin(
      ProfileLoginRequestModel requestModel) async {
    String? authKey = await storage.read(key: 'Authkey');
    await storage.write(
        key: 'childId', value: requestModel.childId?.toString());
    final response = await http.post(
      Uri.parse('$baseUrl/$profileLoginPath'),
      headers: <String, String>{
        'Authorization': "Bearer $authKey",
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(
        requestModel.toJson(),
      ),
    );
    if (response.statusCode == 401) {
      return generalResponse(
        response.statusCode.toString(),
        response.reasonPhrase,
      );
    } else {
      String responseBody = utf8.decode(response.bodyBytes);
      generalResponse loginProfile = generalResponse.fromJson(
        json.decode(responseBody),
      );
      print("++++++++++++++++++++++++++++++++++++++++");
      print('프로필 로그인');
      print(loginProfile);
      print(loginProfile.status);
      print("++++++++++++++++++++++++++++++++++++++++");
      return loginProfile;
    }
  }

  static Future<generalResponse> postProfileLogout() async {
    String? authKey = await storage.read(key: 'Authkey');
    String? childId = await storage.read(key: 'childId');
    String? fcmToken = await storage.read(key: 'fcmToken');
    final Map<String, String> requestData = {
      'childId': childId ?? '',
      'fcmToken': fcmToken ?? ''
    };
    final response = await http.post(
      Uri.parse('$baseUrl/$profileLogoutPath'),
      headers: <String, String>{
        'Authorization': 'Bearer $authKey',
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(requestData),
    );
    if (response.statusCode == 401) {
      return generalResponse(
        response.statusCode.toString(),
        response.reasonPhrase,
      );
    } else {
      generalResponse logoutProfile = generalResponse(
        response.statusCode.toString(),
        response.reasonPhrase,
      );
      print("++++++++++++++++++++++++++++++++++++++++");
      print('프로필 로그아웃 API');
      print(requestData);
      print(logoutProfile.status);
      print("++++++++++++++++++++++++++++++++++++++++");
      return logoutProfile;
    }
  }

  static Future<generalResponse> postCreateProfile(
      CreateprofileRequestModel requestModel) async {
    const storage = FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    final response = await http.post(
      Uri.parse(
        baseUrl,
      ),
      headers: <String, String>{
        'Authorization': "Bearer $authKey",
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(
        requestModel.toJson(),
      ),
    );
    if (response.statusCode == 401) {
      return generalResponse(
        response.statusCode.toString(),
        response.reasonPhrase,
      );
    } else {
      generalResponse profileInfo = generalResponse.fromJson(
        json.decode(
          response.body,
        ),
      );
      print("++++++++++++++++++++++++++++++++++++++++");
      print('프로필 생성 API');
      print(response.body);
      print("++++++++++++++++++++++++++++++++++++++++");
      return generalResponse(profileInfo.code, profileInfo.status);
    }
  }

  static Future<PostCheckInfo> postCheckShool(String school) async {
    const storage = FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    final response = await http.post(
      Uri.parse('$baseUrl/$checkSchoolPath'),
      headers: <String, String>{
        'Authorization': "Bearer $authKey",
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(
        {"keyword": school},
      ),
    );
    print("++++++++++++++++++++++++++++++++++++++++");
    print('학교 확인 API');
    print(response.body);
    print("++++++++++++++++++++++++++++++++++++++++");
    return PostCheckInfo.fromJson(jsonDecode(response.body));
  }

  static Future<int> putProfile(Map<String, String> data) async {
    String? authKey = await storage.read(key: 'Authkey');
    String? childId = await storage.read(key: 'childId');
    final response = await http.put(
      Uri.parse('$baseUrl/$childId'),
      headers: <String, String>{
        'Authorization': "Bearer $authKey",
        'Content-Type': 'application/json',
      },
      body: jsonEncode(data),
    );
    print("++++++++++++++++++++++++++++++++++++++++");
    print('프로필 수정 API');
    print(response.statusCode);
    print("++++++++++++++++++++++++++++++++++++++++");
    return response.statusCode;
  }

  static Future<int> deleteProfile() async {
    const storage = FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    String? childId = await storage.read(key: 'childId');
    final response = await http.delete(
      Uri.parse('$baseUrl/$childId'),
      headers: <String, String>{
        'Authorization': "Bearer $authKey",
        'Content-Type': 'application/json',
      },
    );
    print("++++++++++++++++++++++++++++++++++++++++");
    print('프로필 삭제 API');
    print(childId);
    print(response.statusCode);
    print("++++++++++++++++++++++++++++++++++++++++");
    return response.statusCode;
  }
}
