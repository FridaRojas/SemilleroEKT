package com.ekt.Servicios.controller;

import java.util.*;

import com.ekt.Servicios.entity.Conversacion;
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
	public ResponseEntity<?> crearMensaje(@RequestBody Mensajes mensajes) throws ApiUnprocessableEntity, ResultadoNoEncontrado {

		this.validarMensajeImpl.validator(mensajes);
		
		Optional<User> emisor = userRepository.validarUsuario(mensajes.getIDEmisor());
		Optional<User> receptor = userRepository.validarUsuario(mensajes.getIDReceptor());

		this.validarMensajeImpl.validarOptional(emisor,"emisor");
		this.validarMensajeImpl.validarOptional(receptor,"receptor");
		
		List<User> listaConversacion = listaConversacion(emisor.get().getID());

		boolean existeEnLista = false;

		for (User contactos : listaConversacion) {

			if (contactos.getID().equals(receptor.get().getID())) {
				existeEnLista = true;
			}
		}

		if (existeEnLista) {
			if (emisor.isPresent()) {
				if (receptor.isPresent()) {
					
					if(mensajes.getRutaDocumento().equals("") || !(mensajes.getRutaDocumento().contains("http://"))) {
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
					
					if(conversacionForma1.size()>0) {
						mensajes.setIDConversacion(mensajes.getIDEmisor() + "_" + mensajes.getIDReceptor());
					}else if(conversacionForma2.size()>0) {
						mensajes.setIDConversacion(mensajes.getIDReceptor() + "_" + mensajes.getIDEmisor());
					}else {
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

					//mensajesService.crearMensaje(mensajes);

					return ResponseEntity.status(HttpStatus.CREATED).body(mensajesService.crearMensaje(mensajes).getIDConversacion());

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
		// Existo existo
		// return ResponseEntity.status(HttpStatus.OK).body(existo.get()); //1
		// Jefe
		// return ResponseEntity.status(HttpStatus.OK).body(jefe.get()); //1
		// Hermanos
		// return ResponseEntity.status(HttpStatus.OK).body(hermanos.iterator()); //4
		// Hijos mios
		// return ResponseEntity.status(HttpStatus.OK).body(misHijos.iterator()); //2
		// listaConversacion
		return ResponseEntity.status(HttpStatus.OK).body(listaConversacion(miId)); // 8
	}

	@PutMapping("actualizarLeido")
	public ResponseEntity<?> actualizarLeido(@RequestBody Mensajes mensajes) throws ResultadoNoEncontrado{
		//Buscamos el mensaje
		Optional<Mensajes> mensaje = mensajesService.buscarMensaje(mensajes.getID());
		
		//Validamos que exista
		validarMensajeImpl.validarOptionalMensajes(mensaje, "Mensaje");
		
		if(mensaje.get().isStatusLeido()) {
			return ResponseEntity.status(HttpStatus.OK).body("El status ya es verdadero");
		} else {
			//Si existe:
			//actualizamos la fecha actual a la fecha entrante
			mensaje.get().setFechaLeido(mensajes.getFechaLeido());
			
			//actualizamos el statusLeido actual al entrante
			mensaje.get().setStatusLeido(true);
			
			//guardamos cambios
			mensajesService.save(mensaje.get());
			
			return ResponseEntity.status(HttpStatus.OK).body("Actualizado");
		}
	}
	
	public List<User> listaConversacion(String miId) throws ResultadoNoEncontrado {
		List<User> listaConversacion = new ArrayList<>();

		// 1.- Validar que yo exista
		Optional<User> existo = userRepository.validarUsuario(miId);
		this.validarMensajeImpl.validarOptional(existo,"emisor");
		//existo.ifPresent(listaConversacion::add);
		// 2.- Buscar a mi jefe
		Optional<User> jefe = userRepository.validarUsuario(existo.get().getIDSuperiorInmediato());
		this.validarMensajeImpl.validarOptional(jefe,"jefe");
		jefe.ifPresent(listaConversacion::add);

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

	@GetMapping("/listarConversaciones")
	public List<Conversacion> listarConversaciones(){
		List<String> mensajesList = new ArrayList<>();
		MongoCollection mongoCollection = monogoTemplate.getCollection("Mensajes");
		DistinctIterable distinctIterable = mongoCollection.distinct("idConversacion",String.class);
		MongoCursor mongoCursor = distinctIterable.iterator();
		List<Conversacion> lConversacion = new ArrayList<>();
		while(mongoCursor.hasNext()){

				Conversacion mConv = new Conversacion() ;
			//if(.equals(id)){
				String idConversacion = (String) mongoCursor.next();
				mConv.setIdConversacion(idConversacion);
				Iterable<Mensajes> iter = mensajesService.verConversacion(idConversacion);
				Iterator <Mensajes> cursor = iter.iterator();
				while(cursor.hasNext()){
					Mensajes mensajes = cursor.next();
					//if(mensajes.getIDEmisor()!= mensajes.getIDEmisor()){

							mConv.setIdReceptor(mensajes.getIDReceptor());
							mConv.setNombreConversacionRecepto(mensajes.getNombreConversacionReceptor());
							mConv.setIdConversacion(mensajes.getIDConversacion());

						//mConv.setIdReceptor(mensajes.getIDReceptor());
						//mConv.setNombreConversacionRecepto(mensajes.getNombreConversacionReceptor());
						//mConv.setIdConversacion(mensajes.getIDConversacion());

					//}
					break;
				}
				lConversacion.add(mConv);
				//mensajesList.add(idConversacion);
		}
		return lConversacion;
	}
	//@GetMapping("listaMensajes/{idEmisor}")
	//public ResponseEntity<?> listarMensajes()
}
