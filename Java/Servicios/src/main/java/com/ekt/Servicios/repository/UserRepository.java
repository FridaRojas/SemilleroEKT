package com.ekt.Servicios.repository;


import com.ekt.Servicios.entity.User;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {

    @Query("{'correo': ?0,'password': ?1 }")
    Optional<User> findByCorreoPassoword (String correo,String passwoprd);

    @Query("{ 'idSuperiorInmediato' : ?0}")
    Iterable<User> findByBossId(String id);

    @Query("{ 'correo' : ?0}")
    Optional<User> findByCorreo(String correo);

    @Query("{ 'rfc' : ?0}")
    Optional<User> findByRFC(String rfc);

    @Query("{ 'curp' : ?0}")
    Optional<User> findByCURP(String curp);

    @Query("{ 'numeroEmpleado' : ?0}")
    Optional<User> findByNumeroEmpleado(String numeroEmpleado);

}
