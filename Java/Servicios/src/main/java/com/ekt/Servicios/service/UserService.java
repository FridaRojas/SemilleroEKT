package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.User;

import java.util.Optional;

public interface UserService {

     Iterable<User> findAll();

     Optional<User> findById(String id);

     Optional<User> findUsersByUniqueData(String correo, String curp, String rfc, String empleado);

     Optional<User> userValidate(String id, String password);
    Iterable<User> findUserByBossId(String id);
    User updateIdBoss(String idUser,String idBoss);

     User save(User user);

     void deleteById(String id);

     void reasignaSuperiores(String[] idUsuarios, String[] idSuperiores);




}
