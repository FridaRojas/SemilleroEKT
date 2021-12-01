package com.ekt.Servicios.repository;

import com.ekt.Servicios.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MongoRepository<Task,String> {

    @Query(value = "{'prioridad': ?0}")
    Iterable<Task> findByPriority(String prioridad);

    @Query(value = "{'id_grupo': ?0}")
    Iterable<Task> findByIdGrupo(String id_grupo);

    @Query(value = "{'id_receptor': ?0,'prioridad': ?1 }")
    Iterable<Task> findIdReceptorTareaByPrioridad(String id_receptor, String prioridad);

    @Query(value = "{'id_emisor': ?0}")
    Iterable<Task> getAllOutByUserId(String user_id);

    @Query(value = "{'id_receptor':?0}")
    Iterable<Task> getAllInByUserId(String user_id);
}
