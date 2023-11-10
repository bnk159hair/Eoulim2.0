import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class ApiChild {
  static const baseUrl = 'https://k9c103.p.ssafy.io/api/v1/children';
  static const storage = FlutterSecureStorage();

  static Future<int> putChild(Map<String, String> data) async {
    String? authKey = await storage.read(key: 'Authkey');
    String? childId = await storage.read(key: 'childId');
    final response = await http.put(
      Uri.parse('$baseUrl/$childId'),
      headers: <String, String>{
        'Authorization': "Bearer $authKey",
        'Content-Type': 'application/json',
      },
      body: jsonEncode(data),
    );
    return response.statusCode;
  }

  static Future<int> deleteChild() async {
    const storage = FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    String? childId = await storage.read(key: 'childId');
    final response = await http.delete(
      Uri.parse('$baseUrl/$childId'),
      headers: <String, String>{
        'Authorization': "Bearer $authKey",
        'Content-Type': 'application/json',
      },
    );
    print(childId);
    print(response.statusCode);
    return response.statusCode;
  }
}
