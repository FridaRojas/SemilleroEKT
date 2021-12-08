//
//  MensajesModel.swift
//  AgileUs
//
//  Created by Luis Gregorio Ramirez Villalobos on 02/12/21.
//

import Foundation

struct ObjetoMensajes:Codable{
    let estatus: String
    let mensaje: String
    let data:[Mensajes]
}

struct Mensajes:Codable {
    let id:String
    let idemisor:String
    let idreceptor:String
    let fechaCreacion:String
    let fechaEnviado:String
    let fechaLeido:String
    let statusLeido:Bool
    let statusEnviado:Bool
    let idconversacion:String
}

