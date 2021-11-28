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

struct Objeto:Codable{
    let status:String
    let msj:String
    let data:[Usuario]
}

struct Usuario:Codable{
    let id:String
    let nombre:String
    let fechaInicio:String
    let fechaTermino:String
    let nombreRol:String
    let idgrupo:String
    let idsuperiorInmediato:String
}

class Usuarios{
    
    var webService: ((_ arrDatosUsuario:[Any]) -> Void)?
    //var arrDatosUsuario = [Any]()
    
    let serviceUser = "https://firebasestorage.googleapis.com/v0/b/proyectop-50f0b.appspot.com/o/busquedaPorIdJefe2.json?alt=media&token=3a996bbf-398d-4785-8bbf-9931f5eecfe2"
    
    
    func webServiceUsuarios() {
                
        //print("Entrando al servicio de Usuarios")
        
        let url = URL(string: serviceUser)
        
        //Generar manejo de excepciones
        URLSession.shared.dataTask(with: url!) {
            
            (informacion, response, error) in
            /*print(informacion!)
            print(response!)
            print(error as Any)*/
            
            do{
                //Añadir los datos del Json en el array de datos
                arrData = try JSONDecoder().decode(Objeto.self, from: informacion!)

                DispatchQueue.main.async { [self] in
                    
                    let estatus = arrData!.status
                    //print("\nUsuarios registrados")
                    
                    if "\(estatus)" == "OK"{
                        //print("\nGuardando datos en el lambda")
                        self.webService?(arrData!.data)
                        
                    }
                        
                    
                    filtroDeUsuariosPorJefe(idJefe: idJefe)
                
                }
                
                
                
            }catch{
                print("Error al leer el archivo")
            }
            
            
        }.resume()

    }

    func obtenerIdDelJefeInmediato() -> String{
        return idJefe
    }



    func filtroDeUsuariosPorJefe(idJefe:String){
            
        //print("\nFuncion de Filtrado por Jefe inmediato")
        
        if ((arrData?.data) == nil) {
            print("Hay datos vacios")
        } else {
            for elemto in arrData!.data{
                if elemto.idsuperiorInmediato == idJefe {
                    print("\nSe encontraron coincidencias")
                    print(elemto.nombre)
                }else{
                    //print("No se han encontrado coincidencias")
                }
            }
        }
    }
    
}

