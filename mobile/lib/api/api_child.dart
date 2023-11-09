import 'package:http/http.dart' as http;
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class ApiChild {
  static const baseUrl = 'https://k9c103.p.ssafy.io/api/v1/children';
  static const storage = FlutterSecureStorage();

  static Future<void> putChild() async {
    String? authKey = await storage.read(key: 'Authkey');
    String? childId = await storage.read(key: 'childId');
    final response = await http.put(
      Uri.parse('$baseUrl/$childId'),
      headers: <String, String>{
        'Authorization': "Bearer $authKey",
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: {
        "birth": "string",
        "gender": "string",
        "grade": 1,
        "name": "string",
        "school": "string"
      },
    );
    print(response);
    print(response.statusCode);
  }

  static Future<void> deleteChild() async {
    const storage = FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    String? childId = await storage.read(key: 'childId');
    final response = await http.delete(
      Uri.parse('$baseUrl/$childId'),
      headers: <String, String>{
        'Authorization': "Bearer $authKey",
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );
    print(response);
    print(response.statusCode);
  }
}
