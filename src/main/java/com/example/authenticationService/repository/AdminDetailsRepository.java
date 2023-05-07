package com.example.authenticationService.repository;

import com.example.authenticationService.dtos.Authority;
import com.example.authenticationService.model.AdminDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdminDetailsRepository extends CrudRepository<AdminDetails,Integer> {
    Optional<AdminDetails> findByEmail(String userName);
    List<AdminDetails> findAll();
    Optional<AdminDetails> findById(Integer id);

    Optional<AdminDetails> findByUsername(String userName);

    @Query(value = "select id from admin_details where username = ?1",nativeQuery = true)
    Optional<Integer> fetchId(String username);


}
