package com.ekt.Servicios.repository;


import com.ekt.Servicios.entity.Admin;
import com.ekt.Servicios.entity.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository  extends MongoRepository<Admin,String> {

    @Query("{'correo': ?0,'password': ?1 }")
    Optional<Admin> findByCorreoPassoword (String correo, String passwoprd);

}
