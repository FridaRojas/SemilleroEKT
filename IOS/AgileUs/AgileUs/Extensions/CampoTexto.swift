//
//  CampoTexto.swift
//  AgileUs
//
//  Created by user204412 on 11/18/21.
//

import Foundation
import UIKit

extension UITextField
{
    func agregaFondoEIcono(fondo: String, icono: String){
        self.background = UIImage(named: fondo)
        let imageView = UIImageView()
        imageView.image = UIImage(named: icono)
        self.leftView = imageView
        self.leftViewMode = .always
    }

}
