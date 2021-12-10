package com.ekt.Servicios.controller;

import com.ekt.Servicios.entity.BodyAddUserGroup;
import com.ekt.Servicios.entity.BodyUpdateBoss;
import com.ekt.Servicios.entity.Response;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.repository.UserRepository;
import com.ekt.Servicios.service.GeneralService;
import com.ekt.Servicios.service.GroupServiceImpl;
import com.ekt.Servicios.service.UserService;
import com.mongodb.MongoException;
import com.mongodb.MongoSocketException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public GroupServiceImpl groupService;

    @Autowired
    public UserRepository userRepository;

    @PostMapping("/create")//*
    public ResponseEntity<?> create(@Validated @RequestBody User user){
        try {
            if (user.getCorreo()==null || user.getFechaInicio()==null || user.getFechaTermino()==null || user.getNumeroEmpleado()==null || user.getNombre()==null || user.getPassword()==null || user.getNombreRol()==null || user.getIDGrupo()==null || user.getToken()==null || user.getTelefono()==null || user.getIDSuperiorInmediato()==null || user.getStatusActivo()==null || user.getCurp()==null || user.getRFC()==null){
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE,"Error en las llaves",null));
            }else{
                boolean us= userService.findUsersByUniqueData(user.getCorreo(), user.getCurp(), user.getRFC(), user.getNumeroEmpleado());
                if (us){
                    return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"Usuario existente",null));
                }else {
                    String psw= GeneralService.cifrar(user.getPassword());
                    user.setPassword(psw);
                    user.setTokenAuth("");
                    userService.save(user);
                    return ResponseEntity.ok(new Response(HttpStatus.OK,"Usuario Creado",user));
                }
            }
        } catch (MongoSocketException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (MongoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, e.getMessage(), null));
        }

    }



    /**
     * Busca a todos los usuarios existentes
     * @return Objeto Respuesta que contiene un HttpStatus, un mensaje y una lista de usuarios
     **/
    @GetMapping("/findAll")//*
    public ResponseEntity<?> findAll(){
        try{
            Iterable<User> listaUsuarios= userService.findAll();
            if (listaUsuarios!=null){
                return ResponseEntity.ok(new Response(HttpStatus.OK,"Lista de usuarios encontrada",listaUsuarios));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST,"Error al buscar los datos",null));
            }
        } catch (MongoSocketException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (MongoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, e.getMessage(), null));
        }
    }


    /**
     * Busca a un usuario por id
     * @param id
     * @return Objeto Respuesta que contiene un HttpStatus, un mensaje y un Objeto Usuario
     */
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable String id){
        Optional<User> user;
        try{
            user= userService.findById(id);
            if(user.isPresent()){
                return ResponseEntity.ok(new Response(HttpStatus.OK,"Usuario encontrado",user));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST,"Error usuario no existente",null));
            }
        } catch (MongoSocketException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (MongoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, e.getMessage(), null));
        }
    }

    @PostMapping("/validate")//*
    public ResponseEntity<?> userValidate(@RequestBody User infAcceso){
        try{
            if (infAcceso.getPassword()==null || infAcceso.getCorreo()==null || infAcceso.getToken()==null){
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE,"Error en las llaves",""));
            }else{
                String psw=GeneralService.cifrar(infAcceso.getPassword());
                Optional<User> user=userService.userValidate(infAcceso.getCorreo(),psw);
                if (user.isPresent()){
                    if(user.get().getStatusActivo().equals("true")){
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
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"Usuario no encontrado",""));
                }
            }
        } catch (MongoSocketException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (MongoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, e.getMessage(), null));
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
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"No se puede borrar",null));
            }else{
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"No se puede borrar",null));
            }
        } catch (MongoSocketException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (MongoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, e.getMessage(), null));
        }
        //userService.deleteById(id);

    }

    //actualiza el id del superior inmediato de un usuario.
    @PutMapping("/updateIdBoss")//*
    public ResponseEntity<?> updateIdBoss(@RequestBody BodyUpdateBoss updateBoss){
        try {
            //si no se rebició información no hace alguna acción
            if (updateBoss.getIDUsuarios()==null || updateBoss.getIDSuperiores()==null){
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE,"Error en las llaves",null));
            }else{
                String[] idUser = updateBoss.getIDUsuarios();
                String[] idBoss = updateBoss.getIDSuperiores();
                //actualiza el id del superior inmediato de cada usuario recibido
                for (int i = 0; i < idUser.length; i++) {
                    userService.updateIdBoss(idUser[i], idBoss[i]);
                    groupService.actualizaIdSuperior(idUser[i], idBoss[i]);
                }
                return ResponseEntity.ok(new Response(HttpStatus.OK, "Actualizacion de superior inmediato lista", ""));
            }
        //Manejo de excepciones
        } catch (MongoSocketException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (MongoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, e.getMessage(), null));
        }
    }

    //Actualiza información general del usuario
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody User userUpdate){
        try {
            //busca al usuario a actualizar
            Optional<User> user = userService.findById(userUpdate.getID());
            if(!user.isPresent()) {
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "No se encontró al usuario", null));
            }else {
                //Valida que la información recibida de correo, RFC, CURP, y número de empleado sean únicos en la base de datos
                //de lo contrario no actualiza.
                if(!user.get().getCorreo().equals(userUpdate.getCorreo()) && userService.buscaCorreoUsuario(userUpdate.getCorreo())){
                    return ResponseEntity.ok(new Response(HttpStatus.NOT_ACCEPTABLE, "Correo no válido", null));
                }else if(!user.get().getCurp().equals(userUpdate.getCurp()) && userService.buscaCURPUsuario(userUpdate.getCurp())){
                    return ResponseEntity.ok(new Response(HttpStatus.NOT_ACCEPTABLE, "CURP no válido", null));
                }else if(!user.get().getRFC().equals(userUpdate.getRFC()) && userService.buscaRFCUsuario(userUpdate.getRFC())){
                    return ResponseEntity.ok(new Response(HttpStatus.NOT_ACCEPTABLE, "RFC no válido", null));
                }else if(!user.get().getNumeroEmpleado().equals(userUpdate.getNumeroEmpleado()) && userService.buscaNoEmpleadoUsuario(userUpdate.getNumeroEmpleado())){
                    return ResponseEntity.ok(new Response(HttpStatus.NOT_ACCEPTABLE, "Número de empleado no válido", null));
                }else{
                    //si la contraseña es diferente la cifra y la guarda
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
        //Manejo de excepciones
        } catch (MongoSocketException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (MongoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, e.getMessage(), null));
        }

    }

    //Actualiza el rol de un usuario que pertenece a un grupo
    @PutMapping("/updateRol")
    public ResponseEntity<?> updateRol(@RequestBody BodyAddUserGroup bodyGroup){
        //se usan variables auxiliares
        boolean bandera = false;
        String idSuperior;
        String idGrupo;
        String nombreRol;
        try {
            //busca al usuario a actualizar y valida si la información es diferente a la que ya tiene para actualizar
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
                    return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "No se aceptan los cambios", null));
                }
            }else{
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "No se encontró usuario", null));
            }
        //manejo de excepciones
        } catch (MongoSocketException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (MongoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, e.getMessage(), null));
        }
    }

    //Busca a todos los subordinados de un usuario.
    @GetMapping("/findByBossId/{id}")
    public ResponseEntity<?> findByBossId(@PathVariable String id){
        try {
            Iterable<User> users = userService.findUserByBossId(id);
            if(((Collection<User>) users).size()>0) {
                return ResponseEntity.ok(new Response(HttpStatus.OK, "Usuarios encontrados", users));
            }
            else {
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "No se encontraron usuarios", ""));
            }
        //Manejo de excepciones
        } catch (MongoSocketException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (MongoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, e.getMessage(), null));
        }
    }

    @PostMapping("/existUser")//*
    public ResponseEntity<?> existUser(@RequestBody User user){
        try {
            if (user.getCorreo() == null || user.getCurp() == null || user.getRFC() == null || user.getNumeroEmpleado() == null) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE, "Error en las llaves", null));
            } else {
                boolean us = userService.findUsersByUniqueData(user.getCorreo(), user.getCurp(), user.getRFC(), user.getNumeroEmpleado());
                if (us) {
                    return ResponseEntity.ok(new Response(HttpStatus.OK, "El usuario existe", "true"));
                } else {
                    return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST, "Usuario no encontrado", "false"));
                }
            }
        } catch (MongoSocketException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (MongoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, e.getMessage(), null));
        }
    }

    @PutMapping("/reasigna")//*
    public ResponseEntity reasigna(@RequestBody BodyUpdateBoss body){
        try {
            if (body.getIDUsuarios() == null || body.getIDSuperiores() == null) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE, "Error en las llaves", null));
            } else {
                String[] Usuarios = body.getIDUsuarios();
                String[] Superiores = body.getIDSuperiores();

                for (int i = 0; i < Usuarios.length; i++) {
                }
                userService.reasignaSuperiores(Usuarios, Superiores);
                return ResponseEntity.ok(new Response(HttpStatus.OK, "Superiores Modificados", ""));

            }
        } catch (MongoSocketException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (MongoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND, e.getMessage(), null));
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
                return new Response(HttpStatus.NOT_ACCEPTABLE,"",null);
            }else{
                if (userService.busquedaUsuario(parametro).isPresent()){
                    return new Response(HttpStatus.OK, "Usuario(s) encontrado(s)",userService.busquedaUsuario(parametro).get());
                }else{
                    return new Response(HttpStatus.OK, "Usuario(s) no encontrado(s)",userService.busquedaUsuario(parametro).get());
                }
            }
        }catch (Exception e){
            return new Response(HttpStatus.NOT_FOUND,"",null);
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
                    return  new Response(HttpStatus.BAD_REQUEST,"Error al deslogear",null);
                }
            }else{
                return new Response(HttpStatus.BAD_REQUEST,"Usuario "+idUser+" no existe",null);
            }
        }catch (Exception e){
            return new Response(HttpStatus.NOT_FOUND,"Error al hacer la consulta",null);
        }
    }





}
