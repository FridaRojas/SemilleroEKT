package com.ekt.Servicios.controller;



import com.ekt.Servicios.entity.Response;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> userValidate(@RequestBody User infAcceso){
        Response res = new Response();
        if (infAcceso.getPassword()==null || infAcceso.getCorreo()==null){
            System.out.println("Error en las llaves");
            res.setStatus(HttpStatus.NOT_ACCEPTABLE);
            res.setMsj("Error en las llaves");
            res.setData("");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(res);
        }else{
            Optional<User> user=userService.userValidate(infAcceso.getCorreo(),infAcceso.getPassword());
            if (user.isPresent()){
                System.out.println("Usuario encontrado");
                res.setStatus(HttpStatus.NOT_ACCEPTABLE);
                res.setMsj("Usuario encontrado");
                res.setData(user.get());
                return ResponseEntity.ok(res);
            }else{
            System.out.println("Usuario no encontrado");
            res.setStatus(HttpStatus.BAD_REQUEST);
            res.setMsj("Usuario no encontrado");
            res.setData("");
            return ResponseEntity.ok(res);}
        }
    }

    @DeleteMapping(value="/delete/{id}")
    public void delete(@PathVariable String id){
        userService.deleteById(id);
    }

    /*
    @PutMapping("/updateIdPadre/{idPadre}")
    public String updateIdPadre(@RequestBody User userUpdate, @PathVariable String idPadre){
        System.out.println("idPadre:"+idPadre);
        userService.updateIdPadre(userUpdate,idPadre);
        return "Ok";
    }
*/

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
            user.get().setTelefono(userUpdate.getTelefono());
            userService.save(user.get());
            return "OK";
        }
    }

    /*
    @GetMapping("/findByBossId/{id}")
    public void findByBossId(@PathVariable String id){
        userService.findUserByBossId(id);
    }
    */

    @GetMapping("/existUser/{correo}")
    public boolean existUser(@PathVariable String correo){
        return userService.findUsersByCorreo(correo).isPresent();
    }

}
