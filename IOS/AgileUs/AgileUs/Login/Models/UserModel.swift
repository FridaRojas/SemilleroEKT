//
//  UserModel.swift
//  AgileUs
//
//  Created by user204412 on 12/12/21.
//

import Foundation

struct User:Codable
{
    let id: String
    let correo: String
    let numeroEmpleado: String
    let nombre: String
    let nombreRol: String
    let idsuperiorInmediato: String?
    let tokenAuth: String
    let idgrupo: String?
}
