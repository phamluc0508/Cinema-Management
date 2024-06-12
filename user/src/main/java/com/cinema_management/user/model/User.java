package com.cinema_management.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private String id;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(255)")
    private String username;

    @Column
    private String password;



}
