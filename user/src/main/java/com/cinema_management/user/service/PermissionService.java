package com.cinema_management.user.service;

import com.cinema_management.user.model.Permission;
import com.cinema_management.user.repository.PermissionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepo repo;

    public Permission createPermission(Permission request){
        if(request.getName() == null || request.getName().isEmpty()){
            throw new RuntimeException("request-name-not-empty");
        }
        var exist = repo.findById(request.getName());
        if(exist.isPresent()){
            throw new RuntimeException("request-name-existed");
        }
        return repo.save(request);
    }

    public List<Permission> getAll(){
        return repo.findAll();
    }

    public String deletePermission(String name){
        var exist = repo.findById(name).orElseThrow(() -> new RuntimeException("not-find-permission-with-id: " + name));
        repo.deleteById(name);
        return "successfully delete permission with id: " + name;
    }

}
