package com.example.authenticationService.dtos;

import lombok.Data;

@Data
public class UpdatePassword {
    private Integer id;
    private String email;
    private String username;
    private String code;
}
