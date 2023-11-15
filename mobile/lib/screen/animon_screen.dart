import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:mobile/api/api_animonlist.dart';
import 'package:mobile/api/api_changeanimon.dart';
import 'package:mobile/api/api_profile.dart';
import 'package:mobile/controller/profile_select.dart';
import 'package:mobile/model/response_models/get_animonlist.dart';
import 'package:mobile/model/response_models/get_profile.dart';
import 'package:mobile/util/logout_logic.dart';

class AnimonsScreen extends StatefulWidget {
  AnimonsScreen({Key? key}) : super(key: key);
  List<Animon> animons = List.empty();
  ApiAnimon apiAnimon = ApiAnimon();
  ApiChangeAnimon apichangeAnimon = ApiChangeAnimon();

  @override
  State<AnimonsScreen> createState() => _AnimonsScreenState();
}

class _AnimonsScreenState extends State<AnimonsScreen> {
  ApiChangeAnimon apichangeAnimon = ApiChangeAnimon();

  Future<void> _getProfileInfo() async {
    ProfileInfoModel? result = await ApiProfile.getProfileInfo();
    if (result.code == '200') {
    } else {
      userLogout();
    }
  }

  @override
  void initState() {
    super.initState();
    _getAnimons();
  }

  Future<void> _getAnimons() async {
    GetAnimons result = await widget.apiAnimon.getAnimonsAPI();
    if (result.code == '200') {
      setState(() {
        widget.animons = result.animons!;
      });
    } else if (result.code == '401') {
      userLogout();
    } else {
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

  final ProfileController profileController = Get.find<ProfileController>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          Container(
            decoration: const BoxDecoration(
              image: DecorationImage(
                image: AssetImage('assets/enter.gif'),
                fit: BoxFit.cover,
                opacity: 0.5,
              ),
            ),
          ),
          
            Column(
              children: [
                Align(
                  alignment: Alignment.topCenter,
                  child: Padding(
                    padding: const EdgeInsets.only(top: 60.0),
                    child: Column(
                      children: [
                        const Text(
                          '현재 나의 애니몬',
                          style: TextStyle(
                            fontSize: 20,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        const SizedBox(height: 16),
                        ClipRRect(
                          borderRadius: BorderRadius.circular(25),
                          child: Container(
                            color: Colors.white,
                            child: Padding(
                              padding: const EdgeInsets.all(8.0),
                              child: Obx(() => Image.network(
                                    profileController.selectedProfile.value
                                            ?.profileAnimon?.bodyImagePath ??
                                        '',
                                    width: 100,
                                    height: 100,
                                  )),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 16),
                Container(
                  width: MediaQuery.of(context).size.width / 1.1,
                  decoration: BoxDecoration(
                    image: const DecorationImage(
                      image: AssetImage('assets/woodbox.jpg'),
                      fit: BoxFit.cover,
                    ),
                    borderRadius: BorderRadius.circular(20),
                  ),
                  child: Column(
                    children: [
                      SizedBox(
                        height: 500,
                        child: GridView.builder(
                          gridDelegate:
                              const SliverGridDelegateWithFixedCrossAxisCount(
                            crossAxisCount: 2,
                            crossAxisSpacing: 10.0,
                            mainAxisSpacing: 10.0,
                          ),
                          padding: const EdgeInsets.all(16.0),
                          itemCount: widget.animons.length,
                          itemBuilder: (context, index) {
                            return InkWell(
                              onTap: () {
                                showDialog(
                                  context: context,
                                  barrierDismissible: false,
                                  builder: (BuildContext ctx) {
                                    return CustomDialog(
                                      children: [
                                        const Text(
                                          '애니몬을 변경하실건가요?',
                                          style: TextStyle(
                                            fontSize: 24,
                                            color: Colors.white,
                                          ),
                                        ),
                                        const SizedBox(height: 16),
                                        Row(
                                          mainAxisAlignment:
                                              MainAxisAlignment.center,
                                          children: [
                                            ElevatedButton(
                                              onPressed: () async {
                                                final response =
                                                    await apichangeAnimon
                                                        .getChangeAnimonsAPI(
                                                            widget
                                                                .animons[index]
                                                                .id
                                                                .toString());
                                                if (response.code == '200') {
                                                  _getProfileInfo();
                                                  Navigator.of(ctx).pop();
                                                } else {
                                                  Navigator.of(ctx).pop();
                                                }
                                              },
                                              style: ElevatedButton.styleFrom(
                                                backgroundColor:
                                                    const Color(0xff00b7eb),
                                              ),
                                              child: const Text(
                                                '변경하기',
                                              ),
                                            ),
                                            const SizedBox(width: 16),
                                            ElevatedButton(
                                              onPressed: () {
                                                Navigator.of(ctx).pop();
                                              },
                                              child: const Text('취소하기'),
                                            ),
                                          ],
                                        ),
                                      ],
                                    );
                                  },
                                );
                              },
                              child: Card(
                                elevation: 2.0,
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(20.0),
                                ),
                                child: Stack(
                                  children: [
                                    Center(
                                      child: Image.network(
                                        '${widget.animons[index].bodyImagePath}',
                                        fit: BoxFit.cover,
                                        alignment: Alignment.center,
                                      ),
                                    ),
                                    Positioned(
                                      bottom: 0,
                                      left: 0,
                                      right: 0,
                                      child: Row(
                                        mainAxisAlignment:
                                            MainAxisAlignment.spaceAround,
                                        children: [
                                          Container(
                                            padding: const EdgeInsets.symmetric(
                                              vertical: 8.0,
                                              horizontal: 10.0,
                                            ),
                                            decoration: BoxDecoration(
                                              color:
                                                  Colors.green.withOpacity(0.8),
                                              borderRadius:
                                                  BorderRadius.circular(16.0),
                                            ),
                                            child: Text(
                                              '${widget.animons[index].name}',
                                              style: const TextStyle(
                                                  color: Colors.white),
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            );
                          },
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
        
        ],
      ),
    );
  }
}

class CustomDialog extends StatelessWidget {
  final List<Widget> children;

  const CustomDialog({
    super.key,
    required this.children,
  });

  @override
  Widget build(BuildContext context) {
    return Dialog(
      backgroundColor: Colors.transparent,
      child: Container(
        width: 400,
        height: 300,
        decoration: const BoxDecoration(
          image: DecorationImage(
            image: AssetImage('assets/dialog.png'),
            fit: BoxFit.contain,
          ),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: children,
        ),
      ),
    );
  }
}
