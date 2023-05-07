package com.example.authenticationService.services.impl;

import com.example.authenticationService.Utils.Constants;
import com.example.authenticationService.Utils.Generator;
import com.example.authenticationService.dtos.UpdatePassword;
import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.repository.AdminDetailsRepository;
import com.example.authenticationService.services.FetchInfoService;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

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
            System.out.println(password);
            adminDetails.setUsername(adminDetails.getEmail().substring(0,4).toLowerCase()+"veacy"+ (int) (Math.random() * (9999 - 1000 + 1)) + 1000);
            adminDetails.setPasswordHash(bCryptPasswordEncoder.encode(password));
            adminDetails.setIsActive(true);
            adminDetails.setIsDeleted(false);
            return adminDetailsRepository.save(adminDetails);
    }


    @Override
    public List<AdminDetails> getAllInfo() {
        return adminDetailsRepository.findAll();
    }

    @Override
    public Integer getId(String email) {
        Optional<Integer> optionalAdminId = adminDetailsRepository.fetchId(email);
        return optionalAdminId.orElse(null);
    }

    @Override
    public AdminDetails getInfoById(Integer id) {
        Optional<AdminDetails>  optionalAdminDetails = adminDetailsRepository.findById(id);
        if(optionalAdminDetails.isPresent())
        {
            optionalAdminDetails.get().setPasswordHash("");
            return optionalAdminDetails.get();
        }
        return null;
    }

    @Override
    public AdminDetails getInfoByEmail(String email) {
        Optional<AdminDetails> optionalAdminDetails = adminDetailsRepository.findByEmail(email);
        return optionalAdminDetails.orElse(null);
    }


    @Override
    public String forgotPasswordReset(UpdatePassword updatePassword) {
        Optional<AdminDetails> optionalAdminDetails = adminDetailsRepository.findByEmail(updatePassword.getEmail());
        if(optionalAdminDetails.isPresent())
        {
            optionalAdminDetails.get().setPasswordHash(updatePassword.getPassword());
            adminDetailsRepository.save(optionalAdminDetails.get());
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
            return adminDetailsRepository.save(optionalAdminDetails.get());
        }
        return null;
    }

    @Override
    public Boolean validateByEmail(String email) {
        Optional<AdminDetails> optionalAdminDetails = adminDetailsRepository.findByEmail(email);
        return optionalAdminDetails.isPresent();
    }


}
