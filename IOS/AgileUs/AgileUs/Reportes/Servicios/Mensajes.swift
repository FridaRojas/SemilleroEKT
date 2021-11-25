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




class MensajesService{
    
    var webServiceMessage: ((_ arrDatosTareas:[Any]) -> Void)?
    
    //let serviceMessage = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/Messages.json?alt=media&token=03022225-583c-4114-a056-ce4964b1a928"

    let serviceMessage = "http://10.97.3.27:3040/api/mensajes/listarMensajesRecividos/618e8821c613329636a769ac"
    
    func webServiceMensajes(){
        print("WebService de Mensajes")
        let service = true
        let url = URL(string: serviceMessage)
        
        //Gernerar manejo de excepciones
        URLSession.shared.dataTask(with: url!){
            
            (informacion, response, error) in
            
            //print(informacion!)
            //print(response!)
            //print(error)
            
            do{
                arrMensajes = try JSONDecoder().decode([Mensajes].self, from: informacion!)
                
                DispatchQueue.main.async {
                    
                    
                    if service == true{
                        self.webServiceMessage?(arrMensajes!)
                    }
                    
                    
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

    
}
