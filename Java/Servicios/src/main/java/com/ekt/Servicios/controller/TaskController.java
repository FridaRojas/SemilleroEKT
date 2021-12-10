package com.ekt.Servicios.controller;

import com.ekt.Servicios.entity.ResponseTask;
import com.ekt.Servicios.entity.Task;
import com.ekt.Servicios.entity.TaskLog;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.service.TaskServiceImpl;
import com.ekt.Servicios.service.UserServiceImpl;
import com.ekt.Servicios.service.TaskLogServiceImpl;
import com.mongodb.MongoException;
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
    private TaskLogServiceImpl taskLogServiceImpl;

    @Autowired
    private UserServiceImpl usuarioService;

    @PostMapping("/agregarTarea")   //1. Tareas
    public ResponseEntity<?> create(@RequestHeader("tokenAuth") String token_sesion,@RequestBody Task tarea){
        try {
            String mensaje;

            //Validar sesión
            ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion,tarea.getId_emisor());
            if(!erroresSesion.isEmpty()) {
                mensaje = "Error al procesar solicitud";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
            }

            // Valida que el receptor exista
            Optional<User> usuarioValido = usuarioService.findById(tarea.getId_receptor());
            if (!usuarioValido.isPresent()) {
                mensaje = "Usuario receptor con id: " + tarea.getId_receptor() + " invalido";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
            }
            String token = usuarioValido.get().getToken();
            if (token != null || token != "") {
                tareaService.notificacion(token, tarea.getTitulo());
                System.out.println("Se envio notificacion Token:" + token);
            }

            LocalDateTime date = LocalDateTime.now();
            System.out.println(date.toString());
            String asunto = "";
            System.out.println("Entramos en agregar tarea");
            ArrayList<String> validarTareas = tareaService.validarTareasCrear(tarea);
            if (validarTareas.size() == 0) {
                TaskLog bitacora = new TaskLog();
                bitacora.setId_emisor(tarea.getId_emisor());
                bitacora.setNombre_emisor(tarea.getNombre_emisor());
                bitacora.setAccion("Creo una tarea");

                LocalDateTime ldt = date
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                Date newLdt = Date.from(ldt.atZone(ZoneId.of("UTC")).toInstant());

                tarea.setFecha_BD(newLdt);
                tareaService.save(tarea);
                bitacora.setFecha_actualizacion(newLdt);
                bitacora.setEstatus(tarea.getEstatus());
                taskLogServiceImpl.save(bitacora);
                tareaService.notificacion(token, asunto);

                mensaje = "Tarea creada correctamente";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje, tarea));
            } else {
                mensaje = "Error de validacion de tarea";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, validarTareas));
            }
        }catch (MongoException m){
            String mensaje;
            mensaje = "No se puede conectar con la base de datos";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),mensaje,m.getStackTrace()));
        }
        catch (Exception e){
            String mensaje;
            mensaje = "Ocurrió un Error: No se pudo procesar tu solicitud";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje,e.getStackTrace()));
        }
    }

    @GetMapping("/obtenerTareas/{id_usuario}")   //10. Reportes
    public ResponseEntity<?> readAll(@RequestHeader("tokenAuth") String token_sesion,@PathVariable String id_usuario){
        try {
            String mensaje;
            //Validar sesión
            ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion,id_usuario);
            if(!erroresSesion.isEmpty()) {
                mensaje = "Error al procesar solicitud";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
            }
            //Traer todas las tareas
            Iterable<Task> tareas = tareaService.findAll();
            int nDocumentos = ((Collection<Task>) tareas).size();
            if (nDocumentos == 0) {
                mensaje = "No se encontraron tareas";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
            }
            mensaje = "Tareas encontradas";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje, tareas));
        }catch (MongoException m){
            String mensaje;
            mensaje = "No se puede conectar con la base de datos";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),mensaje,m.getStackTrace()));
        }catch (Exception e){
            String mensaje;
            mensaje = "Ocurrio un Error: No se pudo procesar tu solicitud";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje,e.getStackTrace()));
        }
    }

    @GetMapping(value="/obtenerTareaPorId/{id_tarea}/{id_usuario}")    //2. Tareas
    public ResponseEntity<?> read(@RequestHeader("tokenAuth") String token_sesion, @PathVariable String id_tarea,@PathVariable String id_usuario){
        try {
            Optional<Task> oTarea = tareaService.findById(id_tarea);
            String mensaje;

            //Validar sesión
            ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion,id_usuario);
            if(!erroresSesion.isEmpty()) {
                mensaje = "Error al procesar solicitud";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
            }

            //Si no hay ninguna tarea
            if (!oTarea.isPresent()) {
                Map<String, Object> body = new LinkedHashMap<>();//LinkedHashMap conserva el orden de inserción
                Map<String, String> data = new LinkedHashMap<>();
                body.put("statusCode", String.valueOf(HttpStatus.NOT_FOUND.value()));
                data.put("Tarea", "No existe la tarea con Id: " + id_tarea);
                body.put("Data", data);
                mensaje = "No existe tarea con id: " + id_tarea;
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
            }
            mensaje = "Se encontró una tarea";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje, oTarea));
        }catch (MongoException m){
            String mensaje;
            mensaje = "No se puede conectar con la base de datos";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),mensaje,m.getStackTrace()));
        }catch (Exception e){
            String mensaje;
            mensaje = "Ocurrió un Error: No se pudo procesar tu solicitud";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje,e.getStackTrace()));
        }
    }

    @PutMapping("/actualizarTarea/{id_tarea}/{id_usuario}")  //3. Tareas
    public ResponseEntity<?> updateById(@RequestHeader("tokenAuth") String token_sesion, @PathVariable String id_tarea,@RequestBody Task tarea, @PathVariable String id_usuario){
        try {
            String mensaje = "";
            //Validar sesión
            ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion,id_usuario);
            if(!erroresSesion.isEmpty()) {
                mensaje = "Error al procesar solicitud";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
            }
            //Buscar tarea
            Optional<Task> oTarea = tareaService.findById(id_tarea);
            if (oTarea.isPresent()) {
                ArrayList<String> validarActualizacion = tareaService.validarTareasActualizar(tarea);
                if (validarActualizacion.size() == 0) {
                    LocalDateTime date = LocalDateTime.now();
                    TaskLog bitacora = new TaskLog();
                    String id_tareas = id_tarea;
                    bitacora.setId_emisor(tarea.getId_emisor());
                    bitacora.setNombre_emisor(tarea.getNombre_emisor());
                    bitacora.setAccion("Actualizo la tarea");
                    bitacora.setId_tarea(id_tarea);
                    Date newLdt = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
                    bitacora.setFecha_actualizacion(newLdt);

                    taskLogServiceImpl.save(bitacora);
                    tareaService.updateById(id_tarea, tarea);
                    Optional<Task> newTarea = tareaService.findById(id_tarea);
                    mensaje = "Tarea " + id_tarea + " actualizada correctamente";
                    return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje, newTarea));
                } else {
                    mensaje = "Error de validacion de tarea a actualizar";
                    return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, validarActualizacion));
                }
            } else {
                //return ResponseEntity.ok(new Respuesta(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),e.getMessage()));
                mensaje = "El id no fue encontrado";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
            }
        }catch (MongoException m){
            String mensaje;
            mensaje = "No se puede conectar con la base de datos";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),mensaje,m.getStackTrace()));
        }catch (Exception e){
            String mensaje;
            mensaje = "Ocurrio un error: No se pudo procesar tu solicitud";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje,e.getStackTrace()));
        }
    }

    @DeleteMapping("/cancelarTarea/{id_tarea}/{id_usuario}") //4. Tareas
    public ResponseEntity<?> deleteById(@RequestHeader("tokenAuth") String token_sesion, @PathVariable String id_tarea, @PathVariable String id_usuario){
        try {
            String mensaje ="";
            //Validar sesión
            ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion,id_usuario);
            if(!erroresSesion.isEmpty()) {
                mensaje = "Error al procesar solicitud";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
            }
            Optional<Task> oTarea = tareaService.findById(id_tarea);
            if (oTarea.isPresent()) {
                tareaService.deleteById(id_tarea);
                mensaje = "Tarea con id: " + id_tarea + " cancelada";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje));
            } else {
                mensaje = "Tarea con Id: " + id_tarea + " no encontrado";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
            }
        }catch (MongoException m){
            String mensaje;
            mensaje = "No se puede conectar con la base de datos";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),mensaje,m.getStackTrace()));
        }catch (Exception e){
            String mensaje;
            mensaje = "Ocurrio un Error: No se pudo procesar tu solicitud";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje,e.getStackTrace()));
        }
    }

    @GetMapping("/filtrarTareasPorPrioridad/{prioridad}/{id_usuario}")//Reportes
    public ResponseEntity<?> getTareasByPrioridad(@RequestHeader("tokenAuth") String token_sesion,@PathVariable String prioridad,@PathVariable String id_usuario) {
        try {
            String mensaje;
            //Validar sesión
            ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion,id_usuario);
            if(!erroresSesion.isEmpty()) {
                mensaje = "Error al procesar solicitud";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
            }
            Iterable<Task> tareas = tareaService.findByPriority(prioridad);
            int nDocumentos = ((Collection<Task>) tareas).size();
            if (nDocumentos == 0) {
                return ResponseEntity.notFound().build();
            }

            mensaje = "Se obtuvieron las tareas";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()),mensaje,tareas));
        }catch (MongoException m){
            String mensaje;
            mensaje = "No se puede conectar con la base de datos";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),mensaje,m.getStackTrace()));
        }catch (Exception e){
            String mensaje;
            mensaje = "Ocurrio un Error: No se pudo procesar tu solicitud";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje,e.getStackTrace()));
        }
    }

    @GetMapping("/filtrarTareasPorGrupo/{id_grupo}/{id_usuario}")// Reportes
    public ResponseEntity<?> getTareasByGrupo(@RequestHeader("tokenAuth") String token_sesion,@PathVariable String id_grupo,@PathVariable String id_usuario) {
        try {
            String mensaje;
            //Validar sesión
            ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion,id_usuario);
            if(!erroresSesion.isEmpty()) {
                mensaje = "Error al procesar solicitud";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
            }
            Iterable<Task> tareas = tareaService.findByIdGrupo(id_grupo);
            //Si no existe el id_grupo
            int nDocumentos = ((Collection<Task>) tareas).size();
            if (nDocumentos == 0) {
                //Map<String, Object> body = new LinkedHashMap<>();//LinkedHashMap conserva el orden de inserción
                Map<String, String> data = new LinkedHashMap<>();
                //body.put("statusCode", String.valueOf(HttpStatus.NOT_FOUND.value()));
                data.put("id_grupo", "No hay tareas para el Grupo Id: " + id_grupo);
                //body.put("Data",data);
                mensaje = "No hay tareas para el Grupo Id: " + id_grupo;
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje, data));
            }
            mensaje = "Petición con éxito";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje, tareas.iterator()));
        }catch (MongoException m){
            String mensaje;
            mensaje = "No se puede conectar con la base de datos";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),mensaje,m.getStackTrace()));
        }catch (Exception e){
            String mensaje;
            mensaje = "Ocurrio un Error: No se pudo procesar tu solicitud";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje,e.getStackTrace()));
        }
    }

    @GetMapping("/filtrarPrioridadTareasPorUsuario/{prioridad}/{id_usuario}") //5. Tareas
    public ResponseEntity<?> getUsuarioTareasByPrioridad(@RequestHeader("tokenAuth") String token_sesion,@PathVariable String prioridad,@PathVariable String id_usuario) {
        try {
            String mensaje;
            ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion, id_usuario);
            if(!erroresSesion.isEmpty()) {
                mensaje = "Error al procesar solicitud";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
            }
            // Valida que el receptor exista
            Optional<User> usuarioValido = usuarioService.findById((id_usuario));
            if (!usuarioValido.isPresent()) {
                mensaje = "Usuario receptor con id: " + id_usuario + " invalido";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
            }
            Iterable<Task> tareas = tareaService.findIdReceptorTareaByPrioridad(id_usuario, prioridad);
            int nDocumentos = ((Collection<Task>) tareas).size();
            if (nDocumentos == 0) {
                mensaje = "No se encontró la tarea";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
            }
            mensaje = "Se encontraron tareas";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje, tareas));
        }catch (MongoException m){
            String mensaje;
            mensaje = "No se puede conectar con la base de datos";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),mensaje,m.getStackTrace()));
        }catch (Exception e){
            String mensaje;
            mensaje = "Ocurrio un Error: No se pudo procesar tu solicitud";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje,e.getStackTrace()));
        }
    }
    @GetMapping("/obtenerTareasQueAsignoPorId/{id_usuario}")    // 13
    public ResponseEntity<?> getAllTareasOutByUserId(@RequestHeader("tokenAuth") String token_sesion,@PathVariable String id_usuario){
        try {
            String mensaje;
            ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion,id_usuario);
            if(!erroresSesion.isEmpty()) {
                mensaje = "Error al procesar solicitud";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
            }
            Iterable<Task> tareas = tareaService.getAllOutByUserId(id_usuario);
            int nDocumentos = ((Collection<Task>) tareas).size();
            if (nDocumentos == 0) {
                mensaje = "No se encontraron tareas";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
            }
            mensaje = "Se encontraron tareas";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje, tareas));
        }catch (MongoException m){
            String mensaje;
            mensaje = "No se puede conectar con la base de datos";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),mensaje,m.getStackTrace()));
        }catch (Exception e){
            String mensaje;
            mensaje = "Ocurrio un Error: No se pudo procesar tu solicitud";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje,e.getStackTrace()));
        }
    }

    @GetMapping("/obtenerTareasQueLeAsignaronPorId/{id_usuario}") //14
    public ResponseEntity<?> getAllTareasInByUserId(@RequestHeader("tokenAuth") String token_sesion,@PathVariable String id_usuario){
        try {
            String mensaje;
            ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion,id_usuario);
            if(!erroresSesion.isEmpty()) {
                mensaje = "Error al procesar solicitud";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
            }
            //Validar Id en la BD
            //Validar tareas !=0
            Iterable<Task> tareas = tareaService.getAllInByUserId(id_usuario);
            int nDocumentos = ((Collection<Task>) tareas).size();
            if (nDocumentos == 0) {
                mensaje = "No se encontraron tareas";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
            }
            mensaje = "Se encontraron tareas";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje, tareas));
        }catch (MongoException m){
            String mensaje;
            mensaje = "No se puede conectar con la base de datos";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),mensaje,m.getStackTrace()));
        }catch (Exception e){
            String mensaje;
            mensaje = "Ocurrio un Error: No se pudo procesar tu solicitud";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje,e.getStackTrace()));
        }
    }
    @GetMapping("/obtenerTareasQueLeAsignaronPorIdReportes/{id_usuario}/{id_receptor}") //REPORTES
    public ResponseEntity<?> obtenerTareasAsignadas(@RequestHeader("tokenAuth") String token_sesion,@PathVariable String id_usuario, @PathVariable String id_receptor){
        try {
            String mensaje;
            ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion,id_usuario);
            if(!erroresSesion.isEmpty()) {
                mensaje = "Error al procesar solicitud";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
            }
            //Validar Id en la BD
            //Validar tareas !=0
            Iterable<Task> tareas = tareaService.getAllInByUserId(id_receptor);
            int nDocumentos = ((Collection<Task>) tareas).size();
            if (nDocumentos == 0) {
                mensaje = "No se encontraron tareas";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
            }
            mensaje = "Se encontraron tareas";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje, tareas));
        }catch (MongoException m){
            String mensaje;
            mensaje = "No se puede conectar con la base de datos";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),mensaje,m.getStackTrace()));
        }catch (Exception e){
            String mensaje;
            mensaje = "Ocurrio un Error: No se pudo procesar tu solicitud";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje,e.getStackTrace()));
        }
    }

    @GetMapping("/obtenerTareasPorGrupoYEmisor/{id_grupo}/{id_usuario}")    //Reportes
    public ResponseEntity<?> getAllTareasByGrupoAndIdEmisor(@RequestHeader("tokenAuth") String token_sesion,@PathVariable String id_grupo,@PathVariable String id_usuario){
        try {
            String mensaje;
            ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion, id_usuario);
            if(!erroresSesion.isEmpty()) {
                mensaje = "Error al procesar solicitud";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
            }
            Iterable<Task> tareas = tareaService.getAllByGroupAndIdEmisor(id_grupo, id_usuario);
            int nDocumentos = ((Collection<Task>) tareas).size();
            if (nDocumentos == 0) {
                mensaje = "No se encontraron tareas";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
            }
            mensaje = "Se encontraron tareas";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje, tareas));
        }catch (MongoException m){
            String mensaje;
            mensaje = "No se puede conectar con la base de datos";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),mensaje,m.getStackTrace()));
        }catch (Exception e){
            String mensaje;
            mensaje = "Ocurrio un Error: No se pudo procesar tu solicitud";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje,e.getStackTrace()));
        }
    }

    @PutMapping("/actulizarEstatus/{id_tarea}/{estatus}")   //6. Tareas
    public ResponseEntity<?> actualizarEstatus(@RequestHeader("tokenAuth") String token_sesion,@PathVariable String id_tarea, @PathVariable String estatus) {
        try {
            String mensaje = "";
            //Validar Id tarea
            //Validar estatus
            Optional<Task> oTarea = tareaService.findById(id_tarea);
            if (oTarea.isPresent()) {
                tareaService.actualizarEstatus(id_tarea, estatus);
                mensaje = "Estatus actualizado correctamente";
                Optional<User> usuarioValido = usuarioService.findById(oTarea.get().getId_emisor());
                if (usuarioValido.isPresent()) {//Si el usuario es valido
                    ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion,oTarea.get().getId_emisor());
                    if(!erroresSesion.isEmpty()) {
                        mensaje = "Error al procesar solicitud";
                        return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
                    }
                    String token = usuarioValido.get().getToken();
                    if (token != null || token != "") {//Si existe el token o no es vacio
                        if (estatus.equals("revision")) {
                            tareaService.notificacion(token, "Entró en " + estatus + " Tarea: " + oTarea.get().getTitulo());
                        }
                    }
                }
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje));
            } else {
                mensaje = "Id no encontrado";
                //return ResponseEntity.ok(new Respuesta(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),e.getMessage()));
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
            }
        }catch (MongoException m){
            String mensaje;
            mensaje = "No se puede conectar con la base de datos";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),mensaje,m.getStackTrace()));
        }catch (Exception e){
            String mensaje;
            mensaje = "Ocurrio un Error: No se pudo procesar tu solicitud";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje,e.getStackTrace()));
        }
    }

    @PutMapping("/actualizarLeidoPorIdTarea/{id_tarea}")    //7. Tarea
    public ResponseEntity<?> actualizaLeidoPorIdTarea(@RequestHeader("tokenAuth") String token_sesion,@PathVariable String id_tarea){
        try {
            String mensaje;
            Optional<Task> oTarea = tareaService.findById(id_tarea);
            if (oTarea.isPresent()) {
                ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion,oTarea.get().getId_emisor());
                if(!erroresSesion.isEmpty()) {
                    mensaje = "Error al procesar solicitud";
                    return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
                }
                tareaService.actualizaLeido(id_tarea, true);
                mensaje = "Actualizacion Leído de tarea con id " + id_tarea;
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje));
            } else {
                mensaje = "Id no encontrado";
                //return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
            }
        }catch (MongoException m){
            String mensaje;
            mensaje = "No se puede conectar con la base de datos";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),mensaje,m.getStackTrace()));
        }catch (Exception e){
            String mensaje;
            mensaje = "Ocurrio un Error: No se pudo procesar tu solicitud";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje,e.getStackTrace()));
        }
    }

    @GetMapping("/obtenerTareasQueLeAsignaronPorIdYEstatus/{id_usuario}/{estatus}") //8. Tareas
    public ResponseEntity<?> obtenerTareasQueLeAsignaronPorIdYEstatus(@RequestHeader("tokenAuth") String token_sesion,@PathVariable String id_usuario,@PathVariable String estatus){

        String mensaje;
        ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion,id_usuario);
        if(!erroresSesion.isEmpty()) {
            mensaje = "Error al procesar solicitud";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
        }
        Iterable<Task> tareas = tareaService.getAllByIdReceptorAndStatus(id_usuario,estatus);
        int nDocumentos = ((Collection<Task>) tareas).size();
        if(nDocumentos==0){
            mensaje = "No hay tareas que le asignaron al id: "+id_usuario+" por estatus: "+estatus;
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
        }
        mensaje = "Tareas que le asignaron al id: "+id_usuario+" por estatus: "+estatus+ " obtenidas correctamente";
        return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje, tareas));
    }

    @GetMapping("/obtenerTareasQueAsignoPorIdYEstatus/{id_usuario}/{estatus}")  //9. Tareas
    public ResponseEntity<?> obtenerTareasQueAsignoPorIdYEstatus(@RequestHeader("tokenAuth") String token_sesion,@PathVariable String id_usuario,@PathVariable String estatus){
        try {
            String mensaje;
            ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion, id_usuario);
            if(!erroresSesion.isEmpty()) {
                mensaje = "Error al procesar solicitud";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
            }
            Iterable<Task> tareas = tareaService.getAllByIdEmisorAndStatus(id_usuario, estatus);
            int nDocumentos = ((Collection<Task>) tareas).size();
            if (nDocumentos == 0) {
                mensaje = "No hay tareas que asignó el id: " + id_usuario + " por estatus: " + estatus;
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()), mensaje));
            }
            mensaje = "Tareas que asignó el id: " + id_usuario + " por estatus: " + estatus + " obtenidas correctamente";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()), mensaje, tareas));
        }catch (MongoException m){
            String mensaje;
            mensaje = "No se puede conectar con la base de datos";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),mensaje,m.getStackTrace()));
        }catch (Exception e){
            String mensaje;
            mensaje = "Ocurrio un Error: No se pudo procesar tu solicitud";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje,e.getStackTrace()));
        }
    }

    @PutMapping("/actualizarTareaFechaRealIni/{id_tarea}/{id_usuario}") //11
    public ResponseEntity<?> actualizarFechaRealTareaIni(@RequestHeader("tokenAuth") String token_sesion,@PathVariable String id_tarea,@RequestBody Task tarea, @PathVariable String id_usuario) {
        String mensaje = "";
        try {
            ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion,id_usuario);
            if(!erroresSesion.isEmpty()) {
                mensaje = "Error al procesar solicitud";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
            }
            tareaService.updateRealDateStart(id_tarea, tarea);
            mensaje = "Fecha de inicio guardada correctamente";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()),mensaje));
        }catch (MongoException m){
            mensaje = "No se puede conectar con la base de datos";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),mensaje,m.getStackTrace()));
        }catch (Exception e){
            mensaje = "Ocurrio un Error: No se pudo procesar tu solicitud";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje,e.getStackTrace()));
        }
    }

    @PutMapping("/actualizarTareaFechaRealFin/{id_tarea}/{id_usuario}") //12
    public ResponseEntity<?> actualizarFechaRealTareaFin(@RequestHeader("tokenAuth") String token_sesion,@PathVariable String id_tarea,@RequestBody Task tarea, @PathVariable String id_usuario) {
        String mensaje = "";
        try {
            ArrayList<String> erroresSesion = tareaService.validarSesion(token_sesion, id_usuario);
            if(!erroresSesion.isEmpty()) {
                mensaje = "Error al procesar solicitud";
                return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), mensaje, erroresSesion));
            }
            tareaService.updateRealDateFinish(id_tarea, tarea);
            mensaje = "Fecha de termino guardada correctamente";
            return ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.OK.value()),mensaje));
        }catch (MongoException m){
            mensaje = "No se puede conectar con la base de datos";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),mensaje,m.getStackTrace()));
        }catch (Exception e){
            mensaje = "Ocurrio un Error: No se pudo procesar tu solicitud";
            return  ResponseEntity.ok(new ResponseTask(String.valueOf(HttpStatus.NOT_FOUND.value()),mensaje,e.getStackTrace()));
        }
    }

}
