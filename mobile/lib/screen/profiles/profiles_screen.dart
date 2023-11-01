import 'package:flutter/material.dart';
// import 'package:flutter_swiper_plus/flutter_swiper_plus.dart';
import 'package:mobile/screen/home_screen.dart';
import 'package:mobile/screen/profiles/create_profile_screen.dart';
import 'package:carousel_slider/carousel_slider.dart';

class Profiles extends StatelessWidget {
  const Profiles({super.key});
   @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Flutter Carousel Slider',
      home: CarouselWidget(),
    );
  }
}

class CarouselWidget extends StatefulWidget {
  const CarouselWidget({super.key});

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
          ]),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          CarouselSlider(
            carouselController: carouselController,
            items: [1, 2, 3, 4, 5].map((i) {
              return Builder(
                builder: (BuildContext context) {
                  return Container(
                      width: MediaQuery.of(context).size.width,
                      margin: const EdgeInsets.symmetric(horizontal: 5.0),
                      decoration: const BoxDecoration(color: Colors.amber),
                      child: Center(
                        child: Text(
                          'text $i',
                          style: const TextStyle(fontSize: 16.0),
                        ),
                      ));
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
