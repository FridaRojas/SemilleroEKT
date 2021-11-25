package com.ekt.Servicios.controller;

import java.util.*;

import com.ekt.Servicios.entity.Conversacion;
import com.ekt.Servicios.entity.Response;
import com.ekt.Servicios.repository.MensajesRepository;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ekt.Servicios.controller.config.validator.ValidarMensajeImpl;
import com.ekt.Servicios.entity.Mensajes;
import com.ekt.Servicios.entity.User;
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

		this.validarMensajeImpl.validator(mensajes);

		Optional<User> emisor = userRepository.validarUsuario(mensajes.getIDEmisor());
		Optional<User> receptor = userRepository.validarUsuario(mensajes.getIDReceptor());

		this.validarMensajeImpl.validarOptional(emisor, "emisor");
		// this.validarMensajeImpl.validarOptional(receptor, "receptor");

		List<User> listaConversacion = listaConversacion(emisor.get().getID());

		List<Conversacion> listaGrupo = grupos(mensajes.getIDEmisor());

		// Banderas para saber si se encontraron en alguna lista
		boolean existeEnListaGrupo = false;
		boolean existeEnListaUsuario = false;

		Conversacion cuerpoConversacion = new Conversacion();
		// Comparar receptor en lista de grupos
		for (Conversacion conversacion : listaGrupo) {
			if (conversacion.getIdConversacion().equals(mensajes.getIDReceptor())) {
				existeEnListaGrupo = true;
				cuerpoConversacion.setIdConversacion(conversacion.getIdConversacion());
				cuerpoConversacion.setIdReceptor(conversacion.getIdReceptor());
				cuerpoConversacion.setNombreConversacionRecepto(conversacion.getNombreConversacionRecepto());
			}
		}

		if (existeEnListaGrupo) {
			if (emisor.isPresent()) {
				if (mensajes.getRutaDocumento().equals("") || !(mensajes.getRutaDocumento().contains("http://"))) {
					mensajes.setRutaDocumento("");
					mensajes.setStatusRutaDocumento(false);
				} else {
					mensajes.setTexto("Documento");
					mensajes.setStatusRutaDocumento(true);
				}

				mensajes.setIDConversacion(mensajes.getIDReceptor());

				// Status-fecha Creado
				mensajes.setStatusCreado(true);

				// Status-fecha Enviado
				mensajes.setFechaEnviado(new Date());
				mensajes.setStatusEnviado(true);

				// Status-fecha Leido
				mensajes.setStatusLeido(false);
				mensajes.setFechaLeido(new Date(0));

				mensajes.setVisible(true);

				mensajes.setNombreConversacionReceptor(cuerpoConversacion.getNombreConversacionRecepto());

				mensajes.setConversacionVisible(true);

				mensajesService.crearMensaje(mensajes);

				return ResponseEntity.status(HttpStatus.CREATED).body(
						new Response(HttpStatus.CREATED, "Se creo el mensaje a grupo", mensajes.getIDConversacion()));
			}
		}

		// Comparar receptor en lista de contactos
		for (User contactos : listaConversacion) {
			if (contactos.getID().equals(receptor.get().getID())) {
				existeEnListaUsuario = true;
			}
		}

		if (existeEnListaUsuario) {
			if (emisor.isPresent()) {
				if (receptor.isPresent()) {

					if (mensajes.getRutaDocumento().equals("") || !(mensajes.getRutaDocumento().contains("http://"))) {
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

					// Status-fecha Creado
					mensajes.setStatusCreado(true);

					// Status-fecha Enviado
					mensajes.setFechaEnviado(new Date());
					mensajes.setStatusEnviado(true);

					// Status-fecha Leido
					mensajes.setStatusLeido(false);
					mensajes.setFechaLeido(new Date(0));

					mensajes.setVisible(true);

					mensajes.setNombreConversacionReceptor(receptor.get().getNombre());

					mensajes.setConversacionVisible(true);

					mensajesService.crearMensaje(mensajes);

					return ResponseEntity.status(HttpStatus.CREATED)
							.body(new Response(HttpStatus.CREATED, "Se creo el mensaje", mensajes.getIDConversacion()));

				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(receptor.get());
			} else {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(emisor.get());
			}
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No existe el receptor en la lista");
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

		Optional<Mensajes> mensaje = mensajesService.buscarMensaje(idMensaje);

		if (!mensaje.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		mensaje.get().setVisible(false);

		mensajesService.save(mensaje.get());

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@GetMapping("listaContactos/{miId}")
	public ResponseEntity<?> verListaContactos(@PathVariable(value = "miId") String miId) throws ResultadoNoEncontrado { // 618d9c26beec342d91d747d6
		// listaConversacion
		return ResponseEntity.status(HttpStatus.OK).body(listaConversacion(miId)); // 8
	}

	@PutMapping("actualizarLeido")
	public ResponseEntity<?> actualizarLeido(@RequestBody Mensajes mensajes) throws ResultadoNoEncontrado {
		// Buscamos el mensaje
		Optional<Mensajes> mensaje = mensajesService.buscarMensaje(mensajes.getID());

		// Validamos que exista
		validarMensajeImpl.validarOptionalMensajes(mensaje, "Mensaje");

		if (mensaje.get().isStatusLeido()) {
			return ResponseEntity.status(HttpStatus.OK).body("El status ya es verdadero");
		} else {
			// Si existe:
			// actualizamos la fecha actual a la fecha entrante
			mensaje.get().setFechaLeido(mensajes.getFechaLeido());

			// actualizamos el statusLeido actual al entrante
			mensaje.get().setStatusLeido(true);

			// guardamos cambios
			mensajesService.save(mensaje.get());

			return ResponseEntity.status(HttpStatus.OK).body("Actualizado");
		}
	}

	public List<User> listaConversacion(String miId) throws ResultadoNoEncontrado {
		List<User> listaConversacion = new ArrayList<>();

		// 1.- Validar que yo exista
		Optional<User> existo = userRepository.validarUsuario(miId);
		this.validarMensajeImpl.validarOptional(existo, "emisor");
		// existo.ifPresent(listaConversacion::add);
		// 2.- Buscar a mi jefe
		Optional<User> jefe = userRepository.validarUsuario(existo.get().getIDSuperiorInmediato());
		// this.validarMensajeImpl.validarOptional(jefe,"jefe");
		if (jefe.isPresent()) {
			jefe.ifPresent(listaConversacion::add);
		}

		// 3.- Buscar Hijos de jefe (hermanos)
		Iterable<User> hermanos = userRepository.findByBossId(existo.get().getIDSuperiorInmediato());
		this.validarMensajeImpl.validarIterableUser(hermanos, "hermanos");
		hermanos.forEach(listaConversacion::add);

		// 4.- Buscar a mis hijos
		Iterable<User> misHijos = userRepository.findByBossId(existo.get().getID());
		this.validarMensajeImpl.validarIterableUser(misHijos, "hijos");
		misHijos.forEach(listaConversacion::add);

		return listaConversacion;
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
		while (mongoCursor.hasNext()) {

			Conversacion mConv = new Conversacion();
			// if(){
			String idConversacion = (String) mongoCursor.next();
			mConv.setIdConversacion(idConversacion);
			Iterable<Mensajes> iter = mensajesService.verConversacion(idConversacion);
			Iterator<Mensajes> cursor = iter.iterator();
			while (cursor.hasNext()) {
				Mensajes mensajes = cursor.next();

				mConv.setIdReceptor(mensajes.getIDReceptor());
				mConv.setNombreConversacionRecepto(mensajes.getNombreConversacionReceptor());
				mConv.setIdConversacion(mensajes.getIDConversacion());
				mConv.setIdEmisor(mensajes.getIDEmisor());

			}
			lConversacion.add(mConv);
			//mensajesList.add(idConversacion);

		}
		//lConversacion = listarConversaciones()
		for (Conversacion conv: lConversacion){
			if(/*conv.getIdEmisor().equals(idEmisor) &&*/ conv.getIdConversacion().contains(idEmisor)){
				lConversacion2.add(conv);
			}

		}
		return lConversacion2;
	}

	@GetMapping("listarMensajes/{idEmisor}")
	public ResponseEntity<?> listarMensajesID (@PathVariable (value = "idEmisor")String idEmisor) {

		Iterable<Mensajes> msg = mensajesRepository.traerMensajes(idEmisor);


		return ResponseEntity.status(HttpStatus.ACCEPTED).body(msg.iterator());
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
