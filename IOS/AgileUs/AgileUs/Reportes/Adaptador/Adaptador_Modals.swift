//
//  Adaptador_Modals.swift
//  AgileUs
//
//  Created by Luis Gregorio Ramirez Villalobos on 22/11/21.
//

import UIKit

class Adaptador_Modals {

    func crear_modal_funcion(datos: [Any], Accion_Confirmacion_completion: @escaping (_ Datos: [Any]) -> Void) -> FiltroModalController {
    
    let storyboard = UIStoryboard(name: "FiltroModal", bundle: .main)
    let modal = storyboard.instantiateViewController(identifier: "id_filtro_modal") as! FiltroModalController
        modal.usuarios = datos
        modal.accion_confirmacion = Accion_Confirmacion_completion
    return modal
}
    
}

