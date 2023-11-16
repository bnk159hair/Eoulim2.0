package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Record;
import com.ssafy.eoullim.model.RecordList;
import com.ssafy.eoullim.model.Room;
import com.ssafy.eoullim.model.entity.ChildEntity;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public interface RecordService {
  String uploadToS3(String recordingId, String recordFolder);

  void saveRecordToDB(String url, ChildEntity owner, ChildEntity other);

  void writeVideoToDB(String recordingId, Room room) throws IOException, ParseException;

  List<RecordList> getRecordList(Long masterId);

  Record getRecord(Long recordId);
}
