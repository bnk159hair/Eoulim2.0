import 'package:flutter/material.dart';
// import 'package:flutter_secure_storage/flutter_secure_storage.dart';

// import 'package:mobile/screen/login_screen.dart';

class NotificationItem {
  final String content;
  final String date;

  NotificationItem(
    this.content,
    this.date,
  );
}

class Notifications extends StatefulWidget {
  const Notifications({super.key});

  @override
  State<Notifications> createState() => _NotificationsState();
}

class _NotificationsState extends State<Notifications> {
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

  final List<NotificationItem> notificationItems = [
    NotificationItem('친구가 초대했어요.', '11월 1일'),
    NotificationItem('친구가 초대했어요.', '11월 1일'),
    NotificationItem('친구가 초대했어요.', '11월 1일'),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('초대장'),
        centerTitle: true,
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(16.0),
        itemCount: notificationItems.length,
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
                              notificationItems[index].content,
                            ),
                            const SizedBox(
                              height: 8,
                            ),
                            Text(
                              notificationItems[index].date,
                              style: TextStyle(
                                color: Colors.black.withOpacity(0.6),
                              ),
                            ),
                          ],
                        ),
                      ],
                    ),
                    Row(
                      children: [
                        IconButton(
                          onPressed: () {},
                          icon: const Icon(Icons.circle_outlined),
                          color: Colors.green,
                          iconSize: 40,
                        ),
                        IconButton(
                          onPressed: () {},
                          icon: const Icon(Icons.close),
                          color: Colors.red,
                          iconSize: 40,
                        ),
                      ],
                    )
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
