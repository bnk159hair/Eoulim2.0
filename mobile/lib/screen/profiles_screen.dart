import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:mobile/widgets/parallax_swiper.dart';
import 'package:mobile/screen/home_screen.dart';

class Profile extends StatelessWidget {
  const Profile({super.key});

  static const images = <String>[
    'assets/mercury.png',
    'assets/venus.png',
    'assets/earth.png',
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
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
            ),
          ),
          actions: [
            TextButton(
              onPressed: () {
                ScaffoldMessenger.of(context).clearSnackBars();
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(
                    content: Text('계정 설정으로 이동'),
                    duration: Duration(milliseconds: 1500),
                  ),
                );
              },
              child: Text(
                "계정 설정",
                style: TextStyle(
                  fontSize: 20,
                  color: Color.fromRGBO(255, 255, 255, 1),
                ),
              ),
            ),
          ]),
      body: const Center(
        child: SingleChildScrollView(
          child: Column(
            children: [
              SizedBox(
                height: 450,
                child: ParallaxSwiper(
                  // List of image URLs to display
                  images: images,
                  // Fraction of the viewport for each image
                  viewPortFraction: 0.85,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
