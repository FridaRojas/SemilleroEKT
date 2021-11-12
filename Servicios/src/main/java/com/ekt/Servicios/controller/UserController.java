package com.ekt.Servicios.controller;



import com.ekt.Servicios.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    @Autowired
    public UserService userService;






    @DeleteMapping(value="/{id}")
    public void delete(@PathVariable int id){
        userService.deleteById(id);
    }

}
