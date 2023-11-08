import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:mobile/api/api_profile.dart';
import 'package:mobile/api/api_profilelogin.dart';
import 'package:mobile/model/request_models/put_profilelogin.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:mobile/model/response_models/get_profilelist.dart';
import 'package:mobile/screen/home_screen.dart';
import 'package:mobile/screen/login_screen.dart';
import 'package:mobile/screen/profiles/create_profile_screen.dart';
import 'package:carousel_slider/carousel_slider.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/screen/record_screen.dart';


class Profiles extends StatefulWidget {
  List<Profile> profiles = List.empty();
  Apiprofile apiProfile = Apiprofile();

  Profiles({super.key});

  @override
  _ProfilesState createState() => _ProfilesState();
}

class _ProfilesState extends State<Profiles> {
  @override
  void initState() {
    super.initState();
    _getProfiles();
  }

  void Logout() async {
    final storage = new FlutterSecureStorage();
    await storage.delete(key: 'Authkey');
    Get.offAll(() => Login());
    }

  Future<void> _getProfiles() async {
    getProfiles result = await widget.apiProfile.getprofilesAPI();
    if (result.code == '200') {
      setState(() {
        widget.profiles = result.profiles!;
      });
    } else if (result.code == '401') {
      Logout();
    } else {
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
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Flutter Carousel Slider',
      home: CarouselWidget(profiles: widget.profiles),
    );
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

  @override
  void initState() {
    super.initState();
    _initializeFCMToken();
  }

  Future<void> _initializeFCMToken() async {
    final storage = FlutterSecureStorage();
    fcmToken = (await storage.read(key: 'fcmToken'));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          Container(
            decoration: BoxDecoration(
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
                    onPressed: () {
                      ScaffoldMessenger.of(context).clearSnackBars();
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(
                          content: Text('알림목록으로 이동'),
                          duration: Duration(milliseconds: 1500),
                        ),
                      );
                    },
                    icon: const Icon(
                      Icons.notifications_none,
                      color: Color(0xff000000),
                    ),
                  ),
                  actions: [
                    TextButton(
                      onPressed: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => const CreateProfile(),
                          ),
                        );
                      },
                      child: const Text(
                        "계정 설정",
                        style: TextStyle(
                          fontSize: 20,
                          color: Color(0xff000000),
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
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (context) => const CreateProfile(),
                              ),
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
                            profileloginAuth = await apiProfileLogin.postProfileLoginAPI(
                                ProfileLoginRequestModel(childId: profile.id, fcmToken: fcmToken ?? ""));
                                Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (context) => Home(),
                                ),
                            );
                          },
                          child: Stack(
                            children: [
                              Image.asset(
                                'assets/bear.png',
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
                                      style: TextStyle(
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
                                  onTap: ()async {
                                    final storage = FlutterSecureStorage();
                                    await storage.write(key: 'childId', value: profile.id.toString());
                                    Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                        builder: (context) => Record(),
                                      ),
                                    );
                                  },
                                  child: Container(
                                    width: 100,
                                    height: 40,
                                    decoration: BoxDecoration(
                                      borderRadius: BorderRadius.circular(10),
                                      color: Colors.red,
                                    ),
                                    child: Row(
                                      mainAxisAlignment: MainAxisAlignment.center,
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
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => const CreateProfile(),
            ),
          );
        },
        backgroundColor: const Color(0xffffffff),
        elevation: 0,
        child: const Icon(
          Icons.add,
          color: Color(0xff000000),
        ),
      ),
    );
  }
}
