package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserService {
     Iterable<User> findAll();

     Page<User> findAll(Pageable pageable);

    Optional<User> findById(String id);

    Optional<User> findUsersByCorreo(String correo);
    Optional<User> userValidate(String id, String password);


     User save(User user);

     void deleteById(String id);


}
