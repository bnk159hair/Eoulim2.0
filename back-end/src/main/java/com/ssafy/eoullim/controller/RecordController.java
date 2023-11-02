package com.ssafy.eoullim.controller;

import com.ssafy.eoullim.dto.response.Record.RecordListResponse;
import com.ssafy.eoullim.dto.response.Record.RecordResponse;
import com.ssafy.eoullim.dto.response.SuccessResponse;
import com.ssafy.eoullim.service.RecordService;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ssafy.eoullim.model.Record;

import javax.validation.constraints.NotBlank;
import java.util.*;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/recordings")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @GetMapping("/{childId}")
    public ResponseEntity<SuccessResponse<List<RecordListResponse>>> getRecordList(@NotBlank @PathVariable Long childId) {
        List<RecordListResponse> recordList = recordService.getRecordList(childId);
        return ResponseEntity.ok(new SuccessResponse<>(recordList));
    }

    @GetMapping("/list/{recordId}")
    public ResponseEntity<SuccessResponse<RecordResponse>> getRecordInfo(@NotBlank @PathVariable Long recordId) {
        RecordResponse result = recordService.getRecord(recordId);
        return ResponseEntity.ok(new SuccessResponse<>(result));
    }
}
