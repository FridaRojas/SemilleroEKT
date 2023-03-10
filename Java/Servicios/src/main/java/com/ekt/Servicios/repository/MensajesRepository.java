package com.ekt.Servicios.repository;

import com.ekt.Servicios.entity.Mensajes;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MensajesRepository extends MongoRepository<Mensajes, String> {

    @Query("{'idConversacion' : ?0}")
    Iterable<Mensajes> findByIdConversacion (String idConversacion);
    
    @Query("{'_id' : ?0}")
    Optional<Mensajes> buscarMensaje (String idMensaje);

    @Query("{'idEmisor': ?0 }")
    Iterable<Mensajes> buscarMensajes(String idEmisor);

    @Query("'idReceptor': ?0")
    Iterable<Mensajes> buscarMensajes1(String idEmisor);
}
