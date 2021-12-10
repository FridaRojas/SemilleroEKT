package com.ekt.Servicios.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "GruposMensajeria")
public class GruposMensajeria {
	@Id
	private String id;
	
	private String idConversacion;
	private String idReceptor;
	private String nombreConversacion;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdConversacion() {
		return idConversacion;
	}
	public void setIdConversacion(String idConversacion) {
		this.idConversacion = idConversacion;
	}
	public String getIdReceptor() {
		return idReceptor;
	}
	public void setIdReceptor(String idReceptor) {
		this.idReceptor = idReceptor;
	}
	public String getNombreConversacion() {
		return nombreConversacion;
	}
	public void setNombreConversacion(String nombreConversacion) {
		this.nombreConversacion = nombreConversacion;
	}
}
