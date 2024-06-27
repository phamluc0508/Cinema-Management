package com.cinema_management.user.api;

import com.cinema_management.user.dto.UserDTO;
import com.cinema_management.user.model.User;
import com.cinema_management.user.service.UserService;
import com.cinema_management.user.utility.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserApi {
    private final UserService service;

    @PostMapping()
    protected ResponseEntity createUser(
            @RequestBody @Valid User request
    ){
        try{
            return ResponseUtils.handlerSuccess(service.createUser(request));
        } catch (Exception ex){
            return ResponseUtils.handlerException(ex);
        }
    }

    @PutMapping("/{userId}")
    protected ResponseEntity updateUser(
            @PathVariable("userId") String userId,
            @RequestBody UserDTO request
    ){
        try{
            return ResponseUtils.handlerSuccess(service.updateUser(userId, request));
        } catch (Exception ex){
            return ResponseUtils.handlerException(ex);
        }
    }

    @DeleteMapping("/{userId}")
    protected ResponseEntity deleteUser(
            @PathVariable("userId") String userId
    ){
        try{
            return ResponseUtils.handlerSuccess(service.deleteUser(userId));
        } catch (Exception ex){
            return ResponseUtils.handlerException(ex);
        }
    }

    @GetMapping("/list")
    protected ResponseEntity getAll(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username:{}",authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        try {
            return ResponseUtils.handlerSuccess(service.getUsers());
        } catch (Exception ex){
            return ResponseUtils.handlerException(ex);
        }
    }

    @GetMapping("/{userId}")
    protected ResponseEntity getById(@PathVariable("userId") String userId){
        try{
            return ResponseUtils.handlerSuccess(service.getById(userId));
        } catch (Exception ex){
            return ResponseUtils.handlerException(ex);
        }
    }

    @GetMapping("/myInfo")
    protected ResponseEntity getMyInfo(){
        try{
            return ResponseUtils.handlerSuccess(service.getMyInfo());
        } catch (Exception ex){
            return ResponseUtils.handlerException(ex);
        }
    }
}
