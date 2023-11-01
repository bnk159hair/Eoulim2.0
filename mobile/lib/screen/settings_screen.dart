import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/screen/login_screen.dart';
import 'package:get/get.dart';


class Settings extends StatefulWidget {
  const Settings({Key? key});

  @override
  State<Settings> createState() => _SettingsState();
}

class _SettingsState extends State<Settings> {
  void Logout() async {
    final storage = new FlutterSecureStorage();
    await storage.delete(key: 'Authkey');
    Get.offAll(() => Login());
    // Navigator.of(context).pushReplacement(
    //   MaterialPageRoute(
    //     builder: (context) => Login(),
    //   ),
    // );
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
             child:  Text('로그아웃', style: TextStyle(color: Colors.white)))
          ],
        ),
      ),
    );
  }
}
