//
//  AdaptadorServicios.swift
//  AgileUs
//
//  Created by Fernando González González on 24/11/21.
//

import Foundation
import UIKit

class AdaptadorServicios{
    
    func serviciosWeb(webServiceUser: @escaping (_ Datos: [Any]) -> Void)
    {
        
        let pantallaUsuarios = Usuarios()
        pantallaUsuarios.webService = webServiceUser
        pantallaUsuarios.webServiceUsuarios()
        
        
       
    }
    
}
