package com.ekt.Servicios.controller;

import java.util.*;

import com.ekt.Servicios.entity.*;
import com.ekt.Servicios.repository.GruposMensajeriaRepository;
import com.ekt.Servicios.repository.MensajesRepository;
import com.ekt.Servicios.service.MensajesServiceImpl;
import com.mongodb.MongoException;
import com.mongodb.MongoSocketException;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

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

	MensajesServiceImpl mensajesServiceImpl = new MensajesServiceImpl();

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MensajesRepository mensajesRepository;

	@Autowired
	MongoTemplate monogoTemplate;
	
	@Autowired
	private GruposMensajeriaRepository gruposMensajeriaRepository;

	/*Servicio para enviar un mensaje:
	 *es necesario el token de inicio de sesion y un cuerpo de tipo Mensajes*/
	@PostMapping("crearMensaje")
	public ResponseEntity<?> crearMensaje(@RequestHeader("tokenAuth")String tokenAuth, @RequestBody Mensajes mensajes) {
		try {
			//Validaciones correspondientes:
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
			
			//Buscamos al emisor y al receptor del JSon entrante existan en la base de datos:
			Optional<User> emisor = userRepository.validarUsuario(mensajes.getIDEmisor());
			Optional<User> receptor = userRepository.validarUsuario(mensajes.getIDReceptor());

			/*Iniciamos un contenedor para listar a los usuarios con los que podemos platicar y otro
			 * para contener los grupos a donde podemos mandar un mensaje*/
			List<User> listaConversacion = new ArrayList<>();
			List<Conversacion> listaGrupo = new ArrayList<>();

			//Validamos si el emisor existe y llenamos los contenedores con valores
			if (emisor.isPresent()) {
				listaConversacion = listaConversacion(emisor.get().getID());
				listaGrupo = grupos(mensajes.getIDEmisor());
			} else {
				//En caso de que no exista el emisor, da como respuesta lo siguiente:
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
						"No existe el emisor en la base de datos", mensajes.getIDEmisor()));
			}

			//Hacemos validaciones correspondientes tanto de si el emisor esta activo
			if (emisor.get().getStatusActivo().equals("false")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value())
						,"Usuario invalido",null));
			}

			//Validamos que el token entrante recibido en el header es igual al token de este usuario en la base de datos
			if(!emisor.get().getTokenAuth().equals(tokenAuth) || emisor.get().getTokenAuth() == null || emisor.get().getTokenAuth().equals("")) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseMensajes(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value())
						,"Token invalido",null));
			}

			//Inicializamos banderas para saber si existen grupos y personas a quienes mandaremos mensajes  
			boolean existeEnListaGrupo = false;
			boolean existeEnListaUsuario = false;

			//creamos un nuevo cuerpo de conversacion
			Conversacion cuerpoConversacion = new Conversacion();

			//Iteramos el contenedor de grupos y comparamos si el id de grupo entrante esta en este contenedor 
			for (Conversacion conversacion : listaGrupo) {
				if (conversacion.getIdConversacion().equals(mensajes.getIDReceptor())) {
					/*Si el id de grupo se encuentra se crea un nuevo cuerpo de conversacion y
					la bandera cambia a verdadero*/
					existeEnListaGrupo = true;
					cuerpoConversacion.setIdConversacion(conversacion.getIdConversacion());
					cuerpoConversacion.setIdReceptor(conversacion.getIdReceptor());
					cuerpoConversacion.setNombreConversacionRecepto(conversacion.getNombreConversacionRecepto());
				}
			}

			//Comparamos si la bandera de grupos es verdadera: es porque podemos enviar mensaje a este grupo
			if (existeEnListaGrupo) {
				/*En el cuerpo entrante comparamos si el campo rutaDocumento no contiene el siguiente String,
				  entonces es tratado como mensaje normal y a cualquier cosa que este en este campo se le asigna valor vacio:*/
				if (mensajes.getRutaDocumento().equals("") || !(mensajes.getRutaDocumento().contains("https://"))) {
					mensajes.setRutaDocumento("");
					mensajes.setStatusRutaDocumento(false);
				} else {
					//Si no contiene lo anterior este campo, entonces es tratado como documento:
					mensajes.setTexto("Documento");
					mensajes.setStatusRutaDocumento(true);
				}

				//Se genera la estructura de un nuevo mensaje con los campos por defecto
				mensajes.setIDConversacion(mensajes.getIDReceptor());

				mensajes.setStatusCreado(true);

				mensajes.setFechaEnviado(new Date());
				mensajes.setStatusEnviado(true);

				mensajes.setStatusLeido(false);
				mensajes.setFechaLeido(new Date(0));

				mensajes.setVisible(true);

				mensajes.setNombreConversacionReceptor(cuerpoConversacion.getNombreConversacionRecepto());

				mensajes.setConversacionVisible(true);

				//Se crea el mensaje
				mensajesService.crearMensaje(mensajes);

				/* MANDANDO NOTIFICACIONES A CADA MIEMBRO DEL GRUPO:
				  Cuando el receptor del mensaje es un grupo, el id del grupo esta conformado por todos los ids
				  de los usuarios que pertenecen a ese grupo de mensajeria*/
				List<User> usuarios = new ArrayList<>();
				//Tomamos el id del grupo y transformamos a un arreglo
				String[] lenguajesComoArreglo = mensajes.getIDReceptor().split("-");
				//iteramos este arreglo y buscamos a los respectivos usuarios y almacenamos un contenedor de usuarios
				for (String idUsuario : lenguajesComoArreglo) {
					Optional<User> usuario = userRepository.validarUsuario(idUsuario);
					usuario.ifPresent(usuarios::add);
				}

				//Comparamos: si el mensaje es de tipo documento, la notificacion debe decir algo referenciado a "Documento"
				if (mensajes.getTexto().equals("Documento")) {
					//Recorremos el arreglo de usuarios y mandamos notificaciones a cada integrante
					for (User usuario : usuarios) {
						mensajesServiceImpl.notificacion2(
								"Nuevo Mensaje de " + emisor.get().getNombre() + " a: "
										+ mensajes.getNombreConversacionReceptor(),
								"Nuevo documento", usuario.getToken());
					}
					//Se devuelve como respuesta lo siguiente en caso de ser un archivo enviado
					return ResponseEntity.status(HttpStatus.OK)
							.body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()), "Documento", mensajes.getIDConversacion()));
				}
				
				//Cuando el mensaje es texto, la notificacion debe dar referencia a eso mismo:
				for (User usuario : usuarios) {
					//Recorremos el arreglo de usuarios y mandamos notificaciones a cada integrante
					mensajesServiceImpl.notificacion2(
							"Nuevo Mensaje de " + emisor.get().getNombre() + " a: "
									+ mensajes.getNombreConversacionReceptor(),
							mensajes.getTexto(), usuario.getToken());
				}
				//Se devuelve como respuesta lo siguiente en caso de ser un mensaje enviado
				return ResponseEntity.status(HttpStatus.OK).body(
						new ResponseMensajes(String.valueOf(HttpStatus.OK.value()), "Se creo el mensaje a grupo", mensajes.getIDConversacion()));
			}

			// Comparar receptor en lista de contactos y cambio de valor de bandera:
			for (User contactos : listaConversacion) {
				if (contactos.getID().equals(receptor.get().getID())) {
					existeEnListaUsuario = true;
				}
			}

			//Comparamos si el usuario receptor se encuentra en nuestra lista de contactos aceptada
			if (existeEnListaUsuario) {
				//Comparamos si el mensaje enviado no contiene la siguiente cadena (referenciando no ser un archivo):
				if (mensajes.getRutaDocumento().equals("") || !(mensajes.getRutaDocumento().contains("https://"))) {
					mensajes.setRutaDocumento("");
					mensajes.setStatusRutaDocumento(false);
				} else {
					//Si lo contiene, es tratado como Documento/Archivo
					mensajes.setTexto("Documento");
					mensajes.setStatusRutaDocumento(true);
				}

				//Obtendremos las 2 posibles formas de encontrar una conversacion (idEmisor_idReceptor) o (idReceptor_idEmisor) 
				List<Mensajes> conversacionForma1 = new ArrayList<>();
				List<Mensajes> conversacionForma2 = new ArrayList<>();

				Iterable<Mensajes> conversacionIterable1 = mensajesService
						.verConversacion(mensajes.getIDEmisor() + "_" + mensajes.getIDReceptor());
				conversacionIterable1.forEach(conversacionForma1::add);

				Iterable<Mensajes> conversacionIterable2 = mensajesService
						.verConversacion(mensajes.getIDReceptor() + "_" + mensajes.getIDEmisor());
				conversacionIterable2.forEach(conversacionForma2::add);
				/*Si la busqueda encontro alguna conversacion en la base de datos, 
				  obtendremos el id de conversacion y lo pasaremos al mensaje nuevo,
				  pero si no encontro nada, lo asignaremos como debe ser (idConversacion = id_emisor_idReceptor)*/
				if (conversacionForma1.size() > 0) {
					mensajes.setIDConversacion(mensajes.getIDEmisor() + "_" + mensajes.getIDReceptor());
				} else if (conversacionForma2.size() > 0) {
					mensajes.setIDConversacion(mensajes.getIDReceptor() + "_" + mensajes.getIDEmisor());
				} else {
					mensajes.setIDConversacion(mensajes.getIDEmisor() + "_" + mensajes.getIDReceptor());
				}

				//Asignamos el cuerpo de un mensaje con sus valores por defecto
				mensajes.setStatusCreado(true);

				mensajes.setFechaEnviado(new Date());
				mensajes.setStatusEnviado(true);

				mensajes.setStatusLeido(false);
				mensajes.setFechaLeido(new Date(0));

				mensajes.setVisible(true);

				mensajes.setNombreConversacionReceptor(receptor.get().getNombre());

				mensajes.setConversacionVisible(true);

				//Se crea el mensaje en la base de datos
				mensajesService.crearMensaje(mensajes);

				//Validamos si el mensaje enviado es un archivo/documento y se envia la notificacion
				if (mensajes.getTexto().equals("Documento")) {
					mensajesServiceImpl.notificacion2("Nuevo Mensaje de " + emisor.get().getNombre(), "Nuevo documento",
							receptor.get().getToken());
					return ResponseEntity.status(HttpStatus.OK)
							.body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()), "Documento", mensajes.getIDConversacion()));
				}
				//En caso contrario solo se mandara una notificacion normal del mensaje
				mensajesServiceImpl.notificacion2("Nuevo Mensaje de " + emisor.get().getNombre(), mensajes.getTexto(),
						receptor.get().getToken());
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()), "Se creo el mensaje", mensajes.getIDConversacion()));
			} else {
				//En caso de que el receptor no este en ninguna lista (grupos o contactos) no podrá enviar mensaje a nadie
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
						"No puedes mandarle mensaje a este receptor", mensajes.getIDReceptor()));
			}
		//Manejo de excepciones desde el mas particular hasta el mas general:
		//Se devuelve cuerpo de respuesta con codigo de status, un mensaje de la causa y un objeto
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

	/**
	 *	Peticion tipo GET para listar una conversacion
	 * @param tokenAuth variable que se recibe por header para evaluar la secion de un usuario
	 * @param miID variable que recibimos por url para saber que usuario ara la peticion
	 * @param idConversacion parametro que recibimos por url para listar todos los mensajes que pertenecen a esa conversacion
	 * @return cuerpo de respuesta(ResponseMensajes)
	 */
	@GetMapping("/verConversacion/{miID}/{idConversacion}")
	public ResponseEntity<?> verConversacion(@RequestHeader (value = "tokenAuth") String tokenAuth,
											 @PathVariable(value = "miID") String miID,
											 @PathVariable(value = "idConversacion") String idConversacion) {
		try {
			//validacion donde se evalua que el idConversacion no sea menor a 49 caracteres
			if (idConversacion.length() < 49) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El id de la convbersacion no contiene los caracteres neesarios", null));
			}
			//valida si existe el usuario en la base de datos
			Optional<User> user = userRepository.validarUsuario(miID);
			//validacion donde se sabe si el usuario existe en algun grupo
			if(user.get().getIDGrupo().equals("")){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Usuario invalido",null));
			}
			//validacion donde se evalua el token que se genera al logueo
			if (user.get().getTokenAuth().equals(tokenAuth)) {
				//si el token coincide hace una consulta en la base de datos para encontrar todos los mensajes que tengan el mismo iidConversacion
				Iterable<Mensajes> iter = mensajesService.verConversacion(idConversacion);
				//cuerpo de respuesta (ResponseMensajes), se compone de un HttpStatus, mensaje ,lista de mensajes.
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()), "Lista de mensajes de conversacion: " + idConversacion, iter.iterator()));

			}
			//en caso de que no conincida el token se regresa un cuerpo de respuesta negativo
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Token invalido", null));
			//Manejo de excepciones desde el mas particular hasta el mas general:
			//Se devuelve cuerpo de respuesta con codigo de status, un mensaje de la causa y un objeto
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

	@PutMapping("eliminarMensaje/{idMensaje}/{idUsuario}") //&{idUsuario}
	public ResponseEntity<?> borrarMensaje(@RequestHeader("tokenAuth")String tokenAuth, @PathVariable(value = "idMensaje") String idMensaje, @PathVariable(value = "idUsuario") String idUsuario) {

		try {
			//Validaciones correspondientes de los datos entrantes
			if(idMensaje.length()<24 || idMensaje.length()>24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value())
						,"Tamaño del id del mensaje invalido",null));
			}
			
			if(idUsuario.length()<24 || idUsuario.length()>24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value())
						,"Usuario invalido",null));
			}
			
			//Buscar el mensaje y el usuario en la base de datos
			Optional<User> usuario = userRepository.findById(idUsuario);
			Optional<Mensajes> mensaje = mensajesService.buscarMensaje(idMensaje);
			
			//Validamos que exista el usuario y esté activo
			if (!usuario.isPresent() || usuario.get().getStatusActivo().equals("false")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value())
						,"Usuario invalido",null));
			}
			
			//Validamos el token entrante con el de la base de datos del usuario entrante
			if(!usuario.get().getTokenAuth().equals(tokenAuth) || usuario.get().getTokenAuth() == null || usuario.get().getTokenAuth().equals("")) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseMensajes(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value())
						,"Token invalido",null));
			}
			
			//Validacion del mensaje si existe
			if (!mensaje.isPresent()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value())
						,"El mensaje no fue encontrado",null));
			}
			//Validacion del mensaje si ya fue eliminado
			if(!mensaje.get().getVisible()) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value())
						,"El mensaje se elimino anteriormente",null));
			}

			//Cambiando el status del atributo visible del mensaje a false
			mensaje.get().setVisible(false);
			
			//Actualizar el mismo mensaje con este status diferente
			mensajesService.save(mensaje.get());

			//Cuerpo de respuesta cuando todo sale bien
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()),
					"Se borro el mensaje",mensaje.get().getID()));
			//Manejo de excepciones desde el mas particular hasta el mas general:
			//Se devuelve cuerpo de respuesta con codigo de status, un mensaje de la causa y un objeto
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

	/*Servicio para listar contactos de una persona: Incluye a tu jefe,
	  compañeros y todos los subordinados que esten abajo*/
	@GetMapping("listaContactos/{miId}")
	public ResponseEntity<?> verListaContactos(@RequestHeader("tokenAuth")String tokenAuth,@PathVariable(value = "miId") String miId) {
		try {
			//Validacion del tamaño del id de usuario entrante
			if(miId.length()<24 || miId.length()>24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()),
						"Tamaño del id invalido",null));
			}
			
			//Buscar al usuario por id
			Optional<User> existo = userRepository.validarUsuario(miId);
			
			//Validaciones del usuario si no existe y si no esta activo
			if(!existo.isPresent() || existo.get().getStatusActivo().equals("false")) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
						,"Usuario invalido",miId));
			}
			//Validacion del token de autentificacion entrante contra el token de autentificacion en la base de datos
			if(!existo.get().getTokenAuth().equals(tokenAuth) || existo.get().getTokenAuth() == null || existo.get().getTokenAuth().equals("")) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseMensajes(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value())
						,"Token invalido",null));
			}
			
			//Da como respuesta una lista de contactos del usuario
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value())
					,"Lista de contactos",listaConversacion(miId)));
		/*Manejo de excepciones desde el mas particular hasta el mas general:
		  Se devuelve cuerpo de respuesta con codigo de status, un mensaje de la causa y un objeto*/
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

	/*Servicio para actualizar un mensaje que no esta leido a leido*/
	@PutMapping("actualizarLeido/{idUsuario}")
	public ResponseEntity<?> actualizarLeido(@RequestHeader("tokenAuth")String tokenAuth,@PathVariable(value = "idUsuario") String idUsuario,@RequestBody Mensajes mensajes) {
		/*Este servicio requiere de:
		Un header llamado "tokenAuth"
		Un idEmisor por url
		Un cuerpo de tipo Mensajes que debe tener fecha y idMensaje*/
		try {
			//Validaciones correspondientes del JSon de entrada
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
			//Buscamos un usuario en la base de datos con el idUsuario entrante
			Optional<User> existo = userRepository.validarUsuario(idUsuario);
			
			//Validamos que exista y siga activo
			if(!existo.isPresent() || existo.get().getStatusActivo().equals("false")) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
						,"Usuario invalido",idUsuario));
			}
			//Validamos que el token entrante y el token de la base de datos sea el mismo
			if(!existo.get().getTokenAuth().equals(tokenAuth) || existo.get().getTokenAuth() == null || existo.get().getTokenAuth().equals("")) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseMensajes(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value())
						,"Token invalido",null));
			}
			//Buscamos un mensaje en la base de datos por el idMensaje entrante en el cuerpo de Mensajes
			Optional<Mensajes> mensaje = mensajesService.buscarMensaje(mensajes.getID());
			//Validamos que exista en la base de datos
			if(!mensaje.isPresent()) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
						"El mensaje no existe en la base de datos",mensajes.getID()));
			}
			//Validamos que si el status de leido ya es verdadero, devuelva la siguiente respuesta: 
			if (mensaje.get().isStatusLeido()) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()),
						"El mensaje ya fue leido anteriormente",mensaje.get().getID()));
			} else {
				//Si el mensaje sigue sin leerse, actualizamos la fecha de leido y cambiamos el campo de statusLeido a true
				mensaje.get().setFechaLeido(mensajes.getFechaLeido());

				mensaje.get().setStatusLeido(true);
				//Guardamos en la base el mensaje con sus nuevos valores
				mensajesService.save(mensaje.get()); 
				//Devolvemos una respuesta referenciada
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()),
						"Actualizado",mensaje.get().getID()));
			}
			/*Manejo de excepciones desde el mas particular hasta el mas general:
			  Se devuelve cuerpo de respuesta con codigo de status, un mensaje de la causa y un objeto*/
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
	
	/*Inicializamos un contenedor global total de subordinados de un usuario*/
	List<User> myArregloUsuario = new ArrayList<>();
	//Metodo para obtener lista de Jefe y Compañeros de un usuario
	public List<User> listaConversacion(String miId) {
		//Inicializamos contenedor para listar usuarios
		List<User> listaConversacion = new ArrayList<>();
		listaConversacion.clear();
		//Buscamos un usuario por id en la base de datos
		Optional<User> existo = userRepository.validarUsuario(miId);
		//Buscamos al jefe por id de mi superiorInmediato
		Optional<User> jefe = userRepository.validarUsuario(existo.get().getIDSuperiorInmediato());

		//Validamos que el jefe exista y siga activo y añadimos a la lista de conversacion
		if (jefe.isPresent() && jefe.get().getStatusActivo().equals("true")) {
			jefe.ifPresent(listaConversacion::add);
		}

		//Obtenemos a nuestro compañeros (hermanos) a partir de nuestro jefe y nos quitamos de la lista
		Iterable<User> hermanos = userRepository.findByBossId(existo.get().getIDSuperiorInmediato());
		for (User hermano : hermanos) {
			if (!hermano.getID().equals(miId) && !hermano.getIDSuperiorInmediato().equals("")
					&& !hermano.getIDGrupo().equals("") && hermano.getIDGrupo().equals(existo.get().getIDGrupo())
					&& hermano.getStatusActivo().equals("true")) {
				//Agregamos a la lista de conversacion
				listaConversacion.add(hermano);
			}
		}

		//Buscamos a nuestros subordinados (Hijos) que tenemos acargo por nuestro id en la base de datos
		Iterable<User> misHijos = userRepository.findByBossId(miId);
		List<User> hijosValidos = new ArrayList<>();

		//Recorremos la lista saliente de la base de datos y filtramos y agregamos al contenedor de usuarios validos
		for (User hijo : misHijos) {
			if (hijo.getIDGrupo().equals(existo.get().getIDGrupo()) && hijo.getStatusActivo().equals("true")
					&& !hijo.getIDSuperiorInmediato().equals("")) {
				hijosValidos.add(hijo);
			}
		}

		//Llamamos a un metodo pasandole la lista de hijos/subordinados validos nuestros
		lista(misHijos);

		//Recorremos el contenedor global de todos los subordinados y agregamos a la lista de conversacion
		for (User usuariosHijos : this.myArregloUsuario) {
			listaConversacion.add(usuariosHijos);
		}

		//Limpiamos el contenedor de subordinados global para que no continue acumulando los mismos subordinados 
		this.myArregloUsuario.clear();

		//devolvemos la lista
		return listaConversacion;
	}

	//Metodo recursivo que busca todos los subordinados bajo nuestro cinturon gerencial:
	public void lista(Iterable<User> listaHijos) {
		List<User> contenedor = new ArrayList<>();
		for (User usuario : listaHijos) {
			//Filtramos para no tener usuarios de mas o inactivos
			if (!usuario.getIDSuperiorInmediato().equals("") && !usuario.getIDGrupo().equals("")
					&& usuario.getStatusActivo().equals("true")) {
				//agregamos usuario valido a un contenedor local
				this.myArregloUsuario.add(usuario);
				//Buscamos subordinados de la lista entrante
				Iterable<User> listaNietos = userRepository.findByBossId(usuario.getID());
				listaNietos.forEach(contenedor::add);
				//Cuando el contenedor local NO esta vacio, vuelve a iterar el mismo metodo hasta que el contenedor esté vacio 
				if (contenedor.size() > 0) {
					lista(listaNietos);
				}
			}
		}
	}

	/**
	 * 	Peticion tipo PUT para actualizar el campo de una conversacion a falso ya que los mensajes no se pueden enlimar solo ocultar
	 * @param tokenAuth variable que se recibe por header para evaluar la secion de un usuario
	 * @param miID variable que recibimos por url para saber que usuario ara la peticion
	 * @param idConversacion variable que recibimos por url para saber que conversacion se va actualizar
	 * @return cuerpo de respuesta(ResponseMensajes)
	 */
	@PutMapping("/eliminarConversacion/{miID}/{idConversacion}")
	public ResponseEntity<?> cambiarStatusConversacion(@RequestHeader(value = "tokenAuth")String tokenAuth,
													   @PathVariable (value  ="miID") String miID,
													   @PathVariable(value = "idConversacion") String idConversacion) {
		try{
			//validamos que el idConversacion no sea menor a 49
			if(idConversacion.length()<49) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El id de la conversacion no contiene los caracteres neesarios", null));
			}
			//validamos que el usuario exista en la base de datos
			Optional<User> user= userRepository.validarUsuario(miID);
			//validamos que el usuario pertenesca a un grupo
			if(user.get().getIDGrupo().equals("")){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Usuario invalido",null));
			}
			//validamos el token recibido por header coicida con el del usuario en la base de datos
			if(!user.get().getTokenAuth().equals(tokenAuth)){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Token invalido", null));
			}
			//Iniciamos un contenedor para listar todos los mensajes que pertenecen a la conversacion
			List<Mensajes> list = new ArrayList<>();
			//hacemos una consulta a la base de datos 
			Iterable<Mensajes> iter = mensajesService.verConversacion(idConversacion);
			//itera ttoda la lista y la agrega al contenedor list
			iter.forEach(list::add);
			//valida que el contenido sea difernete a 0
			if(list.size()<1) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMensajes(String.valueOf(HttpStatus.NOT_FOUND.value()), "No existe la conversacion",null));
			}
			//valida si la conversacion ya fue eliminada
			if(!list.get(0).isConversacionVisible()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMensajes(String.valueOf(HttpStatus.NOT_FOUND.value()), "La conversacion ya se elimino anteriormente",null));
			}
			//si la conversacion no ha sido eliminada itera todos los mensajes
			for (Mensajes msg : iter) {
				//valores asignado automaticamente a los mensajes que pertenecen a esa conversacion
				msg.setConversacionVisible(false);
				//guarda el mensaje de cada iteracion
				mensajesService.save(msg);
			}
			//cuerpo de respuesta
			return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMensajes(String.valueOf(HttpStatus.CREATED.value()), "Se elimino la conversacion","Numero de Mensajes eliminados: "+list.size()));
			//Manejo de excepciones desde el mas particular hasta el mas general:
			//Se devuelve cuerpo de respuesta con codigo de status, un mensaje de la causa y un objeto
		}catch (MongoSocketException e) {
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

	/*Servicio para listar grupos a los que un usuario pertenece*/
	@GetMapping("listaGrupos/{miId}")
	public ResponseEntity<?> listaGrupos(@RequestHeader("tokenAuth")String tokenAuth,@PathVariable(value = "miId") String miId) {
		/*Este servicio recibe:
		Un header con nombre "TokenAuth" y
		Un idUsuario por url*/
		try {
			//Validaciones del tamaño del idUsuario entrante
			if(miId.length()<24 || miId.length()>24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()),
						"Tamaño del id incorrecto",null));
			}
			//Buscamos un usuario por este idEntrante
			Optional<User> existo = userRepository.validarUsuario(miId);
			//Validamos que exista este usuario
			if(!existo.isPresent() || existo.get().getStatusActivo().equals("false")) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
						"Usuario invalido",miId));
			}
			//Comparamos el token entrante contra el del usuario encontrado
			if(!existo.get().getTokenAuth().equals(tokenAuth) || existo.get().getTokenAuth() == null || existo.get().getTokenAuth().equals("")) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseMensajes(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value())
						,"Token invalido",null));
			}
			//Llamamos al metodo que lista los grupos a los que pertenece un usuario y pasamos nuestro Id
			List<Conversacion> grupos = grupos(miId);
			//Devuelve como respuesta algo referenciado:
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()),
					"Lista de grupos",grupos));
		/*Manejo de excepciones desde el mas particular hasta el mas general:
		  Se devuelve cuerpo de respuesta con codigo de status, un mensaje de la causa y un objeto*/
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

	/*Servicio para listar todas las personas que pertenecen a un grupo*/
	@GetMapping("listaPersonasGrupo/{idGrupo}/{idUsuario}")
	public ResponseEntity<?> listaDePersonasEnGrupo(@RequestHeader("tokenAuth")String tokenAuth,@PathVariable(value = "idGrupo") String idGrupo,@PathVariable(value = "idUsuario")String idUsuario) {
		/*Requiere:
		 * Header por nombre 'tokenAuth'
		 * idGrupo
		 * idUsuario*/
		try {
			//Validacion del tamaño del idGrupo
			if(idGrupo.length()<74) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()),
						"No es un chat de grupo",null));
			}
			//Buscamos el usuario en la base de datos 
			Optional<User> existo = userRepository.validarUsuario(idUsuario);
			//Validamos que exista y esté activo
			if(!existo.isPresent() || existo.get().getStatusActivo().equals("false")) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMensajes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
						"Usuario invalido",null));
			}
			//Validamos que el token entrante y el de la base sean iguales
			if(!existo.get().getTokenAuth().equals(tokenAuth)) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseMensajes(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value())
						,"Token invalido",null));
			}
			//Creamos un nuevo contenedor
			List<User> usuarios = new ArrayList<>();
			//Convertimos el idGrupo en arreglo de tipo String
			String[] lenguajesComoArreglo = idGrupo.split("-");
			//Iteramos el arreglo
			for (String idUsuarios : lenguajesComoArreglo) {
				//Buscamos cada usuario en la base de datos
				Optional<User> usuario = userRepository.validarUsuario(idUsuarios);
				//Agregamos a contenedor
				usuario.ifPresent(usuarios::add);
			}
			//Retornamos una respuesta similar a lo que se realizó
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()),
					"Lista de personas en grupo",usuarios));
		/*Manejo de excepciones desde el mas particular hasta el mas general:
		  Se devuelve cuerpo de respuesta con codigo de status, un mensaje de la causa y un objeto*/
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

	//Metodo que lista grupos a los que perteneces y pueden mandar mensaje:
	public List<Conversacion> grupos(String miId) {
		//Creamos contenedor para listar los grupos
		List<Conversacion> grupos = new ArrayList<>();
		//Buscamos un usuario para poder realizar movimientos
		Optional<User> existo = userRepository.validarUsuario(miId);
		//Buscamos a nuestro jefe en caso de tenerlo
		Optional<User> jefe = userRepository.validarUsuario(existo.get().getIDSuperiorInmediato());
		
		// Si tenemos jefe, podremos tener un chat de grupo con nuestro jefe 
		if (jefe.isPresent()) {
			//Inicializamos un nuevo contenedor para almancenar ids de los usuarios que son nuestros compañeros 
			List<User> listaPadre = new ArrayList<>();
			//Instanciamos un nuevo StringBuilder que contendra el id de la conversacion de grupo
			StringBuilder idConversacionPadre = new StringBuilder();
			//Instanciamos un nuevo cuerpo para una nueva conversacion
			Conversacion conversacion = new Conversacion();
			//Buscamos y filtramos a nuestros compañeros por id de jefe, filtramos y añadimos a contenedor listaPadre
			Iterable<User> misHermanos = userRepository.findByBossId(jefe.get().getID());
			for(User hermanos : misHermanos) {
				if(hermanos.getStatusActivo().equals("true")) {
					listaPadre.add(hermanos);
				}
			}
			/*Dado que un grupo se forma de 3 personas contando el jefe,
			  comparamos si la listaPadre contiene almenos 2 subordinados, se creará el nuevo grupo*/
			if (listaPadre.size() < 2) {
				idConversacionPadre.append("");
			} else {
				//Al cuerpo de conversacion le agregamos nuevos valores a sus atributos
				idConversacionPadre.append(jefe.get().getID());

				for (User hijos : listaPadre) {
					idConversacionPadre.append("-" + hijos.getID());
				}
				
				conversacion.setIdConversacion(idConversacionPadre.toString());
				conversacion.setIdReceptor(idConversacionPadre.toString());
				conversacion.setNombreConversacionRecepto(
						"Chat grupal con " + jefe.get().getNombreRol() + " " + jefe.get().getNombre());
				//Añadimos a contenedor de grupos
				grupos.add(conversacion);
			}
		}

		//En caso de no tener jefe, nosotros podemos tener 1 grupo con nuestros subordinados inmediatos
		List<User> listaHijos = new ArrayList<>();
		//Iteramos a nuestro subordinados existentes en la base de datos, filtramos y agregamos a la listaHijos(subordinados)
		Iterable<User> misHijos = userRepository.findByBossId(miId);
		for(User hijos : misHijos) {
			if(hijos.getStatusActivo().equals("true")) {
				listaHijos.add(hijos);
			}
		}
		/*De igual manera cuando el contenedor de subordinados es mayor a 1 podemos tener una conversacion de grupo
		Ya que un grupo se conforma de almenos 3 personas, 2 personas es una conversacion de 1-1*/ 
		if (listaHijos.size() > 1) {
			/*Creamos un nuevo objeto StringBuilder que contendra 
			el id de la conversacion grupal: (ids de todos los integrantes de grupo separados por '-')*/
			StringBuilder idMiConversacion = new StringBuilder();
			//Creamos nuevo cuerpo de conversacion
			Conversacion miConversacion = new Conversacion();
			//Comparamos si la lista de subordinados es mayor a 1
			if (listaHijos.size() < 2) {
				idMiConversacion.append("");
			} else {
				//Agregamos nuevos valores a la conversacion
				idMiConversacion.append(miId);

				for (User hijos : listaHijos) {
					idMiConversacion.append("-" + hijos.getID());
				}
				
				miConversacion.setIdConversacion(idMiConversacion.toString());
				miConversacion.setIdReceptor(idMiConversacion.toString());
				miConversacion.setNombreConversacionRecepto(
						"Chat grupal con " + existo.get().getNombreRol() + " " + existo.get().getNombre());
				//Agregamos a la lista
				grupos.add(miConversacion);
			}
		}
		/*Una vez teniendo la lista de grupos, tendremos que guardarla en la base de datos,
		esto en caso de que un usuario sea eliminado o agregado como nuestro compañero,
		nosotros poderactualizar la conversacion*/ 
		//Iteramos la lista de grupos y buscamos en la base de datos si coincide con alguno
		for(Conversacion conv :grupos) {
			GruposMensajeria gruposMensajeria = new GruposMensajeria();
			/*Buscamos un grupo por el nombre (cambia el numero de personas en un grupo
			si se eliminan o se agregan, pero el nombre de la conbersacion siempre es el mismo)*/
			Optional<GruposMensajeria> grupoMensajeria = gruposMensajeriaRepository.buscarPorNombreOptional(conv.getNombreConversacionRecepto());
			//Cuando se encuentra un grupo en la base de datos
			if(grupoMensajeria.isPresent()) {
				List<String> contenedorLocal = new ArrayList<>();
				List<String> contenedorBD = new ArrayList<>();
				
				//Convertimos a arreglo de tipo String el id de la conversacion grupal de la logica de programacion
				String[] arregloLocal = conv.getIdConversacion().split("-");
				//Recorremos el arreglo y agregamos a un contenedor local
				for(String posicion : arregloLocal) {
					contenedorLocal.add(posicion);
				}
				//Convertimos a arreglo de tipo String el id de la conversacion grupal encontrada en la base de datos
				String[] arregloBd = grupoMensajeria.get().getIdConversacion().split("-");
				//Recorremos el arreglo y agregamos a un contenedor local
				for(String posicion : arregloBd) {
					contenedorBD.add(posicion);
				}
				//Comparamos si el numero de personas en el grupo local es diferente que el encontrado en la base de datos
				if(contenedorLocal.size() != contenedorBD.size()) {
					//Buscamos todos los mensajes pertenecientes a esa conversacion
					Iterable<Mensajes> mensajes = mensajesService.verConversacion(grupoMensajeria.get().getIdConversacion());
					/*Actualizamos todos los mensajes con el viejo id de conversacion al
					  nuevo idConversacion (personas agregadas o eliminadas)*/
					for(Mensajes mensajes2 : mensajes) {
						mensajes2.setIDConversacion(conv.getIdConversacion());
						mensajesService.save(mensajes2);
					}
					//Actualizamos el idConversacion en la base de datos
					grupoMensajeria.get().setIdConversacion(conv.getIdConversacion());
					grupoMensajeria.get().setIdReceptor(conv.getIdReceptor());
					grupoMensajeria.get().setNombreConversacion(conv.getNombreConversacionRecepto());
					//Guardamos cambios de la nueva conversacion
					gruposMensajeriaRepository.save(grupoMensajeria.get());
				}
			}else {
				//En caso de que la conversacion del usuario no exista en la base se crea para guardarla
				gruposMensajeria.setIdConversacion(conv.getIdConversacion());
				gruposMensajeria.setIdReceptor(conv.getIdReceptor());
				gruposMensajeria.setNombreConversacion(conv.getNombreConversacionRecepto());
				//Guardamos el nuevo grupo que pertenece al usuario
				gruposMensajeriaRepository.save(gruposMensajeria);
			}
		}
		//Devolvemos la lista de grupos a los que pertenece un usuario
		return grupos;
	}

	/**
	 * 	Peticion GET que trae todas las conversaciones a las que participa un usuario
	 * @param tokenAuth variable que se recibe por header para evaluar la secion de un usuario
	 * @param idEmisor variable que recibimos por url para saber que usuario ara la peticion
	 * @return cuerpo de respuesta
	 */
	@GetMapping("/listarConversaciones/{idEmisor}")
	public ResponseEntity<?> listarConversaciones(@RequestHeader(value = "tokenAuth")String tokenAuth,
												  @PathVariable(value ="idEmisor")String idEmisor){
		try{
			//validacion donde el idEmisor no puede ser mayor o menor a 24
			if(idEmisor.length()<24 || idEmisor.length()>24){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El id del usuario no tiene los caracteres necesarios",null));
			}
			//busca al usuario en la base de datos
			Optional<User> user = userRepository.validarUsuario(idEmisor);
			//evalua si existe este usuario
			if (!user.isPresent()){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMensajes(String.valueOf(HttpStatus.NOT_FOUND.value()),"No se encuentra este usuario",null));
			}
			//evalua si el usuario pertenece a un grupo
			if(user.get().getIDGrupo().equals("")){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Usuario invalido",null));
			}
			//evlaua que el token entrante sea igual al que esta en la base de datos
			if(!user.get().getTokenAuth().equals(tokenAuth)){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Token invalido",null));
			}
			//Esta seccion sirve para traer todos los id conversacion distintos sin repetise
			MongoCollection mongoCollection = monogoTemplate.getCollection("Mensajes");
			DistinctIterable distinctIterable = mongoCollection.distinct("idConversacion", String.class);
			MongoCursor mongoCursor = distinctIterable.iterator();
			//se crea un contenedor donde se almacenaran las difernetes conversaciones
			List<Conversacion> lConversacion3 = new ArrayList<>();
			//iteracion de las diferentes conversaciones encontrada
				while (mongoCursor.hasNext()) {
					//instanciamos el cuerpo de respuesta de conversacion
					Conversacion mConv = new Conversacion();
					//se convierte en string el id de conversacion consultada
					String idConversacion = (String) mongoCursor.next();
					//se le asigna un valor al atributo idConversacion en cada iteracion
					mConv.setIdConversacion(idConversacion);
					//se busca en todos los mensajes de una conversacion para saber elemisor y receptor de la conversacion
					Iterable<Mensajes> iter = mensajesService.verConversacion(idConversacion);
					//iteramos la lista consultada anteriormente
					Iterator<Mensajes> cursor = iter.iterator();
					//iteramos cada mensaje de la lista cursor
					while (cursor.hasNext()) {
						//si encuentra algo en la iteracion se guarda como mensaje
						Mensajes mensajes = cursor.next();
						//comparo en el mensaje si el emisor es diferente al receptor
						if ( !mensajes.getIDReceptor().equals(idEmisor)  ) {
							//se agregan los valores por defecto al cuerpo de conversacion
							mConv.setIdReceptor(mensajes.getIDReceptor());
							mConv.setNombreConversacionRecepto(mensajes.getNombreConversacionReceptor());
							mConv.setIdConversacion(mensajes.getIDConversacion());
							mConv.setIdEmisor(mensajes.getIDEmisor());
						//comparacion si el emisor es igual receptor
						}else if(mensajes.getIDReceptor().equals(idEmisor)) {
							//busco al usuario emisor entrante en la base de datos
							Optional<User> user3 = userRepository.validarUsuario(mensajes.getIDEmisor());
							//se agregan valores al cuerpo de conversacion
							mConv.setIdReceptor(mensajes.getIDEmisor());
							mConv.setNombreConversacionRecepto(user3.get().getNombre());
							mConv.setIdConversacion(mensajes.getIDConversacion());
							mConv.setIdEmisor(mensajes.getIDEmisor());
						}
					}
					//evaluo si la conversacion contiene al idEmisor
					if( mConv.getIdConversacion().contains(idEmisor)){
						//evaluo si la conversacion es menor a 50
						if(mConv.getIdConversacion().length()<50){
							//evaluo que el receptor no sea nulo
							if(mConv.getIdReceptor() !=null ) {
								//busco  el usuario en la base de datos para agregar su roll
								Optional<User> user2 = userRepository.validarUsuario(mConv.getIdReceptor());

								mConv.setNombreRol(user2.get().getNombreRol());
							}
							//agregamos el cuerpo de conversacion a el contenedor declarado anteriormente
							lConversacion3.add(mConv);
						}
					}
				}
			//respuesta valida
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()),"Lista de conversaciones",lConversacion3));
		/*Manejo de excepciones desde el mas particular hasta el mas general:
		  Se devuelve cuerpo de respuesta con codigo de status, un mensaje de la causa y un objeto*/
		}catch (MongoSocketException e) {
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

	/**
	 *		Peticion GET que trae todos los mensajes que ha enviado o recivido un usuario(modulo de reportes)
	 * @param tokenAuth variable que se recibe por header para evaluar la secion de un usuario
	 * @param miID variable que recibimos por url para saber que usuario ara la peticion
	 * @param idEmisor variable que se consultara en la base de datos
	 * @return cuerpo de respuesta(ResponseMensajes)
	 */

	@GetMapping("listarMensajesRecividos/{miID}/{idEmisor}")
	public ResponseEntity<?> listarMensajesRecividos(@RequestHeader(value = "tokenAuth")String tokenAuth,
													 @PathVariable (value  ="miID") String miID,
													 @PathVariable (value = "idEmisor")String idEmisor){
		try{
			//validacion de que el idUsuario a evaluar no sea mayor o menor a 24
			if(idEmisor.length()<24 || idEmisor.length()>24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Tamaño del id invalido",null));
			}
			//valida si el usuario
			Optional<User> user=userRepository.validarUsuario(idEmisor);
			//valida si el usuario no esta en la base de datos
			if(!user.isPresent()){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMensajes(String.valueOf(HttpStatus.NOT_FOUND.value()),"no se encuetra el usuario",null));
			}
			//valida si el usuario que ara la peticion exite en la base de datos
			Optional<User> user2= userRepository.validarUsuario(miID);
			//valida si el usuario que ara la peticion si pertenece a un grupo
			if(user2.get().getIDGrupo().equals("")){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Usuario invalido",null));
			}
			//validacion donde el token que recibimos en el header si es igual que el que esta en la base de datos
			if(user2.get().getTokenAuth().equals(tokenAuth)) {
			//consulta a la base de datos donde trae todos los mensajes
				Iterable<Mensajes> msg = mensajesRepository.findAll();
				//se crea un nuevo contenedor donde habra una lista de los mensajes consultados
				List<Mensajes> lMensajes = new ArrayList<>();
				//iteracion de los mensajes
				for (Mensajes msg2 : msg) {
					//condicion donde si el idConversacion contiene idEmisor
					if (msg2.getIDConversacion().contains(idEmisor)) {
						//si lo contiene se agrega al contenedor declarado anteriormente
						lMensajes.add(msg2);
					}
				}
				//cuerpo de respuesta valido
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMensajes(String.valueOf(HttpStatus.OK.value()),"Lista de mensajes",lMensajes));
			}
			//cuerpo de respuesta invalido
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMensajes(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Token invalido",null));
		/*Manejo de excepciones desde el mas particular hasta el mas general:
		  Se devuelve cuerpo de respuesta con codigo de status, un mensaje de la causa y un objeto*/
		}catch (MongoSocketException e) {
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
}
