//
//  AdaptadorServicios.swift
//  AgileUs
//
//  Created by Fernando González González on 24/11/21.
//

import Foundation
import UIKit

class AdaptadorServicios{
    
    func serviciosWeb(webServiceUser: @escaping (_ Datos: [Any]) -> Void) {
        let pantallaUsuarios = Usuarios()
        pantallaUsuarios.webService = webServiceUser
        pantallaUsuarios.webServiceUsuarios()
    }
    
    /*func servicioWebTareasAdapter(webServiceTareas: @escaping (_ Datos: [Any]) -> Void){
        let claseTareas = TareasService()
        claseTareas.webServiceTask = webServiceTareas
        claseTareas.webServiceTareas()
        
    }*/
        
    func servicioWebMensajesAdapter(webServiceMensajes: @escaping (_ Datos: [Any]) -> Void){
        let claseMensajes = MensajesService()
        claseMensajes.webServiceMessage = webServiceMensajes
        claseMensajes.webServiceMensajes()
    }

}
