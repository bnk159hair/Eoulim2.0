import 'package:flutter/material.dart';
import 'package:flutter_swiper_plus/flutter_swiper_plus.dart';
import 'package:mobile/screen/home_screen.dart';

class Profile extends StatefulWidget {
  const Profile({super.key});

  @override
  State<Profile> createState() => _ProfileState();
}

class _ProfileState extends State<Profile> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Color.fromRGBO(177, 239, 188, 1),
      body: SafeArea(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Padding(
              padding: const EdgeInsets.all(32),
              child: Column(
                children: <Widget>[
                  Row(
                    children: [
                      IconButton(
                        onPressed: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => Home(),
                            ),
                          );
                        },
                        icon: const Icon(
                          Icons.notifications_none,
                        ),
                      ),
                      const Text(
                        '계정 설정',
                        style: TextStyle(
                          // fontFamily: 'Avenir',
                          // fontSize: 40,
                          color: Color(0xffffffff),
                          fontWeight: FontWeight.w900,
                        ),
                        // textAlign: TextAlign.right,
                      ),
                      IconButton(
                        onPressed: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => Home(),
                            ),
                          );
                        },
                        icon: const Icon(
                          Icons.settings_outlined,
                        ),
                        // tooltip: '계정 설정',
                      )
                    ],
                  ),
                ],
              ),
            ),
            SizedBox(
              height: 500,
              child: Padding(
                padding: const EdgeInsets.only(left: 32.0),
                child: Swiper(
                  itemCount: planets.length,
                  fade: 0.3,
                  itemWidth: MediaQuery.of(context).size.width - 2 * 64,
                  layout: SwiperLayout.STACK,
                  pagination: SwiperPagination(
                      builder: DotSwiperPaginationBuilder(
                          activeSize: 20,
                          activeColor: Colors.yellow.shade300,
                          space: 5)),
                  itemBuilder: (context, index) {
                    return InkWell(
                      // onTap: () {
                      //   Navigator.push(
                      //       context,
                      //       PageRouteBuilder(
                      //         opaque: false,
                      //         pageBuilder: (context, a, b) => DetailsView(
                      //           planetInfo: planets[index],
                      //         ),
                      //         transitionsBuilder: (BuildContext context,
                      //             Animation<double> animation,
                      //             Animation<double> secondaryAnimation,
                      //             Widget child) {
                      //           return FadeTransition(
                      //             opacity: animation,
                      //             child: child,
                      //           );
                      //         },
                      //       ));
                      // },
                      child: Stack(
                        children: <Widget>[
                          Column(
                            children: <Widget>[
                              const SizedBox(
                                height: 150,
                              ),
                              Card(
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(32),
                                ),
                                elevation: 8,
                                color: Colors.white,
                                child: Padding(
                                  padding: const EdgeInsets.all(32.0),
                                  child: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: <Widget>[
                                      const SizedBox(
                                        height: 100,
                                      ),
                                      Text(
                                        planets[index].name.toString(),
                                        style: const TextStyle(
                                          fontSize: 40,
                                          fontFamily: 'Avenir',
                                          color: Color(0xff47455f),
                                          fontWeight: FontWeight.w900,
                                        ),
                                        textAlign: TextAlign.left,
                                      ),
                                      const Text(
                                        "Solar System",
                                        style: TextStyle(
                                            fontSize: 23,
                                            fontFamily: 'Avenir',
                                            color: Color(0xFF414C6B),
                                            fontWeight: FontWeight.w400),
                                        textAlign: TextAlign.left,
                                      ),
                                    ],
                                  ),
                                ),
                              )
                            ],
                          ),
                          Hero(
                              tag: planets[index].position,
                              child: Image.asset(
                                  planets[index].iconImage.toString()))
                        ],
                      ),
                    );
                  },
                ),
              ),
            )
          ],
        ),
      ),
    );
  }
}

// @override
// Widget build(BuildContext context) {
//   return Scaffold(
//     body: Padding(
//       padding: EdgeInsets.all(16.0),
//       child: Column(
//         mainAxisAlignment: MainAxisAlignment.center,
//         children: [
//           // 로그인 버튼
//           ElevatedButton(
//             onPressed: () {
//               // 여기에 로그인 로직을 추가하세요
//               Navigator.push(context,
//                   MaterialPageRoute(builder: (context) => const Main()));
//               // 사용자명과 비밀번호는 _usernameController.text와 _passwordController.text로 얻을 수 있습니다.
//             },
//             child: Text('프로필'),
//           ),
//         ],
//       ),
//     ),
//   );
// }

class PlanetInfo {
  final int position;
  final String? name;
  final String? iconImage;
  final String? description;
  final List<String>? images;

  PlanetInfo(
    this.position, {
    this.name,
    this.iconImage,
    this.description,
    this.images,
  });
}

List<PlanetInfo> planets = [
  PlanetInfo(1,
      name: 'Mercury',
      iconImage: 'assets/mercury.png',
      description:
          "Zipping around the sun in only 88 days, Mercury is the closest planet to the sun, and it's also the smallest, only a little bit larger than Earth's moon. Because its so close to the sun (about two-fifths the distance between Earth and the sun), Mercury experiences dramatic changes in its day and night temperatures: Day temperatures can reach a scorching 840  F (450 C), which is hot enough to melt lead. Meanwhile on the night side, temperatures drop to minus 290 F (minus 180 C).",
      images: [
        'https://cdn.pixabay.com/photo/2013/07/18/10/57/mercury-163610_1280.jpg',
        'https://solarsystem.nasa.gov/rails/active_storage/blobs/eyJfcmFpbHMiOnsibWVzc2FnZSI6IkJBaHBBcVlHIiwiZXhwIjpudWxsLCJwdXIiOiJibG9iX2lkIn19--bfcefdbb03ebe1d73fbe2e043abde222b35f1c08/PIA13508.jpg?disposition=attachment',
        'https://solarsystem.nasa.gov/rails/active_storage/blobs/eyJfcmFpbHMiOnsibWVzc2FnZSI6IkJBaHBBcVVFIiwiZXhwIjpudWxsLCJwdXIiOiJibG9iX2lkIn19--271ff8be3125a1e3b9284c6b326bca3bea2dc590/PIA16853.jpg?disposition=attachment',
      ]),
  PlanetInfo(2,
      name: 'Venus',
      iconImage: 'assets/venus.png',
      description:
          "The second planet from the sun, Venus is Earth's twin in size. Radar images beneath its atmosphere reveal that its surface has various mountains and volcanoes. But beyond that, the two planets couldn't be more different. Because of its thick, toxic atmosphere that's made of sulfuric acid clouds, Venus is an extreme example of the greenhouse effect. It's scorching-hot, even hotter than Mercury. The average temperature on Venus' surface is 900 F (465 C). At 92 bar, the pressure at the surface would crush and kill you. And oddly, Venus spins slowly from east to west, the opposite direction of most of the other planets.",
      images: [
        'https://cdn.pixabay.com/photo/2011/12/13/14/39/venus-11022_1280.jpg',
        'https://solarsystem.nasa.gov/rails/active_storage/blobs/eyJfcmFpbHMiOnsibWVzc2FnZSI6IkJBaHBBcjhQIiwiZXhwIjpudWxsLCJwdXIiOiJibG9iX2lkIn19--ab0be61e3f61623e10ec25afdee91cc1a5db6830/PIA00148_1280.jpg?disposition=attachment',
      ]),
  PlanetInfo(3,
      name: 'Earth',
      iconImage: 'assets/earth.png',
      description:
          "The third planet from the sun, Earth is a waterworld, with two-thirds of the planet covered by ocean. It's the only world known to harbor life. Earth's atmosphere is rich in nitrogen and oxygen. Earth's surface rotates about its axis at 1,532 feet per second (467 meters per second) — slightly more than 1,000 mph (1,600 kph) — at the equator. The planet zips around the sun at more than 18 miles per second (29 km per second).",
      images: [
        'https://images.pexels.com/photos/76969/cold-front-warm-front-hurricane-felix-76969.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1',
        'https://images.unsplash.com/photo-1451187580459-43490279c0fa?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1172&q=80',
        'https://images.unsplash.com/photo-1643330683233-ff2ac89b002c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1064&q=80',
        'https://cdn.pixabay.com/photo/2016/10/20/18/35/earth-1756274_960_720.jpg',
      ]),
];
