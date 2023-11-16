class generalResponse {
  String? code;
  String? status;

  generalResponse(String? code, String? status) {
    this.code = code;
    this.status = status;
  }

  generalResponse.fromJson(Map<String, dynamic> json) {
    code = json['code'];
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['code'] = this.code;
    data['status'] = this.status;
    return data;
  }
}
