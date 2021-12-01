package com.ekt.Servicios.repository;

import com.ekt.Servicios.entity.TaskLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskLogRepository extends MongoRepository<TaskLog, String> {
}
