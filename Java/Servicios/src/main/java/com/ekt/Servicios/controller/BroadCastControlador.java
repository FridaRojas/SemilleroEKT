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
		
		Optional<User> existo = userRepository.validarUsuario(miId);
		
		Iterable<User> listaUsuarios =  userRepository.findByGroupID(existo.get().getIDGrupo());
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(listaUsuarios);
	}

	@GetMapping("/mostarMensajesdelBroadcast")
	public Iterable<BroadCast>listarMensajes(){
		Iterable<BroadCast> brd = broadCastRepositorio.findAll();

		return brd;
	}

	@PostMapping("/crearMensajeBroadcast")
	public ResponseEntity<?> crearMensajeBroadCast(@RequestBody BroadCast broadCast){
		BroadCast broadCastM = new BroadCast();
		broadCastM.setIdEmisor(broadCast.getIdEmisor());
		broadCastM.setAsunto(broadCast.getAsunto());
		broadCastM.setDescripcion(broadCast.getDescripcion());
		Optional<User> user = userRepository.findById(broadCast.getIdEmisor());
		if(user.isPresent()){
			broadCastM.setNombreEmisor(user.get().getNombre());
			broadCastRepositorio.save(broadCastM);
			notificacion2(broadCastM.getNombreEmisor()  + " te ha enviado un mensaje", broadCast.getAsunto());
			return ResponseEntity.status(HttpStatus.CREATED).body(new Response(HttpStatus.CREATED,"Se creo el mensaje a broadcast",broadCastM.getIdEmisor()));
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(new Response(HttpStatus.NOT_FOUND,"No se encuentra el emisor",broadCastM.getIdEmisor()));
	}
	@GetMapping("/mostrarMensajesporID/{idEmisor}")
	public List<BroadCast> mostrarMensajes(@PathVariable(value = "idEmisor")String idEmisor){
		List<BroadCast> listBrd = new ArrayList<>();
		Iterable<BroadCast> brd = broadCastRepositorio.findAll();
		for (BroadCast brd2 : brd) {
			if(brd2.getIdEmisor().equals(idEmisor)){
				listBrd.add(brd2);
			}
		}
		return listBrd;
	}
	
	@PostMapping("/enviarMensaje")
	public ResponseEntity<?> enviarMensaje(@RequestBody Mensajes mensajeEntrante){
		//Optional<User> existo = userRepository.validarUsuario(mensajeEntrante.getIDEmisor());
		
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
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(mensaje);
	}


	public void notificacion2(String title, String asunto){
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{\n    \"to\": \"flmUd4adQi-WvEgBCs6wIy:APA91bGRrGBndwMxconv1tIoCus-eY7vTcc-QmkgtYuuFBi7A2vpWAf_HBH-YghkLsSBqMlP6e5iCBz4O1ONaMZ8Cv0i4GxzZy0XF0fOYpuXx0-VXTxFeK1sZ3YwhMdmHQXz1WweNqZQ\",\n    " +
				"\"notification\": {\n        " +
				"\"body\": \""+ asunto +"\",\n        " +
				"\"title\": \""+ title +"\"\n    }\n}");

		Request request = new Request.Builder()
				.url("https://fcm.googleapis.com/fcm/send")
				.method("POST", body)
				.addHeader("Authorization", "key=AAAAIITlXUs:APA91bHueyZr0vJFOSo-yLEbRsG20D8rquPQbQJ1C82JTcnaOjB2ghemxgUljAzwE4wsPEzjQZY2GlrNcI1sFx__SuxsGfszskEF2cx5zy3yYFCdiU2681mCoLwMw_fH4TjmocJIQyYx")
				.addHeader("Content-Type", "application/json")
				.build();
		try {
			okhttp3.Response response = client.newCall(request).execute();
		} catch (IOException e) {
			e.printStackTrace();


			
		}
	}
}

