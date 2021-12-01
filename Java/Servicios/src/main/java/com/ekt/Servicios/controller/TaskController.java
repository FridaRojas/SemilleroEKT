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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

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

    @GetMapping("/obtenerTareas")   //Reportes
    public ResponseEntity<?> readAll(){
        Iterable<Task> tareas = tareaService.findAll();
        String mensaje;
        int nDocumentos = ((Collection<Task>) tareas).size();
        if(nDocumentos == 0) {
            mensaje = "No se encontraron tareas";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
        }
        mensaje = "Tareas encontradas";
        return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()),mensaje,tareas));
    }

    @GetMapping(value="/obtenerTareaPorId/{id}")    //2. Tareas
    public ResponseEntity<?> read(@PathVariable String id){
        Optional<Task> oTarea = tareaService.findById(id);
        String mensaje;
        //Si el iD no existe

        //Si no hay ninguna tarea
        if(!oTarea.isPresent()){
            Map<String, Object> body = new LinkedHashMap<>();//LinkedHashMap conserva el orden de inserción
            Map<String, String> data = new LinkedHashMap<>();
            body.put("statusCode", String.valueOf(HttpStatus.NOT_FOUND.value()));
            data.put("Tarea","No existe la tarea con Id: "+ id);
            body.put("Data",data);
            mensaje = "No existe tarea con id: "+id;
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje));
        }
        mensaje = "Se encontró una tarea";
        return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()),mensaje,oTarea));
    }

    @PutMapping("/actualizarTarea/{id_tarea}")  //3. Tareas
    public ResponseEntity<?> updateById(@PathVariable String id_tarea,@RequestBody Task tarea){
        Optional<Task> oTarea = tareaService.findById(id_tarea);
        String mensaje = "";
        if(oTarea.isPresent()) {
            ArrayList<String> validarActualizacion = tareaService.validarTareasActualizar(tarea);
            if(validarActualizacion.size() == 0) {
                LocalDateTime date = LocalDateTime.now();
                TaskLog bitacora = new TaskLog();
                String id_tareas = id_tarea;
                bitacora.setId_emisor(tarea.getId_emisor());
                bitacora.setNombre_emisor(tarea.getNombre_emisor());
                bitacora.setAccion("Actualizo la tarea");
                bitacora.setId_tarea(id_tarea);
                bitacora.setFecha_actualizacion(date);

                bitacoraRepository.save(bitacora);
                tareaService.updateById(id_tarea, tarea);
                Optional<Task> newTarea = tareaRepository.findById(id_tarea);
                mensaje = "Tarea " + id_tarea + " actualizada correctamente";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje, newTarea));
            }else {mensaje = "Error de validacion de tarea a actualizar";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, validarActualizacion));
            }
        }else {
            //return ResponseEntity.ok(new Respuesta(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),e.getMessage()));
            mensaje = "El id no fue encontrado";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
        }
    }

    @DeleteMapping("/cancelarTarea/{id}") //Tareas
    public ResponseEntity<?> deleteById(@PathVariable String id){
        String mensaje;
        Optional<Task> oTarea = tareaService.findById(id);
        if (oTarea.isPresent()) {
            tareaService.deleteById(id);
            mensaje = "Tarea con id: "+id+" cancelada";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()),mensaje));
        }else {
            mensaje = "Tarea con Id: "+id +" no encontrado";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje));
        }
    }

    @GetMapping("/filtrarTareasPorPrioridad/{prioridad}")//Reportes
    public ResponseEntity<?> getTareasByPrioridad(@PathVariable String prioridad) {
        Iterable<Task> tareas = tareaRepository.findByPriority(prioridad);
        int nDocumentos = ((Collection<Task>) tareas).size();
        if(nDocumentos == 0){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tareas.iterator());
    }

    @GetMapping("/filtrarTareasPorGrupo/{id_grupo}")// Reportes
    public ResponseEntity<?> getTareasByGrupo(@PathVariable String id_grupo) {
        Iterable<Task> tareas = tareaRepository.findByIdGrupo(id_grupo);
        String mensaje;
        //Si no existe el id_grupo
        int nDocumentos = ((Collection<Task>) tareas).size();
        if(nDocumentos == 0){
            //Map<String, Object> body = new LinkedHashMap<>();//LinkedHashMap conserva el orden de inserción
            Map<String, String> data = new LinkedHashMap<>();
            //body.put("statusCode", String.valueOf(HttpStatus.NOT_FOUND.value()));
            data.put("id_grupo","No hay tareas para el Grupo Id: "+ id_grupo);
            //body.put("Data",data);
            mensaje = "No hay tareas para el Grupo Id: "+ id_grupo;
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje, data));
        }
        mensaje = "Petición con éxito";
        return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()),mensaje,tareas.iterator()));
    }

    @GetMapping("/filtrarPrioridadTareasPorUsuario/{prioridad}&{idReceptor}") //Tareas
    public ResponseEntity<?> getUsuarioTareasByPrioridad(@PathVariable String prioridad,@PathVariable String idReceptor) {
        Iterable<Task> tareas = tareaRepository.findIdReceptorTareaByPrioridad(idReceptor, prioridad);
        int nDocumentos = ((Collection<Task>) tareas).size();
        String mensaje;
        if(nDocumentos == 0){
            mensaje = "No se encontró la tarea";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje));
        }
        mensaje = "Se encontraron tareas";
        return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()),mensaje,tareas));
    }
    @GetMapping("/obtenerTareasQueAsignoPorId/{id}")    //REPORTES
    public ResponseEntity<?> getAllTareasOutByUserId(@PathVariable String id){
        Iterable<Task> tareas =tareaRepository.getAllOutByUserId(id);
        int nDocumentos = ((Collection<Task>) tareas).size();
        String mensaje;
        if(nDocumentos == 0){
            mensaje="No se encontraron tareas";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje));
        }
        mensaje = "Se encontraron tareas";
        return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()),mensaje,tareas));
    }

    @GetMapping("/obtenerTareasQueLeAsignaronPorId/{id}") //REPORTES
    public ResponseEntity<?> getAllTareasInByUserId(@PathVariable String id){
        System.out.println(id);
        //Validar Id en la BD
        //Validar tareas !=0
        Iterable<Task> tareas = tareaRepository.getAllInByUserId(id);
        String mensaje;
        int nDocumentos = ((Collection<Task>) tareas).size();
        if(nDocumentos == 0){
            mensaje = "No se encontraron tareas";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje));
        }
        mensaje = "Se encontraron tareas";
        return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()),mensaje,tareas));
    }

    @GetMapping("/obtenerTareasPorGrupoYEmisor/{id_grupo}&{id_emisor}")    //Reportes
    public ResponseEntity<?> getAllTareasByGrupoAndIdEmisor(@PathVariable String id_grupo,@PathVariable String id_emisor){
        Iterable<Task> tareas =tareaRepository.getAllByGroupAndIdEmisor(id_grupo,id_emisor);
        String mensaje;
        int nDocumentos = ((Collection<Task>) tareas).size();
        if(nDocumentos == 0){
            mensaje = "No se encontraron tareas";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje));
        }
        mensaje = "Se encontraron tareas";
        return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()),mensaje,tareas));
    }

    @PutMapping("/actulizarEstatus/{id_tarea}&{estatus}")   // Tareas
    public ResponseEntity<?> actualizarEstatus(@PathVariable String id_tarea, @PathVariable String estatus) {
        //Validar Id tarea
        //Validar estatus
        String mensaje = "";
        Optional<Task> oTarea = tareaService.findById(id_tarea);
        if (oTarea.isPresent()) {
            tareaService.actualizarEstatus(id_tarea, estatus);
            mensaje = "Estatus actualizado correctamente";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje));
        } else {
            mensaje = "Id no encontrado";
            //return ResponseEntity.ok(new Respuesta(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),e.getMessage()));
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
        }
    }
}
