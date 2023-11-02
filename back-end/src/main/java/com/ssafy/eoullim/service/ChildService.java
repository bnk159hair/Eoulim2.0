package com.ssafy.eoullim.service;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.OtherChild;
import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.model.entity.AnimonEntity;
import com.ssafy.eoullim.model.entity.ChildEntity;
import com.ssafy.eoullim.model.entity.UserEntity;
import com.ssafy.eoullim.repository.AnimonRepository;
import com.ssafy.eoullim.repository.ChildCacheRepository;
import com.ssafy.eoullim.repository.ChildRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChildService {

    private final ChildRepository childRepository;
    //  private final ChildAnimonRepository childAnimonRepository;
    private final AnimonRepository animonRepository;
    private final ChildCacheRepository childCacheRepository;

    // Open Api
    @Value("${public-api.service-key}")
    private String openApiServiceKey;

    @Value("${public-api.url}")
    private String openApiUrl;

    @Transactional
    public Child login(Long childId) {
        final var childEntity = getChildEntity(childId);
        childCacheRepository.setStatus(childId);
        return Child.fromEntity(childEntity);
    }

    @Transactional
    public void logout(Long childId) {
        final var childEntity = getChildEntity(childId);
        childCacheRepository.delete(childId);
    }

    @Transactional
    public Child create(User user, Child child) {
        // 저장할 ChildEntity 생성
        ChildEntity childEntity = ChildEntity.of(UserEntity.of(user), child);

        // 애니몬을 랜덤으로 몇개만 지급
        //    List<AnimonEntity> animonEntities = animonRepository.findAllByIdIn(List.of(1,2,3,4));

        // 기본 프로필 애니몬 선택
        AnimonEntity animonEntity =
                animonRepository
                        .findById(1L)
                        .orElseThrow(() -> new EoullimApplicationException(ErrorCode.DB_NOT_FOUND, "애니몬 없다. "));
        childEntity.setAnimon(animonEntity);

        final var newChildEntity = childRepository.save(childEntity);

        // child 저장
        log.error(childEntity.toString());

        // 기본 애니몬 4종을 해당 Child에 부여
        //    List<AnimonEntity> animons = animonRepository.getDefaultAnimon();
        //    for (AnimonEntity animonEntity : animons) {
        //      if (animonEntity.getId() == 1) childEntity.setAnimon(animonEntity); // 4종 중 1번 애니몬을 선택
        //      childAnimonRepository.save(ChildAnimonEntity.of(childEntity, animonEntity));
        //    }
        return Child.fromEntity(newChildEntity);
    }

    @Transactional
    public Child modify(Long childId, Child child) {
        final var childEntity = getChildEntity(childId);
        childEntity.modify(child);
        return Child.fromEntity(childEntity);
    }

    @Transactional
    public void delete(Long childId, Long userId) {
        final var childEntity =
                childRepository
                        .findByIdAndUserId(childId, userId)
                        .orElseThrow(() -> new EoullimApplicationException(ErrorCode.FORBIDDEN_NO_PERMISSION));
        childRepository.delete(childEntity);
    }

    // TODO:  DB에 Child가 만든 애 + Default 1234
    //  public List<Animon> getAnimonList(Integer childId) {
    //    return childAnimonRepository.findAnimonsByChildId(childId).stream()
    //        .map(Animon::fromEntity)
    //        .collect(Collectors.toList());
    //  }

    // TODO: fix
    //  @Transactional
    //  public Animon setAnimon(Integer childId, Integer animonId) {
    //    ChildAnimonEntity childAnimonEntity =
    //        childAnimonRepository
    //            .findByChildIdAndAnimonId(childId, animonId)
    //            .orElseThrow(() -> new
    // EoullimApplicationException(ErrorCode.CHILD_ANIMON_NOT_FOUND));
    //    //    new IllegalArgumentException("Child가 소유하지 않은 애니몬은 사용할 수 없습니다."));
    //    AnimonEntity animonEntity = childAnimonEntity.getAnimon();
    //    ChildEntity childEntity = childAnimonEntity.getChild();
    //    childEntity.setAnimon(animonEntity);
    //    return Animon.fromEntity(animonEntity);
    //  }

    public List<Child> getChildren(Long userId) {
        return childRepository
                .findAllByUserId(userId)
                .orElseThrow(() -> new EoullimApplicationException(ErrorCode.USER_NOT_FOUND))
                .stream()
                .map(Child::fromEntity)
                .collect(Collectors.toList());
    }

    public Child getChild(Long childId, Long userId) {
        final var childEntity = getChildEntity(childId); // 일단 실제 있는 Child인지 조회
        if (!childEntity.getUser().getId().equals(userId)) // 그 Child가 User의 Child인지
            throw new EoullimApplicationException(ErrorCode.FORBIDDEN_NO_PERMISSION);
        return Child.fromEntity(childEntity);
    }

    public ChildEntity getChildEntity(Long childId) {
        return childRepository
                .findById(childId)
                .orElseThrow(() -> new EoullimApplicationException(ErrorCode.CHILD_NOT_FOUND));
    }

    public OtherChild getOtherChild(Long participantId) {
        return OtherChild.fromEntity(getChildEntity(participantId));
    }

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
