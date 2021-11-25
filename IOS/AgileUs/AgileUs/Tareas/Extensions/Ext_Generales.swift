//
//  Ext_Generales.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 23/11/21.
//

import Foundation
import UIKit

extension UIViewController {
    
    func altertaMensaje(title: String, message: String, confirmationMessage: String, popView: Bool = false) {
        let mensajeAlerta = UIAlertController(title: title, message: message, preferredStyle: UIAlertController.Style.alert)
        
        let botonAlertaOk = UIAlertAction(title: confirmationMessage, style: UIAlertAction.Style.default, handler: {
            action in
            if popView {
                self.navigationController?.popViewController(animated: true)
            }
        })
        
        mensajeAlerta.addAction(botonAlertaOk)
        self.present(mensajeAlerta, animated: true, completion: nil)
    }
    
    func alertaConfirmacion(title: String, message: String, confirmationMessage: String, confirmation: @escaping () -> (), cancel: @escaping () -> ()) {
        let mensajeAlerta = UIAlertController(title: title, message: message, preferredStyle: UIAlertController.Style.alert)
        
        let botonAlertaOk = UIAlertAction(title: confirmationMessage, style: UIAlertAction.Style.default, handler: {
            action in
            
            confirmation()
   
        })
        
        let botonCancel = UIAlertAction(title: "Cancelar", style: .cancel, handler: {
            action in
            
            cancel()
        })
        
        mensajeAlerta.addAction(botonAlertaOk)
        mensajeAlerta.addAction(botonCancel)
        self.present(mensajeAlerta, animated: true, completion: nil)
    }
    
}
