package com.ekt.Servicios.repository;

import com.ekt.Servicios.entity.Mensajes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MensajesRepository extends MongoRepository<Mensajes, String> {

    @Query("'idConversacion' : ?0")
    Iterable<Mensajes> findByIdConversacion (String idConversacion);
}
