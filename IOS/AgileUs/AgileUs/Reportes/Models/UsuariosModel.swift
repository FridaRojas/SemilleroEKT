//
//  UsuariosModel.swift
//  AgileUs
//
//  Created by Luis Gregorio Ramirez Villalobos on 02/12/21.
//

import Foundation

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
