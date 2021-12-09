//
//  TareasModel.swift
//  AgileUs
//
//  Created by Luis Gregorio Ramirez Villalobos on 02/12/21.
//

import Foundation

struct ObjetoTareas:Codable{
    let estatus: String?
    let mensaje: String?
    let data:[Tareas]?
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
