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

        if(group.isPresent() && user.isPresent()){
            if (!superior.isPresent()){
                if (idSuperior.equals("")||idSuperior.equals("-1")){
                    //traza
                    System.out.println("este chico es jefe o brodcats");
                }else{
                    System.out.println("error en el idSuperior");
                    resGroup=null;
                }
            }
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
    public void borrarPorId(String id) {
        groupRepository.deleteById(id);
    }

    @Override
    public void borrarUsuarioDeGrupo(String idUser, String idGroup){
        System.out.println("idUser:"+idUser+" idGroup:"+idGroup);

        //buscar el grupo
        Optional<Group> group = buscarPorId(idGroup);

        //obtiene la lista de usuarios que tiene el grupo

        User[] lista = group.get().getUsuarios();
        User[] lista2 = new User[group.get().getUsuarios().length-1];
        int j=0;

        for(int i=0;i<group.get().getUsuarios().length;i++){
            //si el idUsuario que manda no es igual, lo copia
            if (! idUser.equals(lista[i].getID())){
                lista2[j]=lista[i];
                j++;
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
        boolean bandera = false;
        User []usuarios =null;
        System.out.println("usuario.... "+usuario.getNombreRol());
        System.out.println(usuario.getIDGrupo() + " --- " + usuario.getID());
        Optional<Group> grupo = groupRepository.buscarUsuarioEnGrupo(usuario.getIDGrupo(),usuario.getID());
        if (grupo.isPresent()){
            System.out.println(grupo.get().getNombre());
            usuarios = grupo.get().getUsuarios();
            for(User user:usuarios){
                if(user.getID().equals(usuario.getID())){
                    System.out.println(user.getNombre());
                    user.setCorreo(usuario.getCorreo());
                    user.setNombreRol(usuario.getNombreRol());
                    user.setIDSuperiorInmediato(usuario.getIDSuperiorInmediato());
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
        }
        if(bandera){
            grupo.get().setUsuarios(usuarios);
            groupRepository.save(grupo.get());
            return true;
        }else {
            System.out.println("Error en actualizar en grupo");
            return false;
        }
    }

    @Override
    public void actualizaIdSuperior(String idUser, String idSuperior) {
        boolean bandera = false;
        User[] usuarios = null;
        Optional<User> user = userService.findById(idUser);
        Optional<Group> grupo = groupRepository.buscarUsuarioEnGrupo(user.get().getIDGrupo(), idUser);
        if(grupo.isPresent()){
            usuarios = grupo.get().getUsuarios();
            for (User usuario : usuarios) {
                if (usuario.getID().equals(user.get().getID())) {
                    usuario.setIDSuperiorInmediato(idSuperior);
                    bandera = true;
                }
            }
        }
        if(bandera){
            grupo.get().setUsuarios(usuarios);
            groupRepository.save(grupo.get());
        }
    }
}
