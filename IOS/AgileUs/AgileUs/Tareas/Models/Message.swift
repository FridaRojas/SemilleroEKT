//
//  Message.swift
//  AgileUs
//
//  Created by Carlos nitsuga Hernandez hernandez on 01/12/21.
//

import Foundation

struct MessageTask: Codable {
    var idEmisor: String?
    var idReceptor: String?
    var texto: String?
    var rutaDocumento: String?
    var fechaCreacion: String?
}

struct messageTaskResponse: Codable {
    var status: String?
    var msj: String?
    var data: String?
}
