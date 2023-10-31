class SignupRequestModel {
  String id;
  String password;
  String name;
  String phoneNumber;

  SignupRequestModel({
    required this.id,
    required this.password,
    required this.name,
    required this.phoneNumber
  });

  Map<String, String> toJson() {
    Map<String, String> map = {
      'username': id.trim(),
      'password': password.trim(),
      'name': name.trim(),
      'phoneNumber': phoneNumber.trim()
    };

    return map;
  }
}