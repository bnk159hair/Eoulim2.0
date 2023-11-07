import 'package:flutter/material.dart';
import 'package:mobile/screen/home_screen.dart';
import 'package:mobile/screen/login_screen.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/screen/profiles/profiles_screen.dart';

class Splash extends StatefulWidget {
  const Splash({Key? key}) : super(key: key);

  @override
  SplashState createState() => SplashState();
}

class SplashState extends State<Splash> {
  bool isLoading = true; // 추가: 로딩 상태를 나타내는 변수

  @override
  void initState() {
    super.initState();
    checkLoginStatus();
  }

  Future<void> checkLoginStatus() async {
    final storage = new FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    String? childId = await storage.read(key: 'childId');

    await Future.delayed(Duration(seconds: 2)); // 가짜 로딩 시간을 나타내는 코드 (실제 앱에서는 필요 없음)

    if (authKey != null && childId != null) {
      // 'Authkey' 키에 값이 이미 저장되어 있으면 메인 페이지로 이동
      Navigator.of(context).pushReplacement(
        MaterialPageRoute(
          builder: (context) => Home(),
        ),
      );
    } 
    else if (authKey != null){
      Navigator.of(context).pushReplacement(
        MaterialPageRoute(
          builder: (context) => Profiles(),
        ),
      );
    }
    else {
      Navigator.of(context).pushReplacement(
        MaterialPageRoute(
          builder: (context) => Login(),
        ),
      );
    }

    // 비동기 작업이 완료되면 isLoading를 false로 설정
    setState(() {
      isLoading = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Color(0xFFB1EFBC), // 바탕 색상 설정
      body: Stack(
        children: [
          Center(
            child: Image.asset('assets/logo.png', width: 300),
          ),
          Positioned(
            bottom: 0,
            left: 0,
            right: 0,
            child: Image.asset('assets/landingbottom.png'),
          ),
        ],
      ),
    );
  }
}
