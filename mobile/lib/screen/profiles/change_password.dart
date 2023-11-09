import 'package:flutter/material.dart';
import 'package:mobile/api/api_change_password.dart';
import 'package:mobile/screen/login_screen.dart';
import 'package:mobile/util/custom_text_field.dart';

class ChangePassword extends StatefulWidget {
  const ChangePassword({super.key});

  @override
  State<ChangePassword> createState() => _ChangePasswordState();
}

class _ChangePasswordState extends State<ChangePassword> {
  String curPassword = '';
  String newPassword = '';
  String passwordConfirm = '';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      body: Container(
        decoration: const BoxDecoration(
          image: DecorationImage(
            image: AssetImage('assets/login.gif'),
            fit: BoxFit.cover,
            opacity: 0.5,
          ),
        ),
        child: SafeArea(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Padding(
                padding: const EdgeInsets.all(20.0),
                child: Image.asset(
                  'assets/logo.png',
                  width: 150,
                ),
              ),
              Column(
                children: [
                  Container(
                    // width: MediaQuery.of(context).size.width / 1.1,
                    decoration: BoxDecoration(
                      color: Colors.white.withOpacity(0.3),
                      borderRadius: BorderRadius.circular(20),
                    ),
                    child: Column(
                      children: [
                        CustomTextFormField(
                          labelText: "현재 비밀번호",
                          onChanged: (value) {
                            curPassword = value;
                          },
                          icon: Icons.lock_open_rounded,
                          obscureText: true,
                        ),
                        CustomTextFormField(
                          labelText: "새 비밀번호",
                          onChanged: (value) {
                            newPassword = value;
                          },
                          icon: Icons.lock,
                          obscureText: true,
                        ),
                        CustomTextFormField(
                          labelText: "비밀번호 확인",
                          onChanged: (value) {
                            passwordConfirm = value;
                          },
                          icon: Icons.lock,
                          obscureText: true,
                        ),
                      ],
                    ),
                  ),
                  Container(
                    margin: const EdgeInsets.only(top: 20),
                    child: ElevatedButton(
                      style: ElevatedButton.styleFrom(
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(10.0),
                        ),
                        backgroundColor: Colors.green,
                        padding: EdgeInsets.symmetric(
                          horizontal: MediaQuery.of(context).size.width / 3.3,
                          vertical: 20,
                        ),
                      ),
                      onPressed: () async {
                        if (curPassword.isEmpty ||
                            newPassword.isEmpty ||
                            passwordConfirm.isEmpty) {
                          return showDialog(
                            context: context,
                            builder: (BuildContext ctx) {
                              return AlertDialog(
                                content: const Text('모든 정보를 입력해 주세요!'),
                                actions: [
                                  Center(
                                    child: TextButton(
                                      child: const Text('확인'),
                                      onPressed: () {
                                        Navigator.of(ctx).pop();
                                      },
                                    ),
                                  ),
                                ],
                              );
                            },
                          );
                        }

                        if (newPassword != passwordConfirm) {
                          return showDialog(
                            context: context,
                            builder: (BuildContext ctx) {
                              return AlertDialog(
                                content: const Text('비밀번호가 일치하지 않습니다.'),
                                actions: [
                                  Center(
                                    child: TextButton(
                                      child: const Text('확인'),
                                      onPressed: () {
                                        Navigator.of(ctx).pop();
                                      },
                                    ),
                                  ),
                                ],
                              );
                            },
                          );
                        }

                        final response =
                            await ApiChangePassword.patchChangePassword(
                          curPassword,
                          newPassword,
                        );

                        if (response.code == '400') {
                          if (!mounted) return;
                          return showDialog(
                            context: context,
                            builder: (BuildContext ctx) {
                              return AlertDialog(
                                content: const Text('현재 비밀번호가 틀렸습니다'),
                                actions: [
                                  Center(
                                    child: TextButton(
                                      child: const Text('확인'),
                                      onPressed: () {
                                        Navigator.of(ctx).pop();
                                      },
                                    ),
                                  ),
                                ],
                              );
                            },
                          );
                        }

                        if (response.code == '204') {
                          if (!mounted) return;
                          showDialog(
                            context: context,
                            builder: (BuildContext ctx) {
                              return AlertDialog(
                                content: const Text('비밀번호 수정이 완료되었습니다'),
                                actions: [
                                  Center(
                                    child: TextButton(
                                      child: const Text('로그인'),
                                      onPressed: () {
                                        Navigator.push(
                                          context,
                                          MaterialPageRoute(
                                            builder: (ctx) => const Login(),
                                          ),
                                        );
                                      },
                                    ),
                                  ),
                                ],
                              );
                            },
                          );
                        } else {
                          if (!mounted) return;
                          showDialog(
                            context: context,
                            barrierDismissible: false,
                            builder: (BuildContext ctx) {
                              return AlertDialog(
                                content: const Text('알 수 없는 오류'),
                                actions: [
                                  Center(
                                    child: TextButton(
                                      child: const Text('확인'),
                                      onPressed: () {
                                        Navigator.of(ctx).pop();
                                      },
                                    ),
                                  ),
                                ],
                              );
                            },
                          );
                        }
                      },
                      child: const Text(
                        '비밀번호 수정',
                        style: TextStyle(fontSize: 17),
                      ),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
