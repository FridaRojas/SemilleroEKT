//
//  Teclado.swift
//  AgileUs
//
//  Created by Carlos nitsuga Hernandez hernandez on 22/11/21.
//

import UIKit

extension UITextFieldDelegate
{
 func Configurar_teclado()
    {
        /*
        Campo_nombre_tarea.delegate = self
        Campo_descripcion.delegate = self
         
         //Gesto para cerrar teclado al tocar la pantalla
         let gesto_tap = UITapGestureRecognizer(target: self, action: #selector(Ocultar_Teclado(_:)))
         
         //Eventos de teclado
         NotificationCenter.default.addObserver(self, selector: #selector(cambio_teclado(notification:)), name: UIResponder.keyboardWillShowNotification, object: nil)
         NotificationCenter.default.addObserver(self, selector: #selector(cambio_teclado(notification:)), name: UIResponder.keyboardWillHideNotification, object: nil)
         NotificationCenter.default.addObserver(self, selector: #selector(cambio_teclado(notification:)), name: UIResponder.keyboardWillChangeFrameNotification, object: nil)
         
        */
    }
    
    @objc func Ocultar_Teclado(_ sender: UITapGestureRecognizer)
    {
        /*
         Campo_nombre_tarea.resignFirstResponder()
        Campo_descripcion.resignFirstResponder()
         */
    }
    deinit
    {
        /*
        NotificationCenter.default.removeObserver(self, name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.removeObserver(self, name: UIResponder.keyboardWillHideNotification, object: nil)
        NotificationCenter.default.removeObserver(self, name: UIResponder.keyboardWillChangeFrameNotification, object: nil)
        */
    }
    
    @objc func cambio_teclado(notification:Notification)
    {
        /* let info_teclado = notification.userInfo?[UIResponder.keyboardFrameBeginUserInfoKey] as? NSValue
        let dimensiones_teclado = info_teclado?.cgRectValue
        let altura_teclado = dimensiones_teclado?.height
        var nombre_notificacion = notification.name.rawValue

        switch nombre_notificacion
        {
        case "UIKeyboardWillHideNotification": view.frame.origin.y = 0
        case "UIKeyboardWillShowNotification":
            if Campo_nombre_tarea.isFirstResponder
            {
               
                view.frame.origin.y = (-0.5 * altura_teclado!) * 0.5
            }
            else if Campo_descripcion.isFirstResponder
            {
                
                view.frame.origin.y = (-0.5 * altura_teclado!) * 0.9
            }
            else
            {
                
                view.frame.origin.y = (-0.5 * altura_teclado!) * 0.7
            }
            
        default: view.frame.origin.y = 0
        }*/
    }

}
