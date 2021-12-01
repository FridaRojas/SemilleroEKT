//
//  Mensajes.swift
//  AgileUs
//
//  Created by Fernando González González on 23/11/21.
//

import Foundation
import UIKit
import CoreMedia

var arrMensajes: [Mensajes]?
var arrBroadcast: [Broadcast]?
var idRecpt = String()

struct Mensajes:Codable {
    let id:String
    let idemisor:String
    let idreceptor:String
    let fechaCreacion:String
    let fechaEnviado:String
    let fechaLeido:String
    let statusLeido:Bool
    let statusEnviado:Bool
    let idconversacion:String
}

struct Broadcast: Codable {
    let id:String
    let asunto:String
    let descripcion:String
    let idEmisor:String
    let nombreEmisor:String
}

class MensajesService {
    
    var webServiceMessage: ((_ arrDatosTareas:[Any]) -> Void)?
    var webServiceBroad: ((_ arrDatosBroad:[Any]) -> Void)?

    //let serviceMessage = "http://10.97.1.178:3040/api/mensajes/listarMensajesRecividos/618e8821c613329636a769ac"
    let serviceBroadccast = "http://10.97.1.178:3040/api/broadCast//mostrarMensajesporID/618b05c12d3d1d235de0ade0"
    
    //var serviceMessage = "http://10.97.2.202:3040/api/mensajes/listarMensajesRecividos/618e8821c613329636a769ac"
    // 10.97.4.165
    var serviceMessage = "http://10.97.6.83:3040/api/mensajes/listarMensajesRecividos/"
    //var serviceMessage = "http://10.97.7.227:3040/api/mensajes/listarMensajesRecividos/"
    //let serviceMessage = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/mensajes_nuevo.json?alt=media&token=eadcb762-992e-493c-8ee7-50e4c3a93ce2"
    //let serviceBroad = "http://10.97.7.227:3040/api/broadCast//mostrarMensajesporID/618b05c12d3d1d235de0ade0"
    var serviceBroad = "http://10.97.6.83:3040/api/broadCast//mostrarMensajesporID/"
    
    func webServiceMensajes(idUsuario: String) {
        let service = true
        serviceMessage = "\(serviceMessage)\(idUsuario)"
        let url = URL(string: serviceMessage)
    
        //Gernerar manejo de excepciones
        URLSession.shared.dataTask(with: url!){
            
            (informacion, response, error) in
            
            do{
                arrMensajes = try JSONDecoder().decode([Mensajes].self, from: informacion!)
                DispatchQueue.main.async {
                    
                    if service == true {
                        self.webServiceMessage?(arrMensajes!)
                    }
                }
                
            }catch{
                print("Error al leer el archivo Mensajes")
            }
            
        }.resume()
        
    }
    
    func webServiceBroadcast(idUsuario: String) {
        let service = true
        serviceBroad = "\(serviceBroad)\(idUsuario)"
        let url = URL(string: serviceBroad)
        
        URLSession.shared.dataTask(with: url!){
            
            (info, response, error) in
            
            do {
                arrBroadcast = try JSONDecoder().decode([Broadcast].self, from: info!)
                
                DispatchQueue.main.async {
                    if service == true {
                        self.webServiceBroad?(arrBroadcast!)
                    }
                }
            
            } catch {
                print("Error al leer broadcast")
            }
            
        }.resume()
    }
    
}
