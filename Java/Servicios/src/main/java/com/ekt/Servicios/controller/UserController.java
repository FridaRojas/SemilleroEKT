package com.ekt.Servicios.controller;

import com.ekt.Servicios.entity.BodyAddUserGroup;
import com.ekt.Servicios.entity.BodyUpdateBoss;
import com.ekt.Servicios.entity.Response;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.repository.UserRepository;
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
                Optional<User> user=userService.userValidate(infAcceso.getCorreo(),infAcceso.getPassword());
                if (user.isPresent()){
                    System.out.println("Login: Usuario encontrado");
                    //actualizar token
                    user.get().setToken(infAcceso.getToken());
                    userService.save(user.get());

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

    @DeleteMapping(value="/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        try{
            if(userService.findById(id).isPresent()){
                User usr = userService.findById(id).get();
                if(usr.getStatusActivo().equals("true")){
                    usr.setStatusActivo("false");
                    usr.setNombreRol("");
                    usr.setIDGrupo("");
                    usr.setIDSuperiorInmediato("");
                    userService.save(usr);
                    return ResponseEntity.ok(new Response(HttpStatus.OK,"Usuario eliminado correctamente",""));
                }
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"No se puede borrar",""));
            }else{
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"No se puede borrar",""));
            }
        }catch(Exception e){
            System.err.println("Error: "+e);
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND,"Usuario no encontrado",""));
        }
        //userService.deleteById(id);

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
            System.out.println("Servicios:"+userUpdate.getRFC());
            System.out.println("Servicios2 :"+user.get().getRFC());
            if(!user.isPresent()) {
                return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "No se encontró al usuario", ""));
            }else {
                System.out.println("0");
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
                    groupService.actualizaUsuario(userUpdate);
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
            Optional<User> user = userService.findById(bodyGroup.getIDUsuario());
            if(user.isPresent()) {
                if (bodyGroup.getIDSuperior() != null && !bodyGroup.getIDSuperior().equals(user.get().getIDSuperiorInmediato())) {
                    idSuperior = bodyGroup.getIDSuperior();
                    bandera = true;
                }else{
                    idSuperior = user.get().getIDSuperiorInmediato();
                }
                if (bodyGroup.getIDGrupo() != null && !bodyGroup.getIDGrupo().equals(user.get().getIDGrupo())) {
                    idGrupo = bodyGroup.getIDGrupo();
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

    @GetMapping("/buscarFamilia/{id}")
    public Response findFamily(@PathVariable String id){
        System.out.println(id);
        ArrayList<User> listaUsuarios=new ArrayList<>();

        /*
        -verificar que existe el id
        -buscar al padre
        -buscar hermanos
        -buscarhijos
        -buscar hijos de hijos hasta el infinito

         */
        try {
            User tempUser=new User();
            //verificar que existe el id
            if(userService.findById(id).isPresent()){
                tempUser=userService.findById(id).get();
                //buscar al padre
                if (tempUser.getIDSuperiorInmediato().length()>5){
                    if (userService.findById(tempUser.getIDSuperiorInmediato()).isPresent()){
                        listaUsuarios.add(userService.findById(tempUser.getIDSuperiorInmediato()).get());
                    }
                }
                return new Response(HttpStatus.OK,"hasta aqui solo esta el papa",listaUsuarios);

            }else{
                return new Response(HttpStatus.BAD_REQUEST,"Usuario "+id+" no existe","");
            }
        }catch (Exception e){
            System.err.println("Excepcion: "+e);
            return new Response(HttpStatus.NOT_FOUND,"Error en la consulta","");
        }
    }


}
