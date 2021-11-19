package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Optional;

public interface UserService {

     Iterable<User> findAll();

     Optional<User> findById(String id);

     Optional<User> findUsersByCorreo(String correo);

     Optional<User> userValidate(String id, String password);
    Iterable<User> findUserByBossId(String id);
    User updateIdBoss(User userUpdate,String idBoss);

     User save(User user);

     void deleteById(String id);




}
