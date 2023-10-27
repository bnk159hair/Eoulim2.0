class LoginRequestModel {
  String id;
  String password;

  LoginRequestModel({
    required this.id,
    required this.password,
  });

  Map<String, String> toJson() {
    Map<String, String> map = {
      'userName': id.trim(),
      'password': password.trim(),
    };

    return map;
  }
}