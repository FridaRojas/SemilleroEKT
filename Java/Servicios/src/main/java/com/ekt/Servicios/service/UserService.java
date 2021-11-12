package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
     Iterable<User> findAll();

     Page<User> findAll(Pageable pageable);

    Optional<User> findById(String id);

     User save(User user);

     void deleteById(String id);


}
