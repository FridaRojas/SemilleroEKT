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
    
    let serviceTaskByDate = "https://18.218.7.148:3040/api/tareas/obtenerTareas"
    
    func webServiceTareas(){
        let sevice = true
        let serviceByID = "\(serviceTask)\(userID)"
        //let serviceByID = "\(serviceTask)ReceptorAlexis"
        
        print(serviceByID)
        
        let url = URL(string: serviceByID)
        
        print("WebService de tareas")
        
        //Gernerar manejo de excepciones
        URLSession.shared.dataTask(with: url!){
            
            (informacion, response, error) in
            
            print(informacion!)
            print(response!)
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
        let sevice = true
        let url = URL(string: serviceTaskByDate)
        
        print("WebService de tareas")
        
        //Gernerar manejo de excepciones
        URLSession.shared.dataTask(with: url!){
            
            (informacion, response, error) in
            
            //print(informacion!)
            //print(response!)
            //print(error)
            
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

    
}
