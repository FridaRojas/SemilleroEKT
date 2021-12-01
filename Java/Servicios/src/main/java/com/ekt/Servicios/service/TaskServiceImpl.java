package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.Task;
import com.ekt.Servicios.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService{
    @Autowired
    private TaskRepository tareaRepository;

    @Override
    public Task save(Task tarea) {
        return tareaRepository.save(tarea);
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<Task> findById(String id){ return tareaRepository.findById(id); }

    @Override
    public Iterable<Task> findAll() {
        return tareaRepository.findAll();
    }
}
