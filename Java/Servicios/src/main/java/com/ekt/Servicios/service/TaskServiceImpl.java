package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.Task;
import com.ekt.Servicios.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskServiceImpl implements TaskService{
    @Autowired
    private TaskRepository tareaRepository;

    @Override
    public Task save(Task tarea) {
        return tareaRepository.save(tarea);
    }
}
