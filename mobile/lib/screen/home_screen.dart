import 'package:flutter/material.dart';
import 'package:persistent_bottom_nav_bar/persistent_tab_view.dart';
import 'package:mobile/screen/join_screen.dart';
import 'package:mobile/screen/frineds_screen.dart';
import 'package:mobile/screen/settings_screen.dart';

PersistentTabController _controller = PersistentTabController(initialIndex: 0);

List<Widget> _buildScreens() {
  return [
    const Join(),
    const Friends(),
    const Settings(),
  ];
}

List<PersistentBottomNavBarItem> _navBarsItems() {
  return [
    PersistentBottomNavBarItem(
      icon: Icon(Icons.home),
      title: "새 친구",
      activeColorPrimary: Colors.blue,
      inactiveColorPrimary: Colors.grey,
    ),
    PersistentBottomNavBarItem(
      icon: Icon(Icons.people),
      title: ("내 친구"),
      activeColorPrimary: Colors.blue,
      inactiveColorPrimary: Colors.grey,
    ),
    PersistentBottomNavBarItem(
      icon: Icon(Icons.settings_outlined),
      title: ("설정"),
      activeColorPrimary: Colors.blue,
      inactiveColorPrimary: Colors.grey,
    ),
  ];
}

class Home extends StatelessWidget {
  const Home({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        actions: [
          IconButton(
            onPressed: () {},
            icon: Icon(
              Icons.notifications,
            ),
          ),
          IconButton(
            onPressed: () {},
            icon: Icon(
              Icons.account_circle,
            ),
          ),
        ],
      ),
      bottomNavigationBar: PersistentTabView(
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
        itemAnimationProperties: ItemAnimationProperties(
          // Navigation Bar's items animation properties.
          duration: Duration(milliseconds: 50),
          curve: Curves.easeInOut,
        ),
        screenTransitionAnimation: ScreenTransitionAnimation(
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

// class Main extends StatefulWidget {
//   const Main({super.key});

//   @override
//   State<Main> createState() => _MainState();
// }

// class _MainState extends State<Main> {
//   // this is used to control the current index of the bottom navigation bar, and the current index of the FadeIndexedStack
//   int currentIndex = 0;

//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       bottomNavigationBar: _buildBottomSheet(),
//       body: FadeIndexedStack(
//         sizing: StackFit.expand,
//         beginOpacity: 0.5,
//         endOpacity: 1.0,
//         curve: Curves.easeInOut,
//         duration: const Duration(milliseconds: 250),
//         index: currentIndex,
//         children: <Widget>[
//           Join(),
//           Friends(),
//           Settings(),
//         ],
//       ),
//     );
//   }

//   Widget _buildBottomSheet() {
//     return BottomNavigationBar(
//       currentIndex: currentIndex,
//       onTap: (index) {
//         setState(() {
//           currentIndex = index;
//         });
//       },
//       items: const <BottomNavigationBarItem>[
//         BottomNavigationBarItem(
//           icon: Icon(Icons.home),
//           label: 'New',
//         ),
//         BottomNavigationBarItem(
//           icon: Icon(Icons.people),
//           label: 'Friends',
//         ),
//         BottomNavigationBarItem(
//           icon: Icon(Icons.settings),
//           label: 'Settings',
//         ),
//       ],
//     );
//   }
// }