import 'package:get/get.dart';
import 'package:mobile/model/response_models/get_porfile.dart';

class ProfileController extends GetxController {
  // 선택한 프로필을 보유할 Rx<Profile> 변수를 정의합니다.
  var selectedProfile = Rx<Profile?>(null);

  // 선택한 프로필을 업데이트하는 메서드를 만듭니다.
  Future<void> updateSelectedProfile(Profile? profile) async {
    selectedProfile.value = profile;
  }
}
