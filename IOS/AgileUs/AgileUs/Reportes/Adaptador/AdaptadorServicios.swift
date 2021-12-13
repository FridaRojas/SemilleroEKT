//
//  AdaptadorServicios.swift
//  AgileUs
//
//  Created by Fernando González González on 24/11/21.
//

import Foundation
import UIKit

class AdaptadorServicios{
    
    func serviciosWeb(idUsuario: String, webServiceUser: @escaping (_ Datos: [Any]) -> Void) {
        let pantallaUsuarios = Usuarios()
        pantallaUsuarios.webService = webServiceUser
        pantallaUsuarios.webServiceUsuarios(idUsuario: idUsuario)
    }
    
    
    func servicioWebTareasAdapterByBoss(idReceptor:String, webServiceSub: @escaping (_ Datos: [Any]) -> Void){
        let claseSubordinados = TareasService()
        claseSubordinados.webServiceUsuariosTask = webServiceSub
        claseSubordinados.webServiceTareasPorLider(idReceptor: idReceptor)
        
    }
    func servicioWebMensajesAdapter(idUsuario: String, webServiceMensajes: @escaping (_ Datos: [Any]) -> Void){
        let claseMensajes = MensajesService()
        claseMensajes.webServiceMessage = webServiceMensajes
        claseMensajes.webServiceMensajes(idUsuario: idUsuario)
    }
    
    func servicioWebBroadcastAdapter(idUsuario: String, webServiceBroadcast: @escaping (_ Datos: [Any]) -> Void){
        let claseMensajes = MensajesService()
        claseMensajes.webServiceBroad = webServiceBroadcast
        claseMensajes.webServiceBroadcast(idUsuario: idUsuario)
    }
    
    func servicioWebTareasAdapter(idUsuario: String, webServiceTareas: @escaping (_ Datos: [Any]) -> Void){
        let claseTareas = TareasService()
        claseTareas.webServiceTask = webServiceTareas
        claseTareas.webServiceTareas(idUsuario: idUsuario)
    }
    

}
