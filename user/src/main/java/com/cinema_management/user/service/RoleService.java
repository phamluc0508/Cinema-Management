package com.cinema_management.user.service;

import com.cinema_management.user.dto.RoleDTO;
import com.cinema_management.user.model.Role;
import com.cinema_management.user.repository.PermissionRepo;
import com.cinema_management.user.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepo repo;
    private final PermissionRepo permissionRepo;

    public Role createRole(RoleDTO request){
        if(request.getName() == null || request.getName().isEmpty()){
            throw new RuntimeException("request-name-not-empty");
        }
        var exist = repo.findById(request.getName());
        if(exist.isPresent()){
            throw new RuntimeException("request-name-existed");
        }
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        var permissions = permissionRepo.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        return repo.save(role);
    }

    public List<Role> getAll(){
        return repo.findAll();
    }

    public String deleteRole(String name){
        var exist = repo.findById(name).orElseThrow(() -> new RuntimeException("not-find-role-with-id: " + name));
        repo.deleteById(name);
        return "successfully delete role with id: " + name;
    }

}
