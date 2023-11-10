import 'package:flutter/material.dart';
import 'package:custom_radio_grouped_button/custom_radio_grouped_button.dart';
import 'package:get/get.dart';

import 'package:mobile/api/api_createprofile.dart';
import 'package:mobile/model/request_models/put_createprofile.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:mobile/screen/profiles/profiles_screen.dart';
import 'package:mobile/util/custom_text_field.dart';

class CreateprofileForm extends StatefulWidget {
  const CreateprofileForm({super.key});

  @override
  State<CreateprofileForm> createState() => _CreateprofileFormState();
}

class _CreateprofileFormState extends State<CreateprofileForm> {
  final createprofileFormkey = GlobalKey<FormState>();

  String name = '';
  DateTime birth = DateTime.now();
  String school = '';
  String gender = 'M';
  int? grade = 1;
  generalResponse? profileCreate;
  bool isSelected = false;
  bool isClicked = false;
  bool isValidSchool = false;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Container(
          width: MediaQuery.of(context).size.width / 1.1,
          decoration: BoxDecoration(
            color: Colors.white.withOpacity(0.3),
            borderRadius: BorderRadius.circular(20),
          ),
          key: createprofileFormkey,
          child: Column(
            children: [
              Row(
                children: [
                  Expanded(
                    child: CustomTextFormField(
                      labelText: '이름',
                      onChanged: (val) {
                        name = val;
                      },
                      icon: Icons.account_circle_rounded,
                      padding: const EdgeInsets.all(20.0),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(right: 20.0),
                    child: CustomRadioButton(
                      elevation: 0,
                      absoluteZeroSpacing: true,
                      buttonLables: const [
                        "남",
                        "여",
                      ],
                      buttonValues: const [
                        'M',
                        'W',
                      ],
                      buttonTextStyle: ButtonTextStyle(
                        selectedColor: Colors.white,
                        unSelectedColor: Colors.black.withOpacity(0.6),
                        textStyle: const TextStyle(fontSize: 16),
                      ),
                      radioButtonValue: (value) {
                        setState(() {
                          gender = value;
                        });
                      },
                      defaultSelected: 'M',
                      selectedColor: Colors.green,
                      unSelectedColor: Theme.of(context).canvasColor,
                      selectedBorderColor: Colors.green,
                      unSelectedBorderColor: Colors.green,
                      width: 50,
                      height: 60,
                      enableShape: true,
                      shapeRadius: 5,
                    ),
                  ),
                ],
              ),
              Padding(
                padding: const EdgeInsets.fromLTRB(20.0, 0, 20.0, 20.0),
                child: Row(
                  children: [
                    Expanded(
                      child: CustomTextFormField(
                        labelText: '초등학교명',
                        onChanged: (val) {
                          school = val;
                        },
                        icon: Icons.school_rounded,
                        padding: const EdgeInsets.only(right: 20.0),
                      ),
                    ),
                    SizedBox(
                      width: 100,
                      height: 58,
                      child: ElevatedButton(
                        onPressed: () async {
                          if (school.isEmpty) {
                            return showDialog(
                              context: context,
                              barrierDismissible: false,
                              builder: (BuildContext ctx) {
                                return AlertDialog(
                                  content: const Text('학교명을 입력해 주세요!'),
                                  actions: [
                                    Center(
                                      child: TextButton(
                                        child: const Text('확인'),
                                        onPressed: () {
                                          Navigator.of(ctx).pop();
                                        },
                                      ),
                                    ),
                                  ],
                                );
                              },
                            );
                          } else {
                            final response =
                                await ApiCreateprofile.postCheckShool(school);
                            if (response.data) {
                              if (!mounted) return;
                              showDialog(
                                context: context,
                                barrierDismissible: false,
                                builder: (BuildContext ctx) {
                                  return AlertDialog(
                                    content: const Text('확인되었습니다!'),
                                    actions: [
                                      Center(
                                        child: TextButton(
                                          child: const Text('확인'),
                                          onPressed: () {
                                            Navigator.of(ctx).pop();
                                          },
                                        ),
                                      ),
                                    ],
                                  );
                                },
                              );
                              setState(() {
                                isClicked = true;
                                isValidSchool = response.data;
                              });
                            } else {
                              if (!mounted) return;
                              showDialog(
                                context: context,
                                barrierDismissible: false,
                                builder: (BuildContext ctx) {
                                  return AlertDialog(
                                    content: const Text('올바른 학교명을 입력해 주세요!'),
                                    actions: [
                                      Center(
                                        child: TextButton(
                                          child: const Text('확인'),
                                          onPressed: () {
                                            Navigator.of(ctx).pop();
                                          },
                                        ),
                                      ),
                                    ],
                                  );
                                },
                              );
                            }
                          }
                        },
                        style: ElevatedButton.styleFrom(
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(10.0),
                          ),
                          backgroundColor: Colors.green,
                        ),
                        child: const Text(
                          '학교확인',
                          style: TextStyle(fontSize: 16),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              Padding(
                padding: const EdgeInsets.fromLTRB(20, 0, 20, 20),
                child: GestureDetector(
                  onTap: () async {
                    DateTime? newDate = await showDatePicker(
                      context: context,
                      initialDate: birth,
                      firstDate: DateTime(2000),
                      lastDate: DateTime.now(),
                      builder: (BuildContext context, Widget? child) {
                        return Theme(
                          data: ThemeData.light().copyWith(
                            primaryColor: Colors.green,
                            colorScheme:
                                const ColorScheme.light(primary: Colors.green),
                            buttonTheme: const ButtonThemeData(
                              textTheme: ButtonTextTheme.primary,
                            ),
                          ),
                          child: child!,
                        );
                      },
                    );
                    if (newDate != null) {
                      setState(() => {
                            birth = newDate,
                            isSelected = true,
                          });
                    }
                  },
                  child: Container(
                    padding: const EdgeInsets.symmetric(
                        vertical: 20.0, horizontal: 14.0),
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(10),
                    ),
                    child: Row(
                      children: [
                        const Icon(
                          Icons.calendar_month_rounded,
                          color: Colors.green,
                        ),
                        const SizedBox(
                          width: 10,
                        ),
                        Text(
                          isSelected
                              ? '${birth.year}년 ${birth.month}월 ${birth.day}일'
                              : '생년월일',
                          style: TextStyle(
                            fontSize: 16,
                            color: Colors.black.withOpacity(0.6),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(bottom: 20.0),
                child: CustomRadioButton(
                  elevation: 0,
                  buttonLables: const [
                    "1학년",
                    "2학년",
                    "3학년",
                  ],
                  buttonValues: const [
                    1,
                    2,
                    3,
                  ],
                  buttonTextStyle: ButtonTextStyle(
                    selectedColor: Colors.white,
                    unSelectedColor: Colors.black.withOpacity(0.6),
                    textStyle: const TextStyle(fontSize: 16),
                  ),
                  radioButtonValue: (value) {
                    setState(() {
                      grade = value;
                    });
                  },
                  defaultSelected: 1,
                  selectedColor: Colors.green,
                  unSelectedColor: Theme.of(context).canvasColor,
                  selectedBorderColor: Colors.green,
                  unSelectedBorderColor: Colors.green,
                  height: 50,
                  enableShape: true,
                  shapeRadius: 10,
                ),
              ),
            ],
          ),
        ),
        Container(
          margin: const EdgeInsets.only(top: 20),
          child: ElevatedButton(
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
              if (name.isEmpty || school.isEmpty || !isSelected) {
                return showDialog(
                  context: context,
                  barrierDismissible: false,
                  builder: (BuildContext ctx) {
                    return AlertDialog(
                      content: const Text('모든 정보를 입력해 주세요'),
                      actions: [
                        Center(
                          child: TextButton(
                            child: const Text('확인'),
                            onPressed: () {
                              Navigator.of(ctx).pop();
                            },
                          ),
                        ),
                      ],
                    );
                  },
                );
              }

              if (!isValidSchool) {
                return showDialog(
                  context: context,
                  barrierDismissible: false,
                  builder: (BuildContext ctx) {
                    return AlertDialog(
                      content: const Text('학교를 확인해 주세요'),
                      actions: [
                        Center(
                          child: TextButton(
                            child: const Text('확인'),
                            onPressed: () {
                              Navigator.of(ctx).pop();
                            },
                          ),
                        ),
                      ],
                    );
                  },
                );
              }

              createprofileFormkey.currentState?.save();
              profileCreate = await ApiCreateprofile.createprofile(
                CreateprofileRequestModel(
                  name: name,
                  birth: birth.toString().split(' ')[0],
                  gender: gender,
                  school: '$school초등학교',
                  grade: grade,
                ),
              );
              if (profileCreate?.status == 'CREATED') {
                if (!mounted) return;
                showDialog(
                  context: context,
                  barrierDismissible: false,
                  builder: (BuildContext ctx) {
                    return AlertDialog(
                      content: const Text('프로필 생성이 완료되었습니다'),
                      actions: [
                        Center(
                          child: TextButton(
                            child: const Text('프로필 선택창'),
                            onPressed: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (context) => Profiles(),
                                ),
                              );
                            },
                          ),
                        ),
                      ],
                    );
                  },
                );
              } else {
                if (!mounted) return;
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
                              Navigator.of(ctx).pop();
                            },
                          ),
                        ),
                      ],
                    );
                  },
                );
              }
            },
            child: const Text(
              '프로필 생성',
              style: TextStyle(fontSize: 17),
            ),
          ),
        ),
      ],
    );
  }
}
