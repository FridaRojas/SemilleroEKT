package com.ekt.Servicios.controller;

import com.ekt.Servicios.entity.BodyAddUserGroup;
import com.ekt.Servicios.entity.BodyUpdateBoss;
import com.ekt.Servicios.entity.Response;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.repository.UserRepository;
import com.ekt.Servicios.service.GeneralService;
import com.ekt.Servicios.service.GroupServiceImpl;
import com.ekt.Servicios.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    public UserService userService;

    @Autowired
    public GroupServiceImpl groupService;

    @Autowired
    public UserRepository userRepository;

    @PostMapping("/create")//*
    public ResponseEntity<?> create(@Validated @RequestBody User user){
        System.out.println(user.getNombre()+"  "+user.getRFC());
        try {
            if (user.getCorreo()==null || user.getFechaInicio()==null || user.getFechaTermino()==null || user.getNumeroEmpleado()==null || user.getNombre()==null || user.getPassword()==null || user.getNombreRol()==null || user.getIDGrupo()==null || user.getToken()==null || user.getTelefono()==null || user.getIDSuperiorInmediato()==null || user.getStatusActivo()==null || user.getCurp()==null || user.getRFC()==null){
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE,"Error en las llaves",""));
            }else{
                boolean us= userService.findUsersByUniqueData(user.getCorreo(), user.getCurp(), user.getRFC(), user.getNumeroEmpleado());
                if (us){
                    System.out.println("ya existe");
                    return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"Usuario existente",""));
                }else {
                    String psw= GeneralService.cifrar(user.getPassword());
                    user.setPassword(psw);
                    user.setTokenAuth("");
                    userService.save(user);
                    System.out.println("creado");
                    return ResponseEntity.ok(new Response(HttpStatus.ACCEPTED,"Usuario Creado",user));
                }
            }
        }catch (Exception e){
            System.err.println("Error: "+e);
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND,"Error Inesperado",""));
        }

    }

    @GetMapping("/findAll")//*
    public ResponseEntity<?> findAll(){
        try{
            if (userService.findAll()!=null){
                return ResponseEntity.ok(new Response(HttpStatus.ACCEPTED,"Lista de usuarios encontrada",userService.findAll()));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST,"Error al buscar los datos",""));
            }
        }catch (Exception e){
            System.err.println("Error: "+e);
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND,"Error Inesperado",""));
        }
    }

    @GetMapping("/find/{id}")//*
    public ResponseEntity<?> findById(@PathVariable String id){
        //return userService.findById(id);
        try{
            if(userService.findById(id).isPresent()){
                return ResponseEntity.ok(new Response(HttpStatus.ACCEPTED,"Usuario encontrado",userService.findById(id)));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST,"Error usuario no existente",""));
            }
        }catch (Exception e){
            System.err.println("Error: "+e);
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND,"Error Inesperado",""));
        }
    }

    @PostMapping("/validate")//*
    public ResponseEntity<?> userValidate(@RequestBody User infAcceso){
        try{
            if (infAcceso.getPassword()==null || infAcceso.getCorreo()==null || infAcceso.getToken()==null){
                System.out.println("Error en las llaves");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE,"Error en las llaves",""));
            }else{
                String psw=GeneralService.cifrar(infAcceso.getPassword());
                Optional<User> user=userService.userValidate(infAcceso.getCorreo(),psw);
                if (user.isPresent()){
                    System.out.println(user.get().getStatusActivo());
                    if(user.get().getStatusActivo().equals("true")){
                        System.out.println("Login: Usuario encontrado");
                        user.get().setToken(infAcceso.getToken());
                        user.get().setTokenAuth(userService.guardarTokenAuth(user.get().getID()).get());
                        userService.save(user.get());
                        groupService.actualizaUsuario(user.get());


                        user.get().setFechaInicio(null);
                        user.get().setFechaTermino(null);
                        user.get().setPassword(null);
                        user.get().setIDGrupo(null);
                        user.get().setOpcionales(null);
                        user.get().setIDSuperiorInmediato(null);
                        user.get().setStatusActivo(null);
                        user.get().setCurp(null);
                        user.get().setRFC(null);
                        return ResponseEntity.ok(new Response(HttpStatus.ACCEPTED,"Usuario encontrado",user));

                    }else {
                        return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"Usuario no encontrado",""));

                    }
                }else{
                System.out.println("Login: Usuario no encontrado");
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"Usuario no encontrado",""));
                }
            }
        }catch (Exception e){
            System.err.println("Error: "+e);
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND,"Error Inesperado",""));
        }
    }


    /**
     * Metodo que cambia el statusActivo de un usuario, no lo borra de la BD
     * -busca que el usuario exista
     * -cambia el statusActivo a false
     * -cambia nombreRol,idGrupo,idSuperiorInmediato a un string vacio
     * @param id es el id del usuario a borrar
     * @return Response data="" en caso de exito
     */
    @DeleteMapping(value="/delete/{id}")
    public Response delete(@PathVariable String id){
        try{
            if(userService.findById(id).isPresent()){
                User usr = userService.findById(id).get();
                if(usr.getStatusActivo().equals("true")){
                    usr.setStatusActivo("false");
                    usr.setNombreRol("");
                    usr.setIDGrupo("");
                    usr.setIDSuperiorInmediato("");
                    userService.save(usr);
                    return new Response(HttpStatus.OK,"Usuario eliminado correctamente","");
                }
                return new Response(HttpStatus.BAD_REQUEST,"No se puede borrar","");
            }else{
                return new Response(HttpStatus.BAD_REQUEST,"No se puede borrar","");
            }
        }catch(Exception e){
            System.err.println("Error: "+e);
            return new Response(HttpStatus.NOT_FOUND,"Error al hacer la consulta: ",e);
        }
    }

    @PutMapping("/updateIdBoss")//*
    public ResponseEntity<?> updateIdBoss(@RequestBody BodyUpdateBoss updateBoss){

        try {
            if (updateBoss.getIDUsuarios()==null || updateBoss.getIDSuperiores()==null){
                System.out.println("Error en las llaves");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE,"Error en las llaves",""));
            }else{
                String[] idUser = updateBoss.getIDUsuarios();
                String[] idBoss = updateBoss.getIDSuperiores();
                for (int i = 0; i < idUser.length; i++) {
                    userService.updateIdBoss(idUser[i], idBoss[i]);
                    groupService.actualizaIdSuperior(idUser[i], idBoss[i]);
                }
                return ResponseEntity.ok(new Response(HttpStatus.OK, "Actualizacion de superior inmediato lista", ""));
            }

        }catch (Exception e){
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND,e.getMessage(),""));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody User userUpdate){

        try {
            Optional<User> user = userService.findById(userUpdate.getID());

            if(!user.isPresent()) {
                return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "No se encontró al usuario", ""));
            }else {
                if(!user.get().getCorreo().equals(userUpdate.getCorreo()) && userService.buscaCorreoUsuario(userUpdate.getCorreo())){
                    System.out.println("1");
                    return ResponseEntity.ok(new Response(HttpStatus.NOT_ACCEPTABLE, "Correo no válido", ""));
                }else
                    if(!user.get().getCurp().equals(userUpdate.getCurp()) && userService.buscaCURPUsuario(userUpdate.getCurp())){
                    System.out.println("2");
                    return ResponseEntity.ok(new Response(HttpStatus.NOT_ACCEPTABLE, "CURP no válido", ""));
                }else if(!user.get().getRFC().equals(userUpdate.getRFC()) && userService.buscaRFCUsuario(userUpdate.getRFC())){
                    System.out.println("3");
                    return ResponseEntity.ok(new Response(HttpStatus.NOT_ACCEPTABLE, "RFC no válido", ""));
                }else if(!user.get().getNumeroEmpleado().equals(userUpdate.getNumeroEmpleado()) && userService.buscaNoEmpleadoUsuario(userUpdate.getNumeroEmpleado())){
                    System.out.println("4");
                    return ResponseEntity.ok(new Response(HttpStatus.NOT_ACCEPTABLE, "Número de empleado no válido", ""));
                }else{
                        if (!userUpdate.getPassword().equals(user.get().getPassword())){
                            String pwd=GeneralService.cifrar(userUpdate.getPassword());
                            userUpdate.setPassword(pwd);
                        }
                        //si tien grupo actualizamos informacion personal
                        if(!userUpdate.getIDGrupo().equals("")){
                            groupService.actualizaUsuario(userUpdate);
                        }

                    return ResponseEntity.ok(new Response(HttpStatus.OK, "Usuario actualizado correctamente", userService.actualizaUsuario(userUpdate)));
                }
            }
        }catch (Exception e){
            return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, e.toString(), ""));
        }

    }

    @PutMapping("/updateRol")
    public ResponseEntity<?> updateRol(@RequestBody BodyAddUserGroup bodyGroup){
        boolean bandera = false;
        String idSuperior;
        String idGrupo;
        String nombreRol;
        try {
            Optional<User> user = userService.findById(bodyGroup.getIdUsuario());
            if(user.isPresent()) {
                if (bodyGroup.getIdSuperior() != null && !bodyGroup.getIdSuperior().equals(user.get().getIDSuperiorInmediato())) {
                    idSuperior = bodyGroup.getIdSuperior();
                    bandera = true;
                }else{
                    idSuperior = user.get().getIDSuperiorInmediato();
                }
                if (bodyGroup.getIdGrupo() != null && !bodyGroup.getIdGrupo().equals(user.get().getIDGrupo())) {
                    idGrupo = bodyGroup.getIdGrupo();
                    bandera = true;
                }else{
                    idGrupo = user.get().getIDGrupo();
                }
                if (bodyGroup.getNombreRol() != null && !bodyGroup.getNombreRol().equals(user.get().getNombreRol())) {
                    nombreRol = bodyGroup.getNombreRol();
                    bandera = true;
                }else{
                    nombreRol = user.get().getNombreRol();
                }
                if (bandera) {
                    userService.actualizaRol(user.get(), idSuperior, idGrupo, nombreRol);
                    return ResponseEntity.ok(new Response(HttpStatus.OK, "Rol actualizado con éxito", ""));
                } else {
                    return ResponseEntity.ok(new Response(HttpStatus.NOT_ACCEPTABLE, "No se aceptan los cambios", ""));
                }
            }else{
                return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "No se encontró usuario", ""));
            }
        }catch (Exception e){
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "Error desconocido", ""));
        }
    }

    @GetMapping("/findByBossId/{id}")
    public ResponseEntity<?> findByBossId(@PathVariable String id){
        try {
            Iterable<User> users = userService.findUserByBossId(id);
            if(((Collection<User>) users).size()>0) {
                return ResponseEntity.ok(new Response(HttpStatus.OK, "Usuarios encontrados", users));
            }
            else {
                return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "No se encontraron usuarios", ""));
            }
        }catch (Exception e){
            return ResponseEntity.ok(new Response(HttpStatus.NOT_ACCEPTABLE, "Error desconocido", ""));
        }
    }

    @PostMapping("/existUser")//*
    public ResponseEntity<?> existUser(@RequestBody User user){
        try {
            if (user.getCorreo() == null || user.getCurp() == null || user.getRFC() == null || user.getNumeroEmpleado() == null) {
                System.out.println("Error en las llaves");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE, "Error en las llaves", ""));
            } else {
                boolean us = userService.findUsersByUniqueData(user.getCorreo(), user.getCurp(), user.getRFC(), user.getNumeroEmpleado());
                if (us) {
                    System.out.println("El usuario existe");
                    return ResponseEntity.ok(new Response(HttpStatus.ACCEPTED, "El usuario existe", "true"));
                } else {
                    return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "Usuario no encontrado", "false"));
                }
            }
        }catch (Exception e){
            System.err.println("Error: "+e);
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND,"Error Inesperado",""));
        }
    }

    @PutMapping("/reasigna")//*
    public ResponseEntity reasigna(@RequestBody BodyUpdateBoss body){
        try {
            if (body.getIDUsuarios() == null || body.getIDSuperiores() == null) {
                System.out.println("Error en las llaves");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE, "Error en las llaves", ""));
            } else {
                String[] Usuarios = body.getIDUsuarios();
                String[] Superiores = body.getIDSuperiores();

                for (int i = 0; i < Usuarios.length; i++) {
                    System.out.println("Empleado:" + Usuarios[i] + "  Superior:" + Superiores[i]);
                }
                userService.reasignaSuperiores(Usuarios, Superiores);
                return ResponseEntity.ok(new Response(HttpStatus.ACCEPTED, "Superiores Modificados", ""));

            }
        }catch (Exception e){
            System.err.println("Error: "+e);
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND,"Error Inesperado",""));
        }

    }


    /**
     * Busca un usuario filtrando el parametro recibido en los atributos correo, nombre,
     * numeroEmpleado, nombreRol, rfc. Filtrando a los usuarios que tengan el statusActivo=true
     * @param parametro es el string a buscar
     * @return data=ArrayList<User> en caso de exito
     */
    @GetMapping("/busquedaUsuario/{parametro}")
    public Response busquedaUsuario(@PathVariable String parametro) {
        try {
            if (parametro==null){
                return new Response(HttpStatus.BAD_REQUEST,"Faltan datos para realizar la consulta",null);
            }else{
                if (userService.busquedaUsuario(parametro).isPresent()){
                    return new Response(HttpStatus.OK, "Usuario(s) encontrado(s)",userService.busquedaUsuario(parametro).get());
                }else{
                    return new Response(HttpStatus.OK, "Usuario(s) no encontrado(s)",userService.busquedaUsuario(parametro).get());
                }
            }
        }catch (Exception e){
            return new Response(HttpStatus.NOT_FOUND,"Error al hacer la consulta",e);
        }
    }

    @PostMapping("/logout/{idUser}")
    public Response logout(@PathVariable String idUser){
        try{
            if (userService.findById(idUser).isPresent()){
                User usr = userService.findById(idUser).get();
                usr.setTokenAuth("");
                groupService.actualizaUsuario(usr);
                User tmp=userService.save(usr);
                if (tmp.getTokenAuth().length()==0){
                    return new Response(HttpStatus.OK,"Deslogeado correctamente","");
                }
                else{
                    return  new Response(HttpStatus.BAD_REQUEST,"Error al deslogear","");
                }
            }else{
                return new Response(HttpStatus.BAD_REQUEST,"Usuario "+idUser+" no existe","");
            }
        }catch (Exception e){
            return new Response(HttpStatus.NOT_FOUND,"Error al hacer la consulta",e);
        }
    }





}
