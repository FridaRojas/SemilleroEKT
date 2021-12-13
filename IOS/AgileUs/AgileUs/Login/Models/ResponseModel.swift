//
//  ResponseModel.swift
//  AgileUs
//
//  Created by user204412 on 12/12/21.
//

import Foundation

struct Response: Codable
{
    let status: String
    let msj: String
    let data: User
}
