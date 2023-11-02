import 'package:flutter/material.dart';
import 'package:mobile/api/api_profile.dart';
import 'package:mobile/model/response_models/get_profilelist.dart';
import 'package:mobile/screen/home_screen.dart';
import 'package:mobile/screen/profiles/create_profile_screen.dart';
import 'package:carousel_slider/carousel_slider.dart';

class Profiles extends StatefulWidget {
  List<Profile> profiles = List.empty();
  Apiprofile apiProfile = Apiprofile();

  @override
  _ProfilesState createState() => _ProfilesState();
}

class _ProfilesState extends State<Profiles> {
  @override
  void initState() {
    super.initState();
    _getProfiles();
  }

  Future<void> _getProfiles() async {
    getProfiles result = await widget.apiProfile.getprofilesAPI();
    if (result.code == '200') {
      setState(() {
        widget.profiles = result.profiles!;
      });
    } else if (result.code == '401') {
      showDialog(
        context: context,  // 이 부분에 정의가 필요
        builder: (BuildContext context) {
          return AlertDialog(
            content: const Text('로그인을 해주세요'),
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
    } else {
      showDialog(
        context: context,  // 이 부분에 정의가 필요
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

  CarouselWidget({Key? key, required this.profiles}) : super(key: key);

  @override
  State<CarouselWidget> createState() => _CarouselWidgetState();
}

class _CarouselWidgetState extends State<CarouselWidget> {
  CarouselController carouselController = CarouselController();

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0,
        backgroundColor: const Color(0xffffffff),
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
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          CarouselSlider(
            carouselController: carouselController,
            items: widget.profiles.map((profile) {
              return Builder(
                builder: (BuildContext context) {
                  return Container(
                    width: MediaQuery.of(context).size.width,
                    margin: const EdgeInsets.symmetric(horizontal: 5.0),
                    decoration: const BoxDecoration(color: Colors.amber),
                    child: Center(
                      child: Text(
                        profile.name ?? 'No Name',
                        style: const TextStyle(fontSize: 16.0),
                      ),
                    ),
                  );
                },
              );
            }).toList(),
            options: CarouselOptions(
              // Set the height of each carousel item
              height: 350,
              // Set the size of each carousel item
              // if height is not specified
              aspectRatio: 16 / 9,
              // Set how much space current item widget
              // will occupy from current page view
              viewportFraction: 0.8,
              // Set the initial page
              initialPage: 0,
              // Set carousel to repeat when reaching the end
              enableInfiniteScroll: false,
              reverse: false,
              // Set carousel to display next page automatically
              autoPlay: false,
              enlargeCenterPage: true,
              // Do actions for each page change
              onPageChanged: (index, reason) {},
              // Set the scroll direction
              scrollDirection: Axis.horizontal,
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
