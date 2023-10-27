package com.ssafy.eoullim.controller;

import com.ssafy.eoullim.dto.request.ChildSchoolRequest;
import com.ssafy.eoullim.dto.request.ChildRequest;
import com.ssafy.eoullim.dto.response.Response;
import com.ssafy.eoullim.model.Animon;
import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.OtherChild;
import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.service.ChildService;
import com.ssafy.eoullim.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  public Response<Child> login(@PathVariable @NotBlank Integer childId) {
    Child child = childService.login(childId);
    return Response.success(child);
  }

  @PostMapping("/logout/{childId}")
  public Response<Void> logout(@PathVariable @NotBlank Integer childId) {
    childService.logout(childId);
    return Response.success();
  }

  @PostMapping
  public Response<Void> create(@Valid @RequestBody ChildRequest request, Authentication authentication) {
    User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
    childService.create(user, request);
    return Response.success();
  }

  @PutMapping("/{childId}")
  public Response<Void> modify(@PathVariable @NotBlank Integer childId, @Valid @RequestBody ChildRequest request) {
    childService.modify(childId, request);
    return Response.success();
  }

  @DeleteMapping("/{childId}")
  public Response<Void> delete(@PathVariable @NotBlank Integer childId, Authentication authentication) {
    User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
    childService.delete(childId, user.getId());
    return Response.success();
  }

  @GetMapping("/{childId}")
  public Response<Child> getChildInfo(
      @PathVariable @NotBlank Integer childId, Authentication authentication) {
    User user =
        ClassUtils.getSafeCastInstance(
            authentication.getPrincipal(), User.class); // 현재 api를 요청한 사용자(Client)
    Child child = childService.getChildInfo(childId, user.getId());
    return Response.success(child);
  }

  // TODO  : api url refactoring
  @GetMapping("/participant/{participantId}")
  public Response<OtherChild> getParticipantInfo(@PathVariable @NotBlank Integer participantId) {
    OtherChild friend = childService.getParticipantInfo(participantId);
    return Response.success(friend);
  }

  @GetMapping
  public Response<List<Child>> getChildrenList(Authentication authentication) {
    User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
    List<Child> childrenList = childService.getChildrenList(user.getId());
    return Response.success(childrenList);
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
  public Response<String> checkSchool(@Valid @RequestBody ChildSchoolRequest request) {
    childService.checkSchool(request.getKeyword());
    return Response.success();
  }
}
