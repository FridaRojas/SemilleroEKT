package com.ekt.Servicios.controller;



import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    public UserService userService;

    @PostMapping("/crear")
    public User crear(@Validated @RequestBody User user){

        return userService.save(user);
    }

    @GetMapping("/buscar/{id}")
    public Optional<User> buscar(@PathVariable String id){
        return userService.findById(id);

    }

    @GetMapping("/validate")
    public ResponseEntity<?> userValidate(@RequestBody User usuario){
        System.out.println("------"+usuario.getID()+"    "+usuario.getPassword());
            return userService.userValidate(usuario.getID(),usuario.getPassword());
    }

    @DeleteMapping(value="/delete/{id}")
    public void delete(@PathVariable String id){
        userService.deleteById(id);
    }

}
