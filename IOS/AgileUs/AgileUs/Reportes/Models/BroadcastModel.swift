//
//  BroadcastModel.swift
//  AgileUs
//
//  Created by Luis Gregorio Ramirez Villalobos on 07/12/21.
//

import Foundation

struct ObjetoBroadcast:Codable {
    let estatus: String
    let mensaje: String
    let data:[Broadcast]
    
}

struct Broadcast: Codable {
    let id:String
    let asunto:String
    let descripcion:String
    let idEmisor:String
    let nombreEmisor:String
}
