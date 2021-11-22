package com.ekt.Servicios.repository;

import com.ekt.Servicios.entity.Group;
import com.ekt.Servicios.entity.User;
import org.springframework.data.mongodb.repository.DeleteQuery;
import com.ekt.Servicios.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends MongoRepository<Group,String> {

    @Query("{'id': ?0, 'usuarios.id': ?1}")
    Optional<Group> findByIdUser (String id, String user);

}
