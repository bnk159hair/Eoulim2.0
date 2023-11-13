import 'package:flutter/material.dart';
import 'package:mobile/api/api_checkpassword.dart';
import 'package:mobile/api/api_records.dart';
import 'package:mobile/model/response_models/post_check_info.dart';
import 'package:mobile/screen/video_screen.dart';
import 'package:mobile/util/custom_text_field.dart';

class Record extends StatefulWidget {
  const Record({super.key});

  @override
  State<Record> createState() => _RecordState();
}

class _RecordState extends State<Record> {
  String pw = '';
  PostCheckInfo? passwordInfo;
  ApiCheckPassword checkPassword = ApiCheckPassword();
  bool checkingpassword = false;
  Future<dynamic>? recordList;

  @override
  void initState() {
    super.initState();
    recordList = ApiRecords.getRecords();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      extendBodyBehindAppBar: true,
      resizeToAvoidBottomInset: false,
      appBar: AppBar(
        title: const Text(
          '녹화 영상',
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
            image: AssetImage('assets/record.gif'),
            fit: BoxFit.cover,
            opacity: 0.5,
          ),
        ),
        child: SafeArea(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              checkingpassword
                  ? Expanded(
                      child: FutureBuilder(
                        future: recordList,
                        builder: (context, snapshot) {
                          if (snapshot.hasData) {
                            return ListView.separated(
                              itemCount: snapshot.data!.length,
                              itemBuilder: (context, index) {
                                var record = snapshot.data![index];
                                return Card(
                                  elevation: 2.0,
                                  shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(20.0),
                                  ),
                                  child: Row(
                                    mainAxisAlignment:
                                        MainAxisAlignment.spaceBetween,
                                    crossAxisAlignment:
                                        CrossAxisAlignment.center,
                                    children: [
                                      Text(record.animonName),
                                      Column(
                                        children: [
                                          Text(record.name),
                                          Text(record.createTime.join('-')),
                                        ],
                                      ),
                                      IconButton(
                                        onPressed: () {
                                          Navigator.push(
                                            context,
                                            MaterialPageRoute(
                                              builder: (context) => PlayVideo(
                                                  videoPath: record.videoPath),
                                            ),
                                          );
                                        },
                                        icon: const Icon(
                                          Icons.play_circle_outline_rounded,
                                        ),
                                      ),
                                    ],
                                  ),
                                );
                              },
                              separatorBuilder: (context, index) {
                                return const SizedBox(
                                  height: 20,
                                );
                              },
                            );
                          }
                          return const Center(
                            child: CircularProgressIndicator(),
                          );
                        },
                      ),
                    )
                  : Expanded(
                      child: Center(
                        child: Container(
                          decoration: const BoxDecoration(
                            image: DecorationImage(
                              image: AssetImage('assets/passwordbox.png'),
                              fit: BoxFit.fitHeight,
                            ),
                          ),
                          height: 250,
                          padding: const EdgeInsets.all(16.0),
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              CustomTextFormField(
                                labelText: "비밀번호 확인",
                                onChanged: (val) {
                                  pw = val;
                                },
                                icon: Icons.lock,
                                obscureText: true,
                              ),
                              ElevatedButton(
                                style: ElevatedButton.styleFrom(
                                  shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(10.0),
                                  ),
                                  backgroundColor: Colors.green,
                                  padding: EdgeInsets.symmetric(
                                    horizontal:
                                        MediaQuery.of(context).size.width / 3.3,
                                    vertical: 20,
                                  ),
                                ),
                                onPressed: () async {
                                  print(pw);

                                  if (pw.isEmpty) {
                                    showDialog(
                                      context: context,
                                      barrierDismissible: false,
                                      builder: (BuildContext ctx) {
                                        return AlertDialog(
                                          content: const Text('비밀번호를 입력해 주세요'),
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
                                    passwordInfo = await checkPassword
                                        .postCheckPasswordAPI(pw);
                                    if (passwordInfo?.data == true) {
                                      setState(() {
                                        checkingpassword = true;
                                      });
                                      showDialog(
                                        context: context,
                                        barrierDismissible: false,
                                        builder: (BuildContext ctx) {
                                          return AlertDialog(
                                            content: const Text('확인이 완료되었습니다'),
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
                                      showDialog(
                                        context: context,
                                        barrierDismissible: false,
                                        builder: (BuildContext ctx) {
                                          return AlertDialog(
                                            content:
                                                const Text('비밀번호를 다시 입력해주세요'),
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
                                child: const Text(
                                  '비밀번호 확인',
                                  style: TextStyle(fontSize: 14),
                                ),
                              ),
                            ],
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
