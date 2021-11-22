package com.ekt.Servicios.controller;



import com.ekt.Servicios.entity.BodyAddUserGroup;
import com.ekt.Servicios.entity.Group;
import com.ekt.Servicios.entity.Response;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.service.GroupService;
import com.ekt.Servicios.service.UserService;


import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/api/grupo")
public class GroupController {
    @Autowired
    public GroupService groupService;

    @Autowired
    public UserService userService;

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody Group group){
        Group obj= groupService.guardar(group);
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.guardar(group));
    }


    @PutMapping("/guardarGrupo")
    public ResponseEntity<?> addUserGrup(@RequestBody BodyAddUserGroup bodyAddUserGroup){

        if(bodyAddUserGroup.getIDUsuario()==null || bodyAddUserGroup.getIDGrupo()==null || bodyAddUserGroup.getIDSuperior()==null||bodyAddUserGroup.getNombreRol()==null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE,"Error  en las llaves",""));
        }else{
            Group group= groupService.guardarUsuario(bodyAddUserGroup.getIDUsuario(),bodyAddUserGroup.getIDGrupo(), bodyAddUserGroup.getIDSuperior(), bodyAddUserGroup.getNombreRol());
            if (group==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST,"Error al realizar en la operacion,parametro no valido",""));
            }else{
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response(HttpStatus.ACCEPTED,"El usuario se añadio correctamente",group));
            }
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable String id){

        if (groupService.buscarPorId(id).isPresent()){
            return ResponseEntity.ok(new Response(HttpStatus.ACCEPTED,"Grupo encontrado",groupService.buscarPorId(id)));
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST,"Error grupo no existente",""));
        }


        //return groupService.findById(id);
    }

    @DeleteMapping(value="/borrar/{id}")
    public ResponseEntity<Response> delete(@PathVariable String id){
        if (id==null){
            System.out.println("Error en las llaves");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE,"Error en las llaves",""));
        }else{
            Optional<Group> grupo= groupService.buscarPorId(id);
            if (grupo.isPresent()){
                groupService.borrarPorId(id);
                System.out.println("Grupo eliminado");
                return ResponseEntity.ok(new Response(HttpStatus.ACCEPTED,"Grupo eliminado",grupo.get()));
            }else {
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"Grupo no encontrado",""));
            }
        }
    }

    @DeleteMapping("/borrarUsuarioDeGrupo")
    public ResponseEntity deleteUserFromgroup(@RequestBody BodyAddUserGroup body){
        if (body.getIDGrupo()==null || body.getIDUsuario()==null){
            System.out.println("Error en las llaves");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE,"Error en las llaves",""));
        }else{
            Optional<Group> grupo= groupService.buscarUsuarioEnGrupo(body.getIDGrupo(),body.getIDUsuario());

            if (grupo.isPresent()){
                groupService.borrarUsuarioDeGrupo(body.getIDUsuario(), body.getIDGrupo());
                System.out.println("Usuario eliminado del grupo");
                return ResponseEntity.ok(new Response(HttpStatus.ACCEPTED,"Usuario eliminado del grupo",grupo.get()));
            }else {
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"Grupo o usuario no encontrado",""));
            }
        }

    }

    @GetMapping("/buscarUsuarioEnGrupo")
    public Response buscarUsuarioEnGrupo(@RequestBody String json){
        try {
            String idGroup="",idUser ="";
            JSONObject jsn = new JSONObject(json);

            idGroup=jsn.get("idGroup").toString();
            idUser=jsn.get("idUser").toString();



            //verifica que exista el grupo
            if (groupService.buscarPorId(idGroup).isPresent()) {
                //traza
                System.out.println("Grupo existe");

                //verifica que existe el usuario
                if (userService.findById(idUser).isPresent()){
                    //traza
                    System.out.println("Usuario existe");

                    Optional<Group> optGroup;
                    optGroup= groupService.buscarUsuarioEnGrupo(idGroup, idUser);

                    if (optGroup.isPresent()){
                        Group group = optGroup.get();
                        if(group ==null){
                            return new Response(HttpStatus.NOT_FOUND,"el usuario no pertenece al grupo",group);
                        }

                        return new Response(HttpStatus.OK,"usuario encontrado dentro del grupo",group);
                    }else{
                        return new Response(HttpStatus.NOT_FOUND,"No se encontro el usuario dentro del grupo","");
                    }
                }else{
                    return new Response(HttpStatus.NOT_FOUND,"El usuario no existe","");
                }
            }else{
                return new Response(HttpStatus.NOT_FOUND,"El grupo no existe","");
            }



        }catch (Exception e){
            System.out.println("Exception: "+e);
            return new Response(HttpStatus.NOT_FOUND,"Error en la consulta","");
        }
    }
    /*
    @PutMapping("/updateIdBoss")
    public String updateIdBoss(@RequestBody BodyUpdateBoss updateBoss){
        String[] idUser = updateBoss.getIDUser();
        String[] idBoss = updateBoss.getIDBoss();
        for(int i = 0; i < idUser.length; i++){
            userService.updateIdBoss(userService.findById(idUser[i]).get(),idBoss[i]);
        }
        return "Ok";
    }
     */

    @GetMapping("/buscarPorNombre/{nombre}")
    public Response buscarPorNombre(@PathVariable String nombre){
        try {
            if (groupService.buscarPorNombre(nombre).isPresent()){
                return new Response(HttpStatus.OK,"Grupo encontrado",groupService.buscarPorNombre(nombre).get());
            }else{
                return new Response(HttpStatus.BAD_REQUEST,"Nombre no encontrado","");
            }
        }catch (Exception e){
            System.err.println("Excepcion: "+e);
            return new Response(HttpStatus.NOT_FOUND,"Error en la consulta","");
        }
    }

    @PostMapping("/crearGrupo/{nombre}")
    public Response crearGrupo(@PathVariable String nombre){
        try {
            nombre=nombre.toUpperCase();
            if (!groupService.buscarPorNombre(nombre).isPresent()){
                Group group=new Group();

                group.setNombre(nombre);
                group.setUsuarios(new User[0]);
                groupService.guardar(group);
                return new Response(HttpStatus.OK,"Grupo "+nombre+" creado","");
            }else {
                return new Response(HttpStatus.BAD_REQUEST,"Grupo "+nombre+" ya existe","");
            }
        }catch (Exception e){
            System.err.println("Exception: "+e);
            return new Response(HttpStatus.NOT_FOUND,"Error en la consulta: "+e,"" );
        }
        //return new Response();
    }



}
