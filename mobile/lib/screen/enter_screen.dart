import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:mobile/controller/profile_select.dart';
import 'package:mobile/screen/session_screen.dart';

class Enter extends StatefulWidget {
  const Enter({super.key});

  @override
  State<Enter> createState() => _EnterState();
}

class _EnterState extends State<Enter> {
  final ProfileController profileController = Get.find<ProfileController>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        decoration: const BoxDecoration(
          image: DecorationImage(
              image: AssetImage('assets/enter.gif'),
              fit: BoxFit.cover,
              opacity: 0.5),
        ),
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 50.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Obx(() {
                return Image.network(
                  '${profileController.selectedProfile.value?.profileAnimon?.bodyImagePath ?? ''}',
                );
              }),
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
