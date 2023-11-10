import 'package:flutter/material.dart';
import 'package:mobile/widgets/profile/modify_child_form.dart';

class ModifyChild extends StatelessWidget {
  const ModifyChild({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
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
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Padding(
                padding: EdgeInsets.all(20.0),
                child: Text(
                  '아이 정보 수정',
                  style: TextStyle(
                    fontSize: 30,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              Center(
                child: ModifyChildForm(),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
