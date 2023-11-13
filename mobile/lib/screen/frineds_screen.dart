import 'package:flutter/material.dart';
import 'package:mobile/api/api_deletefollowing.dart';
import 'package:mobile/api/api_followings.dart';
import 'package:mobile/model/response_models/get_followings.dart';


class Friends extends StatefulWidget {
  Friends({super.key});
  List<Profile> friends = List.empty();
  Apifollowing apifollowing = Apifollowing();

  @override
  _FriendsState createState() => _FriendsState();
}

class _FriendsState extends State<Friends> {
  
  ApiDeletefollowing apiDeletefollowing = ApiDeletefollowing();

  @override
  void initState() {
    super.initState();
    _getFollowing();
  }

  Future<void> _getFollowing() async {
    GetFollowings result = await widget.apifollowing. getFollowingsAPI();
    if (result.code == '200') {
      setState(() {
        widget.friends = result.followings!;
      });
    } else if (result.code == '401') {
      showDialog(
        context: context, // 이 부분에 정의가 필요
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
    return Scaffold(
      body: Container(
        decoration: const BoxDecoration(
          image: DecorationImage(
            image: AssetImage('assets/friends.gif'),
            fit: BoxFit.cover,
            opacity: 0.5,
          ),
        ),
        
        child: Padding(
          padding: const EdgeInsets.only(top: 90), // 상단 공백을 조절하려면 이 값을 조정하세요
          child: widget.friends.length==0 ?
          Center(
            child: Image.asset('assets/emptyfriend.png'),
          ) :
          GridView.builder(
            gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
              crossAxisCount: 2,
              crossAxisSpacing: 10.0,
              mainAxisSpacing: 10.0,
            ),
            padding: const EdgeInsets.all(16.0),
            itemCount: widget.friends.length,
            itemBuilder: (context, index) {
              return Card(
                elevation: 2.0,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(20.0),
                ),
                child: Stack(
                  children: [
                    Center(
                      child: Image.network(
                        '${widget.friends[index].profileAnimon?.bodyImagePath ?? ''}',
                        fit: BoxFit.cover,
                        alignment: Alignment.center,
                      ),
                    ),
                    Positioned(
                      top: 0,
                      right: 0,
                      child: Row(
                        children: [
                          IconButton(
                            onPressed: () {
                              showDialog(
                                context: context,
                                barrierDismissible: false,
                                builder: (BuildContext ctx) {
                                  return CustomDialog(
                                    children: [
                                      const Text(
                                        '친구를 삭제할거야?',
                                        style: TextStyle(
                                          fontSize: 24,
                                          color: Colors.white,
                                        ),
                                      ),
                                      const SizedBox(height: 16),
                                      Row(
                                        mainAxisAlignment: MainAxisAlignment.center,
                                        children: [
                                          ElevatedButton(
                                            onPressed: () async{
                                              final response = await apiDeletefollowing.deletefollowing(widget.friends[index].id);
                                              if(response.code == '204') {
                                                await _getFollowing();
                                                Navigator.of(ctx).pop();
                                              }
                                              else{
                                                Navigator.of(ctx).pop();
                                              }
                                            },
                                            style: ElevatedButton.styleFrom(
                                              backgroundColor: const Color(0xffff6347),
                                            ),
                                            child: const Text(
                                              '삭제하기',
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
                            icon: const Icon(
                              Icons.delete_forever_rounded,
                              size: 30,
                              color: Colors.red,
                            ),
                            splashRadius: 16,
                          )
                        ],
                      ),
                    ),
                    Positioned(
                      bottom: 0,
                      left: 0,
                      right: 0,
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceAround,
                        children: [
                          Container(
                            padding: const EdgeInsets.symmetric(
                              vertical: 8.0,
                              horizontal: 10.0,
                            ),
                            decoration: BoxDecoration(
                              color: Colors.green.withOpacity(0.8),
                              borderRadius: BorderRadius.circular(16.0),
                            ),
                            child: Text(
                              '${widget.friends[index].name}',
                              style: const TextStyle(color: Colors.white),
                            ),
                          ),
                          ElevatedButton.icon(
                            onPressed: () {
                              showDialog(
                                context: context,
                                barrierDismissible: false,
                                builder: (BuildContext ctx) {
                                  return CustomDialog(
                                    children: [
                                      Text(
                                        '${widget.friends[index].name} 초대할거야?',
                                        style: const TextStyle(
                                          fontSize: 24,
                                          color: Colors.white,
                                        ),
                                      ),
                                      const SizedBox(height: 16),
                                      Row(
                                        mainAxisAlignment: MainAxisAlignment.center,
                                        children: [
                                          ElevatedButton(
                                            onPressed: () {
                                              Navigator.of(ctx).pop();
                                            },
                                            style: ElevatedButton.styleFrom(
                                              backgroundColor: const Color(0xff00b7eb),
                                            ),
                                            child: const Text(
                                              '초대하기',
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
                            style: ElevatedButton.styleFrom(
                              backgroundColor: Colors.green.withOpacity(0.8),
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(16.0),
                              ),
                            ),
                            icon: const Text(
                              '초대',
                              style: TextStyle(fontSize: 14),
                            ),
                            label: const Icon(
                              Icons.send,
                              size: 14,
                            ),
                          )
                        ],
                      ),
                    ),
                  ],
                ),
              );
            },
          ),
        )

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
