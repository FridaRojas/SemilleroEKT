package com.ekt.Servicios.service;



import com.ekt.Servicios.entity.Admin;

import java.util.Optional;

public interface AdminService {

    Optional<Admin> adminValidate(String id, String password);
}
