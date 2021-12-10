package com.ekt.Servicios.service;


<<<<<<< HEAD
import com.ekt.Servicios.entity.Response;
=======
>>>>>>> 1af49fd3a12c50a4e22480c930b409d10b1f5f5c
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.repository.GroupRepository;
import com.ekt.Servicios.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

<<<<<<< HEAD
import org.springframework.http.HttpStatus;
=======
>>>>>>> 1af49fd3a12c50a4e22480c930b409d10b1f5f5c
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

<<<<<<< HEAD
    @Autowired
    private GroupRepository groupRepository;

=======
>>>>>>> 1af49fd3a12c50a4e22480c930b409d10b1f5f5c


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

<<<<<<< HEAD
    @Override
    public Optional<ArrayList<User>> busquedaUsuario(String parametro) {
        return userRepository.busquedaUsuario(parametro);
    }

    @Override
    public Optional<String> guardarTokenAuth(String id) {
        User usr = userRepository.findById(id).get();
        String ret = cifrar("");
        usr.setTokenAuth(ret);
        userRepository.save(usr);
        return Optional.ofNullable(ret);
    }




    /**
     * Recibe un string.
     * Si el string esta vacio crea un sha aleatorio.
     * Si recibe un string con caracteres regresa un sha256 de dicho string.
     * @return
     */
    public String cifrar(String param){
        try {

            if (param.length()>0){

                System.out.println("Se va a cifrar: "+param);
            }else{
                System.out.println("Se va a crear un sha aleatorio");
                for (int i=0;i<20;i++){
                    param+=String.valueOf(Math.random());
                }
            }

            System.out.println("wea a cifrar: "+param);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            BigInteger number = new BigInteger(1, md.digest(param.getBytes(StandardCharsets.UTF_8)));
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 32){
                hexString.insert(0, '0');
            }
            return hexString.toString();
        }catch (Exception e){
            return null;
        }
    }
=======
>>>>>>> 1af49fd3a12c50a4e22480c930b409d10b1f5f5c
}
