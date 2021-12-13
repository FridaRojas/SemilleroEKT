package com.ekt.Servicios.service;


import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.repository.GroupRepository;
import com.ekt.Servicios.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;



    @Override
    public Iterable<User> findAll() {
        return  userRepository.findAll();
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
    @Override
    public Boolean findUsersByUniqueData(String correo, String curp, String rfc, String empleado){

        boolean res=false;
        boolean us= buscaCorreoUsuario(correo);
        boolean us2= buscaCURPUsuario(curp);
        boolean us3= buscaRFCUsuario(rfc);
        boolean us4= buscaNoEmpleadoUsuario(empleado);
        if (us || us2 || us3 || us4){
            res=true;
        }
        return res;
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
    public User updateIdBoss(String idUser, String idBoss){
        //re
        User userUpdate = userRepository.findById(idUser).get();
        userUpdate.setIDSuperiorInmediato(idBoss);

        save(userUpdate);
        return userUpdate;
    }

    @Override
    public void deleteById(String id) {
        //User user
        userRepository.deleteById(id);
    }

    @Override
    public void reasignaSuperiores(String[] idUsuarios, String[] idSuperiores) {
        for(int i=0 ; i<idUsuarios.length ; i++){
            updateIdBoss(idUsuarios[i],idSuperiores[i]);
        }
    }

    @Override
    public User actualizaRol(User usuario, String idSuperior, String idGrupo, String nombreRol) {

        usuario.setIDGrupo(idGrupo);
        usuario.setIDSuperiorInmediato(idSuperior);
        usuario.setNombreRol(nombreRol);
        save(usuario);
        return usuario;
    }

    @Override
    public User actualizaUsuario(User userUpdate){
        Optional<User> user = userRepository.findById(userUpdate.getID());
        user.get().setCorreo(userUpdate.getCorreo());
        user.get().setFechaInicio(userUpdate.getFechaInicio());
        user.get().setFechaTermino(userUpdate.getFechaTermino());
        user.get().setNombre(userUpdate.getNombre());
        user.get().setNumeroEmpleado(userUpdate.getNumeroEmpleado());
        user.get().setPassword(userUpdate.getPassword());
        user.get().setTelefono(userUpdate.getTelefono());
        user.get().setCurp(userUpdate.getCurp());
        user.get().setRFC(userUpdate.getRFC());
        return userRepository.save(user.get());
    }

    @Override
    public boolean buscaCorreoUsuario(String correo) {
        return userRepository.findByCorreo(correo).isPresent();
    }

    @Override
    public boolean buscaCURPUsuario(String curp) {
        return userRepository.findByCURP(curp).isPresent();
    }

    @Override
    public boolean buscaRFCUsuario(String rfc) {
        return userRepository.findByRFC(rfc).isPresent();
    }

    @Override
    public boolean buscaNoEmpleadoUsuario(String noEmpleado) {
        return userRepository.findByNumeroEmpleado(noEmpleado).isPresent();
    }

    @Override
    public Optional<ArrayList<User>> findChilds(String idPadre) {
        return userRepository.findChilds(idPadre);
    }

    @Override
    public Optional<ArrayList<User>> busquedaUsuario(String parametro) {
        return userRepository.busquedaUsuario(parametro);
    }

    @Override
    public Optional<String> guardarTokenAuth(String id) {
        User usr = userRepository.findById(id).get();
        String ret = GeneralService.cifrar("");
        usr.setTokenAuth(ret);
        userRepository.save(usr);
        return Optional.ofNullable(ret);
    }


}
