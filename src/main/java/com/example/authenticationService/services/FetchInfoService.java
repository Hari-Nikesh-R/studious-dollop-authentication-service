package com.example.authenticationService.services;

import com.example.authenticationService.dtos.UpdatePassword;
import com.example.authenticationService.model.AdminDetails;

import java.util.List;

public interface FetchInfoService<T,K> {
    K getId(String username);
    T getInfoById(Integer id);

    List<AdminDetails> getAllUser();

    T getInfoByUsername(String username);

    String forgotPasswordReset(UpdatePassword updatePassword);
    T updateProfile(T details,String email);
    Boolean validateByEmail(String email);
}
