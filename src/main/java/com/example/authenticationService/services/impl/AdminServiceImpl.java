package com.example.authenticationService.services.impl;

import com.example.authenticationService.Utils.Constants;
import com.example.authenticationService.Utils.Generator;
import com.example.authenticationService.dtos.UpdatePassword;
import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.repository.AdminDetailsRepository;
import com.example.authenticationService.services.FetchInfoService;
import com.example.authenticationService.services.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
@Slf4j
@Service
public class AdminServiceImpl implements RegisterService<AdminDetails>, FetchInfoService<AdminDetails, Integer> {
    @Autowired
    AdminDetailsRepository adminDetailsRepository;

    @Override
    public AdminDetails save(AdminDetails adminDetails) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            Optional<AdminDetails> optionalAdminDetails = adminDetailsRepository.findByEmail(adminDetails.getEmail());
            if (optionalAdminDetails.isPresent()) {
                return null;
            }
            String password = Generator.generateNonce();
            log.info("Generated Password: "+password);
                System.out.println(password);
            String username = adminDetails.getEmail().substring(0,4).toLowerCase()+"veacy"+ (int) (Math.random() * (9999 - 1000 + 1)) + 1000;
            adminDetails.setUsername(username);
            log.info("Generated username: "+username);
            adminDetails.setPasswordHash(bCryptPasswordEncoder.encode(password));
            adminDetails.setIsActive(true);
            adminDetails.setIsDeleted(false);
            log.info("Successfully saved");
            return adminDetailsRepository.save(adminDetails);
    }

    @Override
    public Integer getId(String username) {
        Optional<Integer> optionalAdminId = adminDetailsRepository.fetchId(username);
        return optionalAdminId.orElse(null);
    }

    @Override
    public AdminDetails getInfoById(Integer id) {
        Optional<AdminDetails>  optionalAdminDetails = adminDetailsRepository.findById(id);
        if(optionalAdminDetails.isPresent())
        {
            log.info("Admin Details: "+optionalAdminDetails.get());
            optionalAdminDetails.get().setPasswordHash("");
            return optionalAdminDetails.get();
        }
        return null;
    }

    @Override
    public List<AdminDetails> getAllUser() {
        return adminDetailsRepository.findAll();
    }

    @Override
    public AdminDetails getInfoByUsername(String username) {
        Optional<AdminDetails> optionalAdminDetails = adminDetailsRepository.findByUsername(username);
        return optionalAdminDetails.orElse(null);
    }


    @Override
    public String forgotPasswordReset(UpdatePassword updatePassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Optional<AdminDetails> optionalAdminDetails = adminDetailsRepository.findByEmail(updatePassword.getEmail());
        if(optionalAdminDetails.isPresent())
        {
            String password = Generator.generateNonce();
            log.info("Updated Password: "+password);
            optionalAdminDetails.get().setPasswordHash(bCryptPasswordEncoder.encode(password));
            adminDetailsRepository.save(optionalAdminDetails.get());
            log.info("Password updated successfully");
            return Constants.UPDATE_PASSWORD;
        }
        return null;
    }

    @Override
    public AdminDetails updateProfile(AdminDetails details, String email) {
        Optional<AdminDetails> optionalAdminDetails = adminDetailsRepository.findByEmail(email);
        if(optionalAdminDetails.isPresent()) {
            details.setId(optionalAdminDetails.get().getId());
            details.setEmail(optionalAdminDetails.get().getEmail());
            details.setPasswordHash(optionalAdminDetails.get().getPasswordHash());
            BeanUtils.copyProperties(details, optionalAdminDetails.get());
            log.info("Profile updated with BeanUtils: "+optionalAdminDetails.get());
            return adminDetailsRepository.save(optionalAdminDetails.get());
        }
        log.warn("User not found");
        return null;
    }

    @Override
    public Boolean validateByEmail(String email) {
        Optional<AdminDetails> optionalAdminDetails = adminDetailsRepository.findByEmail(email);
        return optionalAdminDetails.isPresent();
    }


}
