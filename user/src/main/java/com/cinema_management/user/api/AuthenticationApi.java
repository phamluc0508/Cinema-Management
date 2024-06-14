package com.cinema_management.user.api;

import com.cinema_management.user.dto.AuthenticationDTO;
import com.cinema_management.user.dto.IntrospectDTO;
import com.cinema_management.user.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            return ResponseEntity.ok(service.authenticate(request));
        } catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/introspect")
    protected ResponseEntity introspect(
            @RequestBody IntrospectDTO request
    ){
        try {
            return ResponseEntity.ok(service.introspect(request));
        } catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
