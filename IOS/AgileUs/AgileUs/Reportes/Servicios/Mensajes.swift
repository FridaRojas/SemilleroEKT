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
var arrBrodcast: Broadcast?
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
    var webServiceBroadcast: ((_ arrDatosBroadcast:[Any]) -> Void)?
    
    let serviceMessage = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/mensajes_nuevo.json?alt=media&token=eadcb762-992e-493c-8ee7-50e4c3a93ce2"

    //let serviceMessage = "http://10.97.1.178:3040/api/mensajes/listarMensajesRecividos/618e8821c613329636a769ac"
    let serviceBroadccast = "http://10.97.1.178:3040/api/broadCast//mostrarMensajesporID/618b05c12d3d1d235de0ade0"
    
    
    func webServiceMensajes() {
        //print("WebService de Mensajes")
        let service = true
        let url = URL(string: serviceMessage)
        let idUsuario = "618e8821c613329636a769ac"
        
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
    
    func webServiceBroadcastF() {
        
        let url = URL(string: serviceBroadccast)
        let idUsuario = "618b05c12d3d1d235de0ade0"
        
        URLSession.shared.dataTask(with: url!) {
            
            (informacion, response, error) in
            
            do{
                arrBrodcast = try JSONDecoder().decode(Broadcast.self, from: informacion!)
                DispatchQueue.main.async {
                    
                    self.webServiceBroadcast?([arrBrodcast!])
                
                }
            }catch{
                print("Error al leer el archivo")
            }
        }.resume()    }

    /*func cantidadDeMensajes(mensaje: [Mensajes], idUsuario: String) -> [Int] {
        
        var leidos = 0
        var recibidos = 0
        var enviados = 0
        
        for i in mensaje {
            
            if i.idreceptor == idUsuario || i.idreceptor.contains(idUsuario) {
                recibidos += 1
            }
            
            if i.idreceptor == idUsuario && i.statusLeido == true {
                leidos += 1
            }
            
            if i.idemisor == idUsuario && i.statusEnviado == true {
                enviados += 1
            }
            
        }
        
        return [enviados, recibidos, leidos]
        
        
    }*/

    func tiempoDeRespuestaPromedio(){
        
    }

    func tiempoDeLecturaPromedio(){
        
    }

    
}
