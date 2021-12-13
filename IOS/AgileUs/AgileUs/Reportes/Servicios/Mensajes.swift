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

class MensajesService {
    
    var webServiceMessage: ((_ arrDatosTareas:[Any]) -> Void)?
    var webServiceBroad: ((_ arrDatosBroad:[Any]) -> Void)?
    
    func webServiceMensajes(idUsuario: String) {
        let service = "\(server)mensajes/listarMensajesRecividos/\(userID)/\(idUsuario)"
        let url = URL(string: service)
        var request = URLRequest(url: url!)
        
        request.setValue(tokenAuth, forHTTPHeaderField: "tokenAuth")
    
        //Gernerar manejo de excepciones
        URLSession.shared.dataTask(with: request){
            
            (informacion, response, error) in
            
            if informacion == nil{
                print("Está vacia la informacion del servicio de Mensajes")
            }else{
                
                do{
                    arrMensajes = try JSONDecoder().decode(ObjetoMensajes.self, from: informacion!)
                    DispatchQueue.main.async {
                        
                        if arrMensajes!.estatus == "200"{
                            self.webServiceMessage?(arrMensajes!.data)
                        }else{
                            print(arrMensajes!.estatus)
                            print(arrMensajes!.mensaje)
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
        let serviceBroad = "\(server)broadCast//mostrarMensajesporID/\(userID)/\(idUsuario)"
        let url = URL(string: serviceBroad)
        
        //let request = NSMutableURLRequest(url: url! as URL)
        //request.setValue("tokenAuth", forHTTPHeaderField: "8b694ba1cea1461d383ad5cd17d0359a0faff81c296b6d18b204db1fba12b281")
        
        //URLSession.shared.dataTask(with: url!){
        var request = URLRequest(url: url!)
        //request.setValue("secret-keyValue", forHTTPHeaderField: "secret-key")
        //request.httpMethod = "GET"
        //request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue(tokenAuth, forHTTPHeaderField: "tokenAuth")

        //URLSession.shared.dataTask(with: request) { data, response, error in }
        URLSession.shared.dataTask(with: request) {
            (info, response, error) in
            
            if info == nil{
                print("La información del servicio del broadcast está vacia")
            }else{
                do {
                    arrBroadcast = try JSONDecoder().decode(ObjetoBroadcast.self, from: info!)
                    
                    DispatchQueue.main.async {
                        
                        if arrBroadcast!.estatus == "200"{
                            self.webServiceBroad?(arrBroadcast!.data)
                        }else{
                            print(arrBroadcast!.estatus)
                            print(arrBroadcast!.mensaje)
                        }
                            
                        
                    }
                
                } catch {
                    print("Error al leer broadcast")
                }
            }
        }.resume()
    }
    
    func webServiceMensajesPorIdSup(idUsuario: String) {
        
        let service = "\(server)mensajes/listarMensajesRecividos/\(userID)/\(idUsuario)"
        let url = URL(string: service)
    
        print("webServiceMensajesPorIdSup")
        //Gernerar manejo de excepciones
        URLSession.shared.dataTask(with: url!){
            
            (informacion, response, error) in
            
            if informacion == nil{
                print("Está vacia la informacion del servicio de Mensajes")
            }else{
                
                do{
                    arrMensajes = try JSONDecoder().decode(ObjetoMensajes.self, from: informacion!)
                    DispatchQueue.main.async {
                        
                        if arrMensajes!.estatus == "200" {
                            self.webServiceMessage?(arrMensajes!.data)
                        }else{
                            print(arrMensajes!.estatus)
                            print(arrMensajes!.mensaje)
                        }
                    }
                    
                }catch{
                    print("Error al leer el archivo Mensajes webServiceMensajesPorIdSup")
                }
            }
        }.resume()
        
    }
    
    
}
