import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';
import 'package:mobile/api/api_user.dart';
import 'package:mobile/model/request_models/put_login.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:mobile/screen/profiles/profiles_screen.dart';
import 'package:mobile/screen/signup_screen.dart';
import 'package:mobile/util/custom_text_field.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final loginFormKey = GlobalKey<FormState>();
  final storage = const FlutterSecureStorage();
  String fcmToken = '';
  String username = '';
  String password = '';
  generalResponse? loginAuth;

  @override
  void initState() {
    _initializeFCMToken();
    super.initState();
  }

  Future<void> _initializeFCMToken() async {
    fcmToken = (await storage.read(key: 'fcmToken')) ?? '';
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      body: Container(
        decoration: const BoxDecoration(
          image: DecorationImage(
              image: AssetImage('assets/login.gif'),
              fit: BoxFit.cover,
              opacity: 0.5),
        ),
        child: SafeArea(
          child: Center(
            child: SingleChildScrollView(
              scrollDirection: Axis.vertical,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Image.asset(
                    'assets/logo.png',
                    height: 100,
                    width: 300,
                  ),
                  const SizedBox(
                    height: 30,
                  ),
                  Container(
                    width: MediaQuery.of(context).size.width / 1.1,
                    decoration: BoxDecoration(
                      color: Colors.white.withOpacity(0.3),
                      borderRadius: BorderRadius.circular(20),
                    ),
                    key: loginFormKey,
                    child: Column(
                      children: [
                        CustomTextFormField(
                          labelText: '아이디',
                          onChanged: (val) {
                            username = val;
                          },
                          icon: Icons.person,
                          padding: const EdgeInsets.only(
                            left: 20,
                            right: 20,
                            bottom: 20,
                            top: 20,
                          ),
                        ),
                        CustomTextFormField(
                          labelText: '비밀번호',
                          onChanged: (val) {
                            password = val;
                          },
                          icon: Icons.lock,
                          obscureText: true,
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(
                    height: 30,
                  ),
                  ElevatedButton(
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
                      print(username);
                      print(password);

                      if (username.isEmpty || password.isEmpty) {
                        showDialog(
                            context: context,
                            barrierDismissible: false,
                            builder: (BuildContext ctx) {
                              return AlertDialog(
                                content: const Text('아이디와 비밀번호를 입력해 주세요'),
                                actions: [
                                  Center(
                                    child: TextButton(
                                      child: const Text('확인'),
                                      onPressed: () {
                                        Navigator.of(ctx).pop();
                                      },
                                    ),
                                  )
                                ],
                              );
                            });
                      } else {
                        loginFormKey.currentState?.save();
                        loginAuth = await ApiUser.postLogin(
                          LoginRequestModel(
                            id: username,
                            password: password,
                            fcmToken: fcmToken,
                          ),
                        );
                        if (loginAuth?.status == 'OK') {
                          Get.offAll(
                            () => ProfilesScreen(),
                            transition: Transition.zoom,
                          );
                        } else {
                          showDialog(
                            context: context,
                            barrierDismissible: false,
                            builder: (BuildContext ctx) {
                              return AlertDialog(
                                content: const Text('아이디와 비밀번호를 다시 입력해주세요'),
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
                    child: const Text(
                      '로그인',
                      style: TextStyle(fontSize: 17),
                    ),
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        '아직 회원이 아니신가요?',
                        style: TextStyle(
                          color: Colors.black.withOpacity(0.6),
                        ),
                      ),
                      TextButton(
                        onPressed: () {
                          Get.to(
                            () => const SignupScreen(),
                            transition: Transition.rightToLeft,
                          );
                        },
                        child: const Text(
                          '회원가입',
                          style: TextStyle(
                            color: Colors.blue,
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                      )
                    ],
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
