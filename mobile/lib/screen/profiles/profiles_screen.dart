import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:mobile/api/api_profile.dart';
import 'package:mobile/model/request_models/put_profilelogin.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:mobile/model/response_models/get_profiles.dart';
import 'package:mobile/screen/home_screen.dart';
import 'package:mobile/screen/profiles/change_password_screen.dart';
import 'package:mobile/screen/profiles/create_profile_screen.dart';
import 'package:carousel_slider/carousel_slider.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/screen/record_screen.dart';
import 'package:mobile/util/logout_logic.dart';

enum MenuOption {
  changePassword,
  logout,
}

class ProfilesScreen extends StatefulWidget {
  List<Profile> profiles = List.empty();

  ProfilesScreen({super.key});

  @override
  State<ProfilesScreen> createState() => _ProfilesScreenState();
}

class _ProfilesScreenState extends State<ProfilesScreen> {
  CarouselController carouselController = CarouselController();
  String? fcmToken;
  generalResponse? profileloginAuth;
  MenuOption? selectedMenu;

  @override
  void initState() {
    _getProfiles();
    _initializeFCMToken();
    super.initState();
  }

  Future<void> _getProfiles() async {
    ProfilesModel result = await ApiProfile.getProfiles();
    if (result.code == '200') {
      setState(() {
        widget.profiles = result.profiles!;
      });
    } else if (result.code == '401') {
      userLogout();
    } else {
      if (!mounted) return;
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

  Future<void> _initializeFCMToken() async {
    const storage = FlutterSecureStorage();
    fcmToken = await storage.read(key: 'fcmToken');
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          Container(
            decoration: const BoxDecoration(
              image: DecorationImage(
                image: AssetImage("assets/login.gif"),
                fit: BoxFit.cover,
              ),
            ),
            child: Column(
              children: [
                AppBar(
                  elevation: 0,
                  backgroundColor: Colors.transparent,
                  leading: IconButton(
                    onPressed: () {},
                    icon: const Icon(
                      Icons.notifications_none,
                    ),
                  ),
                  actions: [
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 14),
                      child: PopupMenuButton<MenuOption>(
                        color: Colors.transparent,
                        initialValue: selectedMenu,
                        onSelected: (MenuOption item) {
                          setState(() {
                            selectedMenu = item;
                          });
                          if (selectedMenu == MenuOption.changePassword) {
                            Get.to(
                              () => const ChangePasswordScreen(),
                              transition: Transition.rightToLeftWithFade,
                            );
                          } else {
                            userLogout();
                          }
                        },
                        itemBuilder: (BuildContext context) => [
                          PopupMenuItem(
                            value: MenuOption.changePassword,
                            child: Container(
                              decoration: const BoxDecoration(
                                image: DecorationImage(
                                  image: AssetImage('assets/dialog.png'),
                                  fit: BoxFit.cover,
                                ),
                              ),
                              child: const ListTile(
                                trailing: Icon(
                                  Icons.change_circle_outlined,
                                ),
                                title: Text(
                                  '비밀번호 변경',
                                ),
                              ),
                            ),
                          ),
                          PopupMenuItem(
                            value: MenuOption.logout,
                            child: Container(
                              decoration: const BoxDecoration(
                                image: DecorationImage(
                                  image: AssetImage('assets/dialog.png'),
                                  fit: BoxFit.cover,
                                ),
                              ),
                              child: const ListTile(
                                trailing: Icon(
                                  Icons.login_rounded,
                                ),
                                title: Text(
                                  ' 로그아웃',
                                ),
                              ),
                            ),
                          ),
                        ],
                        position: PopupMenuPosition.under,
                        child: const Center(
                          child: Padding(
                            padding: EdgeInsets.all(8),
                            child: Row(
                              children: [
                                Text(
                                  '설정',
                                  style: TextStyle(
                                    fontSize: 20,
                                    fontWeight: FontWeight.w600,
                                  ),
                                ),
                                Icon(Icons.settings_outlined),
                              ],
                            ),
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
                Expanded(
                  child: Center(
                    child: widget.profiles.isEmpty
                        ? GestureDetector(
                            onTap: () {
                              Get.to(
                                () => const CreateProfileScreen(),
                                transition: Transition.rightToLeftWithFade,
                              );
                            },
                            child: Image.asset(
                              'assets/createprofile.png',
                              width: 370,
                            ),
                          )
                        : CarouselSlider(
                            carouselController: carouselController,
                            items: widget.profiles.map((profile) {
                              return GestureDetector(
                                onTap: () async {
                                  profileloginAuth =
                                      await ApiProfile.postProfileLogin(
                                    ProfileLoginRequestModel(
                                      childId: profile.id,
                                      fcmToken: fcmToken ?? "",
                                    ),
                                  );
                                  Get.offAll(
                                    () => const HomeScreen(),
                                    transition: Transition.size,
                                  );
                                },
                                child: Stack(
                                  children: [
                                    Image.network(
                                      '${profile.profileAnimon?.bodyImagePath}',
                                      fit: BoxFit.fitHeight,
                                      height: 400,
                                      width: 400,
                                      alignment: Alignment.center,
                                    ),
                                    Positioned(
                                      bottom: 0,
                                      left: 0,
                                      child: Stack(
                                        alignment: Alignment.center,
                                        children: [
                                          Image.asset(
                                            'assets/dialog.png',
                                            width: 170,
                                          ),
                                          Text(
                                            '${profile.name}',
                                            style: const TextStyle(
                                              fontSize: 20,
                                              color: Colors.black,
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                    Positioned(
                                      bottom: 0,
                                      right: 0,
                                      child: GestureDetector(
                                        onTap: () async {
                                          const storage =
                                              FlutterSecureStorage();
                                          await storage.write(
                                            key: 'childId',
                                            value: profile.id.toString(),
                                          );
                                          Get.to(
                                            () => const Record(),
                                            transition:
                                                Transition.rightToLeftWithFade,
                                          );
                                        },
                                        child: Container(
                                          width: 100,
                                          height: 40,
                                          decoration: BoxDecoration(
                                            borderRadius:
                                                BorderRadius.circular(10),
                                            color: const Color(0xffff6347),
                                          ),
                                          child: const Row(
                                            mainAxisAlignment:
                                                MainAxisAlignment.center,
                                            children: [
                                              Icon(
                                                Icons.videocam,
                                                size: 30,
                                                color: Colors.white,
                                              ),
                                              Text(
                                                '녹화영상',
                                                style: TextStyle(
                                                  fontSize: 15,
                                                  color: Colors.white,
                                                ),
                                              ),
                                            ],
                                          ),
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
                              );
                            }).toList(),
                            options: CarouselOptions(
                              height: 350,
                              aspectRatio: 16 / 9,
                              viewportFraction: 0.8,
                              initialPage: 0,
                              enableInfiniteScroll: false,
                              reverse: false,
                              autoPlay: false,
                              enlargeCenterPage: true,
                              onPageChanged: (index, reason) {},
                              scrollDirection: Axis.horizontal,
                            ),
                          ),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          Get.to(
            () => const CreateProfileScreen(),
            transition: Transition.rightToLeftWithFade,
          );
        },
        backgroundColor: Colors.white,
        elevation: 0,
        child: const Icon(
          Icons.add,
          color: Colors.black,
        ),
      ),
    );
  }
}
