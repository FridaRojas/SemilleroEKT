package com.ekt.Servicios.controller;

import com.ekt.Servicios.entity.ResponseTask;
import com.ekt.Servicios.entity.Task;
import com.ekt.Servicios.entity.TaskLog;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.repository.TaskLogRepository;
import com.ekt.Servicios.repository.TaskRepository;
import com.ekt.Servicios.repository.UserRepository;
import com.ekt.Servicios.service.TaskServiceImpl;
import com.ekt.Servicios.service.UserServiceImpl;
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

    @Autowired
    private UserServiceImpl usuarioService;

    @PostMapping("/agregarTarea")   //1. Tareas
    public ResponseEntity<?> create(@RequestBody Task tarea){
        String mensaje = "";
        // Valida que el receptor exista
        Optional<User> usuarioValido = usuarioService.findById(tarea.getId_receptor());
        if(!usuarioValido.isPresent()){
            mensaje = "Usuario receptor con id: "+tarea.getId_receptor()+" invalido";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje));
        }
        String token = usuarioValido.get().getToken();
        if(token != null || token!= "") {
            tareaService.notificacion(token, tarea.getTitulo());
            System.out.println("Se envio notificacion Token:"+ token);
        }

        LocalDateTime date =  LocalDateTime.now();
        System.out.println("Entramos en agregar tarea");
        ArrayList<String> validarTareas = tareaService.validarTareasCrear(tarea);
        if(validarTareas.size() == 0){
            TaskLog bitacora = new TaskLog();
            bitacora.setId_emisor(tarea.getId_emisor());
            bitacora.setNombre_emisor(tarea.getNombre_emisor());
            bitacora.setAccion("Creo una tarea");

            LocalDateTime ldt = date
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            Date newLdt =Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

            tarea.setFecha_BD(newLdt);
            tareaService.save(tarea);
            bitacora.setFecha_actualizacion(newLdt);
            bitacora.setEstatus(tarea.getEstatus());
            bitacoraRepository.save(bitacora);

            mensaje = "Tarea creada correctamente";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje, tarea));
        }else {mensaje = "Error de validacion de tarea";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, validarTareas));}
    }

    @GetMapping("/obtenerTareas")   //10. Reportes
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

        //Si no hay ninguna tarea o no existe
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
                Date newLdt =Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
                bitacora.setFecha_actualizacion(newLdt);

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

    @DeleteMapping("/cancelarTarea/{id}") //4. Tareas
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

    @GetMapping("/filtrarPrioridadTareasPorUsuario/{prioridad}&{idReceptor}") //5. Tareas
    public ResponseEntity<?> getUsuarioTareasByPrioridad(@PathVariable String prioridad,@PathVariable String idReceptor) {
        String mensaje;
        // Valida que el receptor exista
        Optional<User> usuarioValido = usuarioService.findById((idReceptor));
        if(!usuarioValido.isPresent()){
            mensaje = "Usuario receptor con id: "+idReceptor+" invalido";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje));
        }
        Iterable<Task> tareas = tareaRepository.findIdReceptorTareaByPrioridad(idReceptor, prioridad);
        int nDocumentos = ((Collection<Task>) tareas).size();
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

    @PutMapping("/actulizarEstatus/{id_tarea}&{estatus}")   //6. Tareas
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

    @PutMapping("/actualizarLeidoPorIdTarea/{id_tarea}")    //7. Tarea
    public ResponseEntity<?> actualizaLeidoPorIdTarea(@PathVariable String id_tarea){
        String mensaje;
        Optional<Task> oTarea = tareaService.findById(id_tarea);
        if (oTarea.isPresent()) {
            tareaService.actualizaLeido(id_tarea, true);
            mensaje = "Actualizacion Leído de tarea con id " + id_tarea;
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()),mensaje));
        }else{
            mensaje = "Id no encontrado";
            //return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje));
        }
    }

    @GetMapping("/obtenerTareasQueLeAsignaronPorIdYEstatus/{id_usuario}&{estatus}") //8. Tareas
    public ResponseEntity<?> obtenerTareasQueLeAsignaronPorIdYEstatus(@PathVariable String id_usuario,@PathVariable String estatus){
        Iterable<Task> tareas = tareaRepository.getAllByIdReceptorAndStatus(id_usuario,estatus);
        int nDocumentos = ((Collection<Task>) tareas).size();
        String mensaje;
        if(nDocumentos==0){
            mensaje = "No hay tareas que le asignaron al id: "+id_usuario+" por estatus: "+estatus;
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
        }
        mensaje = "Tareas que le asignaron al id: "+id_usuario+" por estatus: "+estatus+ " obtenidas correctamente";
        return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje, tareas));
    }

    @GetMapping("/obtenerTareasQueAsignoPorIdYEstatus/{id_emisor}&{estatus}")  //9. Tareas
    public ResponseEntity<?> obtenerTareasQueAsignoPorIdYEstatus(@PathVariable String id_emisor,@PathVariable String estatus){
        Iterable<Task> tareas = tareaRepository.getAllByIdEmisorAndStatus(id_emisor,estatus);
        String mensaje;
        int nDocumentos = ((Collection<Task>) tareas).size();
        if(nDocumentos ==0){
            mensaje = "No hay tareas que asignó el id: "+id_emisor+" por estatus: "+estatus;
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
        }
        mensaje = "Tareas que asignó el id: "+id_emisor+" por estatus: "+estatus+ " obtenidas correctamente";
        return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje, tareas));
    }

}
