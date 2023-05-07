package com.example.authenticationService.controller;

import com.example.authenticationService.Utils.JwtTokenUtil;
import com.example.authenticationService.dtos.BaseResponse;
import com.example.authenticationService.dtos.UpdatePassword;
import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.services.FetchInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static com.example.authenticationService.Utils.Constants.*;
import static com.example.authenticationService.Utils.Urls.*;


@RestController
@RequestMapping(USER_URL)
public class AdminController {
    @Autowired
    FetchInfoService<AdminDetails,Integer> adminDetailsIntegerFetchInfoService;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping(value = INFO)
    public BaseResponse<AdminDetails> getAdminDetail(@RequestHeader(AUTHORIZATION) String token){
        token = token.replace(BEARER_PREFIX,"");

       String username = jwtTokenUtil.getUsernameFromToken(token);
       AdminDetails adminDetails = adminDetailsIntegerFetchInfoService.getInfoByUsername(username);
        if(Objects.nonNull(adminDetails))
        {
            return new BaseResponse<>(HttpStatus.ACCEPTED.toString(), HttpStatus.OK.value(), true,"",adminDetails);
        }
        return new BaseResponse<>(HttpStatus.NO_CONTENT.toString(), HttpStatus.NO_CONTENT.value(), false,"No Admin User Found",null);
    }
    @PostMapping(value = VALIDATE_EMAIL)
    public Boolean isAdminAvailable(@RequestBody AdminDetails adminDetails)
    {
        return adminDetailsIntegerFetchInfoService.validateByEmail(adminDetails.getEmail());
    }

    @GetMapping(value = FETCH_ID)
    public synchronized Integer getAdminId(@RequestHeader(AUTHORIZATION) String token){
        token = token.replace(BEARER_PREFIX,"");
        String username =jwtTokenUtil.getUsernameFromToken(token);
        Integer adminInfoId = adminDetailsIntegerFetchInfoService.getId(username);
        if(Objects.nonNull(adminInfoId)) {
           return adminInfoId;
        }
        return -1;
    }

    @GetMapping(value = GET_ALL_USER)
    public BaseResponse<List<AdminDetails>> getAllUser(){
        List<AdminDetails> adminDetailsList = adminDetailsIntegerFetchInfoService.getAllUser();
        if(Objects.nonNull(adminDetailsList))
        {
            return new BaseResponse<>("Updated", HttpStatus.OK.value(), true,"",adminDetailsList);
        }
        return new BaseResponse<>("No Users found", HttpStatus.NO_CONTENT.value(), false,"No Users available",null);
    }

    @PutMapping(value = CHANGE_PASSWORD)
    public BaseResponse<String> updatePassword(@RequestBody UpdatePassword updatePassword){

        String isUpdated = adminDetailsIntegerFetchInfoService.forgotPasswordReset(updatePassword);
        if(Objects.nonNull(isUpdated))
        {
            return new BaseResponse<>("Updated", HttpStatus.OK.value(), true,"",isUpdated);
        }
        return new BaseResponse<>(HttpStatus.UPGRADE_REQUIRED.toString(), HttpStatus.UPGRADE_REQUIRED.value(), false,"Failed to update password",null);
    }

    @PatchMapping(value = UPDATE_PROFILE)
    public BaseResponse<AdminDetails> updateAdminDetails(@RequestBody AdminDetails adminDetails, @RequestHeader(AUTHORIZATION) String token)
    {
        token = token.replace(BEARER_PREFIX,"");
        String email = jwtTokenUtil.getUsernameFromToken(token);
        AdminDetails updatedDetail = adminDetailsIntegerFetchInfoService.updateProfile(adminDetails,email);
        if(Objects.nonNull(updatedDetail))
        {
            return new BaseResponse<>("Update Successful",HttpStatus.OK.value(),true,"",updatedDetail);
        }
        return new BaseResponse<>("Not Updated",HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), false,"updateDetails is null",null);
    }


    @GetMapping(value = GET_EMAIL)
    public String getEmailByToken(@RequestHeader(AUTHORIZATION) String token){
        token = token.replace(BEARER_PREFIX, "");
        return jwtTokenUtil.getUsernameFromToken(token);
    }


    private HttpEntity<String> setTokenInHeaders(String token){
        HttpHeaders httpHeaders = getHeaders();
        httpHeaders.set(AUTHORIZATION,token);
        return new HttpEntity<>(httpHeaders);
    }
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.set(ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

}
