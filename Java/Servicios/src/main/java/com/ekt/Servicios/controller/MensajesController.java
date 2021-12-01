package com.ekt.Servicios.controller;

import java.util.*;

import com.ekt.Servicios.entity.*;
import com.ekt.Servicios.repository.MensajesRepository;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.ekt.Servicios.controller.config.validator.ValidarMensajeImpl;
import com.ekt.Servicios.repository.UserRepository;
import com.ekt.Servicios.service.MensajesService;
import com.ekt.Servicios.util.exceptions.ApiUnprocessableEntity;
import com.ekt.Servicios.util.exceptions.ResultadoNoEncontrado;

@RestController
@RequestMapping("/api/mensajes/")
public class MensajesController {
	@Autowired
	private MensajesService mensajesService;

	@Autowired
	private ValidarMensajeImpl validarMensajeImpl;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MensajesRepository mensajesRepository;

	@Autowired
	MongoTemplate monogoTemplate;

	@PostMapping("crearMensaje")
	public ResponseEntity<?> crearMensaje(@RequestBody Mensajes mensajes)
			throws ApiUnprocessableEntity, ResultadoNoEncontrado {

		if (mensajes.getIDEmisor() == null || mensajes.getIDReceptor() == null || mensajes.getTexto() == null
				|| mensajes.getRutaDocumento() == null || mensajes.getFechaCreacion() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Response(HttpStatus.BAD_REQUEST, "Cuerpo de Json incorrecto", ""));
		}

		if (mensajes.getIDEmisor().length() < 24 || mensajes.getIDEmisor().length() > 24) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Response(HttpStatus.BAD_REQUEST, "El tamaño del idEmisor no es correcto", ""));
		}

		if (mensajes.getIDReceptor().length() < 24) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Response(HttpStatus.BAD_REQUEST, "El tamaño del idReceptor no es correcto", ""));
		}

		if (mensajes.getTexto().length() < 1) { 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new Response(HttpStatus.BAD_REQUEST, "El texto del mensaje debe ser de al menos 1 caracter", ""));
		}

		Optional<User> emisor = userRepository.validarUsuario(mensajes.getIDEmisor());
		Optional<User> receptor = userRepository.validarUsuario(mensajes.getIDReceptor());

		List<User> listaConversacion = new ArrayList<>();
		List<Conversacion> listaGrupo = new ArrayList<>();

		/*if (!receptor.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(HttpStatus.NOT_FOUND,
					"No existe el receptor en la base de datos", mensajes.getIDReceptor()));
		}*/

		if (emisor.isPresent()) {
			listaConversacion = listaConversacion(emisor.get().getID());
			listaGrupo = grupos(mensajes.getIDEmisor());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(HttpStatus.NOT_FOUND,
					"No existe el emisor en la base de datos", mensajes.getIDEmisor()));
		}
		
		boolean existeEnListaGrupo = false;
		boolean existeEnListaUsuario = false;

		Conversacion cuerpoConversacion = new Conversacion();
		
		for (Conversacion conversacion : listaGrupo) {
			if (conversacion.getIdConversacion().equals(mensajes.getIDReceptor())) {
				existeEnListaGrupo = true;
				cuerpoConversacion.setIdConversacion(conversacion.getIdConversacion());
				cuerpoConversacion.setIdReceptor(conversacion.getIdReceptor());
				cuerpoConversacion.setNombreConversacionRecepto(conversacion.getNombreConversacionRecepto());
			}
		}

		if (existeEnListaGrupo) {
			if (mensajes.getRutaDocumento().equals("") || !(mensajes.getRutaDocumento().contains("https://"))) {
				mensajes.setRutaDocumento("");
				mensajes.setStatusRutaDocumento(false);
			} else {
				mensajes.setTexto("Documento");
				mensajes.setStatusRutaDocumento(true);
			}

			mensajes.setIDConversacion(mensajes.getIDReceptor());
			
			mensajes.setStatusCreado(true);
			
			mensajes.setFechaEnviado(new Date());
			mensajes.setStatusEnviado(true);
			
			mensajes.setStatusLeido(false);
			mensajes.setFechaLeido(new Date(0));

			mensajes.setVisible(true);

			mensajes.setNombreConversacionReceptor(cuerpoConversacion.getNombreConversacionRecepto());

			mensajes.setConversacionVisible(true);

			mensajesService.crearMensaje(mensajes);

			if (mensajes.getTexto().equals("Documento")) {
				return ResponseEntity.status(HttpStatus.CREATED)
						.body(new Response(HttpStatus.CREATED, "Documento", mensajes.getIDConversacion()));
			}

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new Response(HttpStatus.CREATED, "Se creo el mensaje a grupo", mensajes.getIDConversacion()));
		}

		// Comparar receptor en lista de contactos
		for (User contactos : listaConversacion) {
			if (contactos.getID().equals(receptor.get().getID())) {
				existeEnListaUsuario = true;
			}
		}

		if (existeEnListaUsuario) {

			if (mensajes.getRutaDocumento().equals("") || !(mensajes.getRutaDocumento().contains("https://"))) {
				mensajes.setRutaDocumento("");
				mensajes.setStatusRutaDocumento(false);
			} else {
				mensajes.setTexto("Documento");
				mensajes.setStatusRutaDocumento(true);
			}

			List<Mensajes> conversacionForma1 = new ArrayList<>();
			List<Mensajes> conversacionForma2 = new ArrayList<>();

			Iterable<Mensajes> conversacionIterable1 = mensajesService
					.verConversacion(mensajes.getIDEmisor() + "_" + mensajes.getIDReceptor());
			conversacionIterable1.forEach(conversacionForma1::add);

			Iterable<Mensajes> conversacionIterable2 = mensajesService
					.verConversacion(mensajes.getIDReceptor() + "_" + mensajes.getIDEmisor());
			conversacionIterable2.forEach(conversacionForma2::add);

			if (conversacionForma1.size() > 0) {
				mensajes.setIDConversacion(mensajes.getIDEmisor() + "_" + mensajes.getIDReceptor());
			} else if (conversacionForma2.size() > 0) {
				mensajes.setIDConversacion(mensajes.getIDReceptor() + "_" + mensajes.getIDEmisor());
			} else {
				mensajes.setIDConversacion(mensajes.getIDEmisor() + "_" + mensajes.getIDReceptor());
			}

			mensajes.setStatusCreado(true);

			mensajes.setFechaEnviado(new Date());
			mensajes.setStatusEnviado(true);

			mensajes.setStatusLeido(false);
			mensajes.setFechaLeido(new Date(0));

			mensajes.setVisible(true);

			mensajes.setNombreConversacionReceptor(receptor.get().getNombre());

			mensajes.setConversacionVisible(true);

			mensajesService.crearMensaje(mensajes);

			if (mensajes.getTexto().equals("Documento")) {
				return ResponseEntity.status(HttpStatus.CREATED)
						.body(new Response(HttpStatus.CREATED, "Documento", mensajes.getIDConversacion()));
			}

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new Response(HttpStatus.CREATED, "Se creo el mensaje", mensajes.getIDConversacion()));
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(HttpStatus.NOT_FOUND,
					"No puedes mandarle mensaje a este receptor", mensajes.getIDReceptor()));
		}

	}

	// ver conversacion
	@GetMapping("/verConversacion/{idConversacion}")
	public ResponseEntity<?> verConversacion(@PathVariable(value = "idConversacion") String idConversacion) {
		Iterable<Mensajes> iter = mensajesService.verConversacion(idConversacion);

		return ResponseEntity.ok(iter.iterator());
	}

	// eliminar mensaje(cambiar estado) ?
	@PutMapping("eliminarMensaje/{idMensaje}")
	public ResponseEntity<?> borrarMensaje(@PathVariable(value = "idMensaje") String idMensaje) {
		
		if(idMensaje.length()<24 || idMensaje.length()>24) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.NOT_FOUND,"Tamaño del id del mensaje invalido",""));
		}
		
		Optional<Mensajes> mensaje = mensajesService.buscarMensaje(idMensaje);

		if (!mensaje.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(HttpStatus.NOT_FOUND,"El mensaje no fue encontrado",""));
		}

		mensaje.get().setVisible(false);

		mensajesService.save(mensaje.get());

		return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.OK,"Se borro el mensaje",mensaje.get().getID()));
	}

	@GetMapping("listaContactos/{miId}")
	public ResponseEntity<?> verListaContactos(@PathVariable(value = "miId") String miId) { // 618d9c26beec342d91d747d6
		
		if(miId.length()<24 || miId.length()>24) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST,"Tamaño del id invalido",""));
		}
		
		Optional<User> existo = userRepository.validarUsuario(miId);
		
		if(!existo.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(HttpStatus.NOT_FOUND,"El usuario no existe en la base de datos",miId));
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(listaConversacion(miId));
	}

	@PutMapping("actualizarLeido")
	public ResponseEntity<?> actualizarLeido(@RequestBody Mensajes mensajes) {
		
		if(mensajes.getID()==null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST,"Error en el Json de entrada",""));
		}
		
		if(mensajes.getID().length()<24 || mensajes.getID().length()>24) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST,"Tamaño del id mensaje incorrecto",""));
		}
		
		Optional<Mensajes> mensaje = mensajesService.buscarMensaje(mensajes.getID());

		if(!mensaje.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(HttpStatus.NOT_FOUND,"No se encontro un mensaje",""));
		}
		
		if (mensaje.get().isStatusLeido()) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.OK,"El status ya es verdadero",""));
		} else {
			
			mensaje.get().setFechaLeido(mensajes.getFechaLeido());

			mensaje.get().setStatusLeido(true);

			mensajesService.save(mensaje.get());

			return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.OK,"Actualizado",mensaje.get().getID()));
		}
	}
	
	List<User> myArregloUsuario = new ArrayList<>();
	public List<User> listaConversacion(String miId){
		List<User> listaConversacion = new ArrayList<>();
		
		Optional<User> existo = userRepository.validarUsuario(miId);
		
		Optional<User> jefe = userRepository.validarUsuario(existo.get().getIDSuperiorInmediato());
		
		if (jefe.isPresent()) {
			jefe.ifPresent(listaConversacion::add);
		}

		Iterable<User> hermanos = userRepository.findByBossId(existo.get().getIDSuperiorInmediato());
		for(User hermano : hermanos) {
			if(!hermano.getID().equals(miId)) {
				listaConversacion.add(hermano);
			}
		}

		Iterable<User> misHijos = userRepository.findByBossId(existo.get().getID());
		
		lista(misHijos);
		
		for(User usuariosHijos : this.myArregloUsuario) {
			listaConversacion.add(usuariosHijos);
		}
		
		return listaConversacion;
	}
	
	/*@GetMapping("listarHijos/{id}")
	public ResponseEntity<?> listaHijos(@PathVariable (value = "id") String id){
		
		Iterable<User> hijos = userRepository.findByBossId(id);
		
		lista(hijos);
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.myArregloUsuario);
	}
	
	@GetMapping("listarHijos2/{id}")
	public ResponseEntity<?> listaHijos2(@PathVariable (value = "id") String id){
		
		Iterable<User> hijos = userRepository.findByBossId(id);
		
		//lista(hijos);
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(hijos);
	}*/
	
	public void lista(Iterable<User> listaHijos){
		List<User> contenedor = new ArrayList<>();
		for(User usuario : listaHijos) {
			this.myArregloUsuario.add(usuario);
			Iterable<User> listaNietos = userRepository.findByBossId(usuario.getID());
			listaNietos.forEach(contenedor::add);
			if(contenedor.size()>0) {
				lista(listaNietos);
			}
		}
	}
	
	@PutMapping("/eliminarConversacion/{idConversacion}")
	public ResponseEntity<?> cambiarStatusConversacion(@PathVariable(value = "idConversacion") String idConversacion) {
		Iterable<Mensajes> iter = mensajesService.verConversacion(idConversacion);
		for (Mensajes msg : iter) {
			msg.setConversacionVisible(false);
			mensajesService.save(msg);
		}
		return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.CREATED, "", iter.iterator()));
	}

	@GetMapping("listaGrupos/{miId}")
	public ResponseEntity<?> listaGrupos(@PathVariable(value = "miId") String miId) {

		if(miId.length()<24 || miId.length()>24) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST,"Tamaño del id incorrecto",""));
		}
		
		Optional<User> existo = userRepository.validarUsuario(miId);
		
		if(!existo.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(HttpStatus.NOT_FOUND,"No se encontro usuario en la base de datos",miId));
		}
		
		List<Conversacion> grupos = grupos(miId);

		return ResponseEntity.status(HttpStatus.OK).body(grupos);
	}

	@GetMapping("listaPersonasGrupo/{idGrupo}")
	public ResponseEntity<?> listaDePersonasEnGrupo(@PathVariable(value = "idGrupo") String idGrupo) {
		List<User> usuarios = new ArrayList<>();

		String[] lenguajesComoArreglo = idGrupo.split("-");

		for (String idUsuario : lenguajesComoArreglo) {

			Optional<User> usuario = userRepository.validarUsuario(idUsuario);
			usuario.ifPresent(usuarios::add);

		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(usuarios);
	}

	public List<Conversacion> grupos(String miId) {

		List<Conversacion> grupos = new ArrayList<>();

		Optional<User> existo = userRepository.validarUsuario(miId);

		Optional<User> jefe = userRepository.validarUsuario(existo.get().getIDSuperiorInmediato());

		// Crear lista padre
		if (jefe.isPresent()) {
			List<User> listaPadre = new ArrayList<>();

			StringBuilder idConversacionPadre = new StringBuilder();

			Conversacion conversacion = new Conversacion();

			Iterable<User> misHermanos = userRepository.findByBossId(jefe.get().getID());
			misHermanos.forEach(listaPadre::add);

			if (listaPadre.size() < 2) {
				idConversacionPadre.append("");
			} else {
				idConversacionPadre.append(jefe.get().getID());

				for (User hijos : listaPadre) {
					idConversacionPadre.append("-" + hijos.getID());
				}
			}

			conversacion.setIdConversacion(idConversacionPadre.toString());
			conversacion.setIdReceptor(idConversacionPadre.toString());
			conversacion.setNombreConversacionRecepto(
					"Chat Padre " + jefe.get().getNombreRol() + " " + jefe.get().getNombre());

			grupos.add(conversacion);
		}

		List<User> listaHijos = new ArrayList<>();

		Iterable<User> misHijos = userRepository.findByBossId(miId);
		misHijos.forEach(listaHijos::add);

		if (listaHijos.size() > 1) {
			StringBuilder idMiConversacion = new StringBuilder();

			Conversacion miConversacion = new Conversacion();

			if (listaHijos.size() < 2) {
				idMiConversacion.append("");
			} else {
				idMiConversacion.append(miId);

				for (User hijos : listaHijos) {
					idMiConversacion.append("-" + hijos.getID());
				}
			}

			miConversacion.setIdConversacion(idMiConversacion.toString());
			miConversacion.setIdReceptor(idMiConversacion.toString());
			miConversacion.setNombreConversacionRecepto(
					"Chat Mio " + existo.get().getNombreRol() + " " + existo.get().getNombre());

			grupos.add(miConversacion);
		}

		return grupos;
	}

	@GetMapping("/listarConversaciones/{idEmisor}")
	public List<Conversacion> listarConversaciones2(@PathVariable(value ="idEmisor")String idEmisor){
		//String idEmisor = "618e8821c613329636a769ac";
		List<String> mensajesList = new ArrayList<>();
		MongoCollection mongoCollection = monogoTemplate.getCollection("Mensajes");
		DistinctIterable distinctIterable = mongoCollection.distinct("idConversacion", String.class);
		MongoCursor mongoCursor = distinctIterable.iterator();


		List<Conversacion> lConversacion = new ArrayList<>();
		List<Conversacion> lConversacion2 = new ArrayList<>();
		List<Conversacion> lConversacion3 = new ArrayList<>();
		while (mongoCursor.hasNext()) {

			Conversacion mConv = new Conversacion();

			String idConversacion = (String) mongoCursor.next();
			mConv.setIdConversacion(idConversacion);
			Iterable<Mensajes> iter = mensajesService.verConversacion(idConversacion);

				Iterator<Mensajes> cursor = iter.iterator();
				while (cursor.hasNext()) {

					Mensajes mensajes = cursor.next();
					if (!mensajes.getIDReceptor().equals(idEmisor)) {


						mConv.setIdReceptor(mensajes.getIDReceptor());
						mConv.setNombreConversacionRecepto(mensajes.getNombreConversacionReceptor());
						mConv.setIdConversacion(mensajes.getIDConversacion());
						mConv.setIdEmisor(mensajes.getIDEmisor());

					}
				}
				
			lConversacion.add(mConv);
		}

		for (Conversacion conv: lConversacion){
			if(/*conv.getIdEmisor().equals(idEmisor) &&*/ conv.getIdConversacion().contains(idEmisor)){
				lConversacion2.add(conv);
			}
		}
		for (Conversacion conv2: lConversacion2){
			if(conv2.getIdConversacion().length()<50){
				lConversacion3.add(conv2);
			}
		}
		return lConversacion3;
	}

	@GetMapping("listarMensajesRecividos/{idEmisor}")
	public Iterable<?> listarMensajesRecividos(@PathVariable (value = "idEmisor")String idEmisor){

		Iterable<Mensajes> msg= mensajesRepository.findAll();
		List<Mensajes> lMensajes = new ArrayList<>();

		for (Mensajes msg2: msg) {
			if(msg2.getIDConversacion().contains(idEmisor)){
				lMensajes.add(msg2);
			}
		}
		return lMensajes;
	}



}
