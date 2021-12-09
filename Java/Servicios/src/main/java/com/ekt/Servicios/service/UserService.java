package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.User;

import java.util.ArrayList;
import java.util.Optional;

public interface UserService {

     Iterable<User> findAll();

     Optional<User> findById(String id);

     Boolean findUsersByUniqueData(String correo, String curp, String rfc, String empleado);

     Optional<User> userValidate(String id, String password);
    Iterable<User> findUserByBossId(String id);
    User updateIdBoss(String idUser,String idBoss);
<<<<<<< HEAD

    User save(User user);

    void deleteById(String id);
=======
>>>>>>> 1af49fd3a12c50a4e22480c930b409d10b1f5f5c

    void reasignaSuperiores(String[] idUsuarios, String[] idSuperiores);

    User actualizaRol(User usuario, String idSuperior, String idGrupo, String nombreRol);
    User actualizaUsuario(User usuario);

<<<<<<< HEAD
    boolean buscaCorreoUsuario(String correo);
    boolean buscaCURPUsuario(String curp);
    boolean buscaRFCUsuario(String rfc);
    boolean buscaNoEmpleadoUsuario(String noEmpleado);
    Optional<ArrayList<User>> findChilds(String idPadre);

    Optional<ArrayList<User>> busquedaUsuario(String parametro);
    Optional<String> guardarTokenAuth(String id);
    String cifrar(String pass);
=======
     void reasignaSuperiores(String[] idUsuarios, String[] idSuperiores);

     User actualizaRol(User usuario, String idSuperior, String idGrupo, String nombreRol);
     User actualizaUsuario(User usuario);

     boolean buscaCorreoUsuario(String correo);
     boolean buscaCURPUsuario(String curp);
     boolean buscaRFCUsuario(String rfc);
     boolean buscaNoEmpleadoUsuario(String noEmpleado);
        Optional<ArrayList<User>> findChilds(String idPadre);
>>>>>>> 1af49fd3a12c50a4e22480c930b409d10b1f5f5c
}
