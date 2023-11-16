class CreateprofileRequestModel {
  String name;
  String birth;
  String gender;
  String school;
  int? grade;

  CreateprofileRequestModel({
    required this.name,
    required this.birth,
    required this.gender,
    required this.school,
    required this.grade
  });

  Map<String, dynamic> toJson() {
    Map<String, dynamic> map = {
      'name': name.trim(),
      'birth': birth,
      'gender': gender.trim(),
      'school': school.trim(),
      'grade': grade
    };

    return map;
  }
}