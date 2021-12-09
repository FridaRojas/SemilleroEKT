//
//  Mensajes.swift
//  AgileUs
//
//  Created by Fernando González González on 23/11/21.
//

import Foundation
import UIKit
import CoreMedia

var arrMensajes: ObjetoMensajes?

//var arrMensajes: [Mensajes]?
//var arrBroadcast: [Broadcast]?

var arrBroadcast: ObjetoBroadcast?
var idRecpt = String()

class MensajesService {
    
    var webServiceMessage: ((_ arrDatosTareas:[Any]) -> Void)?
    var webServiceBroad: ((_ arrDatosBroad:[Any]) -> Void)?
    
    var serviceMessage = "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/mensajes/listarMensajesRecividos/"
    //let serviceMessage = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/mensajes_nuevo.json?alt=media&token=eadcb762-992e-493c-8ee7-50e4c3a93ce2"
    //let serviceBroad = "http://10.97.7.227:3040/api/broadCast//mostrarMensajesporID/618b05c12d3d1d235de0ade0"
    //var serviceBroad = "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/broadCast//mostrarMensajesporID/"
    
    var serviceBroad = "http://10.97.1.230:3040/api/broadCast/mostrarMensajesporID/61a83b59d036090b8e8db3c1/618b05c12d3d1d235de0ade0"
    
    
    func webServiceMensajes(idUsuario: String) {
        let service = true
        serviceMessage = "\(serviceMessage)\(userID)/\(idUsuario)"
        let url = URL(string: serviceMessage)
        var request = URLRequest(url: url!)
        request.setValue("a60f68eceb8648b34b5ed39c19b57ac3f442d6e40f0471463df13a03666ff913", forHTTPHeaderField: "tokenAuth")
    
        //Gernerar manejo de excepciones
        URLSession.shared.dataTask(with: request){
            
            (informacion, response, error) in
            
            if informacion == nil{
                print("Está vacia la informacion del servicio de Mensajes")
            }else{
                
                do{
                    arrMensajes = try JSONDecoder().decode(ObjetoMensajes.self, from: informacion!)
                    DispatchQueue.main.async {
                        
                        if service == true {
                            self.webServiceMessage?(arrMensajes!.data)
                        }
                    }
                    
                }catch let error {
                    print(error)
                    print("Error al leer el archivo Mensajes")
                }
            }
        }.resume()
        
    }
    
    func webServiceBroadcast(idUsuario: String) {
        let service = true
        //serviceBroad = "\(serviceBroad)\(idUsuario)"
        let url = URL(string: serviceBroad)
        
        //let request = NSMutableURLRequest(url: url! as URL)
        //request.setValue("tokenAuth", forHTTPHeaderField: "8b694ba1cea1461d383ad5cd17d0359a0faff81c296b6d18b204db1fba12b281")
        
        //URLSession.shared.dataTask(with: url!){
        var request = URLRequest(url: url!)
        //request.setValue("secret-keyValue", forHTTPHeaderField: "secret-key")
        //request.httpMethod = "GET"
        //request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue("a60f68eceb8648b34b5ed39c19b57ac3f442d6e40f0471463df13a03666ff913", forHTTPHeaderField: "tokenAuth")

        //URLSession.shared.dataTask(with: request) { data, response, error in }
        URLSession.shared.dataTask(with: request) {
            (info, response, error) in
            
            if info == nil{
                print("La información del servicio del broadcast está vacia")
            }else{
                do {
                    arrBroadcast = try JSONDecoder().decode(ObjetoBroadcast.self, from: info!)
                    
                    DispatchQueue.main.async {
                        if service == true {
                            self.webServiceBroad?(arrBroadcast!.data)
                        }
                    }
                
                } catch {
                    print("Error al leer broadcast")
                }
            }
        }.resume()
    }
    
    func webServiceMensajesPorIdSup(idUsuario: String) {
        let service = true
        serviceMessage = "\(serviceMessage)\(idUsuario)"
        let url = URL(string: serviceMessage)
    
        //Gernerar manejo de excepciones
        URLSession.shared.dataTask(with: url!){
            
            (informacion, response, error) in
            
            if informacion == nil{
                print("Está vacia la informacion del servicio de Mensajes")
            }else{
                
                do{
                    arrMensajes = try JSONDecoder().decode(ObjetoMensajes.self, from: informacion!)
                    DispatchQueue.main.async {
                        
                        if service == true {
                            self.webServiceMessage?(arrMensajes!.data)
                        }
                    }
                    
                }catch{
                    print("Error al leer el archivo Mensajes")
                }
            }
        }.resume()
        
    }
    
    
}
