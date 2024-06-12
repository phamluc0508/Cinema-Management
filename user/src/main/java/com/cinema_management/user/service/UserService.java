package com.cinema_management.user.service;

import com.cinema_management.user.model.User;
import com.cinema_management.user.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class UserService {

    private final UserRepo repo;
    private final PasswordEncoder passwordEncoder;

    public User createUser (User request){
        var exist = repo.findByUsername(request.getUsername());
        if(exist.isPresent()){
            throw new RuntimeException("username-existed");
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return repo.save(request);
    }

    public User updateUser(String userId, User request){
        var exist = repo.findByUsernameAndIdIsNot(request.getUsername(), userId);
        if(exist.isPresent()){
            throw new RuntimeException("username-existed");
        }

        User user = repo.findById(userId).orElseThrow(() -> new RuntimeException("not-found-with-userId"));
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return repo.save(user);
    }

    public String deleteUser(String userId){
        User user = repo.findById(userId).orElseThrow(() -> new RuntimeException("not-found-with-userId"));
        repo.delete(user);
        return "delete success user with id:" + userId;
    }

    public List<User> getUsers(){
        return repo.findAll();
    }

    public User getById(String userId){
        return repo.findById(userId).orElseThrow(() -> new RuntimeException("not-found-with-userId"));
    }

}
