package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.Group;
import com.ekt.Servicios.entity.Rol;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
