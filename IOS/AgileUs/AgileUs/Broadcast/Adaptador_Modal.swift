//
//  Adaptador_Modals.swift
//  modulo_broadcast
//
//  Created by user205703 on 25/11/21.
//

import UIKit

class Adaptador_Modal
{
    func crear_modal_mensajes_enviados(Accion_Confirmacion_Completion: @escaping (_ Datos: [Any])-> Void) -> Modal_Mensajes_Enviados
    {
        let storyboard = UIStoryboard(name: "Modal_Mensajes_Enviados", bundle: .main)
        let modal = storyboard.instantiateViewController(withIdentifier: "Identificador_Modal_Mensajes_Enviados") as! Modal_Mensajes_Enviados
        
        
        modal.accion_confirmacion = Accion_Confirmacion_Completion
        return modal
    }
    
}
