package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.Task;

import java.util.Optional;

public interface TaskService {
    public Task save(Task tarea);
    public Optional<Task> findById(String id);
    public Iterable<Task> findAll();
    public void  deleteById(String id );
}
