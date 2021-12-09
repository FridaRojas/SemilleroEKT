//
//  SubordinadosModel.swift
//  AgileUs
//
//  Created by Fernando González González on 07/12/21.
//

import Foundation


struct Subordinados:Codable{
    let data:[IdSubordinado]?
}

struct IdSubordinado:Codable{
    let id: String?
    let statusActivo: Bool?
    let idsuperiorInmediato: String?
}
