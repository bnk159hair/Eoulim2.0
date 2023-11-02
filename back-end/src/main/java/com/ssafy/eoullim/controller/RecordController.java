package com.ssafy.eoullim.controller;

import com.ssafy.eoullim.model.RecordList;
import com.ssafy.eoullim.model.Record;
import com.ssafy.eoullim.dto.response.SuccessResponse;
import com.ssafy.eoullim.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<SuccessResponse<List<RecordList>>> getRecordList(@NotBlank @PathVariable Long childId) {
        List<RecordList> recordList = recordService.getRecordList(childId);
        return ResponseEntity.ok(new SuccessResponse<>(recordList));
    }

    @GetMapping("/list/{recordId}")
    public ResponseEntity<SuccessResponse<Record>> getRecordInfo(@NotBlank @PathVariable Long recordId) {
        Record result = recordService.getRecord(recordId);
        return ResponseEntity.ok(new SuccessResponse<>(result));
    }
}
