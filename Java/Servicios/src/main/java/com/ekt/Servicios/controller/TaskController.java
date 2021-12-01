package com.ekt.Servicios.controller;

import com.ekt.Servicios.entity.ResponseTask;
import com.ekt.Servicios.entity.Task;
import com.ekt.Servicios.entity.TaskLog;
import com.ekt.Servicios.repository.TaskLogRepository;
import com.ekt.Servicios.repository.TaskRepository;
import com.ekt.Servicios.service.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/tareas")
public class TaskController {
    @Autowired
    private TaskServiceImpl tareaService;

    @Autowired
    private TaskRepository tareaRepository;

    @Autowired
    private TaskLogRepository bitacoraRepository;

    @PostMapping("/agregarTarea")   //1. Tareas
    public ResponseEntity<?> create(@RequestBody Task tarea){
        LocalDateTime date =  LocalDateTime.now();
        String mensaje = "";
        System.out.println("Entramos en agregar tarea");
        ArrayList<String> validarTareas = tareaService.validarTareasCrear(tarea);
        if(validarTareas.size() == 0){
            TaskLog bitacora = new TaskLog();
            bitacora.setId_emisor(tarea.getId_emisor());
            bitacora.setNombre_emisor(tarea.getNombre_emisor());
            bitacora.setAccion("Creo una tarea");
            bitacora.setFecha_actualizacion(date);
            bitacora.setEstatus(tarea.getEstatus());
            bitacoraRepository.save(bitacora);
            tarea.setFecha_BD(date);

            LocalDateTime ldt = date
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            tarea.setFecha_BD(ldt);
            tareaService.save(tarea);

            mensaje = "Tarea creada correctamente";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje, tarea));
        }else {mensaje = "Error de validacion de tarea";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, validarTareas));}
    }
}
