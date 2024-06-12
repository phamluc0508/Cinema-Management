package com.cinema_management.user.api;

import com.cinema_management.user.model.User;
import com.cinema_management.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserApi {
    private final UserService service;

    @PostMapping()
    protected ResponseEntity createUser(
            @RequestBody User request
    ){
        try{
            return ResponseEntity.ok(service.createUser(request));
        } catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{userId}")
    protected ResponseEntity updateUser(
            @PathVariable("userId") String userId,
            @RequestBody User request
    ){
        try{
            return ResponseEntity.ok(service.updateUser(userId, request));
        } catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    protected ResponseEntity deleteUser(
            @PathVariable("userId") String userId
    ){
        try{
            return ResponseEntity.ok(service.deleteUser(userId));
        } catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/list")
    protected ResponseEntity getAll(){
        try {
            return ResponseEntity.ok(service.getUsers());
        } catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{userId}")
    protected ResponseEntity getById(@PathVariable("userId") String userId){
        try{
            return ResponseEntity.ok(service.getById(userId));
        } catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
