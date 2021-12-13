//
//  UserRequestModel.swift
//  AgileUs
//
//  Created by user204412 on 12/12/21.
//

import Foundation

struct UserRequest: Codable
{
    let correo: String
    let password: String
    let token: String
}
