package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.Task;

import java.util.ArrayList;
import java.util.Optional;

public interface TaskService {
    public Task save(Task tarea);
    public Optional<Task> findById(String id);
    public Iterable<Task> findAll();
    public void  deleteById(String id );
    public void updateById(String id, Task tarea);
    public void  actualizarEstatus(String id_tarea, String estatus );
    public void actualizaLeido(String id_tarea,Boolean leido);
    public void updateRealDateStart(String id_tarea, Task tarea);
    public void updateRealDateFinish(String id_tarea, Task tarea);
    public ArrayList<String> validarTareasCrear(Task tarea);
    public void notificacion(String token, String asunto);
    public ArrayList<String> validarTareasActualizar(Task tarea);
    public ArrayList<String> validarSesion(String token_sesion, String id_usuario);
    public Iterable<Task> findByPriority(String prioridad);
    public Iterable<Task> findByIdGrupo(String id_grupo);
    public Iterable<Task> findIdReceptorTareaByPrioridad(String id_usuario, String prioridad);
    public Iterable<Task> getAllOutByUserId(String id_usuario);
    public Iterable<Task> getAllInByUserId(String id_receptor);
    public Iterable<Task> getAllByGroupAndIdEmisor(String id_grupo, String id_usuario);
    public  Iterable<Task> getAllByIdReceptorAndStatus(String id_usuario, String estatus);
    public Iterable<Task> getAllByIdEmisorAndStatus(String id_usuario, String estatus);
}
