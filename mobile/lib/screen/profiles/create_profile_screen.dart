import 'package:flutter/material.dart';
import 'package:custom_radio_grouped_button/custom_radio_grouped_button.dart';
import 'package:mobile/screen/profiles/profiles_screen.dart';

class CreateProfile extends StatefulWidget {
  const CreateProfile({super.key});

  @override
  State<CreateProfile> createState() => _CreateProfileState();
}

class _CreateProfileState extends State<CreateProfile> {
  String? _name;
  String? _school;
  int _grade = 1;
  String _gender = "M";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xff793939),
      appBar: AppBar(
        backgroundColor: const Color(0xffffffff),
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.black),
          onPressed: () => Navigator.of(context).pop(),
        ),
      ),
      body: Column(
        children: <Widget>[
          Row(
            children: [
              TextFormField(
                onChanged: (value) {
                  _school = value;
                },
                decoration: const InputDecoration(
                  focusedBorder: UnderlineInputBorder(
                    borderSide: BorderSide.none,
                    borderRadius: BorderRadius.all(Radius.circular(10)),
                  ),
                  enabledBorder: UnderlineInputBorder(
                    borderSide: BorderSide.none,
                    borderRadius: BorderRadius.all(Radius.circular(10)),
                  ),
                  prefixIcon: Icon(
                    Icons.school,
                    color: Colors.green,
                  ),
                  filled: true,
                  fillColor: Colors.white,
                  labelText: "학교",
                ),
              ),
              ElevatedButton(
                onPressed: () {},
                child: const Text("학교 확인"),
              ),
            ],
          ),
          const SizedBox(
            height: 30,
          ),
          Row(
            children: [
              TextFormField(
                onChanged: (value) {
                  _name = value;
                },
                decoration: const InputDecoration(
                  focusedBorder: UnderlineInputBorder(
                    borderSide: BorderSide.none,
                    borderRadius: BorderRadius.all(Radius.circular(10)),
                  ),
                  enabledBorder: UnderlineInputBorder(
                    borderSide: BorderSide.none,
                    borderRadius: BorderRadius.all(Radius.circular(10)),
                  ),
                  prefixIcon: Icon(
                    Icons.school,
                    color: Colors.green,
                  ),
                  filled: true,
                  fillColor: Colors.white,
                  labelText: "이름",
                ),
              ),
              CustomRadioButton(
                elevation: 0,
                // padding: EdgeInsets.symmetric(vertical: ),
                absoluteZeroSpacing: true,
                unSelectedColor: Colors.white,
                selectedBorderColor: Colors.green,
                unSelectedBorderColor: Colors.white,
                buttonLables: const [
                  '남자',
                  '여자',
                ],
                buttonValues: const [
                  'M',
                  'W',
                ],
                buttonTextStyle: const ButtonTextStyle(
                  selectedColor: Colors.white,
                  unSelectedColor: Colors.black,
                  textStyle: TextStyle(fontSize: 16),
                ),
                radioButtonValue: (value) {
                  setState(() {
                    _gender = value;
                  });
                },
                selectedColor: Colors.green,
              ),
            ],
          ),
          const SizedBox(
            height: 30,
          ),
          Row(
            children: [
              CustomRadioButton(
                elevation: 0,
                // padding: EdgeInsets.symmetric(vertical: ),
                absoluteZeroSpacing: true,
                unSelectedColor: Colors.white,
                selectedBorderColor: Colors.green,
                unSelectedBorderColor: Colors.white,
                buttonLables: const [
                  '1학년',
                  '2학년',
                  '3학년',
                ],
                buttonValues: const [
                  1,
                  2,
                  3,
                ],
                buttonTextStyle: const ButtonTextStyle(
                  selectedColor: Colors.white,
                  unSelectedColor: Colors.black,
                  textStyle: TextStyle(fontSize: 16),
                ),
                radioButtonValue: (value) {
                  setState(() {
                    _grade = value;
                  });
                },
                selectedColor: Colors.green,
              ),
            ],
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
              if (_name!.isEmpty) {
                showDialog(
                  context: context,
                  builder: (BuildContext context) {
                    return const Dialog(
                      child: Text("이름을 입력해주세요."),
                    );
                  },
                );
              } else if (_school!.isEmpty) {
                showDialog(
                  context: context,
                  builder: (BuildContext context) {
                    return const Dialog(
                      child: Text("학교를 입력해주세요."),
                    );
                  },
                );
                // } else {
                //   loginFormKey.currentState?.save();
                //   loginAuth = await apiLogin
                //       .login(LoginRequestModel(id: id, password: pw));
                //   if (loginAuth?.statusCode == 'SUCCESS') {
                //     Navigator.push(
                //       context,
                //       MaterialPageRoute(
                //         builder: (context) => const Profiles(),
                //       ),
                //     );
              } else {
                showDialog(
                    context: context,
                    builder: (BuildContext context) {
                      return const Dialog(
                        child: Text('알 수 없는 오류'),
                      );
                    });
              }
            },
            child: const Text(
              '생성',
              style: TextStyle(
                fontSize: 22,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
