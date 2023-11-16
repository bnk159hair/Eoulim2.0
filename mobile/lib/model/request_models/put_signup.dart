class SignupRequestModel {
  String username;
  String password;
  String name;
  String phoneNumber;

  SignupRequestModel(
      {required this.username,
      required this.password,
      required this.name,
      required this.phoneNumber});

  Map<String, String> toJson() {
    Map<String, String> map = {
      'username': username.trim(),
      'password': password.trim(),
      'name': name.trim(),
      'phoneNumber': phoneNumber.trim()
    };

    return map;
  }
}
