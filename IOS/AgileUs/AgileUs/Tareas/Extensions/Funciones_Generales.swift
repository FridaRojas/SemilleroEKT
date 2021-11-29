//
//  Funciones_Generales.swift
//  Ejemplo_extensiones
//
//  Created by Carlos nitsuga Hernandez hernandez on 12/10/21.
//

import Foundation
import UIKit


var vSpinner : UIView?
extension UIViewController
{

    func Alerta_CamposVacios(title:String, Mensaje:String)
    {
        let Mensaje_alerta = UIAlertController(title:title, message: Mensaje, preferredStyle: UIAlertController.Style.alert)
        
        let BotonAlertOk = UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil)
        Mensaje_alerta.addAction(BotonAlertOk)
        
        self.present(Mensaje_alerta, animated: true, completion: nil)
    }
    func MostrarSpinner(onView : UIView) {
            let Vistaspinner = UIView.init(frame: onView.bounds)
            Vistaspinner.backgroundColor = UIColor.init(red: 0.5, green: 0.5, blue: 0.5, alpha: 0.5)
            let ai = UIActivityIndicatorView.init(style: .whiteLarge)
            ai.startAnimating()
            ai.center = Vistaspinner.center
            
            DispatchQueue.main.async {
                Vistaspinner.addSubview(ai)
                onView.addSubview(Vistaspinner)
            }
            
            vSpinner = Vistaspinner
        }
        
        func RemoverSpinner() {
            DispatchQueue.main.async {
                vSpinner?.removeFromSuperview()
                vSpinner = nil
            }
        }
}
