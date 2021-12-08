//
//  InfoTareas.swift
//  AgileUs
//
//  Created by user203844 on 07/12/21.
//

import Foundation



struct Status: Codable {
    var estatus: String?
    var mensaje: String?
    var data: [Datos]?
}
struct Datos: Codable {
    
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
    var archivo: String?
    var token: String?
    var fecha_iniR: String?
    var fecha_finR: String?

}

