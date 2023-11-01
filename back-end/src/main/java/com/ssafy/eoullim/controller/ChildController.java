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
  public ResponseEntity<?> login(@PathVariable @NotBlank Long childId) {
    Child child = childService.login(childId);
    return ResponseEntity.ok(new SuccessResponse<>(child));
  }

  @PostMapping("/logout/{childId}")
  public ResponseEntity<?> logout(@PathVariable @NotBlank Long childId) {
    childService.logout(childId);
    return ResponseEntity.ok(new SuccessResponse<>(null));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public SuccessResponse<?> create(
      @Valid @RequestBody ChildRequest request, Authentication authentication) {
    User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
    childService.create(user, Child.of(request));

    return new SuccessResponse<>(HttpStatus.CREATED, null);
  }

  @PutMapping("/{childId}")
  public ResponseEntity<?> modify(
      @PathVariable @NotBlank Long childId, @Valid @RequestBody ChildRequest request) {
    childService.modify(childId, Child.of(request));
    return ResponseEntity.ok(new SuccessResponse<>(null));
  }

  @DeleteMapping("/{childId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public SuccessResponse<?> delete(
      @PathVariable @NotBlank Long childId, Authentication authentication) {
    User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
    childService.delete(childId, user.getId());
    return new SuccessResponse<>(HttpStatus.NO_CONTENT, null);
  }

  @GetMapping
  public ResponseEntity<SuccessResponse<List<Child>>> getChildren(Authentication authentication) {
    User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
    List<Child> childrenList = childService.getChildren(user.getId());
    return ResponseEntity.ok(new SuccessResponse<>(childrenList));
  }

  @GetMapping("/{childId}")
  public ResponseEntity<SuccessResponse<Child>> getChild(
      @PathVariable @NotBlank Long childId, Authentication authentication) {
    User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
    Child child = childService.getChild(childId, user.getId());
    return ResponseEntity.ok(new SuccessResponse<>(child));
  }

  // TODO  : api url refactoring
  @GetMapping("/participant/{participantId}")
  public ResponseEntity<SuccessResponse<OtherChild>> getOtherChild(
      @PathVariable @NotBlank Long participantId) {
    OtherChild friend = childService.getOtherChild(participantId);
    return ResponseEntity.ok(new SuccessResponse<>(friend));
  }

  @PostMapping("/school")
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse<?> isValidSchool(
      @Valid @RequestBody ChildSchoolRequest request) {
    final var isValidSchool = childService.isValidSchool(request.getKeyword());
    return new SuccessResponse<>(isValidSchool);
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
}
