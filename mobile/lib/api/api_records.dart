import 'dart:convert';

import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import 'package:mobile/model/response_models/get_records.dart';
import 'package:mobile/screen/login_screen.dart';
import 'package:mobile/util/logout_logic.dart';

class ApiRecords {
  static const baseUrl = 'https://k9c103.p.ssafy.io/api/v1';
  static const getRecordsPath = 'recordings';

  static Future<dynamic> getRecords() async {
    List<RecordsModel> recordInstances = [];
    const storage = FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    String? childId = await storage.read(key: 'childId');
    final response = await http.get(
      Uri.parse(
        '$baseUrl/$getRecordsPath/$childId',
      ),
      headers: <String, String>{
        'Authorization': "Bearer $authKey",
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );
    if (response.statusCode == 200) {
      final List<dynamic> records =
          jsonDecode(utf8.decode(response.bodyBytes))['data'];
      for (var record in records) {
        recordInstances.add(RecordsModel.fromJson(record));
      }
      print(records);
      print(recordInstances);
      return recordInstances;
    } else if (response.statusCode == 401) {
      userLogout();
    }
    throw Error();
  }
}
