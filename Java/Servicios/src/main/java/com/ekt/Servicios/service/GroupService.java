package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface GroupService {
    Iterable<Group> findAll();

    Page<Group> findAll(Pageable pageable);

    Optional<Group> findById(String id);

    Group save(Group group);

    void deleteById(String id);

    public Optional<Group> userInGroup(String id, String user);
}
