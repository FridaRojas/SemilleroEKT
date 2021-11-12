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

    @PostMapping("/create")
    public User create(@Validated @RequestBody User user){

        return userService.save(user);
    }

    @GetMapping("/find/{id}")
    public Optional<User> findById(@PathVariable String id){
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

    @PutMapping("/update/{id}")
    public String update(@RequestBody User userUpdate, @PathVariable String id){
        Optional<User> user = userService.findById(id);
        if(!user.isPresent()) {
            return "Not Found";
        }else {
            user.get().setCorreo(userUpdate.getCorreo());
            user.get().setFechaInicio(userUpdate.getFechaInicio());
            user.get().setFechaTermino(userUpdate.getFechaTermino());
            user.get().setNombre(userUpdate.getNombre());
            user.get().setPassword(userUpdate.getPassword());
            user.get().setRoles(userUpdate.getRoles());
            user.get().setTelefono(userUpdate.getTelefono());
            user.get().setIDSuperiorInmediato(userUpdate.getIDSuperiorInmediato());
            user.get().setStatus(userUpdate.getStatus());
            userService.save(user.get());
            return "OK";
        }

    }
}
