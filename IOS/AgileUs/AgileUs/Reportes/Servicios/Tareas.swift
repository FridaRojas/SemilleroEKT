//
//  Tareas.swift
//  AgileUs
//
//  Created by Fernando González González on 23/11/21.
//

import Foundation

var arrTareas: ObjetoTareas?
var arrTas: ObjetoTareas?

class TareasService{

    var webServiceTask: ((_ arrDatosTareas:[Any]) -> Void)?
    var webServiceUsuariosTask: ((_ arrDatosUsuariosTareas:[Any]) -> Void)?
    
    func webServiceTareas(idUsuario:String){
        
        let serviceByID = "\(serviceTask)\(idUsuario)"
        
        let sevice = true
        let url = URL(string: serviceByID)
        
        var request = URLRequest(url: url!)
        request.setValue("c9acb094036a82eb6dbac287b6dc437b87f25c95ee954db469a4c424eacdcaba", forHTTPHeaderField: "tokenAuth")
        
        print("WebService de tareas")
        
        //Gernerar manejo de excepciones
        URLSession.shared.dataTask(with: request){
            
            (informacion, response, error) in
            
            if informacion == nil {
                print("La información del servicio de tarea esta vacia")
            }else{
                
                do{
                    
                    arrTareas = try JSONDecoder().decode(ObjetoTareas.self, from: informacion!)
                    DispatchQueue.main.async {
                                            
                        if sevice == true{
                            self.webServiceTask?(arrTareas!.data!)
                        }
                    }
                    
                }catch let services{
                    print(services)
                    print("Error al leer el archivo Tareas")
                }
                
            }
            
        }.resume()
        
    }

    func webServiceTareasPorLider(idUser: String, token:String){
        let servicio = "\(serviceTask)\(idUser)"
        
        let url = URL(string: servicio)
        
        var request = URLRequest(url: url!)
        request.setValue(token, forHTTPHeaderField: "tokenAuth")
        
        URLSession.shared.dataTask(with: request){
            (information, response, error) in
            
            if information == nil{
                print("La información del servicio de tareas está vacia")
            }else{
                do {
                    arrTas = try JSONDecoder().decode(ObjetoTareas.self, from: information!)
                    
                    
                    DispatchQueue.main.async {
                        
                        print("Array de tareas ")
                        
                        
                        self.webServiceUsuariosTask?(arrTas!.data!)
                    
                    }
                    
                }catch{
                    print(error)
                    print("Error al ejecutar el servicio de tareas por lider")
                }
            }
        }.resume()
        
    }
    
}
