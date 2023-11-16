class ProfileLoginRequestModel {
  int? childId;
  String fcmToken;

  ProfileLoginRequestModel({
    this.childId,
    required this.fcmToken,
  });

  Map<String, dynamic> toJson() {
    Map<String, dynamic> map = {
      'childId': childId,
      'fcmToken': fcmToken.trim(),
    };

    return map;
  }
}