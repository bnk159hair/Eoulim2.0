import 'package:flutter/material.dart';
import 'package:mobile/api/api_alarm.dart';
import 'package:mobile/api/api_deletealarm.dart';
import 'package:mobile/model/response_models/get_alarm.dart';
import 'package:mobile/util/logout_logic.dart';

// import 'package:flutter_secure_storage/flutter_secure_storage.dart';

// import 'package:mobile/screen/login_screen.dart';

// class NotificationItem {
//   final String content;
//   final String date;

//   NotificationItem(
//     this.content,
//     this.date,
//   );
// }

class Notifications extends StatefulWidget {
  Notifications({super.key});
  Apialarm apialarm =Apialarm();
  List<Alarm> alarms = List.empty();

  @override
  State<Notifications> createState() => _NotificationsState();
}

class _NotificationsState extends State<Notifications> {
  ApiDeletealarm deletealarm = ApiDeletealarm();
  // void logout() async {
  //   const storage = FlutterSecureStorage();
  //   await storage.delete(key: 'Authkey');
  //   if (!mounted) return;
  //   Navigator.of(context).pushReplacement(
  //     MaterialPageRoute(
  //       builder: (context) => const Login(),
  //     ),
  //   );
  // }
  void initState() {
    super.initState();
    _getAlarms();
  }
  Future<void> _getAlarms() async {
    GetAlarm result = await widget.apialarm.getalarmAPI();
    if (result.code == '200') {
      setState(() {
        widget.alarms = result.alarms!;
      });
    } else if (result.code == '401') {
      Logout();
    } else {
      showDialog(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            content: Text('${result.status}'),
            actions: [
              Center(
                child: TextButton(
                  child: const Text('확인'),
                  onPressed: () {
                    Navigator.of(context).pop();
                  },
                ),
              ),
            ],
          );
        },
      );
    }
  }
  // final List<NotificationItem> notificationItems = [
  //   NotificationItem('친구가 초대했어요.', '11월 1일'),
  //   NotificationItem('친구가 초대했어요.', '11월 1일'),
  //   NotificationItem('친구가 초대했어요.', '11월 1일'),
  // ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('알림'),
        centerTitle: true,
        actions: [
          IconButton(
            onPressed: () {
              showDialog(
                context: context,
                barrierDismissible: false,
                builder: (BuildContext ctx) {
                  return CustomDialog(
                    children: [
                      const Text(
                        '알람을 삭제하시겠습니까?',
                        style: TextStyle(
                          fontSize: 24,
                          color: Colors.white,
                        ),
                      ),
                      const SizedBox(height: 16),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          ElevatedButton(
                            onPressed: () async{
                              final response = await deletealarm.deletealarm();
                              if(response.code == '200') {
                                await _getAlarms();
                                Navigator.of(ctx).pop();
                              }
                              else{
                                Navigator.of(ctx).pop();
                              }
                            },
                            style: ElevatedButton.styleFrom(
                              backgroundColor: const Color(0xffff6347),
                            ),
                            child: const Text(
                              '삭제하기',
                            ),
                          ),
                          const SizedBox(width: 16),
                          ElevatedButton(
                            onPressed: () {
                              Navigator.of(ctx).pop();
                            },
                            child: const Text('취소하기'),
                          ),
                        ],
                      ),
                    ],
                  );
                },
              );
            },
            icon: const Icon(Icons.delete),
          ),
        ],
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(16.0),
        itemCount: widget.alarms.length,
        itemBuilder: (context, index) {
          return Container(
            height: 80,
            padding: const EdgeInsets.symmetric(horizontal: 24.0),
            margin: const EdgeInsets.only(bottom: 16.0),
            decoration: BoxDecoration(
              image: const DecorationImage(
                image: AssetImage('assets/dialog.png'),
                fit: BoxFit.cover,
              ),
              borderRadius: BorderRadius.circular(16.0),
            ),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Row(
                      children: [
                        Container(
                          width: 50,
                          height: 50,
                          decoration: BoxDecoration(
                            shape: BoxShape.circle,
                            border: Border.all(
                              width: 1.0,
                              color: Colors.black.withOpacity(0.4),
                            ),
                          ),
                          child: Icon(
                            Icons.mail,
                            size: 35,
                            color: Colors.black.withOpacity(0.6),
                          ),
                        ),
                        const SizedBox(
                          width: 10,
                        ),
                        Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              widget.alarms[index].text!,
                            ),
                            const SizedBox(
                              height: 8,
                            ),
                            Text(
                              widget.alarms[index].createTime!,
                              style: TextStyle(
                                color: Colors.black.withOpacity(0.6),
                              ),
                            ),
                          ],
                        ),
                      ],
                    ),
                    // Row(
                    //   children: [
                    //     IconButton(
                    //       onPressed: () {},
                    //       icon: const Icon(Icons.circle_outlined),
                    //       color: Colors.green,
                    //       iconSize: 40,
                    //     ),
                    //     IconButton(
                    //       onPressed: () {},
                    //       icon: const Icon(Icons.close),
                    //       color: Colors.red,
                    //       iconSize: 40,
                    //     ),
                    //   ],
                    // )
                  ],
                ),
              ],
            ),
          );
        },
      ),
    );
  }
}

class CustomDialog extends StatelessWidget {
  final List<Widget> children;

  const CustomDialog({
    super.key,
    required this.children,
  });

  @override
  Widget build(BuildContext context) {
    return Dialog(
      backgroundColor: Colors.transparent,
      elevation: 0,
      child: Container(
        width: 400,
        height: 300,
        decoration: const BoxDecoration(
          image: DecorationImage(
            image: AssetImage('assets/dialog.png'),
            fit: BoxFit.contain,
          ),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: children,
        ),
      ),
    );
  }
}
