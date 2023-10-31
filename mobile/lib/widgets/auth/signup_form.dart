import 'package:flutter/material.dart';
import 'package:mobile/api/api_signup.dart';
import 'package:mobile/model/request_models/put_signup.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:mobile/screen/login_screen.dart';

class SginupFrom extends StatefulWidget {
  const SginupFrom({super.key});

  @override
  State<SginupFrom> createState() => _SginupFromState();
}

class _SginupFromState extends State<SginupFrom> {
  final SignupFormKey = GlobalKey<FormState>();

  String id = '';
  String pw = '';
  String name = '';
  String phonenumber = '';
  generalResponse? signupAuth;
  ApiSignup apiSignup = ApiSignup();

  @override
  void initState() {
    super.initState();
  }

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
          key: this.SignupFormKey,
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
                    id = val;
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
                    labelText: "아이디",
                  ),
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(left: 20, right: 20, bottom: 20),
                child: TextFormField(
                  onChanged: (val) {
                    pw = val;
                  },
                  obscureText: true,
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
                    labelText: "비밀번호",
                  ),
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(left: 20, right: 20, bottom: 20),
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
                child: TextFormField(
                  onChanged: (val) {
                    phonenumber = val;
                  },
                  keyboardType: TextInputType.phone,
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
                    labelText: "휴대폰 번호",
                  ),
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
                print(id!);
                print(pw!);

                if (id!.isEmpty || pw!.isEmpty || phonenumber!.isEmpty || name!.isEmpty) {
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
                                Navigator.of(context).pop();
                              }
                            )
                          )
                        ],
                      );
                     });
                } 
                else {
                  SignupFormKey.currentState?.save();
                  signupAuth = await apiSignup.signup(SignupRequestModel(id: id, password: pw, name: name, phoneNumber: phonenumber));
                  if (signupAuth?.status == 'OK') {
                    showDialog(context: context,
                     barrierDismissible: false,
                     builder: (BuildContext ctx){
                      return AlertDialog(
                        content: Text('회원가입이 완료되었습니다'),
                        actions: [
                          Center(
                            child: TextButton(
                              child: Text('로그인하러 가기'),
                              onPressed: (){
                                Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                    builder: (context) =>  Login(),
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
              '회원가입',
              style: TextStyle(fontSize: 17),
            ),
          ),
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              '이미 회원이신가요?',
              style: TextStyle(
                color: Colors.black.withOpacity(0.6),
              ),
            ),
            TextButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => Login(),
                  )
                );
              },
              child: Text(
                '로그인하러 가기',
                style: TextStyle(
                  color: Colors.blue,
                  fontWeight: FontWeight.w500,
                ),
              ),
            )
          ],
        ),
      ],
    );
  }
}
