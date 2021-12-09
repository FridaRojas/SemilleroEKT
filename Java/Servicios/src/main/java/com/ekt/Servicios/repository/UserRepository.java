package com.ekt.Servicios.repository;


import com.ekt.Servicios.entity.User;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

    @Query("{ 'idSuperiorInmediato' : ?0}")
    Optional<ArrayList<User>> findChilds(String idPadre);

<<<<<<< HEAD

    @Query(" {$and:[  {$or:[{'correo':?0},{'nombre':?0},{'rfc':?0},{'nombreRol':?0},{'curp':?0},{'numeroEmpleado':?0}]},{statusActivo:'true'} ]}")
    Optional<ArrayList<User>> busquedaUsuario(String parametro);

=======
>>>>>>> 1af49fd3a12c50a4e22480c930b409d10b1f5f5c
    @Query("{'_id' : ?0}")
    Optional<User> validarUsuario(String _id); 
    
    @Query("{'idGrupo' : ?0}")
    Iterable<User> findByGroupID(String idGrupo); 
    
    @Query("{'nombreRol' : ?0")
    Optional<User> findRol(String nombreRol);
<<<<<<< HEAD

    @Query("{'nombreRol' : ?0")
    Iterable<User> findRolL(String nombreRol);

=======
>>>>>>> 1af49fd3a12c50a4e22480c930b409d10b1f5f5c
}
