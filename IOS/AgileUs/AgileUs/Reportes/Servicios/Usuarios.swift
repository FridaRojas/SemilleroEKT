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
    
    init(id: String,
         nombre: String,
         fechaInicio: String,
         fechaTermino: String,
         nombreRol: String,
         idgrupo: String,
         idsuperiorInmediato: String) {
        self.id = id
        self.nombre = nombre
        self.fechaInicio = fechaInicio
        self.fechaTermino = fechaTermino
        self.nombreRol = nombreRol
        self.idgrupo = idgrupo
        self.idsuperiorInmediato = idsuperiorInmediato
    }
}

class Usuarios{
    
    var webService: ((_ arrDatosUsuario:[Any]) -> Void)?
    //var arrDatosUsuario = [Any]()
    
    //let serviceUser = "https://firebasestorage.googleapis.com/v0/b/proyectop-50f0b.appspot.com/o/busquedaPorIdJefe2.json?alt=media&token=3a996bbf-398d-4785-8bbf-9931f5eecfe2"
    
    let serviceUser = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/usuariosEncontrados2.json?alt=media&token=ceb1c36f-e662-484c-9db3-fb33ef0759b0"
    
    //var serviceUser = "http://10.97.2.198:3040/api/user/findByBossId/"
    
    func webServiceUsuarios(idUsuario: String) {
        
        //serviceUser = "\(serviceUser)\(idUsuario)"
                
        let url = URL(string: serviceUser)
        
        //Generar manejo de excepciones
        URLSession.shared.dataTask(with: url!) {
            
            (informacion, response, error) in
     
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

