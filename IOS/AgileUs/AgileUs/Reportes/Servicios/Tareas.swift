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


func webServiceTareas(service:String){
    
    let url = URL(string: service)
    
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
                
            }
            
        }catch{
            print("Error al leer el archivo")
        }
        
    }.resume()
    
}
