package com.ekt.Servicios.repository;

import com.ekt.Servicios.entity.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends MongoRepository<Group,Integer> {

}
