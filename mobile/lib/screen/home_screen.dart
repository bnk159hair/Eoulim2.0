import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:mobile/screen/alarm_screen.dart';
import 'package:persistent_bottom_nav_bar/persistent_tab_view.dart';
import 'package:mobile/screen/enter_screen.dart';
import 'package:mobile/screen/frineds_screen.dart';
import 'package:mobile/screen/settings_screen.dart';

var backButtonPressedOnce = false;

class Home extends StatelessWidget {
  const Home({super.key});

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
        appBar: AppBar(
          leading: IconButton(
            onPressed: () {
              Navigator.push(context,
                  MaterialPageRoute(builder: (context) => const Alarm()));
            },
            icon: const Icon(
              Icons.notifications,
            ),
            iconSize: 40.0,
          ),
          actions: [
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
              child: Image.asset('assets/bear.png'),
            )
          ],
          // backgroundColor: Colors.black,
          elevation: 6.0,
          toolbarHeight: 100.0,
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
    const Friends(),
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
  const HomeBottomNavBar({super.key});

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
            true, // This needs to be true if you want to move up the screen when keyboard appears. Default is true.
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
          // Screen transition animation on change of selected tab.
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
