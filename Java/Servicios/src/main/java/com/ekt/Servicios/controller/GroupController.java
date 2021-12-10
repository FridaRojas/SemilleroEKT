package com.ekt.Servicios.controller;

import com.ekt.Servicios.entity.BodyAddUserGroup;
import com.ekt.Servicios.entity.Group;
import com.ekt.Servicios.entity.Response;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.service.GroupService;
import com.ekt.Servicios.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/grupo")
public class GroupController {
    @Autowired
    public GroupService groupService;

    @Autowired
    public UserService userService;

    @PostMapping("/crear")
    public ResponseEntity<?> save(@RequestBody Group group) {
        Group obj = groupService.guardar(group);
        System.out.println(group.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.guardar(group));
    }

    @PutMapping("/agregarUsuario")//*
    public ResponseEntity<?> addUserGroup(@RequestBody BodyAddUserGroup bodyAddUserGroup) {
        System.out.println("entra a agregar usuario al grupo");
        try {
            if (bodyAddUserGroup.getIdUsuario() == null || bodyAddUserGroup.getIdGrupo() == null || bodyAddUserGroup.getIdSuperior() == null || bodyAddUserGroup.getNombreRol() == null) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE, "Error  en las llaves", null));
            } else {
                    //verificar el caso de broadcast (caso añadir desde organigrama)
                    if(bodyAddUserGroup.getIdSuperior().equals("BROADCAST")){
                        System.out.println("entra a coso broadcast");
                        //verificar si un broadcast ya existe
                        if(!groupService.buscarBroadCastEnGrupo(bodyAddUserGroup.getIdGrupo())){
                            //agregar
                            Group group = groupService.guardarUsuario(bodyAddUserGroup.getIdUsuario(), bodyAddUserGroup.getIdGrupo(), "-1", "BROADCAST");
                            User user = userService.actualizaRol(userService.findById(bodyAddUserGroup.getIdUsuario()).get(), "-1", bodyAddUserGroup.getIdGrupo(), "BROADCAST");
                            if (group == null && user == null) {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST, "Error al realizar en la operacion,parametro no valido", null));
                            } else {
                                return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.ACCEPTED, "El usuario se añadio correctamente", group));
                            }
                        }else{
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST, "Error al realizar en la operacion,ya existe un Broadcast en el grupo", null));
                        }

                    }else{
                        System.out.println("no entra a caso broadcast");
                        System.out.println("idsuperor:"+bodyAddUserGroup.getIdSuperior());
                        System.out.println("id:"+bodyAddUserGroup.getIdUsuario());
                        System.out.println("rol:"+bodyAddUserGroup.getNombreRol());


                        //verifica si el usuario seleccionado no es el broadcast
                      //  Optional<User> userBroadcast= userService.findById(bodyAddUserGroup.getIdSuperior());

                       // if (!userBroadcast.get().getNombreRol().equals("BROADCAST")){
                            Group group = groupService.guardarUsuario(bodyAddUserGroup.getIdUsuario(), bodyAddUserGroup.getIdGrupo(), bodyAddUserGroup.getIdSuperior(), bodyAddUserGroup.getNombreRol());
                            User user = userService.actualizaRol(userService.findById(bodyAddUserGroup.getIdUsuario()).get(), bodyAddUserGroup.getIdSuperior(), bodyAddUserGroup.getIdGrupo(), bodyAddUserGroup.getNombreRol());
                            if (group == null && user == null) {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST, "Error al realizar en la operacion,parametro no valido", null));
                            } else {
                                return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.OK, "El usuario se añadio correctamente", group));
                            }
                      /*  }else{
                            //no se pueden asignar subordinados al broadcast
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST, "Error al realizar en la operacion,no se pueden asignar subordinados al broadcast", ""));
                        }*/
                    }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e);
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "Error Inesperado  ", null));
        }
    }

    @CrossOrigin(origins = {"*"})
    @GetMapping("/buscar/{id}")//*
    public ResponseEntity<?> buscar(@PathVariable String id) {
        try {
            if (groupService.buscarPorId(id).isPresent()) {
                return ResponseEntity.ok(new Response(HttpStatus.OK, "Grupo encontrado", groupService.buscarPorId(id)));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST, "Error grupo no existente", null));
            }
        } catch (Exception e) {
            System.err.println("Error: " + e);
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "Error Inesperado", null));
        }
    }

    @DeleteMapping(value = "/borrar/{id}") //*
    public ResponseEntity<Response> delete(@PathVariable String id) {
        try {
            if (id == null) {
                System.out.println("Error en las llaves");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE, "Error en las llaves", null));
            } else {
                Optional<Group> grupo = groupService.buscarPorId(id);
                if (grupo.isPresent()) {
                    for (User usr : grupo.get().getUsuarios()) {
                        usr.setIDGrupo("");
                        usr.setIDSuperiorInmediato("");
                        usr.setNombreRol("");
                        userService.save(usr);
                    }
                    groupService.borrarPorId(id);
                    System.out.println("Grupo eliminado");
                    return ResponseEntity.ok(new Response(HttpStatus.OK, "Grupo eliminado", grupo.get()));
                } else {
                    return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "Grupo no encontrado", null));
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e);
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "Error Inesperado", null));
        }
    }

    @DeleteMapping("/borrarUsuarioDeGrupo")//*
    public ResponseEntity deleteUserFromgroup(@RequestBody BodyAddUserGroup body) {
        try {
            if (body.getIdGrupo() == null || body.getIdUsuario() == null) {
                System.out.println("Error en las llaves");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE, "Error en las llaves", null));
            } else {
                Optional<Group> grupo = groupService.buscarUsuarioEnGrupo(body.getIdGrupo(), body.getIdUsuario());
                if (grupo.isPresent()) {

                    groupService.borrarUsuarioDeGrupo(body.getIdUsuario(), body.getIdGrupo());
                    //cambia idSuperior, Rol y idGrupo a ""
                    Optional<User> us = userService.findById(body.getIdUsuario());
                    us.get().setIDSuperiorInmediato("");
                    us.get().setIDGrupo("");
                    us.get().setNombreRol("");
                    userService.save(us.get());

                    System.out.println("Usuario eliminado del grupo");
                    return ResponseEntity.ok(new Response(HttpStatus.OK, "Usuario eliminado del grupo", grupo.get()));
                } else {
                    return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "Grupo o usuario no encontrado", null));
                }
            }
        } catch (Exception e) {
            System.err.println("Error en eliminar usuario de un grupo: " + e);
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "Error Inesperado", null));
        }
    }

    @GetMapping("/buscarUsuarioEnGrupo")
    public Response buscarUsuarioEnGrupo(@RequestBody String json) {
        try {
            String idGroup = "", idUser = "";
            JSONObject jsn = new JSONObject(json);
            idGroup = jsn.get("idGroup").toString();
            idUser = jsn.get("idUser").toString();

            //verifica que exista el grupo
            if (groupService.buscarPorId(idGroup).isPresent()) {

                //verifica que existe el usuario
                if (userService.findById(idUser).isPresent()) {

                    Optional<Group> optGroup;
                    optGroup = groupService.buscarUsuarioEnGrupo(idGroup, idUser);

                    if (optGroup.isPresent()) {
                        Group group = optGroup.get();
                        if (group == null) {
                            return new Response(HttpStatus.BAD_REQUEST, "el usuario no pertenece al grupo", group);
                        }

                        return new Response(HttpStatus.OK, "usuario encontrado dentro del grupo", group);
                    } else {
                        return new Response(HttpStatus.BAD_REQUEST, "No se encontro el usuario dentro del grupo", null);
                    }
                } else {
                    return new Response(HttpStatus.BAD_REQUEST, "El usuario no existe", null);
                }
            } else {
                return new Response(HttpStatus.BAD_REQUEST, "El grupo no existe", null);
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e);
            return new Response(HttpStatus.NOT_FOUND, "Error en la consulta", null);
        }
    }

    @GetMapping("/buscarPorNombre/{nombre}")
    public Response buscarPorNombre(@PathVariable String nombre) {
        try {
            if (groupService.buscarPorNombre(nombre).isPresent()) {
                return new Response(HttpStatus.OK, "Grupo encontrado", groupService.buscarPorNombre(nombre).get());
            } else {
                return new Response(HttpStatus.BAD_REQUEST, "Nombre no encontrado", null);
            }
        } catch (Exception e) {
            System.err.println("Excepcion: " + e);
            return new Response(HttpStatus.NOT_FOUND, "Error en la consulta", null);
        }
    }

    @PostMapping("/crearGrupo/{nombre}")
    public Response crearGrupo(@PathVariable String nombre) {
        try {
            nombre = nombre.toUpperCase();
            if (!groupService.buscarPorNombre(nombre).isPresent()) {
                Group group = new Group();

                group.setNombre(nombre);
                group.setUsuarios(new User[0]);
                groupService.guardar(group);
                return new Response(HttpStatus.OK, "Grupo " + nombre + " creado", "");
            } else {
                return new Response(HttpStatus.BAD_REQUEST, "Grupo " + nombre + " ya existe", null);
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e);
            return new Response(HttpStatus.NOT_FOUND, "Error en la consulta: " + e, null);
        }
        //return new Response();
    }

    @CrossOrigin(origins = {"*"})
    @GetMapping("/buscarTodo")
    public Response buscarTodo() {
        try {
            Iterable<Group> grupos = groupService.buscarTodo();
            return new Response(HttpStatus.OK, "Grupos existentes", grupos);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, "Error al hacer la consulta", null);
        }
    }


    /**
     * @param json Recibe dos parametros "pagina" y "tamaño"
     *             pagina: es la pagina a mostrar
     *             tamaño: es la cantidad de objetos por pagina
     * @return
     */
    @CrossOrigin(origins = "http://localhost:8080/")
    @GetMapping("/buscarTodoPags")
    public Response buscarTodoPageable(@RequestBody String json) {
        try {
            int pagina = -1, tamaño = -1;
            JSONObject jsn = new JSONObject(json);

            if (jsn.get("pagina") == null || jsn.get("tamaño") == null) {
                return new Response(HttpStatus.NOT_ACCEPTABLE, "Faltan datos", null);
            }

            pagina = (int) jsn.get("pagina");
            tamaño = (int) jsn.get("tamaño");

            if (pagina < 0 || tamaño < 1) {
                return new Response(HttpStatus.BAD_REQUEST, "Datos incorrectos", null);
            }

            Iterable<Group> grupos = groupService.buscarTodo(PageRequest.of(pagina, tamaño));
            return new Response(HttpStatus.OK, "Grupos existentes", grupos);

        } catch (Exception e) {
            System.err.println("Exception: " + e);
            return new Response(HttpStatus.NOT_FOUND, "Error al hacer la consulta", null);
        }
    }

    @PutMapping("/actualizaNombre")
    public ResponseEntity<?> actualizaNombre(@RequestBody Group grupo) {
        try {
            if (groupService.buscarPorId(grupo.getId()).isPresent() && grupo.getNombre() != null) {
                return ResponseEntity.ok(new Response(HttpStatus.OK, "Grupo actualizado correctamente", groupService.actualizaNombre(grupo.getId(), grupo.getNombre())));
            } else {
                return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "Grupo no encontrado", null));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "Error desconocido", null));
        }
    }

    @PutMapping("/actualizaUsuarioGrupo")
    public ResponseEntity<?> actualizaUsuarioGrupo(@RequestBody User user) {
        try {
            String pwd = userService.cifrar(user.getPassword());
            user.setPassword(pwd);
            if (groupService.actualizaUsuario(user)) {
                return ResponseEntity.ok(new Response(HttpStatus.OK, "Usuario en grupo actualizado correctamente", ""));
            } else {
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "Usuario no encontrado", null));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "Error desconocido", null));
        }
    }

    @PostMapping("/reasignaUsuarioGrupo")
    public ResponseEntity<?> reasignaUsuarioGrup(@RequestBody BodyAddUserGroup body) {
        //verificamos que no se quiera hacer una asignacion incorrecta
        try {
            if (!body.getIdUsuario().equals(body.getIdSuperior())) {
                //buscamos el usuario para actualizarlo
                Optional<User> user = userService.findById(body.getIdUsuario());
                if (user.isPresent()) {
                    user.get().setIDSuperiorInmediato(body.getIdSuperior());
                    user.get().setNombreRol(body.getNombreRol());

                    //actualizamos en grupo y lista de usuarios
                    if (groupService.actualizaUsuario(user.get()) && (userService.save(user.get()) != null)) {
                        return ResponseEntity.ok(new Response(HttpStatus.OK, "Usuario  actualizado correctamente", ""));
                    } else {
                        return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "Usuario no actualizado", null));
                    }

                }else{
                    return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "Usuario no encontrado", null));
                }
            } else {
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "Error no se puede asignar a si mismo como superior", null));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "Error desconocido", null));
        }


    }

}






