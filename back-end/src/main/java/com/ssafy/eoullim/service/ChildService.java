package com.ssafy.eoullim.service;

import com.ssafy.eoullim.dto.request.ChildRequest;
import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.*;
import com.ssafy.eoullim.model.entity.AnimonEntity;
import com.ssafy.eoullim.model.entity.ChildAnimonEntity;
import com.ssafy.eoullim.model.entity.ChildEntity;
import com.ssafy.eoullim.model.entity.UserEntity;
import com.ssafy.eoullim.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChildService {

  private final ChildRepository childRepository;
  private final ChildAnimonRepository childAnimonRepository;
  private final AnimonRepository animonRepository;
  private final ChildCacheRepository childCacheRepository;

  @Value("${public-api.service-key}")
  private String serviceKey;

  private String schoolApiUrl = "http://api.data.go.kr/openapi/tn_pubr_public_elesch_mskul_lc_api";



  @Transactional
  public void create(User user, ChildRequest request) {
    ChildEntity childEntity = ChildEntity.of(UserEntity.of(user), Child.of(request));
    childRepository.save(childEntity);

    // 기본 애니몬 4종을 해당 Child에 부여
    List<AnimonEntity> animons = animonRepository.getDefaultAnimon();
    for (AnimonEntity animonEntity : animons) {
      if (animonEntity.getId() == 1) childEntity.setAnimon(animonEntity); // 4종 중 1번 애니몬을 선택
      childAnimonRepository.save(ChildAnimonEntity.of(childEntity, animonEntity));
    }
  }

  @Transactional
  public Child login(Integer childId) {
    final var childEntity = getChildEntity(childId);
    childCacheRepository.setStatus(childId);
    return Child.fromEntity(childEntity);
  }

  @Transactional
  public void logout(Integer childId) {
    final var childEntity = getChildEntity(childId);
    childCacheRepository.delete(childId);
  }

  public Child getChildInfo(Integer childId, Integer userId) {
    final var childEntity = getChildEntity(childId);

    if (!childEntity.getUser().getId().equals(userId))
      throw new EoullimApplicationException(ErrorCode.FORBIDDEN_NO_PERMISSION);
    return Child.fromEntity(childEntity);
  }

  @Transactional
  public void modify(Integer childId, ChildRequest request) {
    final var childEntity = getChildEntity(childId);
    childEntity.setName(request.getName());
    childEntity.setBirth(request.getBirth());
    childEntity.setGender(request.getGender());
    childEntity.setSchool(request.getSchool());
    childEntity.setGrade(request.getGrade());
  }

  @Transactional
  public void delete(Integer childId, Integer userId) {
    final var childEntity = getChildEntity(childId);

    if (!childEntity.getUser().getId().equals(userId))
      throw new EoullimApplicationException(ErrorCode.FORBIDDEN_NO_PERMISSION);
    childRepository.delete(childEntity);
  }

  public List<Animon> getAnimonList(Integer childId) {
    return childAnimonRepository.findAnimonsByChildId(childId).stream()
        .map(Animon::fromEntity)
        .collect(Collectors.toList());
  }

  @Transactional
  public Animon setAnimon(Integer childId, Integer animonId) {
    ChildAnimonEntity childAnimonEntity =
        childAnimonRepository
            .findByChildIdAndAnimonId(childId, animonId)
            .orElseThrow(() -> new EoullimApplicationException(ErrorCode.CHILD_ANIMON_NOT_FOUND));
    //    new IllegalArgumentException("Child가 소유하지 않은 애니몬은 사용할 수 없습니다."));
    AnimonEntity animonEntity = childAnimonEntity.getAnimon();
    ChildEntity childEntity = childAnimonEntity.getChild();
    childEntity.setAnimon(animonEntity);
    return Animon.fromEntity(animonEntity);
  }

  public ChildEntity getChildEntity(Integer childId) {
    return childRepository
        .findById(childId)
        .orElseThrow(() -> new EoullimApplicationException(ErrorCode.CHILD_NOT_FOUND));
  }

  public List<Child> getChildrenList(Integer userId) {
    return childRepository.findAllByUserId(userId).stream()
            .map(Child::fromEntity)
            .collect(Collectors.toList());
  }

  public Friend getParticipantInfo(Integer participantId) {
    ChildEntity participant =
        childRepository
            .findById(participantId)
            .orElseThrow(() -> new EoullimApplicationException(ErrorCode.CHILD_NOT_FOUND));
    return Friend.fromEntity(participant);
  }

  public void checkSchool(String keyword) {
    try {
      // API URL Build
      StringBuilder urlBuilder = new StringBuilder(schoolApiUrl);
      urlBuilder
          .append("?")
          .append(URLEncoder.encode("ServiceKey", "UTF-8"))
          .append("=")
          .append(serviceKey);
      urlBuilder
          .append("&")
          .append(URLEncoder.encode("pageNo", "UTF-8"))
          .append("=")
          .append(URLEncoder.encode("1", "UTF-8"));
      urlBuilder
          .append("&")
          .append(URLEncoder.encode("numOfRows", "UTF-8"))
          .append("=")
          .append(URLEncoder.encode("100", "UTF-8"));
      urlBuilder
          .append("&")
          .append(URLEncoder.encode("type", "UTF-8"))
          .append("=")
          .append(URLEncoder.encode("json", "UTF-8"));
      urlBuilder
          .append("&")
          .append(URLEncoder.encode("schoolSe", "UTF-8"))
          .append("=")
          .append(URLEncoder.encode("초등학교", "UTF-8"));
      urlBuilder
          .append("&")
          .append(URLEncoder.encode("schoolNm", "UTF-8"))
          .append("=")
          .append(URLEncoder.encode(keyword + "초등학교", "UTF-8"));
      // http connection
      URL url = new URL(urlBuilder.toString());
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Content-type", "application/json");
      System.out.println("Response code: " + conn.getResponseCode());
      BufferedReader rd;
      if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) { // success
        rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
      } else {
        rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
      }
      StringBuilder outputStringBuilder = new StringBuilder(); // output store
      String line;
      while ((line = rd.readLine()) != null) {
        outputStringBuilder.append(line);
      }
      rd.close();
      conn.disconnect();
      // ERROR : 일치하는 초등학교가 없는 경우
      if (outputStringBuilder.toString().contains("NODATA_ERROR")) {
        throw new EoullimApplicationException(ErrorCode.INVALID_SCHOOL_NAME);
      } else { // 나온 결과물을 가지고 갈 수도 있음!! 지금은 쓸모 없으니 그냥 있나 없나만 확인
        return; // 일치하는 초등학교 있는 경우
      }
    } catch (IOException e) { // ERROR : Http Connection (api 호출 과정에서 error)
      throw new EoullimApplicationException(ErrorCode.OPEN_API_CONNECTION_ERROR);
    }
  }
}
