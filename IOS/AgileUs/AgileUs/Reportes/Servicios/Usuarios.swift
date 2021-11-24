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
    let data:[Datos]
}

struct Datos:Codable{
    let id:String
    let nombre:String
    let fechaInicio:String
    let fechaTermino:String
    let nombreRol:String
    let idgrupo:String
    let idsuperiorInmediato:String
    
}


func webServiceUsuarios(service:String){
    
    var arrDatosUsuarios: [Any]?
    
    let url = URL(string: service)
    
    //Gernerar manejo de excepciones
    URLSession.shared.dataTask(with: url!){
        
        (informacion, response, error) in
        
        print(informacion!)
        //print(response!)
        //print(error)
        
        do{
            //Añadir los datos del Json en el array de datos
            arrData = try JSONDecoder().decode(Objeto.self, from: informacion!)
            
            DispatchQueue.main.async {
                
                arrDatosUsuarios = [arrData!]
            
                //print(type(of: arrData!.data))
                
                //Mostrar los datos del arreglo de datos de los usuarios
                print("\nUsuarios registrados")
                
                for elemento in arrData!.data{
                    print("\nId Grupo")
                    print(elemento.idgrupo)
                    print("Id Usuario")
                    print(elemento.id)
                    print("Rol")
                    print(elemento.nombreRol)
                    
                    //Id del superior inmediato
                    idJefe = elemento.idsuperiorInmediato
                }
                
                //filtroDeUsuariosPorJefe(idJefe: id)
                
                print("ID: \(obtenerIdDelJefeInmediato())")
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
        
    print("\nFuncion de Filtrado por Jefe inmediato")
    
    
    if ((arrData?.data) == nil){
        print("Hay datos vacios")
    }else{
            
        for elemto in arrData!.data{
            
            if elemto.idsuperiorInmediato == idJefe{
                
                print("\nSe encontraron coincidencias")
                print(elemto.nombre)
                
            }else{
                
                print("No se han encontrado coincidencias")
                
            }
            
            
        }
        
    }
    
}


