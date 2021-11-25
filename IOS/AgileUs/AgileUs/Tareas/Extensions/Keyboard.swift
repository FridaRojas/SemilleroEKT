//
//  Keyboard.swift
//  AgileUs
//
//  Created by Carlos nitsuga Hernandez hernandez on 24/11/21.
//

import Foundation
import UIKit

extension AsignarTareaViewController: UITextFieldDelegate
{
    func initTextFieldDelegate()
    {
        nameTaskField.delegate = self
        descriptionText.delegate = self
        personSelectField.delegate = self
        priortyField.delegate = self
        dateStartField.delegate = self
        dateEndField.delegate = self
        fileField.delegate = self
    }
    func Configurar_teclado()
       {
           
           initTextFieldDelegate()
            //Gesto para cerrar teclado al tocar la pantalla
            let gesto_tap = UITapGestureRecognizer(target: self, action: #selector(Ocultar_Teclado(_:)))
           self.view.addGestureRecognizer(gesto_tap)
            //Eventos de teclado
            NotificationCenter.default.addObserver(self, selector: #selector(cambio_teclado(notification:)), name: UIResponder.keyboardWillShowNotification, object: nil)
            NotificationCenter.default.addObserver(self, selector: #selector(cambio_teclado(notification:)), name: UIResponder.keyboardWillHideNotification, object: nil)
            NotificationCenter.default.addObserver(self, selector: #selector(cambio_teclado(notification:)), name: UIResponder.keyboardWillChangeFrameNotification, object: nil)
            
           
       }
    
        @objc func Ocultar_Teclado(_ sender: UITapGestureRecognizer)
        {
        
         nameTaskField.resignFirstResponder()
         descriptionText.resignFirstResponder()
         personSelectField.resignFirstResponder()
         priortyField.resignFirstResponder()
         dateStartField.resignFirstResponder()
         dateEndField.resignFirstResponder()
         fileField.resignFirstResponder()
        
         
        }

    
    @objc func cambio_teclado(notification:Notification)
    {
         let info_teclado = notification.userInfo?[UIResponder.keyboardFrameBeginUserInfoKey] as? NSValue
        let dimensiones_teclado = info_teclado?.cgRectValue
        let altura_teclado = dimensiones_teclado?.height
        var nombre_notificacion = notification.name.rawValue

        switch nombre_notificacion
        {
        case "UIKeyboardWillHideNotification": view.frame.origin.y = 0
        case "UIKeyboardWillShowNotification":
            if nameTaskField.isFirstResponder
            {
               
                view.frame.origin.y = (-0.5 * altura_teclado!) * 0.1
            }
            else if descriptionText.isFirstResponder
            {
                
                view.frame.origin.y = (-0.5 * altura_teclado!) * 1.3
            }
            else if fileField.isFirstResponder
            {
                
                view.frame.origin.y = (-0.5 * altura_teclado!) * 1.7
            }

            else
            {
                
                view.frame.origin.y = (-0.5 * altura_teclado!) * 0.1
            }
            
        default: view.frame.origin.y = 0
        }
    }
    //pregunta al delegado si debe procesar la pulsación del botón Retorno para el campo de texto.
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        switch textField
        {
        case nameTaskField:
            personSelectField.becomeFirstResponder()
        case personSelectField:
            priortyField.becomeFirstResponder()
        case priortyField:
            dateStartField.becomeFirstResponder()
        case dateStartField:
            dateEndField.becomeFirstResponder()
        case dateEndField:
            descriptionText.becomeFirstResponder()
        default: textField.becomeFirstResponder()
        }
        return true
    }
    
}
