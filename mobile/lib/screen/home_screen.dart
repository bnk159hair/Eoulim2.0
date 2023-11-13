import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:mobile/api/api_profile.dart';
import 'package:mobile/controller/profile_select.dart';
import 'package:mobile/model/response_models/get_profile.dart';
import 'package:mobile/screen/animon_screen.dart';
import 'package:mobile/screen/notifications_screen.dart';
import 'package:mobile/screen/enter_screen.dart';
import 'package:mobile/screen/frineds_screen.dart';
import 'package:mobile/screen/session_screen.dart';
import 'package:mobile/screen/settings_screen.dart';
import 'package:persistent_bottom_nav_bar/persistent_tab_view.dart';
import 'package:mobile/util/logout_logic.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final ProfileController profileController = Get.find<ProfileController>();
  final PersistentTabController _controller =
      PersistentTabController(initialIndex: 0);
  var backButtonPressedOnce = false;

  @override
  void initState() {
    super.initState();
    _getProfileInfo();

    FirebaseMessaging.onMessage.listen((RemoteMessage message) async {
      RemoteNotification? notification = message.notification;

      if (notification != null) {
        Future.delayed(const Duration(milliseconds: 1000), () {
          Get.defaultDialog(
            title: '친구가 초대를 보냈어요!',
            middleText: '만나러 가볼까요?',
            actions: [
              IconButton(
                onPressed: () => Get.to(() => const SessionPage()),
                icon: const Icon(
                  Icons.circle_outlined,
                  size: 40,
                  color: Colors.green,
                ),
              ),
              IconButton(
                onPressed: () => Get.back(),
                icon: const Icon(
                  Icons.close_rounded,
                  size: 40,
                  color: Colors.red,
                ),
              ),
            ],
          );
          // showDialog(
          //   context: context,
          //   builder: (context) => AlertDialog(
          //     title: const Text('알림 도착'),
          //     content: Text(notification.body ?? ''),
          //     actions: [
          //       TextButton(
          //         onPressed: () {
          //           Navigator.of(context).pop();
          //         },
          //         child: const Text('확인'),
          //       ),
          //     ],
          //   ),
          // );
          print(notification.body);
          print("수신자 측 메시지 수신");
        });
      }
    });
  }

  Future<void> _getProfileInfo() async {
    ProfileInfoModel? result = await ApiProfile.getProfileInfo();
    if (result.code == '200') {
    } else {
      userLogout();
    }
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        // 첫 번째 뒤로가기 버튼 누를 때
        if (backButtonPressedOnce) {
          SystemNavigator.pop();
          return true; // 앱 종료
        }

        // 두 번째 뒤로가기 버튼 누를 때
        backButtonPressedOnce = true;
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('뒤로가기 버튼을 한 번 더 누르면 종료합니다.')),
        );

        // 2초 내에 두 번째 뒤로가기 버튼을 누르지 않으면 초기화
        Future.delayed(const Duration(seconds: 2), () {
          backButtonPressedOnce = false;
        });

        return false;
      },
      child: Scaffold(
        extendBodyBehindAppBar: true,
        appBar: AppBar(
          leading: Container(),
          // leading: IconButton(
          //   onPressed: () {
          //     Navigator.push(
          //       context,
          //       MaterialPageRoute(
          //         builder: (context) => Notifications(),
          //       ),
          //     );
          //   },
          //   icon: const Icon(
          //     Icons.notifications,
          //   ),
          //   iconSize: 40.0,
          //   color: Colors.yellow,
          // ),
          actions: [
            Row(
              children: [
                FutureBuilder<ProfileInfoModel?>(
                  future: ApiProfile.getProfileInfo(),
                  builder: (context, snapshot) {
                    if (snapshot.connectionState == ConnectionState.waiting) {
                      return const CircularProgressIndicator(
                        color: Colors.white,
                      ); // 데이터를 기다릴 동안 로딩 표시
                    } else if (snapshot.hasError) {
                      return Text('에러 발생: ${snapshot.error}');
                    } else if (!snapshot.hasData) {
                      return const Text('데이터 없음');
                    } else {
                      final profileInfo = snapshot.data!;
                      print('여기서 호출중');
                      print(profileInfo.profile?.profileAnimon?.bodyImagePath);
                      // final imagePath = profileInfo.profileAnimon?.bodyImagePath;

                      return Row(
                        children: [
                          MaterialButton(
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(30.0),
                            ),
                            onPressed: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (context) => AnimonsScreen(),
                                ),
                              );
                            },
                            child: Obx(
                              () {
                                return Image.network(
                                  profileController.selectedProfile.value
                                          ?.profileAnimon?.bodyImagePath ??
                                      '',
                                );
                              },
                            ),
                          ),
                          Obx(
                            () {
                              return Text(
                                '${profileController.selectedProfile.value?.name ?? ''}님 어서오세요',
                                style: const TextStyle(fontSize: 16),
                              );
                            },
                          ),
                        ],
                      );
                    }
                  },
                )
              ],
            )
          ],
          backgroundColor: Colors.transparent,
          elevation: 0.0,
          toolbarHeight: 70.0,
        ),
        body: PersistentTabView(
          context,
          controller: _controller,
          screens: [
            const Enter(),
            Friends(),
            SettingsScreen(),
          ],
          items: [
            PersistentBottomNavBarItem(
              icon: const Icon(Icons.home),
              title: "새 친구",
              activeColorPrimary: Colors.blue,
              inactiveColorPrimary: Colors.grey,
            ),
            PersistentBottomNavBarItem(
              icon: const Icon(Icons.people),
              title: "내 친구",
              activeColorPrimary: Colors.blue,
              inactiveColorPrimary: Colors.grey,
            ),
            PersistentBottomNavBarItem(
              icon: const Icon(Icons.settings_outlined),
              title: "설정",
              activeColorPrimary: Colors.blue,
              inactiveColorPrimary: Colors.grey,
            ),
          ],
          confineInSafeArea: true,
          backgroundColor: Colors.white, // Default is Colors.white.
          handleAndroidBackButtonPress: true, // Default is true.
          resizeToAvoidBottomInset:
              true, // This needs to be true if you want to move up the screen when the keyboard appears. Default is true.
          stateManagement: true, // Default is true.
          hideNavigationBarWhenKeyboardShows:
              true, // Recommended to set 'resizeToAvoidBottomInset' as true while using this argument. Default is true.
          decoration: NavBarDecoration(
            borderRadius: BorderRadius.circular(10.0),
            colorBehindNavBar: Colors.white,
          ),
          popAllScreensOnTapOfSelectedTab: true,
          popActionScreens: PopActionScreensType.all,
          itemAnimationProperties: const ItemAnimationProperties(
            // Navigation Bar's items animation properties.
            duration: Duration(milliseconds: 100),
            curve: Curves.easeInOut,
          ),
          screenTransitionAnimation: const ScreenTransitionAnimation(
            // Screen transition animation on change of the selected tab.
            animateTabTransition: true,
            curve: Curves.easeInOut,
            duration: Duration(milliseconds: 50),
          ),
          navBarStyle: NavBarStyle
              .style9, // Choose the nav bar style with this property.
        ),
      ),
    );
  }
}
