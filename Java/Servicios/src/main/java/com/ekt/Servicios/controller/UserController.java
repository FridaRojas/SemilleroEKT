package com.ekt.Servicios.controller;



import com.ekt.Servicios.entity.BodyAddUserGroup;
import com.ekt.Servicios.entity.BodyUpdateBoss;
import com.ekt.Servicios.entity.Response;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.repository.UserRepository;
import com.ekt.Servicios.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    public UserService userService;

    @Autowired
    public UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Validated @RequestBody User user){
        if (user.getCorreo()==null || user.getFechaInicio()==null || user.getFechaTermino()==null || user.getNumeroEmpleado()==null || user.getNombre()==null || user.getPassword()==null || user.getNombreRol()==null || user.getIDGrupo()==null || user.getToken()==null || user.getTelefono()==null || user.getIDSuperiorInmediato()==null || user.getStatusActivo()==null || user.getCurp()==null || user.getRFC()==null){
            System.out.println("Error en las llaves");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE,"Error en las llaves",""));
        }else{
            Optional<User> us= userRepository.findUsersByUniqueData(user.getCorreo(), user.getCurp(), user.getRFC(), user.getNumeroEmpleado());

            if (us.isPresent()){
                return ResponseEntity.ok(new Response(HttpStatus.CONFLICT,"Usuario existente",""));
            }else {
                userService.save(user);
                System.out.println("Usuario Creado");
                return ResponseEntity.ok(new Response(HttpStatus.ACCEPTED,"Usuario Creado",user));
            }
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(){
    if (userService.findAll()!=null){
        return ResponseEntity.ok(new Response(HttpStatus.ACCEPTED,"Lista de usuarios encontrada",userService.findAll()));
    }else{
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE,"Error al buscar los datos",""));
    }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable String id){
        //return userService.findById(id);
        if(userService.findById(id).isPresent()){
            return ResponseEntity.ok(new Response(HttpStatus.ACCEPTED,"Usuario encontrado",userService.findById(id)));
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST,"Error usuario no existente",""));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> userValidate(@RequestBody User infAcceso){

        if (infAcceso.getPassword()==null || infAcceso.getCorreo()==null || infAcceso.getToken()==null){
            System.out.println("Error en las llaves");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE,"Error en las llaves",""));
        }else{
            Optional<User> user=userService.userValidate(infAcceso.getCorreo(),infAcceso.getPassword());
            if (user.isPresent()){
                System.out.println("Usuario encontrado");
                //actualizar token
                user.get().setToken(infAcceso.getToken());
                userService.save(user.get());

                return ResponseEntity.ok(new Response(HttpStatus.ACCEPTED,"Usuario encontrado",user.get()));
            }else{
            System.out.println("Usuario no encontrado");
            return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"Usuario no encontrado",""));}
        }
    }

    @DeleteMapping(value="/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        try{
            if(userService.findById(id).isPresent()){
                User u = userService.findById(id).get();
                if(u.getStatusActivo().equals("true")){
                    u.setStatusActivo("false");
                    userService.save(u);
                    return ResponseEntity.ok(new Response(HttpStatus.OK,"Usuario eliminado correctamente",""));
                }
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"No se puede borrar",""));
            }
        }catch(Exception e){
            System.err.println("Error: "+e);
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND,"Usuario no encontrado",""));
        }
        //userService.deleteById(id);
        return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"error desconocido",""));
    }

    @PutMapping("/updateIdBoss")
    public ResponseEntity<?> updateIdBoss(@RequestBody BodyUpdateBoss updateBoss){
        String[] idUser = updateBoss.getIDUser();
        String[] idBoss = updateBoss.getIDBoss();
        try {
            for (int i = 0; i < idUser.length; i++) {
                userService.updateIdBoss(userService.findById(idUser[i]).get(), idBoss[i]);
            }
            return ResponseEntity.ok(new Response(HttpStatus.OK, "Actualizacion de superior inmediato lista", ""));
        }catch (Exception e){
            return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"Error desconocido",""));
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody User userUpdate, @PathVariable String id){
        try {
            Optional<User> user = userService.findById(id);
            if(!user.isPresent()) {
                return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, "No se encontró al usuario", ""));
            }else {
                user.get().setCorreo(userUpdate.getCorreo());
                user.get().setFechaInicio(userUpdate.getFechaInicio());
                user.get().setFechaTermino(userUpdate.getFechaTermino());
                user.get().setNombre(userUpdate.getNombre());
                user.get().setPassword(userUpdate.getPassword());
                user.get().setTelefono(userUpdate.getTelefono());
                user.get().setCurp(userUpdate.getCurp());
                user.get().setRFC(userUpdate.getRFC());
                userService.save(user.get());
                return ResponseEntity.ok(new Response(HttpStatus.OK, "Usuario actualizado", user.get()));
            }
        }catch (Exception e){
            System.out.println("No se puede actualizar el usuario: " + e);
            return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, e.toString(), ""));
        }

    }

    @PutMapping("/updateRol")
    public ResponseEntity<?> updateRol(@RequestBody BodyAddUserGroup bodyGroup){
        boolean bandera = false;
        try {
            Optional<User> user = userService.findById(bodyGroup.getIDUsuario());
            if(user.isPresent()) {
                if (bodyGroup.getIDSuperior() != null && !bodyGroup.getIDSuperior().equals(user.get().getIDSuperiorInmediato())) {
                    user.get().setIDSuperiorInmediato(bodyGroup.getIDSuperior());
                    bandera = true;
                }
                if (bodyGroup.getIDGrupo() != null && !bodyGroup.getIDGrupo().equals(user.get().getIDGrupo())) {
                    user.get().setIDGrupo(bodyGroup.getIDGrupo());
                    bandera = true;
                }
                if (bodyGroup.getNombreRol() != null && !bodyGroup.getNombreRol().equals(user.get().getNombreRol())) {
                    user.get().setNombreRol(bodyGroup.getNombreRol());
                    bandera = true;
                }
                if (bandera) {
                    userService.save(user.get());
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
            System.out.println(((Collection<User>) users).size());
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


    @GetMapping("/existUser")
    public ResponseEntity<?> existUser(@RequestBody User user){
        if (user.getCorreo()==null || user.getCurp()==null || user.getRFC()==null || user.getNumeroEmpleado()==null){
            System.out.println("Error en las llaves");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE,"Error en las llaves",""));
        }else{
            Optional<User> us= userService.findUsersByUniqueData(user.getCorreo(), user.getCurp(),user.getRFC(),user.getNumeroEmpleado());
            if (us.isPresent()){
                System.out.println("El usuario existe");
                return ResponseEntity.ok(new Response(HttpStatus.ACCEPTED,"El usuario existe",us.get()));
            }else {
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"Usuario no encontrado",""));
            }
        }
    }


}
