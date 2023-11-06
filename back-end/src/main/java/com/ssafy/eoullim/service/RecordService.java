package com.ssafy.eoullim.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.eoullim.model.RecordList;
import com.ssafy.eoullim.model.Record;
import com.ssafy.eoullim.model.Room;
import com.ssafy.eoullim.model.entity.ChildEntity;
import com.ssafy.eoullim.model.entity.GuideEntity;
import com.ssafy.eoullim.model.entity.RecordEntity;
import com.ssafy.eoullim.model.entity.RecordGuideEntity;
import com.ssafy.eoullim.repository.jpa.ChildRepository;
import com.ssafy.eoullim.repository.jpa.GuideRepository;
import com.ssafy.eoullim.repository.jpa.RecordGuideRepository;
import com.ssafy.eoullim.repository.jpa.RecordRepository;
import com.ssafy.eoullim.repository.query.RecordQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final GuideRepository guideRepository;
    private final RecordGuideRepository recordGuideRepository;
    private final ChildRepository childRepository;

    private final RecordQueryRepository recordQueryRepository;

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String S3BucketName;

    @Value("${OPENVIDU_URL}")
    private String OPENVIDU_URL;

    public void uploadVideoToS3(String recordingId, Room room) {
        String dir = "/var/lib/recorings/";
        String recordFolder = dir + recordingId + "/";

        // 업로드 할 파일 경로
        String filePath = recordFolder + "VideoInfo.json";

        // S3 객체 키 (파일 경로와 파일 이름)
        String s3ObjectKey = "recordings/" + recordingId + "/VideoInfo.json";

        try {
            // Amazon S3로 파일 업로드
            PutObjectRequest putRequest = new PutObjectRequest(S3BucketName, s3ObjectKey, new File(filePath));
            amazonS3Client.putObject(putRequest);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        }
    }

    public void writeVideoToDB(String recordingId, Room room) throws IOException, ParseException {

        System.out.println(room.getChildOne());
        System.out.println(room.getChildTwo());

        /* 압축 해제 코드 시작 */
//        String dir = "C:\\Users\\ssafy\\Downloads\\";
        String dir = "/var/lib/recordings/";

        String recordFolder = dir + recordingId + "/";
        File recordZip = new File(recordFolder, "VideoInfo.zip");

        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(recordZip))) {
            try (ZipInputStream zipInputStream = new ZipInputStream(in)) {
                ZipEntry zipEntry = null;

                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    int length = 0;
                    try (BufferedOutputStream out = new BufferedOutputStream((new FileOutputStream(recordFolder + zipEntry.getName())))) {
                        while ((length = zipInputStream.read()) != -1) {
                            out.write(length);
                        }
                        zipInputStream.closeEntry();
                        ;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        /* 압축 해제 코드 종료 */

        /* JSON Parse 시작 */
        JSONParser parser = new JSONParser();

        Reader reader = new FileReader(recordFolder + "VideoInfo.json");
        JSONObject jsonObject = (JSONObject) parser.parse(reader);

        JSONArray files = (JSONArray) jsonObject.get("files");

        String downDir = OPENVIDU_URL + "/openvidu/recordings/";
        String downFolder = downDir + recordingId + "/";
        for (int i = 0; i < files.size(); i++) {
            JSONObject element = (JSONObject) files.get(i);
            String name = String.valueOf(element.get("name"));
            JSONObject clientData = (JSONObject) parser.parse((String) element.get("clientData"));
            Long masterId = Long.parseLong((String) clientData.get("childId"));
            ChildEntity master = childRepository.findById(masterId).orElseThrow();
            ChildEntity participant = null;
            if(room.getChildOne().equals(masterId)){ // 영상의 주인이 첫번째 사람
                participant = childRepository.findById(room.getChildTwo()).orElseThrow();

//                recordRepository.save(RecordEntity.of(downFolder+name, master, participant, room.getGuideSeq(), room.getTimeline()));
            }
            if(room.getChildTwo().equals(masterId)){ // 영상의 주인이 두번째 사람
                participant = childRepository.findById(room.getChildOne()).orElseThrow();
            }
            RecordEntity record = RecordEntity.builder().videoPath(downFolder+name).master(master).participant(participant).build();
            recordRepository.save(record);
            List<Integer> order = room.getGuideSeq();
            List<String> timeline = room.getTimeline();
            for(int idx=0; idx< order.size(); idx++){
                GuideEntity guide = guideRepository.findById(order.get(idx).longValue()).orElseThrow();
                RecordGuideEntity recordGuide = RecordGuideEntity.builder().record(record).guide(guide).sequence(idx).timeline(timeline.get(idx)).build();
                recordGuideRepository.save(recordGuide);
            }

        }
        /* JSON Parse 종료 */
    }

    public List<RecordList> getRecordList(Long masterId) {
        return recordQueryRepository.findRecordsByChildId(masterId);
    }

    public Record getRecord(Long recordId) {
        Record result = recordQueryRepository.findRecordById(recordId);
        result.setGuideInfo(recordQueryRepository.findGuideInfoById(recordId));
        return result;
    }
}