import 'package:flutter/material.dart';
import 'package:flutter_swiper_plus/flutter_swiper_plus.dart';
import 'package:mobile/screen/home_screen.dart';
import 'package:mobile/screen/profiles/create_profile_screen.dart';

class Profiles extends StatelessWidget {
  const Profiles({super.key});

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
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          SizedBox(
            height: 500,
            child: Padding(
              padding: const EdgeInsets.only(left: 2.0),
              child: Swiper(
                itemCount: 3,
                fade: 0.3,
                itemWidth: MediaQuery.of(context).size.width - 2 * 64,
                layout: SwiperLayout.STACK,
                pagination: const SwiperPagination(
                  builder: DotSwiperPaginationBuilder(
                    color: Color.fromRGBO(177, 239, 188, 1),
                    activeSize: 20,
                    activeColor: Colors.green,
                    space: 25,
                  ),
                ),
                itemBuilder: (context, index) {
                  return InkWell(
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => const Home(),
                        ),
                      );
                    },
                    child: Stack(
                      children: <Widget>[
                        Column(
                          children: <Widget>[
                            const SizedBox(
                              height: 150,
                            ),
                            Card(
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(20),
                              ),
                              elevation: 8,
                              color: const Color.fromRGBO(177, 239, 188, 1),
                              child: const Padding(
                                padding: EdgeInsets.all(52.0),
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: <Widget>[
                                    SizedBox(
                                      height: 50,
                                    ),
                                    Text(
                                      // planets[index].name.toString(),
                                      '이름',
                                      style: TextStyle(
                                        fontSize: 40,
                                        color: Color(0xff47455f),
                                        fontWeight: FontWeight.w900,
                                      ),
                                      textAlign: TextAlign.center,
                                    ),
                                  ],
                                ),
                              ),
                            )
                          ],
                        ),
                        Hero(
                          tag: index,
                          child: Image.asset(
                            'assets/bear.png',
                            width: 100,
                            height: 300,
                          ),
                        )
                      ],
                    ),
                  );
                },
              ),
            ),
          )
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
