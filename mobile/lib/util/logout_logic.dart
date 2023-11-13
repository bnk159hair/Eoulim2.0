import 'package:get/get.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/api/api_profile.dart';
import 'package:mobile/api/api_user.dart';
import 'package:mobile/screen/login_screen.dart';
import 'package:mobile/screen/profiles/profiles_screen.dart';

const storage = FlutterSecureStorage();

void userLogout() async {
  await ApiUser.postLogout();
  await storage.delete(key: 'Authkey');
  Get.offAll(
    () => const LoginScreen(),
    transition: Transition.size,
  );
}

void profileLogout() async {
  await ApiProfile.postProfileLogout();
  await storage.delete(key: 'childId');
  Get.offAll(
    () => ProfilesScreen(),
    transition: Transition.size,
  );
}
