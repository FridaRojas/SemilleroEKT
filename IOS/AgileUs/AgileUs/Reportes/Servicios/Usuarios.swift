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
    //var arrDatosUsuario = [Any]()
    
    //let serviceUser = "https://firebasestorage.googleapis.com/v0/b/proyectop-50f0b.appspot.com/o/busquedaPorIdJefe2.json?alt=media&token=3a996bbf-398d-4785-8bbf-9931f5eecfe2"
    
    //  let serviceUser = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/usuariosEncontrados2.json?alt=media&token=ceb1c36f-e662-484c-9db3-fb33ef0759b0"
    
    var serviceUser =  "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/user/findByBossId/"
    
    //var serviceUser = "http://18.218.7.148:3040/api/user/findByBossId/"
    
    func webServiceUsuarios(idUsuario: String) {
        
        serviceUser = "\(serviceUser)\(idUsuario)"
                
        let url = URL(string: serviceUser)
        
        //Generar manejo de excepciones
        URLSession.shared.dataTask(with: url!) {
            
            (informacion, response, error) in
            
            if informacion == nil {
                print("La informacion del servicio de Usuarios está vacia")
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

