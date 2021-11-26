package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.BroadCast;
import com.ekt.Servicios.repository.BroadCastRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class BroadCastServicioImpl implements BroadCastServicio {

    @Autowired
    private BroadCastRepositorio broadCastRepositorio;

    @Override
    @Transactional
    public BroadCast save(BroadCast broadCast) {
        return broadCastRepositorio.save(broadCast) ;
    }
}
