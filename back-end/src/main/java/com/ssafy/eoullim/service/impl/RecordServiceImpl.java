package com.ssafy.eoullim.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
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
import com.ssafy.eoullim.service.RecordService;
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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

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

    @Override
    public String uploadToS3(String recordingId, String recordFolder) {
        // S3에 업로드할 파일 정보
        File recordZip = new File(recordFolder, "VideoInfo.zip");

        // S3에 업로드할 객체 키 (파일 이름)
        String s3ObjectKey = "recordings/" + recordingId + "/VideoInfo.zip";

        try {
            // 파일을 S3에 업로드
            PutObjectRequest putObjectRequest = new PutObjectRequest(S3BucketName, s3ObjectKey, recordZip);
            ObjectMetadata metadata = new ObjectMetadata();
            putObjectRequest.setMetadata(metadata);
            amazonS3Client.putObject(putObjectRequest);

            System.out.println("파일이 성공적으로 S3에 업로드되었습니다.");

            // S3 URL 생성
            String s3FileURL = amazonS3Client.getUrl(S3BucketName, s3ObjectKey).toString();

            // S3 URL을 데이터베이스에 저장 또는 필요한 처리 수행
            // 예: recordRepository.save(RecordEntity.of(s3FileURL, ...));
            return s3FileURL;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("파일 업로드 중 오류가 발생했습니다.");
            throw new EoullimApplicationException(ErrorCode.S3_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public void saveRecordToDB(String url, ChildEntity owner, ChildEntity other) {
        final var recordEntity = RecordEntity.builder().participant(other)
                .master(owner)
                .videoPath(url).build();

        final var savedRecordEntity = recordRepository.save(recordEntity);
        return ;    //mapper
    }

    @Override
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

        // S3에 업로드
//        String s3url = uploadToS3(recordingId, recordFolder);

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

            // S3 Data To DB
//            saveRecordToDB(s3url, master, participant);

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

    @Override
    public List<RecordList> getRecordList(Long masterId) {
        return recordQueryRepository.findRecordsByChildId(masterId);
    }

    @Override
    public Record getRecord(Long recordId) {
        Record result = recordQueryRepository.findRecordById(recordId);
        result.setGuideInfo(recordQueryRepository.findGuideInfoById(recordId));
        return result;
    }
}