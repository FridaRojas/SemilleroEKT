package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.Admin;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Optional<Admin> adminValidate(String correo, String password) {
            return  adminRepository.findByCorreoPassoword(correo,password);
    }
}
