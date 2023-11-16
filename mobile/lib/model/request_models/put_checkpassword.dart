class CheckpasswordRequestModel {
  String password;

  CheckpasswordRequestModel({
    required this.password,
  });

  Map<String, String> toJson() {
    Map<String, String> map = {
      'password': password.trim(),
    };

    return map;
  }
}