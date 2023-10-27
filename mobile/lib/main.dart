import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:mobile/screen/splash_screen.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    SystemChrome.setSystemUIOverlayStyle(
      const SystemUiOverlayStyle(
        statusBarColor: Colors.transparent,
        //color set to transperent or set your own color
        statusBarIconBrightness: Brightness.light,
        //set brightness for icons, like dark background light icons
      ),
    );

    return const MaterialApp(
      debugShowCheckedModeBanner: false,
      debugShowMaterialGrid: false,
      title: 'Eoullim',
      home: Splash(),
    );
  }
}

// void main() async {
//   runApp(
//     MaterialApp(
//       debugShowCheckedModeBanner: false,
//       title: 'Eoullim',
//       initialRoute: '/',
//       routes: {
//         '/': (context) => Login(),
//       },
//     ),
//   );
// }
