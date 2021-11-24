//
//  Funciones_Generales.swift
//  Ejemplo_extensiones
//
//  Created by Carlos nitsuga Hernandez hernandez on 12/10/21.
//

import Foundation
import UIKit



extension UIViewController
{
    
    func Alerta_CamposVacios(title:String, Mensaje:String)
    {
        let Mensaje_alerta = UIAlertController(title:title, message: Mensaje, preferredStyle: UIAlertController.Style.alert)
        
        let BotonAlertOk = UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil)
        Mensaje_alerta.addAction(BotonAlertOk)
        
        self.present(Mensaje_alerta, animated: true, completion: nil)
    }


}
