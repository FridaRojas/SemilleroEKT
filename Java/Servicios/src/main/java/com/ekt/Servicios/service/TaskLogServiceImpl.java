package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.TaskLog;
import com.ekt.Servicios.repository.TaskLogRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskLogServiceImpl implements TaskLogService{
    @Autowired
    TaskLogRepository bitacoraRepository;

    @Override
    public TaskLog save(TaskLog bitacora) {

        return bitacoraRepository.save(bitacora);
    }
}
