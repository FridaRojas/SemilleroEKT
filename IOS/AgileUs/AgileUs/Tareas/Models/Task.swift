//
//  Task.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 24/11/21.
//

import Foundation

struct Task: Codable {
    var id_grupo: String
    var id_emisor: String
    var nombre_emisor: String
    var id_receptor: String
    var nombre_receptor: String
    var fecha_ini: String
    var fecha_fin: String
    var titulo: String
    var descripcion: String
    var prioridad: String
    var estatus: String
    
    func toJson() {
        do {
            let jsonData = try JSONEncoder().encode(self)
        } catch {
            print(error)
        }
    }
}

