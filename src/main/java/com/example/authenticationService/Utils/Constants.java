package com.example.authenticationService.Utils;


public interface Constants {

    String UNAUTHORIZED = "Unauthorized";
    String USER_NOT_FOUND = "User not found with username: ";
    String DEFAULT_USER = "hari.nikesh.r.cce@sece.ac.in";
    String DEFAULT_PASSWORD = "Admin@123";
    String AUTHORIZATION = "Authorization";
    String BEARER_PREFIX = "Bearer ";
    String CLAIMS_ATTR = "claims";
    String PASSWORD_VALIDATION = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    String SUB = "sub";
    String EMAIL_VALIDATION = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    String UPDATE_PASSWORD = "Password Updated Successfully";

    String IS_REFRESH_TOKEN = "isRefreshToken";
    String TRUE = "true";
    String REFRESH_TOKEN = "refreshtoken";
    String CONTENT_TYPE = "Content-Type";
    String ACCEPT = "Accept";
    String USER_DISABLED = "USER_DISABLED";
    String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";


}
