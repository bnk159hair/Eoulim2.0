import 'package:flutter/material.dart';
import 'package:mobile/screen/login_screen.dart';

class Splash extends StatelessWidget {
  const Splash({Key? key});

  @override
  Widget build(BuildContext context) {
    Future.delayed(Duration(seconds: 2), () {
      Navigator.of(context).pushReplacement(
        MaterialPageRoute(
          builder: (context) => Login(),
        ),
      );
    });

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
