package com.ekt.Servicios.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ekt.Servicios.entity.*;
import com.ekt.Servicios.repository.BroadCastRepositorio;
import com.ekt.Servicios.service.BroadCastServicio;

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
	@Autowired(required = false)
	private BroadCastServicio broadCastServicio;
	@Autowired
	private BroadCastRepositorio broadCastRepositorio;

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private MensajesService mensajesService;

	@GetMapping("/listaUsuarios/{miId}")
	public ResponseEntity<?> listaUsuariosGeneral(@PathVariable (value = "miId")String miId){
		try{
			Optional<User> existo = userRepository.validarUsuario(miId);

			if(!existo.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"No se encontro usuario Broadcast",""));
			}

			if(!existo.get().getNombreRol().equals("BROADCAST")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"No es un usuario BROADCAST",""));
			}

			Iterable<User> listaUsuarios =  userRepository.findByGroupID(existo.get().getIDGrupo());

			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBroadcast(String.valueOf(HttpStatus.ACCEPTED.value()),"Lista de usuarios",listaUsuarios));

		}catch (MongoSocketOpenException e) {
			return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
					.body(new Response(HttpStatus.REQUEST_TIMEOUT, e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Response(HttpStatus.BAD_REQUEST, e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new Response(HttpStatus.NOT_FOUND, e.getMessage(), e.getCause()));
		}

	}

	@GetMapping("/mostarMensajesdelBroadcast/{miId}")
	public ResponseEntity<?>listarMensajes(@PathVariable (value = "miId")String miId){
		try {
			if(miId.length()<24 || miId.length()>24){
				ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El tamaño del id no es correcto", ""));
			}
			Optional<User> user = userRepository.validarUsuario(miId);
			if(!user.isPresent()) {
				ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()), "El usuario no existe",""));
			}
			if(user.isPresent()){

				if(user.get().getNombreRol().equals("BROADCAST")) {

					Iterable<BroadCast> brd = broadCastRepositorio.findAll();
					return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBroadcast(String.valueOf(HttpStatus.ACCEPTED.value()),"Lista de mensajes del Broadcast",brd));

				}
			}

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"El usuario ingresado no es un usuario BROADCAST", ""));
		}catch (MongoSocketOpenException e) {
			return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
					.body(new Response(HttpStatus.REQUEST_TIMEOUT, e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Response(HttpStatus.BAD_REQUEST, e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new Response(HttpStatus.NOT_FOUND, e.getMessage(), e.getCause()));
		}

	}

	@PostMapping("/crearMensajeBroadcast/{miId}")
	public ResponseEntity<?> crearMensajeBroadCast(@RequestBody BroadCast broadCast, @PathVariable(value = "miId")String miId) {
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
				Optional<User> user = userRepository.findById(broadCast.getIdEmisor());
				Optional<User> user2 = userRepository.validarUsuario(miId);
				if(user2.get().getNombreRol().equals("BROADCAST") || user2.get().getIDSuperiorInmediato().equals("-1")){
					if (user.isPresent()) {
						broadCastM.setNombreEmisor(user.get().getNombre());
						broadCastM.setAtendido(false);
						broadCastRepositorio.save(broadCastM);
						notificacion2(broadCastM.getNombreEmisor() + " Envio un mensaje", broadCast.getAsunto(),user2.get().getToken());
						return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
					}
				}
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()), "No se encuentra el emisor", ""));
			}catch (MongoSocketOpenException e) {
				return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
						.body(new Response(HttpStatus.REQUEST_TIMEOUT, e.getMessage(), e.getCause()));
			} catch (MongoException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new Response(HttpStatus.BAD_REQUEST, e.getMessage(), e.getCause()));
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new Response(HttpStatus.NOT_FOUND, e.getMessage(), e.getCause()));
			}

	}
	@GetMapping("/mostrarMensajesporID/{idEmisor}")
	public ResponseEntity<?> mostrarMensajes(@PathVariable(value = "idEmisor")String idEmisor){
		try{
			if(idEmisor.length() < 24 || idEmisor.length() > 24){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El tamaño del id no es correcto", "" ));
			}
			Optional<User> user = userRepository.validarUsuario(idEmisor);
			if(!user.isPresent()){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()), "El Usuario no existe", "" ));
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
			return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
					.body(new Response(HttpStatus.REQUEST_TIMEOUT, e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Response(HttpStatus.BAD_REQUEST, e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new Response(HttpStatus.NOT_FOUND, e.getMessage(), e.getCause()));
		}

	}
	
	@PostMapping("/enviarMensaje")
	public ResponseEntity<?> enviarMensaje(@RequestBody Mensajes mensajeEntrante){
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
			return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
					.body(new Response(HttpStatus.REQUEST_TIMEOUT, e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Response(HttpStatus.BAD_REQUEST, e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new Response(HttpStatus.NOT_FOUND, e.getMessage(), e.getCause()));
		}

	}

	@PutMapping("/actualizarAtendido/{miID}")
	public ResponseEntity<?> actualizarAtendido(@PathVariable (value = "miID")String miID){
try{

	Optional<BroadCast> opt = broadCastRepositorio.findById(miID);
	opt.get().setAtendido(true);
	broadCastRepositorio.save(opt.get());

	return ResponseEntity.status(HttpStatus.OK).body(new ResponseBroadcast(String.valueOf(HttpStatus.OK.value()),"Se atendio el mesnaje de Usuario: " + opt.get().getNombreEmisor(),""));

}catch (MongoSocketOpenException e) {
	return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
			.body(new Response(HttpStatus.REQUEST_TIMEOUT, e.getMessage(), e.getCause()));
} catch (MongoException e) {
	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new Response(HttpStatus.BAD_REQUEST, e.getMessage(), e.getCause()));
} catch (Exception e) {
	return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new Response(HttpStatus.NOT_FOUND, e.getMessage(), e.getCause()));
}


	}


	public void notificacion2(String title, String asunto, String token){
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{\n    \"to\": \""+ token +"\",\n    " +
				"\"notification\": {\n        " +
				"\"body\": \""+ asunto +"\",\n        " +
				"\"title\": \""+ title +"\"\n    }\n}");

		Request request = new Request.Builder()
				.url("https://fcm.googleapis.com/fcm/send")
				.method("POST", body)
				.addHeader("Authorization", "key=AAAAOMDADOM:APA91bF39PZzaPSPbFgPbEO6KvjsOD-AtfnpwEgNGZ6lMFQyx4xaswBX6HDe3iQfjAPiP5MR32Onws1Ry5diSbVY_PwRBhZLQ0PGJzPFLUk14xR8ELQVyleVG2_z00wdWBqs1inATbLP")
				.addHeader("Content-Type", "application/json")
				.build();
		try {
			okhttp3.Response response = client.newCall(request).execute();
		} catch (IOException e) {
			e.printStackTrace();


			
		}
	}
}

