package com.ekt.Servicios.repository;

import com.ekt.Servicios.entity.Group;
import com.ekt.Servicios.entity.Mensajes;
import com.ekt.Servicios.entity.User;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ekt.Servicios.entity.BroadCast;
import org.springframework.data.mongodb.repository.Query;

import java.io.IOException;
import java.util.Optional;

public interface BroadCastRepositorio extends MongoRepository<BroadCast, String> {
    @Query(value= "{'idGrupo': ?0}")
    Iterable<BroadCast> buscarMensajesEnGrupo (String idGrupo);
    @Query(value= "{'idEmisor': ?0}")
    Iterable<BroadCast> buscarMensajesId(String idEmisor);
}
