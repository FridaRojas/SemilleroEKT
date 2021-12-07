package com.ekt.Servicios.controller;

import java.io.IOException;
import java.util.*;

import com.ekt.Servicios.entity.*;
import com.ekt.Servicios.repository.MensajesRepository;
import com.mongodb.MongoException;
import com.mongodb.MongoSocketException;
import com.mongodb.MongoSocketOpenException;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.ekt.Servicios.repository.UserRepository;
import com.ekt.Servicios.service.MensajesService;

@RestController
@RequestMapping("/api/mensajes/")
public class MensajesController {
	@Autowired
	private MensajesService mensajesService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MensajesRepository mensajesRepository;

	@Autowired
	MongoTemplate monogoTemplate;

	@PostMapping("crearMensaje")
	public ResponseEntity<?> crearMensaje(@RequestHeader("tokenSesion")String tokenSesion, @RequestBody Mensajes mensajes) {
		try {

			if (mensajes.getIDEmisor() == null || mensajes.getIDReceptor() == null || mensajes.getTexto() == null
					|| mensajes.getRutaDocumento() == null || mensajes.getFechaCreacion() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Cuerpo de Json incorrecto", null));
			}

			if (mensajes.getIDEmisor().length() < 24 || mensajes.getIDEmisor().length() > 24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El tamaño del idEmisor no es correcto", null));
			}

			if (mensajes.getIDReceptor().length() < 24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El tamaño del idReceptor no es correcto", null));
			}

			if (mensajes.getTexto().length() < 1) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()),
						"El texto del mensaje debe ser de al menos 1 caracter", null));
			}

			Optional<User> emisor = userRepository.validarUsuario(mensajes.getIDEmisor());
			Optional<User> receptor = userRepository.validarUsuario(mensajes.getIDReceptor());

			List<User> listaConversacion = new ArrayList<>();
			List<Conversacion> listaGrupo = new ArrayList<>();

			if (emisor.isPresent()) {
				listaConversacion = listaConversacion(emisor.get().getID());
				listaGrupo = grupos(mensajes.getIDEmisor());
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
						"No existe el emisor en la base de datos", mensajes.getIDEmisor()));
			}
			
			if (emisor.get().getStatusActivo().equals("false")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value())
						,"Usuario invalido",null));
			}
			
			if(!emisor.get().getTokenAuth().equals(tokenSesion)) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseMensajes(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value())
						,"Token invalido",null));
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

				List<User> usuarios = new ArrayList<>();
				String[] lenguajesComoArreglo = mensajes.getIDReceptor().split("-");
				for (String idUsuario : lenguajesComoArreglo) {
					Optional<User> usuario = userRepository.validarUsuario(idUsuario);
					usuario.ifPresent(usuarios::add);
				}

				if (mensajes.getTexto().equals("Documento")) {
					for (User usuario : usuarios) {
						notificacion2(
								"Nuevo Mensaje de " + emisor.get().getNombre() + " a: "
										+ mensajes.getNombreConversacionReceptor(),
								"Nuevo documento", usuario.getToken());
					}
					return ResponseEntity.status(HttpStatus.OK)
							.body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()), "Documento", mensajes.getIDConversacion()));
				}

				for (User usuario : usuarios) {
					notificacion2(
							"Nuevo Mensaje de " + emisor.get().getNombre() + " a: "
									+ mensajes.getNombreConversacionReceptor(),
							mensajes.getTexto(), usuario.getToken());
				}
				return ResponseEntity.status(HttpStatus.OK).body(
						new ResponseMensajes(String.valueOf(HttpStatus.OK.value()), "Se creo el mensaje a grupo", mensajes.getIDConversacion()));
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
					notificacion2("Nuevo Mensaje de " + emisor.get().getNombre(), "Nuevo documento",
							receptor.get().getToken());
					return ResponseEntity.status(HttpStatus.OK)
							.body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()), "Documento", mensajes.getIDConversacion()));
				}

				notificacion2("Nuevo Mensaje de " + emisor.get().getNombre(), mensajes.getTexto(),
						receptor.get().getToken());
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()), "Se creo el mensaje", mensajes.getIDConversacion()));
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
						"No puedes mandarle mensaje a este receptor", mensajes.getIDReceptor()));
			}
		} catch (MongoSocketException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		}
	}

	@GetMapping("/verConversacion/{idConversacion}")
	public ResponseEntity<?> verConversacion(@PathVariable(value = "idConversacion") String idConversacion) {
		try{
			if(idConversacion.length()<49){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El id de la convbersacion no contiene los caracteres neesarios", ""));
			}
			Iterable<Mensajes> iter = mensajesService.verConversacion(idConversacion);

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()),"Lista de mensajes de conversacion: "+ idConversacion,iter.iterator()));

		}catch (MongoSocketOpenException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		}

	}

	@PutMapping("eliminarMensaje/{idMensaje}/{idUsuario}") //&{idUsuario}
	public ResponseEntity<?> borrarMensaje(@RequestHeader("tokenSesion")String tokenSesion, @PathVariable(value = "idMensaje") String idMensaje, @PathVariable(value = "idUsuario") String idUsuario) {
		try {
			if(idMensaje.length()<24 || idMensaje.length()>24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value())
						,"Tamaño del id del mensaje invalido",null));
			}
			
			if(idUsuario.length()<24 || idUsuario.length()>24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value())
						,"Usuario invalido",null));
			}
			
			Optional<User> usuario = userRepository.findById(idUsuario);
			Optional<Mensajes> mensaje = mensajesService.buscarMensaje(idMensaje);
			
			if (!usuario.isPresent() || usuario.get().getStatusActivo().equals("false")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value())
						,"Usuario invalido",null));
			}
			
			if(!usuario.get().getTokenAuth().equals(tokenSesion)) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseMensajes(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value())
						,"Token invalido",null));
			}
			
			if (!mensaje.isPresent()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value())
						,"El mensaje no fue encontrado",null));
			}
			if(!mensaje.get().getVisible()) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value())
						,"El mensaje se elimino anteriormente",null));
			}

			mensaje.get().setVisible(false);

			mensajesService.save(mensaje.get());

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()),
					"Se borro el mensaje",mensaje.get().getID()));
		} catch (MongoSocketException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		}
	}

	@GetMapping("listaContactos/{miId}")
	public ResponseEntity<?> verListaContactos(@RequestHeader("tokenSesion")String tokenSesion,@PathVariable(value = "miId") String miId) {
		try {
			if(miId.length()<24 || miId.length()>24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()),
						"Tamaño del id invalido",null));
			}
			
			Optional<User> existo = userRepository.validarUsuario(miId);
			
			if(!existo.isPresent() || existo.get().getStatusActivo().equals("false")) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
						,"Usuario invalido",miId));
			}
			
			if(!existo.get().getTokenAuth().equals(tokenSesion)) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseMensajes(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value())
						,"Token invalido",null));
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value())
					,"Lista de contactos",listaConversacion(miId)));
		} catch (MongoSocketException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		}
	}

	@PutMapping("actualizarLeido/{idUsuario}")
	public ResponseEntity<?> actualizarLeido(@RequestHeader("tokenSesion")String tokenSesion,@PathVariable(value = "idUsuario") String idUsuario,@RequestBody Mensajes mensajes) {
		try {
			if(mensajes.getID()==null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()),
						"Error en el Json de entrada",null));
			}
			
			if(mensajes.getID().length()<24 || mensajes.getID().length()>24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value())
						,"Tamaño del id mensaje incorrecto",null));
			}
			
			if(idUsuario.length()<24 || idUsuario.length()>24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value())
						,"El id del usuario no es valido",null));
			}
			
			Optional<User> existo = userRepository.validarUsuario(idUsuario);
			
			if(!existo.isPresent() || existo.get().getStatusActivo().equals("false")) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
						,"Usuario invalido",idUsuario));
			}
			
			if(!existo.get().getTokenAuth().equals(tokenSesion)) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseMensajes(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value())
						,"Token invalido",null));
			}
			
			Optional<Mensajes> mensaje = mensajesService.buscarMensaje(mensajes.getID());

			if(!mensaje.isPresent()) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
						"El mensaje no existe en la base de datos",mensajes.getID()));
			}
			
			if (mensaje.get().isStatusLeido()) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()),
						"El mensaje ya fue leido anteriormente",mensaje.get().getID()));
			} else {
				
				mensaje.get().setFechaLeido(mensajes.getFechaLeido());

				mensaje.get().setStatusLeido(true);

				mensajesService.save(mensaje.get()); 

				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()),
						"Actualizado",mensaje.get().getID()));
			}
		} catch (MongoSocketException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		}
	}
	
	List<User> myArregloUsuario = new ArrayList<>();

	public List<User> listaConversacion(String miId) {
		List<User> listaConversacion = new ArrayList<>();
		listaConversacion.clear();

		Optional<User> existo = userRepository.validarUsuario(miId);

		Optional<User> jefe = userRepository.validarUsuario(existo.get().getIDSuperiorInmediato());

		if (jefe.isPresent() && jefe.get().getStatusActivo().equals("true")) {
			jefe.ifPresent(listaConversacion::add);
		}

		Iterable<User> hermanos = userRepository.findByBossId(existo.get().getIDSuperiorInmediato());
		for (User hermano : hermanos) {
			if (!hermano.getID().equals(miId) && !hermano.getIDSuperiorInmediato().equals("")
					&& !hermano.getIDGrupo().equals("") && hermano.getIDGrupo().equals(existo.get().getIDGrupo())
					&& hermano.getStatusActivo().equals("true")) {
				listaConversacion.add(hermano);
			}
		}

		Iterable<User> misHijos = userRepository.findByBossId(miId);
		List<User> hijosValidos = new ArrayList<>();

		for (User hijo : misHijos) {
			if (hijo.getIDGrupo().equals(existo.get().getIDGrupo()) && hijo.getStatusActivo().equals("true")
					&& !hijo.getIDSuperiorInmediato().equals("")) {
				hijosValidos.add(hijo);
			}
		}

		lista(misHijos);

		for (User usuariosHijos : this.myArregloUsuario) {
			listaConversacion.add(usuariosHijos);
		}

		this.myArregloUsuario.clear();

		return listaConversacion;
	}

	public void lista(Iterable<User> listaHijos) {
		List<User> contenedor = new ArrayList<>();
		for (User usuario : listaHijos) {
			if (!usuario.getIDSuperiorInmediato().equals("") && !usuario.getIDGrupo().equals("")
					&& usuario.getStatusActivo().equals("true")) {
				this.myArregloUsuario.add(usuario);
				Iterable<User> listaNietos = userRepository.findByBossId(usuario.getID());
				listaNietos.forEach(contenedor::add);
				if (contenedor.size() > 0) {
					lista(listaNietos);
				}
			}
		}
	}
	
	@PutMapping("/eliminarConversacion/{idConversacion}")
	public ResponseEntity<?> cambiarStatusConversacion(@PathVariable(value = "idConversacion") String idConversacion) {
		try{
			if(idConversacion.length()<49) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El id de la conversacion no contiene los caracteres neesarios", ""));
			}

			List<Mensajes> list = new ArrayList<>();

			Iterable<Mensajes> iter = mensajesService.verConversacion(idConversacion);
			iter.forEach(list::add);
			if(list.size()<1) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMensajes(String.valueOf(HttpStatus.NOT_FOUND.value()), "No existe la conversacion",""));
			}
			if(!list.get(0).isConversacionVisible()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMensajes(String.valueOf(HttpStatus.NOT_FOUND.value()), "La conversacion ya se elimino anteriormente",""));
			}
			for (Mensajes msg : iter) {
				msg.setConversacionVisible(false);
				mensajesService.save(msg);
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMensajes(String.valueOf(HttpStatus.CREATED.value()), "Se elimino la conversacion",""));

		}catch (MongoSocketOpenException e) {
			return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		}

	}

	@GetMapping("listaGrupos/{miId}")
	public ResponseEntity<?> listaGrupos(@RequestHeader("tokenSesion")String tokenSesion,@PathVariable(value = "miId") String miId) {
		try {
			if(miId.length()<24 || miId.length()>24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()),
						"Tamaño del id incorrecto",null));
			}
			
			Optional<User> existo = userRepository.validarUsuario(miId);
			
			if(!existo.isPresent() || existo.get().getStatusActivo().equals("false")) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
						"Usuario invalido",miId));
			}
			
			if(!existo.get().getTokenAuth().equals(tokenSesion)) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseMensajes(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value())
						,"Token invalido",null));
			}
			
			List<Conversacion> grupos = grupos(miId);

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()),
					"Lista de grupos",grupos));
		} catch (MongoSocketException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		}
	}

	@GetMapping("listaPersonasGrupo/{idGrupo}/{idUsuario}")
	public ResponseEntity<?> listaDePersonasEnGrupo(@RequestHeader("tokenSesion")String tokenSesion,@PathVariable(value = "idGrupo") String idGrupo,@PathVariable(value = "idUsuario")String idUsuario) {
		try {
			if(idGrupo.length()<74) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()),
						"No es un chat de grupo",null));
			}
			
			Optional<User> existo = userRepository.validarUsuario(idUsuario);
			
			if(!existo.isPresent() || existo.get().getStatusActivo().equals("false")) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
						"Usuario invalido",null));
			}
			
			if(!existo.get().getTokenAuth().equals(tokenSesion)) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseMensajes(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value())
						,"Token invalido",null));
			}
			
			
			List<User> usuarios = new ArrayList<>();

			String[] lenguajesComoArreglo = idGrupo.split("-");

			for (String idUsuarios : lenguajesComoArreglo) {

				Optional<User> usuario = userRepository.validarUsuario(idUsuarios);
				usuario.ifPresent(usuarios::add);

			}

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()),
					"Lista de personas en grupo",usuarios));
		} catch (MongoSocketException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		}
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
				
				conversacion.setIdConversacion(idConversacionPadre.toString());
				conversacion.setIdReceptor(idConversacionPadre.toString());
				conversacion.setNombreConversacionRecepto(
						"Chat grupal con " + jefe.get().getNombreRol() + " " + jefe.get().getNombre());

				grupos.add(conversacion);
			}
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
				
				miConversacion.setIdConversacion(idMiConversacion.toString());
				miConversacion.setIdReceptor(idMiConversacion.toString());
				miConversacion.setNombreConversacionRecepto(
						"Chat grupal con " + existo.get().getNombreRol() + " " + existo.get().getNombre());

				grupos.add(miConversacion);
			}
		}

		return grupos;
	}


	@GetMapping("/listarConversaciones/{idEmisor}")
	public ResponseEntity<?> listarConversaciones(@PathVariable(value ="idEmisor")String idEmisor){
		try{
			if(idEmisor.length()<24 || idEmisor.length()>24){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El id del usuario no tiene los caracteres necesarios",""));
			}
			Optional<User> user = userRepository.validarUsuario(idEmisor);
			if (!user.isPresent()){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMensajes(String.valueOf(HttpStatus.NOT_FOUND.value()),"No se encuentra este usuario",""));
			}
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
					if (/*!mensajes.getIDEmisor().equals(idEmisor) ||*/ !mensajes.getIDReceptor().equals(idEmisor)  ) {

						mConv.setIdReceptor(mensajes.getIDReceptor());
						mConv.setNombreConversacionRecepto(mensajes.getNombreConversacionReceptor());
						mConv.setIdConversacion(mensajes.getIDConversacion());
						mConv.setIdEmisor(mensajes.getIDEmisor());

					}else if(mensajes.getIDReceptor().equals(idEmisor)) {
						Optional<User> user3 = userRepository.validarUsuario(mensajes.getIDEmisor());
						System.out.println();
						mConv.setIdReceptor(mensajes.getIDEmisor());
						mConv.setNombreConversacionRecepto(user3.get().getNombre());
						mConv.setIdConversacion(mensajes.getIDConversacion());
						mConv.setIdEmisor(mensajes.getIDEmisor());
					}
				}
				//lConversacion.add(mConv);
				if(/*conv.getIdEmisor().equals(idEmisor) &&*/ mConv.getIdConversacion().contains(idEmisor)){
					if(mConv.getIdConversacion().length()<50){
						if(mConv.getIdReceptor() !=null ) {
							Optional<User> user2 = userRepository.validarUsuario(mConv.getIdReceptor());

							mConv.setNombreRol(user2.get().getNombreRol());
						}
						lConversacion3.add(mConv);
					}
				}
			}

//			for (Conversacion conv: lConversacion){
//				if(/*conv.getIdEmisor().equals(idEmisor) &&*/ conv.getIdConversacion().contains(idEmisor)){
//					lConversacion2.add(conv);
//				}
//			}
//			for (Conversacion conv2: lConversacion2){
//				if(conv2.getIdConversacion().length()<50){
//					if(conv2.getIdReceptor() !=null ) {
//						Optional<User> user2 = userRepository.validarUsuario(conv2.getIdReceptor());
//
//						conv2.setNombreRol(user2.get().getNombreRol());
//					}
//					lConversacion3.add(conv2);
//				}
//			}
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()),"Lista de conversaciones",lConversacion3));
		}catch (MongoSocketOpenException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		}


	}

	@GetMapping("listarMensajesRecividos/{idEmisor}")
	public ResponseEntity<?> listarMensajesRecividos(@PathVariable (value = "idEmisor")String idEmisor){
		try{
			if(idEmisor.length()<24 || idEmisor.length()>24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Tamaño del id invalido",""));
			}
			Optional<User> user=userRepository.validarUsuario(idEmisor);
			if(!user.isPresent()){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMensajes(String.valueOf(HttpStatus.NOT_FOUND.value()),"no se encuetra el usuario",""));
			}
			Iterable<Mensajes> msg= mensajesRepository.findAll();
			List<Mensajes> lMensajes = new ArrayList<>();

			for (Mensajes msg2: msg) {
				if(msg2.getIDConversacion().contains(idEmisor)){
					lMensajes.add(msg2);
				}
			}
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()),"Lista de mensajes",lMensajes));
		}catch (MongoSocketOpenException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (MongoException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e.getCause()));
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
