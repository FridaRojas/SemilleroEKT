package com.ekt.Servicios.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ekt.Servicios.entity.BroadCast;

public interface BroadCastRepositorio extends MongoRepository<BroadCast, String> {
}
