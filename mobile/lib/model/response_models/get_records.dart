class RecordsModel {
  final int id;
  final List<dynamic> createTime;
  final String videoPath, name, school, animonPath;

  RecordsModel.fromJson(Map<String, dynamic> json)
      : id = json['id'],
        createTime = json['createTime'],
        videoPath = json['videoPath'],
        name = json['name'],
        school = json['school'],
        animonPath = json['animonPath'];
}
