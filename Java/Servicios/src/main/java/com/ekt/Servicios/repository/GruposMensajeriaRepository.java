package com.ekt.Servicios.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.ekt.Servicios.entity.GruposMensajeria;

public interface GruposMensajeriaRepository extends MongoRepository<GruposMensajeria, String>{
	
	@Query("{'nombreConversacion' : ?0}")
    Iterable<GruposMensajeria> buscarPorNombre(String nombreGrupo);
	
	@Query("{'nombreConversacion' : ?0}")
    Optional<GruposMensajeria> buscarPorNombreOptional(String nombreGrupo); 
}
