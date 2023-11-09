import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/controller/profile_select.dart';
import 'package:mobile/model/response_models/get_porfile.dart';

class Apiprofileinfo {
  Future<getProfileinfo> getprofileAPI() async {
    const storage = FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    String? childId = await storage.read(key: 'childId');
    String url = "https://k9c103.p.ssafy.io/api/v1/children/$childId";
    final response = await http.get(Uri.parse(url), headers: <String, String>{
      'Authorization': "Bearer $authKey",
      'Content-Type': 'application/json; charset=UTF-8',
    });
    if (response.statusCode == 401) {
      return getProfileinfo(
          response.statusCode.toString(), response.reasonPhrase, null);
    } else {
      String responseBody = utf8.decode(response.bodyBytes);
      getProfileinfo profileInfo =
          getProfileinfo.fromJson(json.decode(responseBody));
      print(response.body);
      print("++++++++++++++++++++++++++++++++++++++++");
      print(profileInfo);
      ProfileController profileController = Get.find();
      // 응답값을 ProfileController에 저장
      await profileController.updateSelectedProfile(profileInfo.profile);
      return profileInfo;
    }
  }
}
