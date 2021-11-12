package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.Group;
import com.ekt.Servicios.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    public Iterable<User> findAll();

    public Page<User> findAll(Pageable pageable);

    public Optional<User> findById(int id);

    public User save(User user);

    public void deleteById(int id);


}
