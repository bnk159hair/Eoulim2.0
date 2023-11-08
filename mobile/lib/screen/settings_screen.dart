import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/api/api_profilelogout.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:get/get.dart';
import 'package:mobile/screen/profiles/profiles_screen.dart';
import 'package:mobile/util/logout_logic.dart';


class Settings extends StatefulWidget {
  const Settings({Key? key});

  @override
  State<Settings> createState() => _SettingsState();
}

class _SettingsState extends State<Settings> {
  generalResponse? profileloginAuth;
  ApiprofileLogout apiProfileLogout = ApiprofileLogout();


  void profileLogout() async {
    await apiProfileLogout.postProfileLogoutAPI();
    final storage = new FlutterSecureStorage();
    await storage.delete(key: 'childId');
    Get.offAll(() => Profiles());
  }



  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('설정'),
      ),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            ElevatedButton(onPressed: (){
              Logout();
            },
            style: ButtonStyle(
                backgroundColor: MaterialStateProperty.all(Colors.red)),
             child:  Text('로그아웃', style: TextStyle(color: Colors.white))),
          ElevatedButton(
              onPressed: () {
                profileLogout();
              },
              style: ButtonStyle(
                backgroundColor: MaterialStateProperty.all(Colors.blue),
              ),
              child: Text('프로필로 돌아가기', style: TextStyle(color: Colors.white)),
            ),
          ],
        ),
      ),
    );
  }
}
