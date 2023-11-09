import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:mobile/api/api_profileinfo.dart';
import 'package:mobile/controller/profile_select.dart';
import 'package:mobile/model/response_models/get_porfile.dart';
import 'package:mobile/screen/notifications_screen.dart';
import 'package:mobile/screen/enter_screen.dart';
import 'package:mobile/screen/frineds_screen.dart';
import 'package:mobile/screen/settings_screen.dart';
import 'package:persistent_bottom_nav_bar/persistent_tab_view.dart';
import 'package:mobile/util/logout_logic.dart';

var backButtonPressedOnce = false;

class Home extends StatefulWidget {
  const Home({Key? key}) : super(key: key);

  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home> {
  Apiprofileinfo apiProfileinfo = Apiprofileinfo();
  final ProfileController profileController = Get.find<ProfileController>();

  @override
  void initState() {
    super.initState();
    _getProfileInfo();
  }

  Future<void> _getProfileInfo() async {
    getProfileinfo? result = await apiProfileinfo.getprofileAPI();
    if (result.code == '200') {
    } else {
      Logout();
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
          leading: IconButton(
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => const Notifications(),
                ),
              );
            },
            icon: const Icon(
              Icons.notifications,
            ),
            iconSize: 40.0,
            color: Colors.yellow,
          ),
          actions: [
            Row(
              children: [
                MaterialButton(
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(30.0),
                    ),
                    onPressed: () {
                      ScaffoldMessenger.of(context).clearSnackBars();
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(
                          content: Text('Hello There!'),
                          duration: Duration(milliseconds: 1500),
                        ),
                      );
                    },
                    child: Obx(() {
                      return Image.network(
                          '${profileController.selectedProfile.value?.profileAnimon?.bodyImagePath}');
                    })),
                Obx(() {
                  return Text(
                    '${profileController.selectedProfile.value?.name}님 어서오세요',
                    style: const TextStyle(fontSize: 16),
                  );
                }),
                // FutureBuilder<getProfileinfo?>(
                //   future: apiProfileinfo.getprofileAPI(),
                //   builder: (context, snapshot) {
                //     if (snapshot.connectionState == ConnectionState.waiting) {
                //       return const CircularProgressIndicator(
                //         color: Colors.white,
                //       ); // 데이터를 기다릴 동안 로딩 표시
                //     } else if (snapshot.hasError) {
                //       return Text('에러 발생: ${snapshot.error}');
                //     } else if (!snapshot.hasData) {
                //       return const Text('데이터 없음');
                //     } else {
                //       final profileInfo = snapshot.data!;
                //       print('여기서 호출중');
                //       print(profileInfo.profile?.profileAnimon?.bodyImagePath);
                //       // final imagePath = profileInfo.profileAnimon?.bodyImagePath;

                //       return Row(
                //         children: [
                //           Obx(() {
                //             return Image.network(
                //               '${profileController.selectedProfile.value?.profileAnimon?.bodyImagePath}',
                //             );
                //           }),
                //           Obx(() {
                //             return Text(
                //               '${profileController.selectedProfile.value?.name}님 어서오세요',
                //               style: const TextStyle(fontSize: 16),
                //             );
                //           }),
                //         ],
                //       );
                //     }
                //   },
                // )
              ],
            )
          ],
          backgroundColor: Colors.transparent,
          elevation: 0.0,
          toolbarHeight: 70.0,
        ),
        body: const HomeBottomNavBar(),
      ),
    );
  }
}

PersistentTabController _controller = PersistentTabController(initialIndex: 0);

List<Widget> _buildScreens() {
  return [
    const Enter(),
    Friends(),
    const Settings(),
  ];
}

List<PersistentBottomNavBarItem> _navBarsItems() {
  return [
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
  ];
}

class HomeBottomNavBar extends StatelessWidget {
  const HomeBottomNavBar({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async => false,
      child: PersistentTabView(
        context,
        controller: _controller,
        screens: _buildScreens(),
        items: _navBarsItems(),
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
          duration: Duration(milliseconds: 50),
          curve: Curves.easeInOut,
        ),
        screenTransitionAnimation: const ScreenTransitionAnimation(
          // Screen transition animation on change of the selected tab.
          animateTabTransition: true,
          curve: Curves.easeInOut,
          duration: Duration(milliseconds: 50),
        ),
        navBarStyle:
            NavBarStyle.style9, // Choose the nav bar style with this property.
      ),
    );
  }
}
