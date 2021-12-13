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
 
    func webServiceTareas(){
        
        let serviceByID = "\(server)tareas/obtenerTareasQueLeAsignaronPorId/\(userID)"
        
        let url = URL(string: serviceByID)
        var request = URLRequest(url: url!)
        request.setValue(tokenAuth, forHTTPHeaderField: "tokenAuth")
        
        print("WebService de tareas Por usuario logeado")
        
        //Gernerar manejo de excepciones
        URLSession.shared.dataTask(with: request){
            
            (informacion, response, error) in
            
            do{
                
                arrTareas = try JSONDecoder().decode(ObjetoTareas.self, from: informacion!)
                DispatchQueue.main.async {
                                        
                    if arrTareas!.estatus! == "200"{
                        self.webServiceTask?(arrTareas!.data!)
                    }else{
                        print(arrTareas!.estatus!)
                        print(arrTareas!.mensaje!)
                    }
                    
                }
                
            }catch{
                print("Error al leer el archivo Tareas logeado")
            }
            
        }.resume()
        
    }
    
    func webServiceTareas(idUsuario:String){
        
        let serviceByID = "\(server)tareas/obtenerTareasQueLeAsignaronPorIdReportes/\(userID)/\(idUsuario)"
        
        let url = URL(string: serviceByID)
        var request = URLRequest(url: url!)
        
        request.setValue(tokenAuth, forHTTPHeaderField: "tokenAuth")
        
        print("WebService de tareas por id usuario")
        
        //Gernerar manejo de excepciones
        URLSession.shared.dataTask(with: request){
            
            (informacion, response, error) in
            
            if informacion == nil {
                print("La información del servicio de tarea esta vacia")
            }else{
                
                do{
                    
                    arrTareas = try JSONDecoder().decode(ObjetoTareas.self, from: informacion!)
                    DispatchQueue.main.async {
                        
                        if arrTareas!.estatus! == "200"{
                            self.webServiceTask?(arrTareas!.data!)
                        }else{
                            self.webServiceTask?([])
                            print(arrTareas!.estatus!)
                            print(arrTareas!.mensaje!)
                        }
                    }
                    
                }catch let services{
                    print(services)
                    print("Error al leer el archivo Tareas por idUsuario")
                }
                
            }
            
        }.resume()
    }

    
    func webServiceTareasPorLider(idReceptor:String){

        let servicio = "\(server)tareas/obtenerTareasQueLeAsignaronPorIdReportes/\(userID)/\(idReceptor)"
        let url = URL(string: servicio)
        var request = URLRequest(url: url!)
        
        request.setValue(tokenAuth, forHTTPHeaderField: "tokenAuth")
        
        URLSession.shared.dataTask(with: request){
            (information, response, error) in
            
            if information == nil{
                print("La información del servicio de tareas está vacia")
            }else{
                do {
                    arrTas = try JSONDecoder().decode(ObjetoTareas.self, from: information!)
                    
                    
                    DispatchQueue.main.async {
                        
                        if arrTas!.estatus! == "200"{
                            self.webServiceUsuariosTask?(arrTas!.data!)
                        }else{
                            print(arrTas!.estatus!)
                            print(arrTas!.mensaje!)
                        }
                    
                    }
                    
                }catch{
                    print(error)
                    print("Error al ejecutar el servicio de tareas por lider")
                }
            }
        }.resume()
        
    }

    
}
