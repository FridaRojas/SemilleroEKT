//
//  Tareas.swift
//  AgileUs
//
//  Created by Fernando González González on 23/11/21.
//

import Foundation

var arrTareas: ObjetoTareas?

class TareasService{

    var webServiceTask: ((_ arrDatosTareas:[Any]) -> Void)?
    
    let serviceTask = "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/tareas/obtenerTareasQueLeAsignaronPorId/"

    //let serviceTaskBySubordinado = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/tareas.json?alt=media&token=fd5f6b25-d02b-4582-812f-16445b66e553"
    
    //let serviceTaskByDate = "https://18.218.7.148:3040/api/tareas/obtenerTareas"
    let serviceTaskByDate = "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/tareas/obtenerTareasQueLeAsignaronPorId/"
    
    func webServiceTareas(){
        let sevice = true
        //let serviceByID = "\(serviceTask)\(userID)"
        let serviceByID = "\(serviceTask)618d9c26beec342d91d747d6"
        
        print(serviceByID)
        
        let url = URL(string: serviceByID)
        var request = URLRequest(url: url!)
        request.setValue("c9acb094036a82eb6dbac287b6dc437b87f25c95ee954db469a4c424eacdcaba", forHTTPHeaderField: "tokenAuth")
        
        print("WebService de tareas")
        
        //Gernerar manejo de excepciones
        URLSession.shared.dataTask(with: request){
            
            (informacion, response, error) in
            
            //print(informacion!)
            //print(response!)
            print(error)
            
            do{
                
                arrTareas = try JSONDecoder().decode(ObjetoTareas.self, from: informacion!)
                DispatchQueue.main.async {
                                        
                    if sevice == true{
                        self.webServiceTask?(arrTareas!.data)
                    }
                    //print(arrTareas!)
                    
                }
                
            }catch{
                print("Error al leer el archivo Tareas")
            }
            
        }.resume()
        
    }
    
    func webServiceTareas(idUsuario:String){
        
        let serviceByID = "\(serviceTask)\(idUsuario)"
        //let serviceByID = "\(serviceTask)618d9c26beec342d91d747d6"
        
        let sevice = true
        let url = URL(string: serviceByID)
        
        print("WebService de tareas")
        var request = URLRequest(url: url!)
        request.setValue("c9acb094036a82eb6dbac287b6dc437b87f25c95ee954db469a4c424eacdcaba", forHTTPHeaderField: "tokenAuth")
        
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
                            self.webServiceTask?(arrTareas!.data)
                        }
                    }
                    
                }catch{
                    print("Error al leer el archivo Tareas")
                }
                
            }
            
        }.resume()
        
    }

    
}
