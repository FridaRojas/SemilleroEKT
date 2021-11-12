package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface GroupService {
    public Iterable<Group> findAll();

    public Page<Group> findAll(Pageable pageable);

    public Optional<Group> findById(String id);

    public Group save(Group group);

    public void deleteById(int id);
}
