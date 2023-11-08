import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';
import 'package:mobile/api/api_logout.dart';
import 'package:mobile/screen/login_screen.dart';

final ApiLogout apiLogout = ApiLogout();

void Logout() async {
    await apiLogout.postLogoutAPI();
    final storage = new FlutterSecureStorage();
    await storage.delete(key: 'Authkey');
    Get.offAll(() => Login());
    }