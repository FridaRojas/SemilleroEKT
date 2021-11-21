package com.ekt.Servicios.entity;


import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Mensajes")
public class Mensajes {
	@Id
	private String id;
	
    private String idConversacion;
    private String idEmisor;
    private String idReceptor;
    private String texto;
    private boolean visible;
    
    private String nombreConversacionReceptor;
    
    private Date fechaCreacion;
    private boolean statusCreado;
    
    private Date fechaEnviado;
    private boolean statusEnviado;
    
    private Date fechaLeido;
    private boolean statusLeido;

    public String getID() { return id; }
    public void setID(String value) { this.id = value; }
    
	public boolean isStatusCreado() {
		return statusCreado;
	}
	public void setStatusCreado(boolean statusCreado) {
		this.statusCreado = statusCreado;
	}
	
	public Date getFechaLeido() {
		return fechaLeido;
	}
	public void setFechaLeido(Date fechaLeido) {
		this.fechaLeido = fechaLeido;
	}
	public boolean isStatusLeido() {
		return statusLeido;
	}
	public void setStatusLeido(boolean statusLeido) {
		this.statusLeido = statusLeido;
	}
	public Date getFechaEnviado() {
		return fechaEnviado;
	}
	public void setFechaEnviado(Date fechaEnviado) {
		this.fechaEnviado = fechaEnviado;
	}
	public boolean isStatusEnviado() {
		return statusEnviado;
	}
	public void setStatusEnviado(boolean statusEnviado) {
		this.statusEnviado = statusEnviado;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getIDConversacion() { return idConversacion; }
    public void setIDConversacion(String value) { this.idConversacion = value; }

    public String getIDEmisor() { return idEmisor; }
    public void setIDEmisor(String value) { this.idEmisor = value; }

    public String getIDReceptor() { return idReceptor; }
    public void setIDReceptor(String value) { this.idReceptor = value; }

    public String getTexto() { return texto; }
    public void setTexto(String value) { this.texto = value; }

    public boolean getVisible() { return visible; }
    public void setVisible(boolean value) { this.visible = value; }
    
	public String getNombreConversacionReceptor() {
		return nombreConversacionReceptor;
	}
	public void setNombreConversacionReceptor(String nombreConversacionReceptor) {
		this.nombreConversacionReceptor = nombreConversacionReceptor;
	}
}
