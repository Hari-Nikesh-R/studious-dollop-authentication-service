package com.example.authenticationService.model;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_table")
public class AdminDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String email;
    private String mobile;
    private String username;
    private String passwordHash;
    private Boolean isDeleted;
    private Long roleId;
    private Boolean isActive;
}
