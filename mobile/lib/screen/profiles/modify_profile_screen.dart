import 'package:flutter/material.dart';
import 'package:mobile/widgets/profile/modify_profile_form.dart';

class ModifyProfileScreen extends StatelessWidget {
  const ModifyProfileScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      extendBodyBehindAppBar: true,
      resizeToAvoidBottomInset: false,
      appBar: AppBar(
        title: const Text(
          '프로필 수정',
          style: TextStyle(
            fontSize: 24,
          ),
        ),
        elevation: 1,
        centerTitle: true,
        backgroundColor: Colors.black.withOpacity(0.2),
      ),
      body: Container(
        decoration: const BoxDecoration(
          image: DecorationImage(
            image: AssetImage('assets/settings.jpg'),
            fit: BoxFit.cover,
            opacity: 0.5,
          ),
        ),
        child: const SafeArea(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Center(
                child: ModifyProfileForm(),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
