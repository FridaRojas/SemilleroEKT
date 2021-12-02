package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.Group;
import com.ekt.Servicios.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface GroupService {
    Iterable<Group> buscarTodo();

    Page<Group> buscarTodo(Pageable pageable);

    Optional<Group> buscarPorId(String id);

    Group guardar(Group group);

    Group guardarUsuario(String idUser, String idGrupo, String idSuperior,String nombreRol);

    boolean actualizaUsuario(User usuario);
    Group actualizaNombre(String idGrupo, String nombreGrupo);
    void borrarPorId(String id);


    void borrarUsuarioDeGrupo( String idUser, String idGroup);

    Optional<Group> buscarUsuarioEnGrupo(String id, String user);

    Optional<Group> buscarPorNombre(String nombre);

    void actualizaIdSuperior(String idUser, String idSuperior);

}
