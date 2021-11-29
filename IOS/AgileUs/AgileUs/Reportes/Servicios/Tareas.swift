//
//  Tareas.swift
//  AgileUs
//
//  Created by Fernando González González on 23/11/21.
//

import Foundation

var arrTareas: [Tareas]?

struct TareasSubordinados:Codable{
    let id_tarea:String
    let id_grupo:String
    let id_emisor:String
    let nombre_emisor:String
    let id_receptor:String
    let nombre_receptor:String
    let fecha_ini:String
    let fecha_iniR:String
    let fecha_fin:String
    let fecha_finR:String
    let titulo:String
    let prioridad:String
    let estatus:String
    let leido:String
    let fecha_BD:String
    let archivo:String
}

struct Tareas:Codable{
    
    //let id_grupo:String
    //let id_emisor:String
    //let nombre_emisor:String
    let id_receptor:String
    let estatus:String
    //let leido:Bool
    //let fecha_ini:String
    //let fecha_iniR:String
    //let fecha_fin:String
    //let fecha_finR:String
}

class TareasService{

    var webServiceTask: ((_ arrDatosTareas:[Any]) -> Void)?
    
    let serviceTask = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/Task.json?alt=media&token=bb6a2086-2e39-411a-8385-2294dabcc2d5"

    let serviceTaskBySubordinado = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/tareas.json?alt=media&token=fd5f6b25-d02b-4582-812f-16445b66e553"
    
    func webServiceTareas(){
        let sevice = true
        let url = URL(string: serviceTaskBySubordinado)
        
        print("WebService de tareas")
        
        //Gernerar manejo de excepciones
        URLSession.shared.dataTask(with: url!){
            
            (informacion, response, error) in
            
            //print(informacion!)
            //print(response!)
            //print(error)
            
            do{
                
                arrTareas = try JSONDecoder().decode([Tareas].self, from: informacion!)
                DispatchQueue.main.async {
                                        
                    if sevice == true{
                        self.webServiceTask?(arrTareas!)
                    }
                    //print(arrTareas!)
                    
                }
                
            }catch{
                print("Error al leer el archivo Tareas")
            }
            
        }.resume()
        
    }
    
    

    
}
