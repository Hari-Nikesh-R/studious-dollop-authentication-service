package com.example.authenticationService.services;

import com.example.authenticationService.dtos.BaseResponse;

import java.util.List;

public interface RegisterService<T> {
    T save(T details);
}
