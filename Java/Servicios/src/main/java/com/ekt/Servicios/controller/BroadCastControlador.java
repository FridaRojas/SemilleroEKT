package com.ekt.Servicios.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ekt.Servicios.entity.*;
import com.ekt.Servicios.repository.BroadCastRepositorio;
import com.ekt.Servicios.service.BroadCastServicio;

import com.ekt.Servicios.service.BroadCastServicioImpl;
import com.ekt.Servicios.service.MensajesService;

import com.mongodb.MongoException;
import com.mongodb.MongoSocketOpenException;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ekt.Servicios.repository.UserRepository;


@RestController
@RequestMapping("/api/broadCast")
public class BroadCastControlador {

	@Autowired
	private UserRepository userRepository;

	BroadCastServicioImpl broadCastServicio = new BroadCastServicioImpl();
	@Autowired
	private BroadCastRepositorio broadCastRepositorio;

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private MensajesService mensajesService;

	@GetMapping("/listaUsuarios/{miId}")
	public ResponseEntity<?> listaUsuariosGeneral(@RequestHeader(value = "tokenAuth")String tokenAuth,
												  @PathVariable (value = "miId")String miId){
		try{
			Optional<User> existo = userRepository.validarUsuario(miId);
			if(existo.get().getTokenAuth()== null){
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),"Token no valido",""));
			}
			if(!existo.get().getTokenAuth().equals(tokenAuth)){
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),"Token no coincide",""));
			}
			if(!existo.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"No se encontro usuario Broadcast",""));
			}

			if(!existo.get().getNombreRol().equals("BROADCAST")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"No es un usuario BROADCAST",""));
			}

			Iterable<User> listaUsuarios =  userRepository.findByGroupID(existo.get().getIDGrupo());
			
			List<User> miLista = new ArrayList<>();
			for(User usuario : listaUsuarios) {
				if(usuario.getStatusActivo().equals("true") && !usuario.getID().equals(existo.get().getID())) {
					miLista.add(usuario);
				}
			}

			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBroadcast(String.valueOf(HttpStatus.ACCEPTED.value()),"Lista de usuarios",miLista));

		}catch (MongoSocketOpenException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseBroadcast(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseBroadcast(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseBroadcast(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		}
	}

	@GetMapping("/mostarMensajesdelBroadcast/{miId}")
	public ResponseEntity<?>listarMensajes(@RequestHeader(value = "tokenAuth")String tokenAuth,
										   @PathVariable (value = "miId")String miId){
		try {

			if(miId.length()<24 || miId.length()>24){
				ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El tamaño del id no es correcto", ""));
			}
			Optional<User> user = userRepository.validarUsuario(miId);
			if(!user.isPresent()) {
				ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()), "El usuario no existe",""));
			}
			if(user.get().getTokenAuth()== null){
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),"Token no valido",""));
			}
			if(!user.get().getTokenAuth().equals(tokenAuth)){
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),"Token no coincide",""));
			}if(user.isPresent()){


				if(user.get().getNombreRol().equals("BROADCAST") || user.get().getIDSuperiorInmediato().equals("-1")) {

					Iterable<BroadCast> brd = broadCastRepositorio.findAll();
					return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBroadcast(String.valueOf(HttpStatus.ACCEPTED.value()),"Lista de mensajes del Broadcast",brd));

				}
			}

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"El usuario ingresado no es un usuario BROADCAST", ""));
		}catch (MongoSocketOpenException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseBroadcast(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseBroadcast(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseBroadcast(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		}
	}

	@PostMapping("/crearMensajeBroadcast/{miID}")
	public ResponseEntity<?> crearMensajeBroadCast(@RequestHeader("tokenAuth")String tokenAuth,
												   @RequestBody BroadCast broadCast,
												   @PathVariable  (value = "miID")String miID) {
			try{

				if (broadCast.getIdEmisor() == null || broadCast.getAsunto() == null || broadCast.getDescripcion() == null) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()), "No se encuentra el dato", ""));
				}
				if (broadCast.getIdEmisor().equals("") || broadCast.getIdEmisor().equals("null")) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El campo idEmisor es no puede estar vacio", ""));
				}
				if (broadCast.getIdEmisor().length() < 24 || broadCast.getIdEmisor().length() > 24) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El tamaño del idEmisor no es valido", ""));
				}
				if (broadCast.getAsunto().equals("") || broadCast.getAsunto().equals("null")) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El campo Asunto no puede estar vacio", ""));
				}
				if (broadCast.getAsunto().length() < 5) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El tamaño del Asunto no es valido", ""));
				}
				if (broadCast.getDescripcion().length() < 10) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El tamaño del texto descripcion debe ser al menos de 1 caracter", ""));
				}

				BroadCast broadCastM = new BroadCast();
				broadCastM.setIdEmisor(broadCast.getIdEmisor());
				broadCastM.setAsunto(broadCast.getAsunto());
				broadCastM.setDescripcion(broadCast.getDescripcion());
				Optional<User> user = userRepository.findById(miID);

				Iterable<User> iter= userRepository.findByGroupID(user.get().getIDGrupo());
				List<User> lista= new ArrayList<>();
				for (User  usuario: iter) {
					if(!usuario.getStatusActivo().equals("false")&&usuario.getNombreRol().equals("BROADCAST")){
						lista.add(usuario);
					}
				}
				Optional<User> user2 = userRepository.validarUsuario(lista.get(0).getID());
				if(user.get().getTokenAuth()== null){
					return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),"Token no valido",""));
				}
				if(!user.get().getTokenAuth().equals(tokenAuth)) {
					return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), "Token no coincide", ""));
				}
				if(user2.get().getNombreRol().equals("BROADCAST") || user2.get().getIDSuperiorInmediato().equals("-1")){
					if (user.get().getID().equals(miID)) {

						broadCastM.setNombreEmisor(user.get().getNombre());
						broadCastM.setAtendido(false);
						broadCastRepositorio.save(broadCastM);
						broadCastServicio.notificacion2(broadCastM.getNombreEmisor() + " Envio un mensaje", broadCast.getAsunto(),user2.get().getToken());
						return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseBroadcast(String.valueOf(HttpStatus.CREATED.value()), "Se creo el Mensaje a BROADCAST", "Usuario: "+broadCastM.getNombreEmisor()));
					}
				}
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()), "No se encuentra el usuario BROADCAST", ""));
			}catch (MongoSocketOpenException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()), e.getMessage(), e.getCause()));
			} catch (MongoException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()), e.getMessage(), e.getCause()));
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()), e.getMessage(), e.getCause()));
			}

	}
	@GetMapping("/mostrarMensajesporID/{miID}/{idEmisor}")
	public ResponseEntity<?> mostrarMensajes(@RequestHeader(value = "tokenAuth")String tokenAuth,
											 @PathVariable (value  ="miID") String miID,
											 @PathVariable(value = "idEmisor")String idEmisor){
		try{
			if(idEmisor.length() < 24 || idEmisor.length() > 24){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El tamaño del id no es correcto", "" ));
			}
			Optional<User> user = userRepository.validarUsuario(idEmisor);
			if(!user.isPresent()){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()), "El Usuario no existe", "" ));
			}
			if(user.get().getTokenAuth()== null){
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),"Token no valido",""));
			}
			if(!user.get().getTokenAuth().equals(tokenAuth)) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), "Token no coincide", ""));
			}
			if(user.isPresent()){
				List<BroadCast> listBrd = new ArrayList<>();
				Iterable<BroadCast> brd = broadCastRepositorio.findAll();
				for (BroadCast brd2 : brd) {
					if(brd2.getIdEmisor().equals(idEmisor)){
						listBrd.add(brd2);
					}
				}
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseBroadcast(String.valueOf(HttpStatus.OK.value()),"Lista de Mensajes de: " + user.get().getNombre(),listBrd));
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"El usuario no tiene mensajes enviados ", "")));
		}catch (MongoSocketOpenException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseBroadcast(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseBroadcast(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseBroadcast(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		}

	}
	
	@PostMapping("/enviarMensaje")
	public ResponseEntity<?> enviarMensaje(@RequestHeader(value = "tokenAuth")String tokenAuth,
										   @RequestBody Mensajes mensajeEntrante){
		try{
			if(mensajeEntrante.getIDEmisor()==null || mensajeEntrante.getIDReceptor()==null || mensajeEntrante.getTexto()==null || mensajeEntrante.getFechaCreacion() ==null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Campos no validos",""));
			}

			if(mensajeEntrante.getIDEmisor().equals("") || mensajeEntrante.getIDEmisor().equals("null")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El campo idEmisor es no puede estar vacio",""));
			}
			if(mensajeEntrante.getIDEmisor().length()<24 || mensajeEntrante.getIDEmisor().length()>24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El tamaño del idEmisor no es valido",""));
			}

			if(mensajeEntrante.getIDReceptor().equals("") || mensajeEntrante.getIDReceptor().equals("null")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El campo idReceptor es no puede estar vacio",""));
			}
			if(mensajeEntrante.getIDReceptor().length()<24 || mensajeEntrante.getIDReceptor().length()>24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El tamaño del idReceptor no es valido",""));
			}

			if(mensajeEntrante.getTexto().equals("") || mensajeEntrante.getTexto().equals("null")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El campo texto es no puede estar vacio",""));
			}
			if(mensajeEntrante.getTexto().length()<1) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El tamaño del texto no es debe ser al menos de 1 caracter",""));
			}

			Optional<User> existo = userRepository.validarUsuario(mensajeEntrante.getIDEmisor());

			if(!existo.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"El usuario broadcast no fue encontrado",""));
			}

			if(!existo.get().getNombreRol().equals("BROADCAST")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"No es un usuario BROADCAST",""));
			}
			if(existo.get().getTokenAuth()== null){
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),"Token no valido",""));
			}
			if(!existo.get().getTokenAuth().equals(tokenAuth)) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), "Token no coincide", ""));
			}

			Iterable<User> listaUsuarios =  userRepository.findByGroupID(existo.get().getIDGrupo());

			boolean bandera = false;

			for(User usuarios : listaUsuarios) {
				if(mensajeEntrante.getIDReceptor().equals(usuarios.getID())) {
					bandera = true;
				}
			}

			if(!bandera) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"El usuario broadcast no puede mandar mensaje a la otra persona",""));
			}

			Mensajes mensaje = new Mensajes();

			mensaje.setIDConversacion(mensajeEntrante.getIDEmisor()+"_"+mensajeEntrante.getIDReceptor());
			mensaje.setConversacionVisible(true);
			mensaje.setIDEmisor(mensajeEntrante.getIDEmisor());
			mensaje.setIDReceptor(mensajeEntrante.getIDReceptor());
			mensaje.setTexto(mensajeEntrante.getTexto());
			mensaje.setVisible(false);
			mensaje.setRutaDocumento(null);
			mensaje.setNombreConversacionReceptor("Conversacion BroadCast");

			mensaje.setFechaCreacion(mensajeEntrante.getFechaCreacion());
			mensaje.setStatusCreado(true);

			mensaje.setFechaEnviado(new Date());
			mensaje.setStatusEnviado(true);

			mensaje.setFechaLeido(new Date(0));
			mensaje.setStatusLeido(false);

			mensajesService.crearMensaje(mensaje);

			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBroadcast(String.valueOf(HttpStatus.CREATED.value()),"Mensaje enviado",mensaje.getIDConversacion()));
		}catch (MongoSocketOpenException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseBroadcast(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseBroadcast(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseBroadcast(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		}

	}

	@PutMapping("/actualizarAtendido/{miID}/{miIDmensaje}")
	public ResponseEntity<?> actualizarAtendido(@RequestHeader(value = "tokenAuth")String tokenAuth,
												@PathVariable (value = "miID")String miID,
												@PathVariable (value = "miIDmensaje")String miIDmensaje){
try{
	if(miIDmensaje.length()<24 ||miIDmensaje.length()>24){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El tamaño del id no es valido", ""));
	}
	Optional<BroadCast> opt = broadCastRepositorio.findById(miIDmensaje);
	Optional<User> user = userRepository.validarUsuario(miID);
	if(!opt.isPresent()){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"El mensaje a atrender no existe",""));
	}
	if(user.get().getTokenAuth()== null){
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),"Token no valido",""));
	}
	if(!user.get().getTokenAuth().equals(tokenAuth)) {
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), "Token no coincide", ""));
	}
	opt.get().setAtendido(true);
	broadCastRepositorio.save(opt.get());

	return ResponseEntity.status(HttpStatus.OK).body(new ResponseBroadcast(String.valueOf(HttpStatus.OK.value()),"Se atendio el mesnaje de Usuario: " + opt.get().getNombreEmisor(),""));

}catch (MongoSocketOpenException e) {
	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ResponseBroadcast(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
} catch (MongoException e) {
	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ResponseBroadcast(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
} catch (Exception e) {
	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ResponseBroadcast(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
}
	}
}

