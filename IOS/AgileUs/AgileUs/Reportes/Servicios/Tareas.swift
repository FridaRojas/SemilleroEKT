//
//  Tareas.swift
//  AgileUs
//
//  Created by Fernando González González on 23/11/21.
//

import Foundation

var arrTareas: Tareas?


struct Tareas:Codable{
    
    let id_grupo:String
    let id_emisor:String
    let nombre_emisor:String
    let id_receptor:String
    let fecha_ini:String
    let fecha_fin:String
    let prioridad:String
    let status:String
    let leido:Bool
    let createdDate:String
}

class TareasService{

    var webServiceTask: ((_ arrDatosTareas:[Any]) -> Void)?
    
    let serviceTask = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/Task.json?alt=media&token=bb6a2086-2e39-411a-8385-2294dabcc2d5"

    
    func webServiceTareas(){
        let sevice = true
        let url = URL(string: serviceTask)
        
        print("WebService de tareas")
        
        //Gernerar manejo de excepciones
        URLSession.shared.dataTask(with: url!){
            
            (informacion, response, error) in
            
            //print(informacion!)
            //print(response!)
            //print(error)
            
            do{
                
                    
                arrTareas = try JSONDecoder().decode(Tareas.self, from: informacion!)
                DispatchQueue.main.async {
                    
                    print(type(of: arrTareas!))
                    
                    for elemento in arrTareas!.nombre_emisor{
                        print(elemento)
                    }
                    
                    if sevice == true{
                        self.webServiceTask?([arrTareas!])
                    }
                    
                    
                }
                
            }catch{
                print("Error al leer el archivo")
            }
            
        }.resume()
        
    }

    
}
