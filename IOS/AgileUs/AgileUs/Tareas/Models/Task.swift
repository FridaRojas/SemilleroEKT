//
//  Task.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 24/11/21.
//

import Foundation

struct Task: Codable {
    
    var id_tarea: String?
    var id_grupo: String?
    var id_emisor: String?
    var nombre_emisor: String?
    var id_receptor: String?
    var nombre_receptor: String?
    var fecha_ini: String?
    var fecha_BD: String?
    var fecha_fin: String?
    var titulo: String?
    var descripcion: String?
    var prioridad: String?
    var estatus: String?
    var leido: Bool?
    var fechaLeido: String?
    var createdDate: String?
    var observaciones: String?
    
}

