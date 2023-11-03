package com.ssafy.eoullim.controller;

import com.ssafy.eoullim.dto.request.ChildLoginRequest;
import com.ssafy.eoullim.dto.request.ChildLogoutRequest;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/children")
@RestController
public class ChildController {

    private final ChildService childService;

    // Child basic CRUD
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse<?> login(@Valid @RequestBody ChildLoginRequest request, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        Child child = childService.login(request.getChildId(), request.getFcmToken(), user.getId());
        return new SuccessResponse<>(child);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SuccessResponse<?> logout(@Valid @RequestBody ChildLogoutRequest request, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        childService.logout(request.getChildId(), user.getId());
        return new SuccessResponse<>(HttpStatus.NO_CONTENT, null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public SuccessResponse<?> create(
            @Valid @RequestBody ChildRequest request, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        log.error(user.toString());
        final var child = childService.create(user, Child.of(request));

        return new SuccessResponse<>(HttpStatus.CREATED, child);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SuccessResponse<List<Child>> getChildren(Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        List<Child> childrenList = childService.getChildren(user.getId());
        return new SuccessResponse<>(childrenList);
    }

    @GetMapping("/{childId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SuccessResponse<Child> getChild(
            @PathVariable @NotBlank Long childId, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        Child child = childService.getChild(childId, user.getId());
        return new SuccessResponse<>(child);
    }

    @PutMapping("/{childId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SuccessResponse<?> modify(
            @PathVariable @NotBlank Long childId, @Valid @RequestBody ChildRequest request) {
        Child updatedChild = childService.modify(childId, Child.of(request));
        return new SuccessResponse<>(updatedChild);
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

    // TODO  : api url refactoring
    @GetMapping("/participant/{participantId}")
    public ResponseEntity<SuccessResponse<OtherChild>> getOtherChild(
            @PathVariable @NotBlank Long participantId) {
        OtherChild friend = childService.getOtherChild(participantId);
        return ResponseEntity.ok(new SuccessResponse<>(friend));
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
