package com.ekt.Servicios.controller;

import com.ekt.Servicios.entity.BodyAddUserGroup;
import com.ekt.Servicios.entity.Group;
import com.ekt.Servicios.entity.Response;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.service.GeneralService;
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
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE, "Error  en las llaves", ""));
            } else {
                    //verificar el caso de broadcast (caso añadir desde organigrama)
                    if(bodyAddUserGroup.getIdSuperior().equals("BROADCAST")){
                        System.out.println("entra a coso broadcast");
                        //verificar si un broadcast ya existe
                        if(!groupService.buscarBroadCastEnGrupo(bodyAddUserGroup.getIdGrupo())){
                            System.out.println("intenta agregar");
                            //agregar
                            Group group = groupService.guardarUsuario(bodyAddUserGroup.getIdUsuario(), bodyAddUserGroup.getIdGrupo(), "-1", "BROADCAST");
                            User user = userService.actualizaRol(userService.findById(bodyAddUserGroup.getIdUsuario()).get(), "-1", bodyAddUserGroup.getIdGrupo(), "BROADCAST");
                            if (group == null && user == null) {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST, "Error al realizar en la operacion,parametro no valido", ""));
                            } else {
                                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response(HttpStatus.ACCEPTED, "El usuario se añadio correctamente", group));
                            }
                        }else{
                            System.out.println("No se agrega el usuario");
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST, "Error al realizar en la operacion,ya existe un Broadcast en el grupo", ""));
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
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST, "Error al realizar en la operacion,parametro no valido", ""));
                            } else {
                                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response(HttpStatus.ACCEPTED, "El usuario se añadio correctamente", group));
                            }
                      /*  }else{
                            //no se pueden asignar subordinados al broadcast
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST, "Error al realizar en la operacion,no se pueden asignar subordinados al broadcast", ""));
                        }*/
                    }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e);
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "Error Inesperado  ", ""));
        }
    }


    /**
     * Busca un grupo filtrando por id.
     * @param id es un string del id a buscar
     * @return data=Group en caso de exito
     */
    @GetMapping("/buscar/{id}")
    public Response buscar(@PathVariable String id) {
        try {
            if (groupService.buscarPorId(id).isPresent()) {
                return new Response(HttpStatus.ACCEPTED, "Grupo encontrado", groupService.buscarPorId(id));
            } else {
                return new Response(HttpStatus.BAD_REQUEST, "Error grupo no existente", "");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e);
            return new Response(HttpStatus.NOT_FOUND, "Error Inesperado", "");
        }
    }

    /**
     * Borra un grupo.
     * -Cambia el idGrupo, idSupereior inmediato y nombreRol de los usuarios en el grupo a ""
     * -Elimina el grupo de la BD
     * @param id String con el id del grupo a borrar
     * @return
     */
    @DeleteMapping(value = "/borrar/{id}")
    public Response delete(@PathVariable String id) {
        try {
            if (id == null) {
                return new Response(HttpStatus.NOT_ACCEPTABLE, "Error en las llaves", "");
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
                    return new Response(HttpStatus.ACCEPTED, "Grupo eliminado", "");
                } else {
                    return new Response(HttpStatus.BAD_REQUEST, "Grupo no encontrado", "");
                }
            }
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, "Error Inesperado", e);
        }
    }

    @DeleteMapping("/borrarUsuarioDeGrupo")//*
    public ResponseEntity deleteUserFromgroup(@RequestBody BodyAddUserGroup body) {
        try {
            if (body.getIdGrupo() == null || body.getIdUsuario() == null) {
                System.out.println("Error en las llaves");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE, "Error en las llaves", ""));
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
                    return ResponseEntity.ok(new Response(HttpStatus.ACCEPTED, "Usuario eliminado del grupo", grupo.get()));
                } else {
                    return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "Grupo o usuario no encontrado", ""));
                }
            }
        } catch (Exception e) {
            System.err.println("Error en eliminar usuario de un grupo: " + e);
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "Error Inesperado", ""));
        }
    }


    /**
     * Buscar usuario en grupo
     * -valida que exista el grupo
     * -valida que exista el usuario
     * @param json contiene idGrupo y idUsuaurio
     * @return data=Group en caso de exito
     */
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
                            return new Response(HttpStatus.NOT_FOUND, "el usuario no pertenece al grupo", group);
                        }

                        return new Response(HttpStatus.OK, "usuario encontrado dentro del grupo", group);
                    } else {
                        return new Response(HttpStatus.NOT_FOUND, "No se encontro el usuario dentro del grupo", "");
                    }
                } else {
                    return new Response(HttpStatus.NOT_FOUND, "El usuario no existe", "");
                }
            } else {
                return new Response(HttpStatus.NOT_FOUND, "El grupo no existe", "");
            }

        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, "Error en la consulta", e);
        }
    }

    /**
     * Busca un grupo filtrando por nombre.
     * @param nombre es un string del nombre a buscar
     * @return data=Group en caso de exito
     */
    @GetMapping("/buscarPorNombre/{nombre}")
    public Response buscarPorNombre(@PathVariable String nombre) {
        try {
            if (groupService.buscarPorNombre(nombre).isPresent()) {
                return new Response(HttpStatus.OK, "Grupo encontrado", groupService.buscarPorNombre(nombre).get());
            } else {
                return new Response(HttpStatus.BAD_REQUEST, "Nombre no encontrado", "");
            }
        } catch (Exception e) {
            System.err.println("Excepcion: " + e);
            return new Response(HttpStatus.NOT_FOUND, "Error en la consulta", "");
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
                return new Response(HttpStatus.BAD_REQUEST, "Grupo " + nombre + " ya existe", "");
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e);
            return new Response(HttpStatus.NOT_FOUND, "Error en la consulta: " + e, "");
        }
        //return new Response();
    }


    /**
     * Busca todos los grupos
     * @return data=ArrayList<Group> en caso de exito
     */
    @CrossOrigin(origins = {"*"})
    @GetMapping("/buscarTodo")
    public Response buscarTodo() {
        try {
            Iterable<Group> grupos = groupService.buscarTodo();
            return new Response(HttpStatus.OK, "Grupos existentes", grupos);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, "Error al hacer la consulta", e);
        }
    }


    /**
     * Busca todos los grupos con paginacion
     * @param json Recibe dos parametros "pagina" y "tamaño"
     *             pagina: es la pagina a mostrar
     *             tamaño: es la cantidad de objetos por pagina
     * @return data=ArrayList<Group> en caso de exito
     */
    @CrossOrigin(origins = {"*"})
    @GetMapping("/buscarTodoPags")
    public Response buscarTodoPageable(@RequestBody String json) {
        try {
            int pagina = -1, tamaño = -1;
            JSONObject jsn = new JSONObject(json);

            if (jsn.get("pagina") == null || jsn.get("tamaño") == null) {
                return new Response(HttpStatus.BAD_REQUEST, "Faltan datos", "");
            }

            pagina = (int) jsn.get("pagina");
            tamaño = (int) jsn.get("tamaño");

            if (pagina < 0 || tamaño < 1) {
                return new Response(HttpStatus.BAD_REQUEST, "Datos incorrectos", "");
            }

            Iterable<Group> grupos = groupService.buscarTodo(PageRequest.of(pagina, tamaño));
            return new Response(HttpStatus.OK, "Grupos existentes", grupos);

        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, "Error al hacer la consulta", e);
        }
    }

    @PutMapping("/actualizaNombre")
    public ResponseEntity<?> actualizaNombre(@RequestBody Group grupo) {
        try {
            if (groupService.buscarPorId(grupo.getId()).isPresent() && grupo.getNombre() != null) {
                return ResponseEntity.ok(new Response(HttpStatus.OK, "Grupo actualizado correctamente", groupService.actualizaNombre(grupo.getId(), grupo.getNombre())));
            } else {
                return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "Grupo no encontrado", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "Error desconocido", ""));
        }
    }

    @PutMapping("/actualizaUsuarioGrupo")
    public ResponseEntity<?> actualizaUsuarioGrupo(@RequestBody User user) {
        try {
            String pwd = GeneralService.cifrar(user.getPassword());
            user.setPassword(pwd);
            if (groupService.actualizaUsuario(user)) {
                return ResponseEntity.ok(new Response(HttpStatus.OK, "Usuario en grupo actualizado correctamente", ""));
            } else {
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "Usuario no encontrado", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "Error desconocido", ""));
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
                        return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "Usuario no actualizado", ""));
                    }

                }else{
                    return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "Usuario no encontrado", ""));
                }
            } else {
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "Error no se puede asignar a si mismo como superior", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "Error desconocido", ""));
        }


    }

}






