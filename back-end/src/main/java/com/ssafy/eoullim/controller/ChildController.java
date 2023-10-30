package com.ssafy.eoullim.controller;

import com.ssafy.eoullim.dto.request.ChildSchoolRequest;
import com.ssafy.eoullim.dto.request.ChildRequest;
import com.ssafy.eoullim.dto.response.SuccessResponse;
import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.OtherChild;
import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.service.ChildService;
import com.ssafy.eoullim.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/children")
@RequiredArgsConstructor
public class ChildController {

  private final ChildService childService;

  @PostMapping("/login/{childId}")
  public ResponseEntity<?> login(@PathVariable @NotBlank Integer childId) {
    Child child = childService.login(childId);
    return ResponseEntity.ok(new SuccessResponse<>(child));
  }

  @PostMapping("/logout/{childId}")
  public ResponseEntity<?> logout(@PathVariable @NotBlank Integer childId) {
    childService.logout(childId);
    return ResponseEntity.ok(new SuccessResponse<>(null));
  }

  @PostMapping
  public ResponseEntity<?> create(
      @Valid @RequestBody ChildRequest request, Authentication authentication) {
    User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
    childService.create(user, Child.of(request));
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new SuccessResponse<>(HttpStatus.CREATED.name(), HttpStatus.CREATED.value(), null));
  }

  @PutMapping("/{childId}")
  public ResponseEntity<?> modify(
      @PathVariable @NotBlank Integer childId, @Valid @RequestBody ChildRequest request) {
    childService.modify(childId, Child.of(request));
    return ResponseEntity.ok(new SuccessResponse<>(null));
  }

  @DeleteMapping("/{childId}")
  public ResponseEntity<?> delete(
      @PathVariable @NotBlank Integer childId, Authentication authentication) {
    User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
    childService.delete(childId, user.getId());
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .body(
            new SuccessResponse<>(
                HttpStatus.NO_CONTENT.name(), HttpStatus.NO_CONTENT.value(), null));
  }

  @GetMapping("/{childId}")
  public ResponseEntity<SuccessResponse<Child>> getChildInfo(
      @PathVariable @NotBlank Integer childId, Authentication authentication) {
    User user =
        ClassUtils.getSafeCastInstance(
            authentication.getPrincipal(), User.class); // 현재 api를 요청한 사용자(Client)
    Child child = childService.getChildInfo(childId, user.getId());
    return ResponseEntity.ok(new SuccessResponse<>(child));
  }

  // TODO  : api url refactoring
  @GetMapping("/participant/{participantId}")
  public ResponseEntity<SuccessResponse<OtherChild>> getParticipantInfo(
      @PathVariable @NotBlank Integer participantId) {
    OtherChild friend = childService.getParticipantInfo(participantId);
    return ResponseEntity.ok(new SuccessResponse<>(friend));
  }

  @GetMapping
  public ResponseEntity<SuccessResponse<List<Child>>> getChildrenList(
      Authentication authentication) {
    User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
    List<Child> childrenList = childService.getChildrenList(user.getId());
    return ResponseEntity.ok(new SuccessResponse<>(childrenList));
  }

  //  @GetMapping("/{childId}/animons")
  //  public Response<List<Animon>> getAnimonList(@PathVariable @NotBlank Integer childId) {
  //    List<Animon> animonList = childService.getAnimonList(childId);
  //    return Response.success(animonList);
  //  }

  //  @GetMapping("/{childId}/animons/{animonId}")
  //  public Response<Animon> selectAnimon(
  //      @PathVariable @NotBlank Integer childId, @PathVariable Integer animonId) {
  //    Animon animon = childService.setAnimon(childId, animonId);
  //    return Response.success(animon);
  //  }

  @PostMapping("/school")
  public ResponseEntity<SuccessResponse<?>> isValidSchoolName(
      @Valid @RequestBody ChildSchoolRequest request) {
    final var result = childService.isValidSchoolName(request.getKeyword());
    return ResponseEntity.ok(new SuccessResponse<>(result));
  }
}
