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

        Group  resGroup = null;

        //validar que los ids existen
        Optional<Group> group = findById(idGrupo);
        Optional<User> user = userService.findById(idUser);
        Optional<User> superior = userService.findById(idSuperior);

        if(group.isPresent() && user.isPresent() && superior.isPresent()){
            //verificar que el usuario no existe en ningun grupo
            if( !userInGroup(idGrupo,idUser).isPresent()){
                //actualizar informacion usuario
                user.get().setIDSuperiorInmediato(idSuperior);
                user.get().setNombreRol(nombreRol);
                user.get().setIDGrupo(idGrupo);

                //añadir el usuario al grupo
                User[] lista = new User[group.get().getUsers().length+1];

                for(int i=0;i<group.get().getUsers().length;i++){
                    lista[i]=group.get().getUsers()[i];
                }

                lista[lista.length-1]=user.get();
                group.get().setUsers(lista);
                resGroup=groupRepository.save(group.get());
            }else{
                System.out.println("error el usuario ya existe en el grupo");
                resGroup=null;
            }
        }else{
            System.out.println("algun parametro no existe");
            resGroup=null;
        }
        return  resGroup;
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
