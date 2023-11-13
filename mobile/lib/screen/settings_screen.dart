import 'package:flutter/material.dart';
import 'package:mobile/api/api_profile.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:get/get.dart';
import 'package:mobile/screen/profiles/modify_profile_screen.dart';
import 'package:mobile/util/logout_logic.dart';

class SettingsScreen extends StatelessWidget {
  SettingsScreen({super.key});

  generalResponse? profileloginAuth;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SizedBox.expand(
        child: Container(
          decoration: const BoxDecoration(
            image: DecorationImage(
              image: AssetImage('assets/settings.jpg'),
              fit: BoxFit.cover,
              opacity: 0.5,
            ),
          ),
          child: SafeArea(
            child: Padding(
              padding:
                  const EdgeInsets.symmetric(vertical: 10.0, horizontal: 30.0),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Column(
                    children: [
                      _buildButtonRow(
                        '프로필 정보 수정',
                        onPressed: () {
                          Get.to(() => const ModifyProfileScreen());
                        },
                      ),
                      _buildButtonRow(
                        '프로필 전환',
                        onPressed: profileLogout,
                      ),
                    ],
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(bottom: 30),
                        child: TextButton(
                          onPressed: () {
                            Get.defaultDialog(
                              title: '정말로 삭제하시겠습니까?',
                              middleText: '되돌릴 수 없습니다!',
                              textConfirm: '삭제',
                              textCancel: '취소',
                              buttonColor: Colors.red,
                              confirmTextColor: Colors.white,
                              cancelTextColor: Colors.red,
                              onConfirm: () async {
                                final response =
                                    await ApiProfile.deleteProfile();
                                if (response == 204) {
                                  profileLogout();
                                } else if (response == 401) {
                                  userLogout();
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
                                                profileLogout();
                                                userLogout();
                                              },
                                            ),
                                          ),
                                        ],
                                      );
                                    },
                                  );
                                }
                              },
                            );
                          },
                          style: TextButton.styleFrom(
                            side: BorderSide(
                              color: Colors.black.withOpacity(0.9),
                              width: 1.0,
                            ),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(10),
                            ),
                            padding: const EdgeInsets.symmetric(
                              vertical: 14,
                              horizontal: 80,
                            ),
                          ),
                          child: Text(
                            '프로필 정보 삭제',
                            style: TextStyle(
                              color: Colors.black.withOpacity(0.9),
                              fontSize: 20,
                            ),
                          ),
                        ),
                      ),
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

Widget _buildButtonRow(String buttonText, {VoidCallback? onPressed}) {
  return InkWell(
    onTap: onPressed,
    splashColor: Colors.transparent,
    highlightColor: Colors.transparent,
    child: Padding(
      padding: const EdgeInsets.only(
        bottom: 25.0,
        top: 25.0,
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(
            buttonText,
            style: TextStyle(
              color: Colors.black.withOpacity(0.7),
              fontSize: 24,
            ),
          ),
          Icon(
            Icons.chevron_right,
            color: Colors.black.withOpacity(0.7),
          ),
        ],
      ),
    ),
  );
}
