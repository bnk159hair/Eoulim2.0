class LoginRequestModel {
  String id;
  String password;
  String fcmToken;

  LoginRequestModel({
    required this.id,
    required this.password,
    required this.fcmToken
  });

  Map<String, String> toJson() {
    Map<String, String> map = {
      'username': id.trim(),
      'password': password.trim(),
      'fcmToken': fcmToken.trim()
    };

    return map;
  }
}