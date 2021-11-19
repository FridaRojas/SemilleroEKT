package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.Group;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService{
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService ;

    @Override
    public Iterable<Group> findAll() {
        return null;
    }

    @Override
    public Page<Group> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Group> findById(String id) {
        return groupRepository.findById(id);
    }

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Group saveUser( String idUser, String idGrupo, String idSuperior,String nombreRol){

//queda pendiente validacionde existencia y mandar a llamar la actualizacion de roles en coleccion de usuarios

        //buscar el grupo
        Optional<Group> group = findById(idGrupo);

        //verificar que el usuario no existe en ningun grupo


        //busca usuario y actualizo informacion
        Optional<User> user = userService.findById(idUser);
        System.out.println("correo:"+user.get().getCorreo());

        //actualizar informacion usuario
        user.get().setIDSuperiorInmediato(idSuperior);


        user.get().setNombreRol(nombreRol);
        user.get().setIDGrupo(idGrupo);

        //añadir el usuario al grupo

        System.out.println("leng"+group.get().getUsers().length);
        User[] lista = new User[group.get().getUsers().length+1];

        for(int i=0;i<group.get().getUsers().length;i++){
            lista[i]=group.get().getUsers()[i];
        }

         lista[lista.length-1]=user.get();

        group.get().setUsers(lista);

        return  groupRepository.save(group.get());
    }

    @Override
    public void deleteById(String id) {groupRepository.deleteById(id);

    }
    @Override
    public void deleteUserFromGroup( String idUser, String idGroup){
        System.out.println("idUser:"+idUser+" idGroup:"+idGroup);

        //buscar el grupo
        Optional<Group> group = findById(idGroup);

        //obtiene la lista de usuarios que tiene el grupo

        User[] lista = group.get().getUsers();
        User[] lista2 = new User[group.get().getUsers().length-1];

        for(int i=0;i<group.get().getUsers().length;i++){
            //si el idUsuario que manda no es igual, lo copia
            if (! idUser.equals(lista[i].getID())){
                lista2[i]=lista[i];
            }
        }

        group.get().setUsers(lista2);

        groupRepository.save(group.get());
    }

    @Override
    public Optional<Group> userInGroup(String id, String user){
        return  groupRepository.findByIdUser(id,user);
    }



}
