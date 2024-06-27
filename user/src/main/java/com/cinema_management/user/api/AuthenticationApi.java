package com.cinema_management.user.api;

import com.cinema_management.user.dto.AuthenticationDTO;
import com.cinema_management.user.service.AuthenticationService;
import com.cinema_management.user.utility.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationApi {

    private final AuthenticationService service;

    @PostMapping("/token")
    protected ResponseEntity login(
            @RequestBody AuthenticationDTO request
    ){
        try {
            return ResponseUtils.handlerSuccess(service.authenticate(request));
        } catch (Exception ex){
            return ResponseUtils.handlerException(ex);
        }
    }

    @PostMapping("/introspect")
    protected ResponseEntity introspect(
            @RequestParam String token
    ){
        try {
            return ResponseUtils.handlerSuccess(service.introspect(token));
        } catch (Exception ex){
            return ResponseUtils.handlerException(ex);
        }
    }

    @PostMapping("/logout")
    protected ResponseEntity logout(
            @RequestParam String token
    ){
        try {
            service.logout(token);
            return ResponseUtils.handlerSuccess();
        } catch (Exception ex){
            return ResponseUtils.handlerException(ex);
        }
    }

    @PostMapping("/refresh")
    protected ResponseEntity refresh(
            @RequestParam String token
    ){
        try {
            return ResponseUtils.handlerSuccess(service.refreshToken(token));
        } catch (Exception ex){
            return ResponseUtils.handlerException(ex);
        }
    }
}
