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
    public Iterable<Group> buscarTodo() {
        try {
            return groupRepository.findAll();
        }catch (Exception e){
            System.err.println("Excepcion: "+e);
            return null;
        }
    }

    @Override
    public Page<Group> buscarTodo(Pageable pageable) {
        try {
            return groupRepository.findAll(pageable);
        }catch (Exception e){
            System.err.println("Excepcion: "+e);
            return null;
        }
    }

    @Override
    public Optional<Group> buscarPorId(String id) {


        return groupRepository.findById(id);
    }

    @Override
    public Group guardar(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Group guardarUsuario( String idUser, String idGrupo, String idSuperior,String nombreRol){

        Group  resGroup = null;

        //validar que los ids existen
        Optional<Group> group = buscarPorId(idGrupo);
        Optional<User> user = userService.findById(idUser);
        Optional<User> superior = userService.findById(idSuperior);

        if(group.isPresent() && user.isPresent() && superior.isPresent()){
            //verificar que el usuario no existe en ningun grupo
            if( !buscarUsuarioEnGrupo(idGrupo,idUser).isPresent()){
                //actualizar informacion usuario
                user.get().setIDSuperiorInmediato(idSuperior);
                user.get().setNombreRol(nombreRol);
                user.get().setIDGrupo(idGrupo);

                //añadir el usuario al grupo
                User[] lista = new User[group.get().getUsuarios().length+1];

                for(int i=0;i<group.get().getUsuarios().length;i++){
                    lista[i]=group.get().getUsuarios()[i];
                }

                lista[lista.length-1]=user.get();
                group.get().setUsuarios(lista);
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
    public void borrarPorId(String id) {groupRepository.deleteById(id);

    }
    @Override
    public void borrarUsuarioDeGrupo( String idUser, String idGroup){
        System.out.println("idUser:"+idUser+" idGroup:"+idGroup);

        //buscar el grupo
        Optional<Group> group = buscarPorId(idGroup);

        //obtiene la lista de usuarios que tiene el grupo

        User[] lista = group.get().getUsuarios();
        User[] lista2 = new User[group.get().getUsuarios().length-1];

        for(int i=0;i<group.get().getUsuarios().length;i++){
            //si el idUsuario que manda no es igual, lo copia
            if (! idUser.equals(lista[i].getID())){
                lista2[i]=lista[i];
            }
        }

        group.get().setUsuarios(lista2);

        groupRepository.save(group.get());
    }

    @Override
    public Optional<Group> buscarUsuarioEnGrupo(String id, String user){
        return  groupRepository.buscarUsuarioEnGrupo(id,user);
    }


    @Override
    public Optional<Group> buscarPorNombre(String nombre) {
        return groupRepository.buscarPorNombre(nombre);
    }

    @Override
    public Group actualizaNombre(String idGrupo, String nombreGrupo){
        Optional<Group> grupo = groupRepository.findById(idGrupo);
        grupo.get().setNombre(nombreGrupo);
        return groupRepository.save(grupo.get());
    }

    @Override
    public boolean actualizaUsuario(User usuario){
        Optional<Group> grupo = groupRepository.buscarUsuarioEnGrupo(usuario.getIDGrupo(),usuario.getID());
        User []usuarios = grupo.get().getUsuarios();
        boolean bandera = false;
        for(User user:usuarios){
            if(user.getID().equals(usuario.getID())){
                user.setCorreo(usuario.getCorreo());
                user.setFechaInicio(usuario.getFechaInicio());
                user.setFechaTermino(usuario.getFechaTermino());
                user.setNombre(usuario.getNombre());
                user.setPassword(usuario.getPassword());
                user.setRFC(usuario.getRFC());
                user.setCurp(usuario.getCurp());
                user.setNumeroEmpleado(user.getNumeroEmpleado());
                user.setTelefono(usuario.getTelefono());
                bandera = true;
            }
        }
        if(bandera){
            grupo.get().setUsuarios(usuarios);
            groupRepository.save(grupo.get());
            return true;
        }else {
            return false;
        }
    }

}
