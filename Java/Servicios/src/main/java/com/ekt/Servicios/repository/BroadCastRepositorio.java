package com.ekt.Servicios.repository;

import com.ekt.Servicios.entity.Mensajes;
import com.ekt.Servicios.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ekt.Servicios.entity.BroadCast;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface BroadCastRepositorio extends MongoRepository<BroadCast, String> {


}
