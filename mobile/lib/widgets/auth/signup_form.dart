import 'package:flutter/material.dart';
import 'package:mobile/api/api_signup.dart';
import 'package:mobile/model/request_models/put_signup.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:mobile/screen/login_screen.dart';
import 'package:mobile/util/custom_text_field.dart';

class SignupForm extends StatefulWidget {
  const SignupForm({super.key});

  @override
  State<SignupForm> createState() => _SignupFormState();
}

class _SignupFormState extends State<SignupForm> {
  final signupFormKey = GlobalKey<FormState>();

  String username = '';
  String pw = '';
  String pwCheck = '';
  String name = '';
  String phoneNumber = '';
  generalResponse? signupAuth;
  ApiSignup apiSignup = ApiSignup();
  bool isClicked = false;
  bool isValidUsername = false;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Container(
          width: MediaQuery.of(context).size.width / 1.1,
          decoration: BoxDecoration(
            color: Colors.white.withOpacity(0.3),
            borderRadius: BorderRadius.circular(20),
          ),
          key: signupFormKey,
          child: Column(
            children: [
              Padding(
                padding: const EdgeInsets.all(20.0),
                child: Row(
                  children: [
                    Expanded(
                      child: CustomTextFormField(
                        labelText: "아이디",
                        onChanged: (val) {
                          username = val;
                        },
                        icon: Icons.person,
                        padding: const EdgeInsets.only(right: 20.0),
                      ),
                    ),
                    SizedBox(
                      width: 80,
                      height: 58,
                      child: ElevatedButton(
                        onPressed: () async {
                          if (username.isEmpty) {
                            return showDialog(
                              context: context,
                              barrierDismissible: false,
                              builder: (BuildContext ctx) {
                                return AlertDialog(
                                  content: const Text('아이디를 입력해 주세요!'),
                                  actions: [
                                    Center(
                                      child: TextButton(
                                        child: const Text('확인'),
                                        onPressed: () {
                                          Navigator.of(context).pop();
                                        },
                                      ),
                                    ),
                                  ],
                                );
                              },
                            );
                          } else {
                            final response =
                                await apiSignup.checkUsername(username);
                            if (response.data) {
                              showDialog(
                                context: context,
                                barrierDismissible: false,
                                builder: (BuildContext ctx) {
                                  return AlertDialog(
                                    content: const Text('사용 가능한 아이디입니다!'),
                                    actions: [
                                      Center(
                                        child: TextButton(
                                          child: const Text('확인'),
                                          onPressed: () {
                                            Navigator.of(context).pop();
                                          },
                                        ),
                                      ),
                                    ],
                                  );
                                },
                              );
                              setState(() {
                                isClicked = true;
                                isValidUsername = response.data;
                              });
                            } else {
                              showDialog(
                                context: context,
                                barrierDismissible: false,
                                builder: (BuildContext ctx) {
                                  return AlertDialog(
                                    content: const Text('이미 사용중인 아이디입니다!'),
                                    actions: [
                                      Center(
                                        child: TextButton(
                                          child: const Text('확인'),
                                          onPressed: () {
                                            Navigator.of(context).pop();
                                          },
                                        ),
                                      ),
                                    ],
                                  );
                                },
                              );
                            }
                          }
                        },
                        style: ElevatedButton.styleFrom(
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(10.0),
                          ),
                          backgroundColor: Colors.green,
                        ),
                        child: const Text(
                          '중복확인',
                          style: TextStyle(fontSize: 12),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              CustomTextFormField(
                labelText: "비밀번호",
                onChanged: (val) {
                  pw = val;
                },
                icon: Icons.lock,
                obscureText: true,
              ),
              CustomTextFormField(
                labelText: "비밀번호 확인",
                onChanged: (val) {
                  pwCheck = val;
                },
                icon: Icons.lock,
                obscureText: true,
              ),
              CustomTextFormField(
                labelText: "이름",
                onChanged: (val) {
                  name = val;
                },
                icon: Icons.account_box_rounded,
              ),
              CustomTextFormField(
                labelText: "휴대폰 번호",
                onChanged: (val) {
                  phoneNumber = val;
                },
                icon: Icons.phone,
                keyboardType: TextInputType.phone,
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
              if (username.isEmpty ||
                  pw.isEmpty ||
                  pwCheck.isEmpty ||
                  phoneNumber.isEmpty ||
                  name.isEmpty) {
                return showDialog(
                  context: context,
                  barrierDismissible: false,
                  builder: (BuildContext ctx) {
                    return AlertDialog(
                      content: const Text('모든 정보를 입력해 주세요!'),
                      actions: [
                        Center(
                          child: TextButton(
                            child: const Text('확인'),
                            onPressed: () {
                              Navigator.of(context).pop();
                            },
                          ),
                        ),
                      ],
                    );
                  },
                );
              }

              if (!isClicked) {
                return showDialog(
                  context: context,
                  barrierDismissible: false,
                  builder: (BuildContext ctx) {
                    return AlertDialog(
                      content: const Text('중복 확인을 해주세요!'),
                      actions: [
                        Center(
                          child: TextButton(
                            child: const Text('확인'),
                            onPressed: () {
                              Navigator.of(context).pop();
                            },
                          ),
                        ),
                      ],
                    );
                  },
                );
              }

              if (!isValidUsername) {
                return showDialog(
                  context: context,
                  barrierDismissible: false,
                  builder: (BuildContext ctx) {
                    return AlertDialog(
                      content: const Text('아이디가 중복되었습니다.'),
                      actions: [
                        Center(
                          child: TextButton(
                            child: const Text('확인'),
                            onPressed: () {
                              Navigator.of(context).pop();
                            },
                          ),
                        ),
                      ],
                    );
                  },
                );
              }

              if (pw != pwCheck) {
                return showDialog(
                  context: context,
                  barrierDismissible: false,
                  builder: (BuildContext ctx) {
                    return AlertDialog(
                      content: const Text('비밀번호가 일치하지 않습니다.'),
                      actions: [
                        Center(
                          child: TextButton(
                            child: const Text('확인'),
                            onPressed: () {
                              Navigator.of(context).pop();
                            },
                          ),
                        ),
                      ],
                    );
                  },
                );
              }

              signupFormKey.currentState?.save();
              signupAuth = await apiSignup.signup(
                SignupRequestModel(
                  username: username,
                  password: pw,
                  name: name,
                  phoneNumber: phoneNumber,
                ),
              );

              if (signupAuth?.status == 'CREATED') {
                showDialog(
                  context: context,
                  barrierDismissible: false,
                  builder: (BuildContext ctx) {
                    return AlertDialog(
                      content: const Text('회원가입이 완료되었습니다'),
                      actions: [
                        Center(
                          child: TextButton(
                            child: const Text('로그인'),
                            onPressed: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (context) => const Login(),
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
                              Navigator.of(context).pop();
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
              '회원가입',
              style: TextStyle(fontSize: 17),
            ),
          ),
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              '이미 회원이신가요?',
              style: TextStyle(
                color: Colors.black.withOpacity(0.6),
                fontWeight: FontWeight.w600,
              ),
            ),
            TextButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => const Login(),
                  ),
                );
              },
              child: const Text(
                '로그인하러 가기',
                style: TextStyle(
                  color: Colors.blue,
                  fontWeight: FontWeight.w600,
                ),
              ),
            )
          ],
        ),
      ],
    );
  }
}
