import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/api/api_child.dart';
import 'package:mobile/api/api_profilelogout.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:get/get.dart';
import 'package:mobile/screen/profiles/modify_child_screen.dart';
import 'package:mobile/screen/profiles/profiles_screen.dart';
import 'package:mobile/util/logout_logic.dart';

class Settings extends StatelessWidget {
  Settings({super.key});

  generalResponse? profileloginAuth;

  ApiprofileLogout apiProfileLogout = ApiprofileLogout();

  void profileLogout() async {
    await apiProfileLogout.postProfileLogoutAPI();
    const storage = FlutterSecureStorage();
    await storage.delete(key: 'childId');
    Get.offAll(() => Profiles());
  }

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
                  const EdgeInsets.symmetric(vertical: 40.0, horizontal: 30.0),
              child: Column(
                children: [
                  _buildButtonRow(
                    '프로필 정보 수정',
                    onPressed: () {
                      Get.offAll(() => const ModifyChild());
                    },
                  ),
                  _buildButtonRow(
                    '프로필 정보 삭제',
                    onPressed: () async {
                      final response = await ApiChild.deleteChild();
                      if (response == 204) {
                        profileLogout();
                      } else if (response == 401) {
                        Logout();
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
                                      Logout();
                                    },
                                  ),
                                ),
                              ],
                            );
                          },
                        );
                      }
                    },
                  ),
                  _buildButtonRow(
                    '프로필 전환',
                    onPressed: profileLogout,
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
        bottom: 50.0,
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
