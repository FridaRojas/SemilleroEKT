package com.ekt.Servicios.repository;

import com.ekt.Servicios.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MongoRepository<Task,String> {

    @Query(value = "{'prioridad': ?0}")
    Iterable<Task> findByPriority(String prioridad);
}
