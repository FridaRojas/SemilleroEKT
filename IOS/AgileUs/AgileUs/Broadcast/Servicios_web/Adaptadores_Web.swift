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
    func Servicio_Lista_Mensajes_broadcast(Funcion_Lambda_Broadcast: @escaping(_ Datos: Any) -> Void)
    {
        let instancia_Servicio = servicios_broadcast()
        instancia_Servicio.sw_lista_de_mensajes = Funcion_Lambda_Broadcast
        instancia_Servicio.servicio_listar_mensajes()
    }
    
    func Servicio_crear_mensaje_a_broadcast(Func_Lambda_Enviar_Mensaje: @escaping(_ Enviar_Datos: Any) -> Void)
    {
        let instancia_enviar_mensaje = servicios_broadcast()
        instancia_enviar_mensaje.sw_enviar_mensaje = Func_Lambda_Enviar_Mensaje
        instancia_enviar_mensaje.servicio_mensaje_a_broadcast()
    }
    
    
    
    
}
