package com.cinema_management.user.api;

import com.cinema_management.user.model.Permission;
import com.cinema_management.user.service.PermissionService;
import com.cinema_management.user.utility.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
public class PermissionApi {
    private final PermissionService service;

    @PostMapping()
    protected ResponseEntity createPermission(
            @RequestBody Permission request
    ){
        try {
            return ResponseUtils.handlerSuccess(service.createPermission(request));
        } catch (Exception ex){
            return ResponseUtils.handlerException(ex);
        }
    }

    @DeleteMapping("/{name}")
    protected ResponseEntity deletePermission(
            @PathVariable("name") String name
    ){
        try{
            return ResponseUtils.handlerSuccess(service.deletePermission(name));
        } catch (Exception ex){
            return ResponseUtils.handlerException(ex);
        }
    }

    @GetMapping()
    protected ResponseEntity getAll(){
        try {
            return ResponseUtils.handlerSuccess(service.getAll());
        } catch (Exception ex){
            return ResponseUtils.handlerException(ex);
        }
    }

}
