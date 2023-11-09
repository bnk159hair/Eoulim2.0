import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/model/response_models/general_response.dart';
import 'package:mobile/model/response_models/get_animonlist.dart';
import 'package:http/http.dart' as http;

class ApiChangeAnimon {
  
  Future<generalResponse> getChangeAnimonsAPI(animonid) async {
    final storage = FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    String? childId = await storage.read(key: 'childId');
    String url = 'https://k9c103.p.ssafy.io/api/v1/children/$childId/animons';
   final Map<String, String> requestData = {
      'animonId': animonid.toString(),
    };
    final response = await http.patch(
      Uri.parse(url),
      headers: <String, String>{
        'Authorization': 'Bearer $authKey',
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(
        requestData
      )
    );
    if (response.statusCode == 401) {
      return generalResponse(response.statusCode.toString(), response.reasonPhrase); 
    }else{
    generalResponse animons = generalResponse(response.statusCode.toString(), response.reasonPhrase); 
    print(response.body);
    print(animons);
    return animons;}
  }
}
