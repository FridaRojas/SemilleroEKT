package com.ekt.Servicios.repository;

import com.ekt.Servicios.entity.Task;
import com.ekt.Servicios.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends MongoRepository<Task,String> {

    @Query(value = "{'prioridad': ?0}")
    Iterable<Task> findByPriority(String prioridad);

    @Query(value = "{'id_grupo': ?0}")
    Iterable<Task> findByIdGrupo(String id_grupo);

    @Query(value = "{'id_receptor': ?0,'prioridad': ?1 }")
    Iterable<Task> findIdReceptorTareaByPrioridad(String id_receptor, String prioridad);

    @Query(value = "{'id_emisor': ?0}",sort = "{fecha_ini: 1}")
    Iterable<Task> getAllOutByUserId(String user_id);

    @Query(value = "{'id_receptor':?0}")
    Iterable<Task> getAllInByUserId(String user_id);

    @Query(value = "{'id_grupo': ?0,'id_emisor': ?1 }")
    Iterable<Task> getAllByGroupAndIdEmisor(String id_grupo, String id_emisor);

    @Query(value = "{'id_receptor': ?0, 'estatus': ?1}",sort = "{fecha_ini: 1}")//Todas las que le fueron asignadas mediante un estatus
    Iterable<Task> getAllByIdReceptorAndStatus(String id_receptor, String estatus);

    @Query(value = "{'id_emisor': ?0, 'estatus': ?1}",sort = "{fecha_ini: 1}")
    Iterable<Task> getAllByIdEmisorAndStatus(String id_emisor, String estatus);

}
