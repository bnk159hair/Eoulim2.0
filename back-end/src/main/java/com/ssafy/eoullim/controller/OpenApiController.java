package com.ssafy.eoullim.controller;

import com.ssafy.eoullim.dto.request.OpenApiSchoolRequest;
import com.ssafy.eoullim.dto.response.SuccessResponse;
import com.ssafy.eoullim.service.impl.OpenApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/open-api")
@RestController
public class OpenApiController {
  private final OpenApiService openApiService;

  @PostMapping("/schools")
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse<?> isValidSchool(@Valid @RequestBody OpenApiSchoolRequest request) {
    final var isValidSchool = openApiService.isValidSchool(request.getKeyword());
    return new SuccessResponse<>(isValidSchool);
  }
}
