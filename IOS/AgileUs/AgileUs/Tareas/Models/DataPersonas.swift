//
//  DataPersonas.swift
//  AgileUs
//
//  Created by Carlos nitsuga Hernandez hernandez on 28/11/21.
//

import Foundation
struct FindPersons: Codable {
let data: [DatosPersons]
}
struct DatosPersons: Codable {
    let id:String
    let nombre:String
}
