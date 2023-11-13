import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:mobile/api/api_profile.dart';
import 'package:mobile/api/api_profilelogin.dart';
import 'package:mobile/model/request_models/put_profilelogin.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:mobile/model/response_models/get_profilelist.dart';
import 'package:mobile/screen/home_screen.dart';
import 'package:mobile/screen/login_screen.dart';
import 'package:mobile/screen/profiles/change_password.dart';
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
  Apiprofile apiProfile = Apiprofile();

  ProfilesScreen({super.key});

  @override
  State<ProfilesScreen> createState() => _ProfilesScreenState();
}

class _ProfilesScreenState extends State<ProfilesScreen> {
  @override
  void initState() {
    _getProfiles();
    super.initState();
  }

  Future<void> _getProfiles() async {
    getProfiles result = await widget.apiProfile.getprofilesAPI();
    if (result.code == '200') {
      setState(() {
        widget.profiles = result.profiles!;
      });
    } else if (result.code == '401') {
      userLogout();
    } else {
      if (!mounted) return;
      showDialog(
        context: context, // 이 부분에 정의가 필요
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

  @override
  Widget build(BuildContext context) {
    return CarouselWidget(profiles: widget.profiles);
    //  MaterialApp(
    //   debugShowCheckedModeBanner: false,
    //   title: 'Flutter Carousel Slider',
    //   home: CarouselWidget(profiles: widget.profiles),
    // );
  }
}

class CarouselWidget extends StatefulWidget {
  final List<Profile> profiles;

  const CarouselWidget({Key? key, required this.profiles}) : super(key: key);

  @override
  State<CarouselWidget> createState() => _CarouselWidgetState();
}

class _CarouselWidgetState extends State<CarouselWidget> {
  CarouselController carouselController = CarouselController();
  String? fcmToken;
  generalResponse? profileloginAuth;
  ApiprofileLogin apiProfileLogin = ApiprofileLogin();
  MenuOption? selectedMenu;

  @override
  void initState() {
    super.initState();
    _initializeFCMToken();
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
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (context) => const ChangePassword(),
                              ),
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
                                // Add your background image or color here
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
                                  '비밀번호 수정',
                                ),
                              ),
                            ),
                          ),
                          PopupMenuItem(
                            value: MenuOption.logout,
                            child: Container(
                              decoration: const BoxDecoration(
                                // Add your background image or color here
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
                            child: Text(
                              '설정',
                              style: TextStyle(
                                fontSize: 20,
                                fontWeight: FontWeight.w600,
                              ),
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
                                      await apiProfileLogin.postProfileLoginAPI(
                                    ProfileLoginRequestModel(
                                      childId: profile.id,
                                      fcmToken: fcmToken ?? "",
                                    ),
                                  );
                                  if (!mounted) return;
                                  Get.offAll(() => const HomeScreen(),
                                      transition: Transition.size);
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
                                              color: Color(0xff000000),
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
                                          if (!mounted) return;
                                          Navigator.push(
                                            context,
                                            MaterialPageRoute(
                                              builder: (context) =>
                                                  const Record(),
                                            ),
                                          );
                                        },
                                        child: Container(
                                          width: 100,
                                          height: 40,
                                          decoration: BoxDecoration(
                                            borderRadius:
                                                BorderRadius.circular(10),
                                            color: Colors.red,
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
