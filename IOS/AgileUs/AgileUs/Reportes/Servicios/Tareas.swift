//
//  Tareas.swift
//  AgileUs
//
//  Created by Fernando González González on 23/11/21.
//

import Foundation

var arrTareas: ObjetoTareas?


struct ObjetoTareas:Codable{
    let estatus: String
    let mensaje: String
    let data:[Tareas]
}

struct Tareas:Codable{
    
    //let id_grupo:String
    //let id_emisor:String
    //let nombre_emisor:String
    let nombre_receptor:String?
    let id_receptor:String?
    let estatus:String?
    let leido:Bool?
    let fecha_ini:String?
    let fecha_iniR:String?
    let fecha_fin:String?
    let fecha_finR:String?
}

class TareasService{

    var webServiceTask: ((_ arrDatosTareas:[Any]) -> Void)?
    
    let serviceTask = "http://10.97.6.35:2021/api/tareas/obtenerTareasQueLeAsignaronPorId/"

    let serviceTaskBySubordinado = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/tareas.json?alt=media&token=fd5f6b25-d02b-4582-812f-16445b66e553"
    
    let serviceTaskByDate = "https://10.97.3.134:2021/api/tareas/obtenerTareas"
    
    func webServiceTareas(){
        let sevice = true
        //let serviceByID = "\(serviceTask)\(userID)"
        let serviceByID = "\(serviceTask)ReceptorAlexis"
        
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
