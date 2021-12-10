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
    Optional<Group> buscarUsuarioEnGrupo (String id, String user);

<<<<<<< HEAD
    @Query("{'id': ?0, 'usuarios.idSuperiorInmediato': '-1'}")
    Optional<Group> buscarBroadCastEnGrupo (String id);

=======
>>>>>>> 1af49fd3a12c50a4e22480c930b409d10b1f5f5c
    @Query("{nombre:?0}")
    Optional<Group> buscarPorNombre (String nombre);

}
