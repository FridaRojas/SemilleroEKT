//
//  DataTareas.swift
//  AgileUs
//
//  Created by Carlos nitsuga Hernandez hernandez on 28/11/21.
//

import Foundation
struct Tareas: Codable
{
    let id_tarea:String
    let titulo:String
    let nombre_receptor:String
    let prioridad:String
    let estatus:String
    let descripcion:String
    let fecha_ini:String
    let fecha_fin:String
    let observaciones:String?
}
