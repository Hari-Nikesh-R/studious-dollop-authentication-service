package com.example.authenticationService.controller;

import com.example.authenticationService.Utils.Urls;
import com.example.authenticationService.config.JwtTokenUtil;
import com.example.authenticationService.dtos.BaseResponse;
import com.example.authenticationService.dtos.UpdatePassword;
import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.services.FetchInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
        token = token.replace("Bearer ","");

       String email = jwtTokenUtil.getUsernameFromToken(token);
       AdminDetails adminDetails = adminDetailsIntegerFetchInfoService.getInfoByEmail(email);
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
        token = token.replace("Bearer ","");
        String email =jwtTokenUtil.getUsernameFromToken(token);
        Integer adminInfoId = adminDetailsIntegerFetchInfoService.getId(email);
        if(Objects.nonNull(adminInfoId)) {
           return adminInfoId;
        }
        return -1;
    }

//    @PutMapping(value = Urls.UPDATE_PASSWORD)
//        public BaseResponse<String> updatePassword(@RequestBody UpdatePassword updatePassword, @RequestHeader(AUTHORIZATION) String token){
//        HttpEntity<String> entity = setTokenInHeaders(token);
//        Integer id = restTemplate.exchange(AUTHENTICATION_URL + "/user/fetch-id", HttpMethod.GET,entity,Integer.class).getBody();
//        updatePassword.setId(id);
//        String isUpdated = adminDetailsIntegerFetchInfoService.changePassword(updatePassword);
//        if(Objects.nonNull(isUpdated))
//        {
//            return new BaseResponse<>("Updated", HttpStatus.OK.value(), true,"",isUpdated);
//        }
//        return new BaseResponse<>(HttpStatus.UPGRADE_REQUIRED.toString(), HttpStatus.UPGRADE_REQUIRED.value(), false,"Failed to update password",null);
//    }

    @PutMapping(value = CHANGE_PASSWORD)
    public BaseResponse<String> updatePassword(@RequestBody UpdatePassword updatePassword){

        String isUpdated = adminDetailsIntegerFetchInfoService.forgotPasswordReset(updatePassword);
        if(Objects.nonNull(isUpdated))
        {
            return new BaseResponse<>("Updated", HttpStatus.OK.value(), true,"",isUpdated);
        }
        return new BaseResponse<>(HttpStatus.UPGRADE_REQUIRED.toString(), HttpStatus.UPGRADE_REQUIRED.value(), false,"Failed to update password",null);
    }

    @PutMapping(value = UPDATE_PROFILE)
    public BaseResponse<AdminDetails> updateAdminDetails(@RequestBody AdminDetails adminDetails, @RequestHeader(AUTHORIZATION) String token)
    {
        token = token.replace("Bearer ","");
        String email = jwtTokenUtil.getUsernameFromToken(token);
        AdminDetails updatedDetail = adminDetailsIntegerFetchInfoService.updateProfile(adminDetails,email);
        if(Objects.nonNull(updatedDetail))
        {
            return new BaseResponse<>("Update Successful",HttpStatus.OK.value(),true,"",updatedDetail);
        }
        return new BaseResponse<>("Not Updated",HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), false,"updateDetails is null",null);
    }


    @GetMapping(value = "/get-email")
    public String getEmailByToken(@RequestHeader(AUTHORIZATION) String token){
        token = token.replace("Bearer ", "");
        return jwtTokenUtil.getUsernameFromToken(token);
    }


    private HttpEntity<String> setTokenInHeaders(String token){
        HttpHeaders httpHeaders = getHeaders();
        httpHeaders.set(AUTHORIZATION,token);
        return new HttpEntity<>(httpHeaders);
    }
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

}
