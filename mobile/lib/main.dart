import 'package:mobile/screen/login_screen.dart';
import 'package:flutter/material.dart';

void main() async {
  runApp(
    MaterialApp(debugShowCheckedModeBanner: false, initialRoute: '/', routes: {
      '/': (context) => Login(),
    }),
  );
}
