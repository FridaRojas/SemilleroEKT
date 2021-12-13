//
//  ServiciosWeb.swift
//  AgileUs
//
//  Created by Fernando González González on 23/11/21.
//

import Foundation
import UIKit

var arrData: Objeto?

class Usuarios{
    
    var webService: ((_ arrDatosUsuario:[Any]) -> Void)?
   
    func webServiceUsuarios(idUsuario: String) {
        
        let serviceUser = "\(server)user/findByBossId/\(idUsuario)"
                
        let url = URL(string: serviceUser)
        
        
        //Generar manejo de excepciones
        URLSession.shared.dataTask(with: url!) {
            
            (informacion, response, error) in
            
            if informacion == nil {
                print("La informacion del servicio de Usuarios está vacia")
                return
            } else {
                
                var estatus = ""
                
                do{
                    //Añadir los datos del Json en el array de datos
                    arrData = try JSONDecoder().decode(Objeto.self, from: informacion!)

                    DispatchQueue.main.async { [self] in
                        
                        estatus = arrData!.status
                        
                        print(estatus)

                        if "\(estatus)" == "BAD_REQUEST"{
                            print("Bad Request")
                            self.webService?([])
                        } else if "\(estatus)" == "OK"{
                            print("Ok")
                            self.webService?(arrData!.data)
                            
                        }else{
                            print(estatus)
                            print(error!.localizedDescription)
                        }
                    }
                    
                } catch {
                    self.webService?([])
                    print(estatus)
                    print(error.localizedDescription)
                    print("Error al leer el archivo usuarios")
                }
            }
     
        }.resume()

    }
    
}

