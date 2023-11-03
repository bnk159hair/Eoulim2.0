import 'package:flutter/material.dart';

class Friend {
  final String name;
  final String imageUrl;

  Friend(
    this.name,
    this.imageUrl,
  );
}

class Friends extends StatefulWidget {
  const Friends({super.key});

  @override
  State<Friends> createState() => _FriendsState();
}

class _FriendsState extends State<Friends> {
  final List<Friend> friends = [
    Friend('친구1', 'assets/bear.png'),
    Friend('친구2', 'assets/bear.png'),
    Friend('친구3', 'assets/bear.png'),
  ];

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
        child: GridView.builder(
          gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 2,
            crossAxisSpacing: 10.0,
            mainAxisSpacing: 10.0,
          ),
          padding: const EdgeInsets.all(16.0),
          itemCount: friends.length,
          itemBuilder: (context, index) {
            return Card(
              elevation: 2.0,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(20.0),
              ),
              child: Stack(
                children: [
                  Center(
                    child: Image.asset(
                      friends[index].imageUrl,
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
                                      mainAxisAlignment:
                                          MainAxisAlignment.center,
                                      children: [
                                        ElevatedButton(
                                          onPressed: () {
                                            Navigator.of(ctx).pop();
                                          },
                                          style: ElevatedButton.styleFrom(
                                            backgroundColor:
                                                const Color(0xffff6347),
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
                            friends[index].name,
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
                                      '${friends[index].name} 초대할거야?',
                                      style: const TextStyle(
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
                                          onPressed: () {
                                            Navigator.of(ctx).pop();
                                          },
                                          style: ElevatedButton.styleFrom(
                                            backgroundColor:
                                                const Color(0xff00b7eb),
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
