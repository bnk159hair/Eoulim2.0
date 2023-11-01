import 'package:flutter/material.dart';
import 'package:mobile/screen/session_screen.dart';

class Join extends StatefulWidget {
  const Join({super.key});

  @override
  State<Join> createState() => _JoinState();
}

class _JoinState extends State<Join> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('새 친구'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            // 로그인 버튼
            ElevatedButton(
              onPressed: () {
                // 여기에 로그인 로직을 추가하세요
                Navigator.push(context,
                    MaterialPageRoute(builder: (context) => const Session()));
                // 사용자명과 비밀번호는 _usernameController.text와 _passwordController.text로 얻을 수 있습니다.
              },
              child: const Text('새 친구'),
            ),
          ],
        ),
      ),
    );
  }
}
