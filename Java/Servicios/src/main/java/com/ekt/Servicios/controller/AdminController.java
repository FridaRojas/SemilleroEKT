package com.ekt.Servicios.controller;

import com.ekt.Servicios.entity.Admin;
import com.ekt.Servicios.entity.Response;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    AdminService adminService;



    @PostMapping("/validate")
    public ResponseEntity<?> userValidate(@RequestBody Admin infAcceso){
        if (infAcceso.getPassword()==null || infAcceso.getCorreo()==null){
            System.out.println("Error en las llaves");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE,"Error en las llaves",""));
        }else{
            System.out.println(infAcceso.getCorreo()+"   "+infAcceso.getPassword());
            Optional<Admin> admin=adminService.adminValidate(infAcceso.getCorreo(),infAcceso.getPassword());
            if (admin.isPresent()){
                System.out.println("Login: Administrador encontrado");

                return ResponseEntity.ok(new Response(HttpStatus.ACCEPTED,"Usuario encontrado",admin));
            }else{
                System.out.println("Login: Administrador no encontrado");
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"Administrador no encontrado",""));}
        }
    }
}
