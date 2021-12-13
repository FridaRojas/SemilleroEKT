//
//  Adaptadores_Web.swift
//  AgileUs
//
//  Created by user205703 on 10/12/21.
//

import Foundation
import UIKit


class Adaptadores_Web
{
    func Servicio_Lista_Mensajes_broadcast(Funcion_Lamda: @escaping(_ Datos: Any) -> Void)
    {
        let instancia_Servicio = servicios_broadcast()
        instancia_Servicio.sw_lista_de_mensajes = Funcion_Lamda
        instancia_Servicio.servicio_listar_mensajes()
    }
    
}
