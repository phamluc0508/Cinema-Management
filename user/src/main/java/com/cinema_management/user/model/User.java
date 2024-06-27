package com.cinema_management.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private String id;

    @Column
    @Email(message = "invalid-email-format")
    private String email;

    @Column
    @Pattern(regexp = "^0\\d{9}$", message = "invalid-phone-format")
    private String phoneNumber;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(255)")
    private String username;

    @Column
    @Size(min = 8, message = "password must be ae least 8 characters")
    private String password;

    @ManyToMany
    Set<Role> roles;


}
