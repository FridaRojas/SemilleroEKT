package com.ekt.Servicios.repository;


import com.ekt.Servicios.entity.Group;
import com.ekt.Servicios.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,String> {

}
