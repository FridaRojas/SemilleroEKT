package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;



    @Override
    public Iterable<User> findAll() {
        return  userRepository.findAll();
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
    @Override
    public Optional<User> findUsersByUniqueData(String correo, String curp, String rfc, String empleado){

        return userRepository.findUsersByUniqueData(correo, curp,rfc,empleado);
    }

    @Override
    public Optional<User> userValidate(String correo, String password){

        return  userRepository.findByCorreoPassoword(correo,password);
    }

    @Override
    public Iterable<User> findUserByBossId(String id){
        Iterable<User> users;
        users = userRepository.findByBossId(id);
        return users;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateIdPadre(User userUpdate, String idPadre){
        userUpdate.setIDSuperiorInmediato(idPadre);
        save(userUpdate);
        return  userUpdate;
    }

    @Override
    public void deleteById(String id) {
        //User user
        userRepository.deleteById(id);
    }



}
