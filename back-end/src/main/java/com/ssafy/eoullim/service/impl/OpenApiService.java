package com.ssafy.eoullim.service.impl;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Service
public class OpenApiService {
  // Open Api
  @Value("${public-api.service-key}")
  private String openApiServiceKey;

  @Value("${public-api.url}")
  private String openApiUrl;

  public Boolean isValidSchool(String keyword) {
    try {
      URL url = getOpenApiUrl(keyword);
      // http connection
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Content-type", "application/json");
      System.out.println("Response code: " + conn.getResponseCode());
      // response reader
      BufferedReader rd;
      if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) { // success
        rd =
            new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
      } else {
        rd =
            new BufferedReader(
                new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
      }
      StringBuilder outputStringBuilder = new StringBuilder(); // output store
      String line;
      while ((line = rd.readLine()) != null) {
        outputStringBuilder.append(line);
      }
      // disconnect
      rd.close();
      conn.disconnect();
      // result
      return !outputStringBuilder
          .toString()
          .contains("NODATA_ERROR"); // 있는 학교면 True, No Data면 False
    } catch (IOException e) { // ERROR : Http Connection (api 호출 과정에서 error)
      throw new EoullimApplicationException(
          ErrorCode.OPEN_API_CONNECTION_ERROR, "Http Connection ERROR");
    }
  }

  public URL getOpenApiUrl(String keyword) {
    StringBuilder urlBuilder = new StringBuilder(openApiUrl);
    try {
      urlBuilder
          .append("?")
          .append(URLEncoder.encode("ServiceKey", StandardCharsets.UTF_8))
          .append("=")
          .append(openApiServiceKey);
      urlBuilder
          .append("&")
          .append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8))
          .append("=")
          .append(URLEncoder.encode("1", StandardCharsets.UTF_8));
      urlBuilder
          .append("&")
          .append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8))
          .append("=")
          .append(URLEncoder.encode("100", StandardCharsets.UTF_8));
      urlBuilder
          .append("&")
          .append(URLEncoder.encode("type", StandardCharsets.UTF_8))
          .append("=")
          .append(URLEncoder.encode("json", StandardCharsets.UTF_8));
      urlBuilder
          .append("&")
          .append(URLEncoder.encode("schoolSe", StandardCharsets.UTF_8))
          .append("=")
          .append(URLEncoder.encode("초등학교", StandardCharsets.UTF_8));
      urlBuilder
          .append("&")
          .append(URLEncoder.encode("schoolNm", StandardCharsets.UTF_8))
          .append("=")
          .append(URLEncoder.encode(keyword + "초등학교", StandardCharsets.UTF_8));
      return new URL(urlBuilder.toString());
    } catch (MalformedURLException e) {
      throw new EoullimApplicationException(
          ErrorCode.OPEN_API_CONNECTION_ERROR, "잘못된 URL로 인해 API 요청을 보낼 수 없습니다.");
    }
  }
}
