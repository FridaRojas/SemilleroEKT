//
//  ServiciosWeb.swift
//  AgileUs
//
//  Created by Fernando González González on 23/11/21.
//

import Foundation
import UIKit

var arrData: Objeto?
var idJefe = ""

class Usuarios{
    
    var webService: ((_ arrDatosUsuario:[Any]) -> Void)?
    
   
    
    func webServiceUsuarios(idUsuario: String) {
        
        serviceUserBoos = "\(serviceUserBoos)\(idUsuario)"
                
        let url = URL(string: serviceUserBoos)
        
        //Generar manejo de excepciones
        URLSession.shared.dataTask(with: url!) {
            
            (informacion, response, error) in
            
            if informacion == nil {
                print("La informacion del servicio de Usuarios está vacia")
                return
            }else{
                do{
                    //Añadir los datos del Json en el array de datos
                    arrData = try JSONDecoder().decode(Objeto.self, from: informacion!)

                    DispatchQueue.main.async { [self] in
                        
                        let estatus = arrData!.status

                        if "\(estatus)" == "OK"{
                            self.webService?(arrData!.data)
                        }
                        filtroDeUsuariosPorJefe(idJefe: idJefe)
                    }
                    
                } catch {
                    print("Error al leer el archivo")
                }
            }
     
        }.resume()

    }

    func obtenerIdDelJefeInmediato() -> String{
        return idJefe
    }

    func filtroDeUsuariosPorJefe(idJefe:String){
                    
        if ((arrData?.data) == nil) {
        } else {
            for elemto in arrData!.data{
                if elemto.idsuperiorInmediato == idJefe {
                    print(elemto.nombre)
                }else{
                    //print("No se han encontrado coincidencias")
                }
            }
        }
    }
    
}

