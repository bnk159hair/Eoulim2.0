import 'package:flutter/material.dart';
import 'package:mobile/screen/profiles_screen.dart';

class Login extends StatefulWidget {
  const Login({Key? key});

  @override
  State<Login> createState() => _LoginState();
}

class _LoginState extends State<Login> {
  // TextEditingController를 사용하여 입력 필드의 값을 관리
  TextEditingController _usernameController = TextEditingController();
  TextEditingController _passwordController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('로그인'),
      ),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            // 사용자명 입력 필드
            TextFormField(
              controller: _usernameController,
              decoration: InputDecoration(
                labelText: '사용자명',
              ),
            ),
            // 비밀번호 입력 필드
            TextFormField(
              controller: _passwordController,
              decoration: InputDecoration(
                labelText: '비밀번호',
              ),
              obscureText: true, // 비밀번호를 숨기기
            ),
            SizedBox(height: 20),
            // 로그인 버튼
            ElevatedButton(
              onPressed: () {
                // 여기에 로그인 로직을 추가하세요
                Navigator.push(context,
                    MaterialPageRoute(builder: (context) => const Profile()));
                // 사용자명과 비밀번호는 _usernameController.text와 _passwordController.text로 얻을 수 있습니다.
              },
              child: Text('로그인'),
            ),
          ],
        ),
      ),
    );
  }
}
