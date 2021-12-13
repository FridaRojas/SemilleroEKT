package com.ekt.Servicios.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ekt.Servicios.entity.*;
import com.ekt.Servicios.repository.BroadCastRepositorio;
import com.ekt.Servicios.repository.GroupRepository;

import com.ekt.Servicios.service.BroadCastServicioImpl;
import com.ekt.Servicios.service.MensajesService;

import com.mongodb.MongoException;
import com.mongodb.MongoSocketException;
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
	private GroupRepository groupRepository;
	
	@Autowired
	private MensajesService mensajesService;

	/*Servicio para listar contactos del administrador broadCast*/
	@GetMapping("/listaUsuarios/{miId}")
	public ResponseEntity<?> listaUsuariosGeneral(@RequestHeader(value = "tokenAuth")String tokenAuth,
												  @PathVariable (value = "miId")String miId){
		/*Necesita:
		 * IdUsuarioBroadCast por url
		 * Header con nombre 'tokenAuth'*/
		try{
			//Validamos la longitud del idUsuario entrante
			if (miId.length() < 24 || miId.length() > 24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El tamaño del idEmisor no es valido", null));
			}
			//Buscamos en la base de datos a un usuario por este id
			Optional<User> existo = userRepository.validarUsuario(miId);
			//Validamos que el token entrante no sea nulo
			if(existo.get().getTokenAuth()== null){
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),"Token no valido", null));
			}
			//Validamos que el campo de grupo del usuario no esté vacio
			if(existo.get().getIDGrupo().equals("")){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Usuario invalido",null));
			}
			//Validamos que el token entrate sea igual que el token en base de datos
			if(!existo.get().getTokenAuth().equals(tokenAuth)){
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),"Token no coincide",null));
			}
			//Validamos que el usuario exista y su rol se el de un BROADCAST
			if(!existo.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"No se encontro usuario Broadcast",null));
			}
			if(!existo.get().getNombreRol().equals("BROADCAST") || !existo.get().getIDSuperiorInmediato().equals("-1")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"No es un usuario BROADCAST",null));
			}
			//Buscamos todos los usuarios que pertenezcan al mismo grupo que el broadCast
			Iterable<User> listaUsuarios =  userRepository.findByGroupID(existo.get().getIDGrupo());
			//Iteramos esta lista para filtrar y agregamos al contenedor
			List<User> miLista = new ArrayList<>();
			for(User usuario : listaUsuarios) {
				if(usuario.getStatusActivo().equals("true") && !usuario.getID().equals(existo.get().getID()) && usuario.getIDGrupo().equals(existo.get().getIDGrupo())) {
					miLista.add(usuario);
				}
			}
			//Devolvemos una respuesta referenciada a lo resuelto
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBroadcast(String.valueOf(HttpStatus.ACCEPTED.value()),"Lista de usuarios",miLista));
		/*Manejo de excepciones desde el mas particular hasta el mas general:
		  Se devuelve cuerpo de respuesta con codigo de status, un mensaje de la causa y un objeto*/
		}catch (MongoSocketException e) {
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

	/**
	 *	Peticion GET para traer todos los mensajes que tiene el BROADCAST segun el grupo al que pertenesca
	 * @param tokenAuth variable que se recibe por header para evaluar la secion de un usuario
	 * @param miID variable que recibimos por url para saber que usuario ara la peticion
	 * @return cuerpo de respuesta
	 */
	@GetMapping("/mostarMensajesdelBroadcast/{miID}")
	public ResponseEntity<?>listarMensajes(@RequestHeader(value = "tokenAuth")String tokenAuth,
										   @PathVariable (value = "miID")String miID){
		try {
			//validacion donde el id ingresado no sea menor o mayor a 25
			if(miID.length()<24 || miID.length()>24){
				ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El tamaño del id no es correcto", null));
			}
			//validacion de usuario BROADCAST
			Optional<User> user = userRepository.validarUsuario(miID);
			//valicacion donde no el usuario existe en la base de datos
			if(!user.isPresent()) {
				ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()), "El usuario no existe",null));
			}
			//validacion para saber si el usuario pertenece a un grupo
			if(user.get().getIDGrupo().equals("")){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Usuario invalido",null));
			}
			//validacion para saber si el token recibido en el header es igual al que esta en la base de datos
			if(!user.get().getTokenAuth().equals(tokenAuth)){
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),"Token no coincide",null));
			}

			if(user.isPresent()){
				//condicion donde se sabe si el usuario es usuario broadcast
				if(user.get().getNombreRol().equals("BROADCAST") || user.get().getIDSuperiorInmediato().equals("-1")) {
					//consulta a la base de datos de todos los mensajes que pertenecen a ese al grupo del usuario BROADCAST
					Iterable<BroadCast> brd = broadCastRepositorio.buscarMensajesEnGrupo(user.get().getIDGrupo());
					//respuesta valida
					return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBroadcast(String.valueOf(HttpStatus.ACCEPTED.value()),"Lista de mensajes del Broadcast",brd));

				}
			}
			//respuesta invalida
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"El usuario ingresado no es un usuario BROADCAST", null));
			/*Manejo de excepciones desde el mas particular hasta el mas general:
		  	Se devuelve cuerpo de respuesta con codigo de status, un mensaje de la causa y un objeto*/
		}catch (MongoSocketException e) {
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

	/**
	 *	Peticion tipo POST para que un usuario normal pueda mandar mensaje al usuario BROADCAST del mismo grupo
	 * @param tokenAuth variable que se recibe por header para evaluar la secion de un usuario
	 * @param broadCast Cuerpo de tipo broadcast que recibe este servicio
	 * @param miID variable que recibimos por url para saber que usuario ara la peticion
	 * @return cuerpo de respuesta
	 */
	@PostMapping("/crearMensajeBroadcast/{miID}")
	public ResponseEntity<?> crearMensajeBroadCast(@RequestHeader("tokenAuth")String tokenAuth,
												   @RequestBody BroadCast broadCast,
												   @PathVariable  (value = "miID")String miID) {
			try{
				//validacion donde el Emisor no puede enviarse nulo
				if (broadCast.getIdEmisor() == null || broadCast.getAsunto() == null || broadCast.getDescripcion() == null) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()), "No se encuentra el dato", null));
				}
				//validacion donde el emisor no puede ser una cadena vacia o con un mensaje null
				if (broadCast.getIdEmisor().equals("") || broadCast.getIdEmisor().equals("null")) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El campo idEmisor es no puede estar vacio", null));
				}
				//validacion donde el idEmisor no puede ser mayor o menor a 24 caracteres
				if (broadCast.getIdEmisor().length() < 24 || broadCast.getIdEmisor().length() > 24) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El tamaño del idEmisor no es valido", null));
				}
				//validacion donde el asunto no puede ser vacio o null
				if (broadCast.getAsunto().equals("") || broadCast.getAsunto().equals("null")) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El campo Asunto no puede estar vacio", null));
				}
				//validacion donde el asunto no puede venir con menos de 5 caracteres
				if (broadCast.getAsunto().length() < 5) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El tamaño del Asunto no es valido", null));
				}
				//validacion donde la descripcion no puede venir con menos de 10 caracteres
				if (broadCast.getDescripcion().length() < 10) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El tamaño del texto descripcion debe ser al menos de 1 caracter", null));
				}

				//busca a el usuario en la base
				Optional<User> user = userRepository.validarUsuario(miID);
				//busca al usuario broadcast en la base de datos que pertenece al mismo grupo
				Optional <User> broadcast = userRepository.findRolL("BROADCAST",user.get().getIDGrupo());
				//trae el id del broadcast de la base de datos
				Optional<User> user2= userRepository.validarUsuario(broadcast.get().getID());
				//validacion donde el usuario que enviara el mensaje pertenece a algun grupo
				if(user2.get().getIDGrupo().equals("")){
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Usuario invalido",null));
				}
				//validacion donde se evalua que el token de la base de datos sea difernte al ingresado en el header
				if(!user.get().getTokenAuth().equals(tokenAuth)) {
					return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), "Token no coincide", null));
				}
				//instancio la clase d broadcast para poder insertar los valores correspondientes
				BroadCast broadCastM = new BroadCast();
				broadCastM.setIdEmisor(broadCast.getIdEmisor());
				broadCastM.setAsunto(broadCast.getAsunto());
				broadCastM.setDescripcion(broadCast.getDescripcion());
				//condicion donde evaluamos que el id entrante sea igual al de la base de datos
					if (user.get().getID().equals(miID)) {
						//asignamos los valores por default
						broadCastM.setNombreEmisor(user.get().getNombre());
						broadCastM.setAtendido(false);
						broadCastM.setIdGrupo(user2.get().getIDGrupo());
						broadCastM.setIdBroadcast(user2.get().getID());
						//guardamos en la base de datos
						broadCastRepositorio.save(broadCastM);
						//mandamos a traer este metodo para poder hacer las notificaciones push al usuario BROADCAST
						broadCastServicio.notificacion2(broadCastM.getNombreEmisor() + " Envio un mensaje", broadCast.getAsunto(),user2.get().getToken());
						//respuesta valida
						return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseBroadcast(String.valueOf(HttpStatus.CREATED.value()), "Se creo el Mensaje a BROADCAST", "Usuario: "+broadCastM.getNombreEmisor()+"  Rol: "+user.get().getNombreRol()));
					}
					//respuesta invalida
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()), "No se encuentra el usuario BROADCAST", null));
			/*Manejo de excepciones desde el mas particular hasta el mas general:
		  	Se devuelve cuerpo de respuesta con codigo de status, un mensaje de la causa y un objeto*/
			}catch (MongoSocketException e) {
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

	/**
	 * 	Peticion GET para traer todos los mensajes que el usuario a evaluar manda a BROADCAST (modulo de reportes)
	 * @param tokenAuth variable que se recibe por header para evaluar la secion de un usuario
	 * @param miID variable que recibimos por url para saber que usuario ara la peticion
	 * @param idEmisor variable que recibimos por url para evaluar los mensajes que este usuario ha mandado a broadcast
	 * @return cuerpo de respuesta
	 */
	@GetMapping("/mostrarMensajesporID/{miID}/{idEmisor}")
	public ResponseEntity<?> mostrarMensajes(@RequestHeader(value = "tokenAuth")String tokenAuth,
											 @PathVariable (value  ="miID") String miID,
											 @PathVariable(value = "idEmisor")String idEmisor){
		try{
			//validacion donde el idEmisor no sea menor o mayor a 24
			if(idEmisor.length() < 24 || idEmisor.length() > 24){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()), "El tamaño del id no es correcto", null ));
			}
			//busco al usuario que ara la peticion en la base de datos
			Optional<User> user = userRepository.validarUsuario(miID);
			//busco al usuario al que se consultara sus mensajes en la base de datos
			Optional<User> user2= userRepository.validarUsuario(idEmisor);
			//validacion para saber si el usuario que hace la peticion exite
			if(!user.isPresent()){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()), "El Usuario no existe", null));
			}
			//validacion donde para saber si el usuario pertenece a un grupo
			if(user.get().getIDGrupo().equals("")){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Usuario invalido",null));
			}
			// validacion del token recivido por header con el del usuario encontrado en la base de datos
			if(!user.get().getTokenAuth().equals(tokenAuth)) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), "Token no coincide", null));
			}
				//evaluacion donde el usuario que se evaluara existe en la base de satos
				if (user2.isPresent()) {
					//busca todos los mensajes en broadcast que el usuario a evaluar ha enviado
					Iterable<BroadCast> brd= broadCastRepositorio.buscarMensajesId(user2.get().getID());
					// respuesta valida
					return ResponseEntity.status(HttpStatus.OK).body(new ResponseBroadcast(String.valueOf(HttpStatus.OK.value()), "Lista de Mensajes de: " + user2.get().getNombre(), brd));
				}
			//respuesta invalida
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"El usuario no tiene mensajes enviados ", null)));
		/*Manejo de excepciones desde el mas particular hasta el mas general:
		  	Se devuelve cuerpo de respuesta con codigo de status, un mensaje de la causa y un objeto*/
		}catch (MongoSocketException e) {
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
	
	/*Servicio para enviar mensaje de un administrador broadCast a un usuario de su lista de contactos*/
	@PostMapping("/enviarMensaje")
	public ResponseEntity<?> enviarMensaje(@RequestHeader(value = "tokenAuth")String tokenAuth,
										   @RequestBody Mensajes mensajeEntrante){
		try{
			//Validamos los datos entrantes
			if(mensajeEntrante.getIDEmisor()==null || mensajeEntrante.getIDReceptor()==null || mensajeEntrante.getTexto()==null || mensajeEntrante.getFechaCreacion() ==null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Campos no validos",null));
			}

			if(mensajeEntrante.getIDEmisor().equals("") || mensajeEntrante.getIDEmisor().equals("null")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El campo idEmisor es no puede estar vacio",null));
			}
			if(mensajeEntrante.getIDEmisor().length()<24 || mensajeEntrante.getIDEmisor().length()>24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El tamaño del idEmisor no es valido",null));
			}

			if(mensajeEntrante.getIDReceptor().equals("") || mensajeEntrante.getIDReceptor().equals("null")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El campo idReceptor es no puede estar vacio",null));
			}
			if(mensajeEntrante.getIDReceptor().length()<24 || mensajeEntrante.getIDReceptor().length()>24) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El tamaño del idReceptor no es valido",null));
			}

			if(mensajeEntrante.getTexto().equals("") || mensajeEntrante.getTexto().equals("null")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El campo texto es no puede estar vacio",null));
			}
			if(mensajeEntrante.getTexto().length()<1) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El tamaño del texto no es debe ser al menos de 1 caracter",null));
			}

			//Buscamos a un usuario en la base de datos 
			Optional<User> existo = userRepository.validarUsuario(mensajeEntrante.getIDEmisor());
			//Validamos que exista este usuario
			if(!existo.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"El usuario broadcast no fue encontrado",null));
			}
			//Validamos que pertenezca a un grupo
			if(existo.get().getIDGrupo().equals("")){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Usuario invalido",null));
			}
			//Validamos que sea un usuario broascast
			if(!existo.get().getNombreRol().equals("BROADCAST")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"No es un usuario BROADCAST",null));
			}
			//Validamos que el token entrante no sea null
			if(existo.get().getTokenAuth()== null){
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),"Token no valido",null));
			}
			//Validamos que el token entrante sea igual al token del usuario de la base de datos
			if(!existo.get().getTokenAuth().equals(tokenAuth)) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), "Token no coincide", null));
			}
			
			//Creamos variable 'bandera'
			boolean bandera = false;
			
			//Buscamos a los usuarios que pertenezcan al grupo del usuario broadcast
			Iterable<User> listaUsuarios =  userRepository.findByGroupID(existo.get().getIDGrupo());
			//Filtramos usuarios y buscamos el usuario receptor aceptable
			for(User usuario : listaUsuarios) {
				if(usuario.getIDGrupo().equals(existo.get().getIDGrupo()) && usuario.getStatusActivo().equals("true") && usuario.getID().equals(mensajeEntrante.getIDReceptor())) {
					bandera = true;
				}
			}
			//Comparamos la bandera, en caso de que no se encuentre el usuario en la lista, devuelve: 
			if(!bandera) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"El usuario broadcast no puede mandar mensaje a la otra persona",null));
			}

			//Se crea nuevo cuerpo de mensaje con sus valores por defecto 
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
			//Se crea el mensaje en la base de datos
			mensajesService.crearMensaje(mensaje);
			//Devolvemos respuesta similar a lo realizado
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBroadcast(String.valueOf(HttpStatus.CREATED.value()),
					"Mensaje enviado",mensaje.getIDConversacion()));
		/*Manejo de excepciones desde el mas particular hasta el mas general:
		  Se devuelve cuerpo de respuesta con codigo de status, un mensaje de la causa y un objeto*/			
		}catch (MongoSocketException e) {
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
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El tamaño del id no es valido", null));
	}
	Optional<BroadCast> opt = broadCastRepositorio.findById(miIDmensaje);
	Optional<User> user = userRepository.validarUsuario(miID);
	if(!user.isPresent()){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"El usuario no existe",null));
	}
	if(user.get().getIDGrupo().equals("")){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"Usuario invalido",null));
	}
	if(!user.get().getNombreRol().equals("BROADCAST")){
		return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(new ResponseBroadcast(String.valueOf(HttpStatus.BAD_REQUEST.value()),"El usuario no es usuario BROADCAST",null));
	}
	if(user.get().getTokenAuth()== null){
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),"Token no valido",null));
	}
	if(!user.get().getTokenAuth().equals(tokenAuth)) {
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseBroadcast(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), "Token no coincide", null));
	}
	if(!opt.isPresent()){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBroadcast(String.valueOf(HttpStatus.NOT_FOUND.value()),"El mensaje no existe",null));
	}
	opt.get().setAtendido(true);
	broadCastRepositorio.save(opt.get());

	return ResponseEntity.status(HttpStatus.OK).body(new ResponseBroadcast(String.valueOf(HttpStatus.OK.value()),"Se atendio el mesnaje "," Usuario: " + opt.get().getNombreEmisor()));

}catch (MongoSocketException e) {
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

