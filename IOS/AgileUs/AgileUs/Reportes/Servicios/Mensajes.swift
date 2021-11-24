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
var leidos = 0
var recibidos = 0
var enviados = 0
var idRecpt = String()


struct Mensajes:Codable{
    
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


func webServiceMensajes(service:String){
    print("WebService de Mensajes")
    
    let url = URL(string: service)
    
    //Gernerar manejo de excepciones
    URLSession.shared.dataTask(with: url!){
        
        (informacion, response, error) in
        
        print(informacion!)
        //print(response!)
        //print(error)
        
        do{
            arrMensajes = try JSONDecoder().decode([Mensajes].self, from: informacion!)
            
            DispatchQueue.main.async {
                
                var idChat = String()
                
                for i in arrMensajes!{
                    
                    // ---------> Lectura de mensajes
                    if idChat.isEmpty{
                        print("Asignando un id de conversacion")
                        idChat = i.idconversacion
                    }else{
                        print("Se ha asignado un id al chat")
                    }
                    
                    if idChat == i.idconversacion{
                        //El id del usuario logeado es igual al id del idEmisor
                        idRecpt = i.idreceptor
                        
                        if i.idreceptor == idRecpt{
                            recibidos += 1
                        }
                        
                        if i.statusLeido == true{
                            leidos += 1
                        }else if i.statusEnviado == true{
                            enviados += 1
                        }else{
                            print("No se han enviado mensajes")
                        }

                    }
                    
                }
                
                print("\nId de la conversacion: \(idChat)")
                print("Enviados: \(enviados) \nRecibidos: \(recibidos) Leidos: \(leidos)")
                
            }
            
        }catch{
            print("Error al leer el archivo")
        }
        
    }.resume()
    
}



func cantidadDeMensajes(idChat:String, statusRead:Bool, statusSend:Bool) {
    
   
}

func tiempoDeRespuestaPromedio(){
    
}

func tiempoDeLecturaPromedio(){
    
}
