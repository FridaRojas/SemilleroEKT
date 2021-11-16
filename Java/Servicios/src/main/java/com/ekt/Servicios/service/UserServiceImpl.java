package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;


    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
    @Override
    public Optional<User> findUsersByCorreo(String correo){

        return userRepository.findUsersByCorreo(correo);
    }

    @Override
    public Optional<User> userValidate(String id, String password){

        return  userRepository.findByIdPassoword(id,password);
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
        userRepository.deleteById(id);
    }

}
