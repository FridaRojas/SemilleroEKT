package com.ekt.Servicios.controller;


import com.ekt.Servicios.entity.Group;
import com.ekt.Servicios.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/group/")
public class GroupController {
    @Autowired
    public GroupService groupService;

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody Group group){
        Group obj= groupService.save(group);
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.save(group));
    }


    @GetMapping("/buscar/{id}")
    public Optional<Group> buscar(@PathVariable String id){
        return groupService.findById(id);
    }

    @DeleteMapping(value="/delete/{id}")
    public void delete(@PathVariable String id){


        System.out.println(id);
        groupService.deleteById(id);
    }


}
