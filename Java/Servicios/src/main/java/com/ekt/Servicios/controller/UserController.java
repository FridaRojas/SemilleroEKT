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

        if (infAcceso.getPassword()==null || infAcceso.getCorreo()==null){
            System.out.println("Error en las llaves");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE,"Error en las llaves",""));
        }else{
            Optional<User> user=userService.userValidate(infAcceso.getCorreo(),infAcceso.getPassword());
            if (user.isPresent()){
                System.out.println("Usuario encontrado");
                return ResponseEntity.ok(new Response(HttpStatus.NOT_ACCEPTABLE,"Usuario encontrado",user.get()));
            }else{
            System.out.println("Usuario no encontrado");
            return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"Usuario no encontrado",""));}
        }
    }

    @DeleteMapping(value="/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        try{
            if(userService.findById(id).isPresent()){
                User u = userService.findById(id).get();
                if(u.getStatusActivo().equals("true")){
                    u.setStatusActivo("false");
                    userService.save(u);
                    return ResponseEntity.ok(new Response(HttpStatus.OK,"Usuario eliminado correctamente",""));
                }
                return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"No se puede borrar",""));
            }
        }catch(Exception e){
            System.err.println("Error: "+e);
            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND,"Usuario no encontrado",""));
        }
        //userService.deleteById(id);
        return ResponseEntity.ok(new Response(HttpStatus.BAD_REQUEST,"error desconocido",""));
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
