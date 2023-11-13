import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:mobile/api/api_profileinfo.dart';
import 'package:mobile/controller/profile_select.dart';
import 'package:mobile/model/response_models/get_porfile.dart';
import 'package:mobile/screen/session_screen.dart';
import 'package:mobile/util/logout_logic.dart';

class Enter extends StatefulWidget {
  const Enter({Key? key}) : super(key: key);

  @override
  State<Enter> createState() => _EnterState();
}

class _EnterState extends State<Enter> {
  Apiprofileinfo apiProfileinfo = Apiprofileinfo();
  final ProfileController profileController = Get.find<ProfileController>();

  @override
  void initState() {
    super.initState();
    _getProfileInfo();
  }

  Future<void> _getProfileInfo() async {
    getProfileinfo? result = await apiProfileinfo.getprofileAPI();
    if (result != null && result.code == '200') {
      // Handle the result as needed
    } else {
      Logout();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        decoration: const BoxDecoration(
          image: DecorationImage(
            image: AssetImage('assets/enter.gif'),
            fit: BoxFit.cover,
            opacity: 0.5,
          ),
        ),
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 50.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              FutureBuilder<getProfileinfo?>(
                future: apiProfileinfo.getprofileAPI(),
                builder: (context, snapshot) {
                  if (snapshot.connectionState == ConnectionState.waiting) {
                    return const CircularProgressIndicator(
                      color: Colors.white,
                    ); // 데이터를 기다릴 동안 로딩 표시
                  } else if (snapshot.hasError) {
                    return Text('에러 발생: ${snapshot.error}');
                  } else if (!snapshot.hasData) {
                    return const Text('데이터 없음');
                  } else {
                    final profileInfo = snapshot.data!;
                    print('여기서 호출중');
                    print(
                        profileInfo.profile?.profileAnimon?.bodyImagePath);
                    return Obx(() {
                      return Image.network(
                        '${profileController.selectedProfile.value?.profileAnimon?.bodyImagePath ?? ''}',
                      );
                    });
                  }
                },
              ),
              const SizedBox(
                height: 50,
              ),
              Container(
                decoration: BoxDecoration(
                  gradient: const LinearGradient(
                    begin: Alignment.topLeft,
                    end: Alignment.bottomRight,
                    colors: [
                      Color(0xff16A085),
                      Color(0xffF4D03F),
                    ],
                  ),
                  borderRadius: BorderRadius.circular(16.0),
                ),
                child: ElevatedButton(
                  onPressed: () {
                    Get.to(() => SessionPage(), arguments: {'sessionId': ''});
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.transparent,
                    shadowColor: Colors.transparent,
                    minimumSize: const Size(400, 150),
                    maximumSize: const Size(600, 300),
                  ),
                  child: const Text(
                    '새 친구 만나기',
                    style: TextStyle(
                      fontSize: 36,
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
