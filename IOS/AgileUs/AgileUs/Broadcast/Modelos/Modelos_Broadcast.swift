//
//  Modelos_Broadcast.swift
//  AgileUs
//
//  Created by user205703 on 10/12/21.
//

import Foundation

// LISTAS MENSAJES  ****************************************************

struct Respuesta_Lista_Mensajes: Codable
{
    let estatus:String
    let mensaje:String
    let data: [Mensajes_Broacast]
}
struct Mensajes_Broacast: Codable
{
    let id:String
    let asunto:String
    let descripcion:String
    let idEmisor:String
    let nombreEmisor:String
}
// LISTAS MENSAJES  ****************************************************
