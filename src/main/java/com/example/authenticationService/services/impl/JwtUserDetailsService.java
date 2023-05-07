package com.example.authenticationService.services.impl;

import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.repository.AdminDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

import static com.example.authenticationService.Utils.Constants.*;
@Slf4j
@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    AdminDetailsRepository adminDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        AdminDetails adminDetails = new AdminDetails();
        Optional<AdminDetails> adminCred = adminDetailsRepository.findByUsername(username);
        if (adminCred.isPresent()) {
            if (adminCred.get().getUsername().equals(username)) {
                log.info("Valid User: "+username);
                return new User(username, adminCred.get().getPasswordHash(), new ArrayList<>());
            }
        }
        else if (DEFAULT_USER.equals(username)) {
            adminDetails.setEmail(DEFAULT_USER);
            adminDetails.setPasswordHash(bCryptPasswordEncoder.encode(DEFAULT_PASSWORD));
            Optional<AdminDetails> optionalAdminDetails = adminDetailsRepository.findByUsername(DEFAULT_USER);
            if(optionalAdminDetails.isEmpty())
            {
                adminDetailsRepository.save(adminDetails);
            }
            return new User(username, bCryptPasswordEncoder.encode(DEFAULT_PASSWORD),new ArrayList<>());
        }
        throw new UsernameNotFoundException(USER_NOT_FOUND + username);
    }
}
