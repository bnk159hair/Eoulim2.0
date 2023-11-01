import 'package:flutter/material.dart';
import 'package:mobile/api/api_createprofile.dart';
import 'package:mobile/model/request_models/put_createprofile.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:mobile/screen/profiles/profiles_screen.dart';
import 'package:flutter/cupertino.dart';


class CreateprofileForm extends StatefulWidget {
  const CreateprofileForm({super.key});

  @override
  State<CreateprofileForm> createState() => _CreateprofileFormState();
}

class _CreateprofileFormState extends State<CreateprofileForm> {
  final CreateprofileFormkey = GlobalKey<FormState>();

  String name = '';
  String birth = '';
  String gender = 'M';
  String school = '';
  int? grade = 1;
  generalResponse? profileCreate;
  ApiCreateprofile apiProfile = ApiCreateprofile();

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
          key: this.CreateprofileFormkey,
          child: Column(
            children: [
              Padding(
                padding: const EdgeInsets.only(
                  left: 20,
                  right: 20,
                  bottom: 20,
                  top: 20,
                ),
                child: TextFormField(
                  onChanged: (val) {
                    name = val;
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
                      Icons.person,
                      color: Colors.green,
                    ),
                    filled: true,
                    fillColor: Colors.white,
                    labelText: "이름",
                  ),
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(left: 20, right: 20, bottom: 20),
                child: 
                TextFormField(
                  onChanged: (val) {
                    birth = val;
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
                      Icons.lock,
                      color: Colors.green,
                    ),
                    filled: true,
                    fillColor: Colors.white,
                    labelText: "생년월일",
                  ),
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(left: 20, right: 20, bottom: 20),
                child: TextFormField(
                  onChanged: (val) {
                    gender = val;
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
                      Icons.person,
                      color: Colors.green,
                    ),
                    filled: true,
                    fillColor: Colors.white,
                    labelText: "성별",
                  ),
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(left: 20, right: 20, bottom: 20),
                child: TextFormField(
                  onChanged: (val) {
                    school = val;
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
                      Icons.phone,
                      color: Colors.green,
                    ),
                    filled: true,
                    fillColor: Colors.white,
                    labelText: "학교",
                  ),
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(left: 20, right: 20, bottom: 20),
                child: Row(
                  children: [
                    Expanded(
                      child: ListTile(
                        title: Text('1학년'),
                        leading: Radio<int>(
                          value: 1,
                          groupValue: grade,
                          onChanged: (value) {
                            setState(() {
                              grade = value;
                            });
                          },
                        ),
                      ),
                    ),
                    Expanded(
                      child: ListTile(
                        title: Text('2학년'),
                        leading: Radio<int>(
                          value: 2,
                          groupValue: grade,
                          onChanged: (value) {
                            setState(() {
                              grade = value;
                            });
                          },
                        ),
                      ),
                    ),
                    Expanded(
                      child: ListTile(
                        title: Text('3학년'),
                        leading: Radio<int>(
                          value: 3,
                          groupValue: grade,
                          onChanged: (value) {
                            setState(() {
                              grade = value;
                            });
                          },
                        ),
                      ),
                    ),
                  ],
                ),           
              ),
            ],
          ),
        ),
        Container(
          margin: EdgeInsets.only(top: 20), // 위쪽으로 10 픽셀의 여백을 추가합니다.
          child: ElevatedButton(
            style: ElevatedButton.styleFrom(
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(10.0),
              ),
              backgroundColor: Colors.blue,
              padding: EdgeInsets.symmetric(
                horizontal: MediaQuery.of(context).size.width / 3.3,
                vertical: 20,
              ),
            ),
            onPressed: () async {
                if (name.isEmpty || birth.isEmpty || school.isEmpty) {
                  showDialog(context: context,
                     barrierDismissible: false,
                     builder: (BuildContext ctx){
                      return AlertDialog(
                        content: Text('모든 정보를 입력해 주세요'),
                        actions: [
                          Center(
                            child: TextButton(
                              child: Text('확인'),
                              onPressed: (){
                              ;
                              }
                            )
                          )
                        ],
                      );
                     });
                } 
                else {
                  CreateprofileFormkey.currentState?.save();
                  profileCreate = await apiProfile.createprofile(CreateprofileRequestModel(name: name, birth: birth, gender: gender, school: school, grade: grade));
                  if (profileCreate?.status == 'OK') {
                    showDialog(context: context,
                     barrierDismissible: false,
                     builder: (BuildContext ctx){
                      return AlertDialog(
                        content: Text('프로필 생성이 완료되었습니다'),
                        actions: [
                          Center(
                            child: TextButton(
                              child: Text('로그인하러 가기'),
                              onPressed: (){
                                Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                    builder: (context) =>  Profiles(),
                                  ),
                                );
                              }
                            )
                          )
                        ],
                      );
                     });
                  } else {
                    showDialog(context: context,
                     barrierDismissible: false,
                     builder: (BuildContext ctx){
                      return AlertDialog(
                        content: Text('알 수 없는 오류'),
                        actions: [
                          Center(
                            child: TextButton(
                              child: Text('확인'),
                              onPressed: (){
                                Navigator.of(context).pop();
                              }
                            )
                          )
                        ],
                      );
                     });
                  }
                }
              },
            child: Text(
              '프로필 생성',
              style: TextStyle(fontSize: 17),
            ),
          ),
        ),
      ],
    );
  }
  
}