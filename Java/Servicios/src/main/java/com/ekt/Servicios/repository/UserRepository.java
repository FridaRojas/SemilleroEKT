package com.ekt.Servicios.repository;


import com.ekt.Servicios.entity.User;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {

    @Query("{ 'correo' : ?0 }")
    Optional<User> findUsersByCorreo(String name);

    @Query("{'correo': ?0,'password': ?1 }")
    Optional<User> findByCorreoPassoword (String correo,String passwoprd);

    @Query("{ 'idSuperiorInmediato' : ?0}")
    Iterable<User> findByBossId(String id);

}
