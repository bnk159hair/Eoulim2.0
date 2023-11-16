import 'package:mobile/model/response_models/general_response.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile/model/response_models/post_invite.dart';

class ApiInvite {
  Future<invitePost> inviteAPI(
    String childId,
    int friendId,
  ) async {
    String url = "https://k9c103.p.ssafy.io/api/v1/meetings/friend/start";
    final storage = new FlutterSecureStorage();
    String? authKey = await storage.read(key: 'Authkey');
    final response = await http.post(
      Uri.parse(url),
      headers: <String, String>{
        'Authorization': "Bearer ${authKey}",
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(
        {
          "childId": childId,
          "friendId": friendId,
        },
      ),
    );
    if (response.statusCode == 401) {
      return invitePost(
          response.statusCode.toString(), response.reasonPhrase, null);
    } else {
      String responseBody = utf8.decode(response.bodyBytes);
      invitePost inviteInfo = invitePost.fromJson(json.decode(responseBody));
      print(response.body);
      print("++++++++++++++++++++++++++++++++++++++++");
      print(inviteInfo);
      return inviteInfo;
    }
  }
}
