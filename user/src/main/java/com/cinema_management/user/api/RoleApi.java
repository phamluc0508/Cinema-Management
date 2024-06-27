package com.cinema_management.user.api;

import com.cinema_management.user.dto.RoleDTO;
import com.cinema_management.user.service.RoleService;
import com.cinema_management.user.utility.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleApi {
    private final RoleService service;

    @PostMapping()
    protected ResponseEntity createPermission(
            @RequestBody RoleDTO request
    ){
        try {
            return ResponseUtils.handlerSuccess(service.createRole(request));
        } catch (Exception ex){
            return ResponseUtils.handlerException(ex);
        }
    }

    @DeleteMapping("/{name}")
    protected ResponseEntity deletePermission(
            @PathVariable("name") String name
    ){
        try{
            return ResponseUtils.handlerSuccess(service.deleteRole(name));
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
