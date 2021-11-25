//
//  TextFieldExtension.swift
//  AgileUs
//
//  Created by user204412 on 11/19/21.
//

import Foundation
import UIKit

extension UITextField{
    
    func addBackgroundAndIcon(fondo: String, icono: String){
        self.background = UIImage(named: fondo)
        //print("FONDO")
        
        
        let imageView = UIImageView()
        imageView.image = UIImage(named: icono)
        self.leftView = imageView
        self.leftViewMode = .always
    }
    
    func addBackground(background: String){
        self.background = UIImage(named: background)
    }
    
    func addIcon(icon: String, direction: String, paddingRight: Double, paddingLeft: Double){
        
        let imageView = UIImageView()
        imageView.image = UIImage(named: icon)
        
        //let invisibleRectangle = UIView(frame: CGRect(x: 0, y: 0, width: width, height: self.frame.height))
        self.leftView = UIView(frame: CGRect(x: 0, y: 0, width: paddingRight, height: self.frame.height))
        self.leftViewMode = .always
        
        
        switch direction{
            case "Right":
                self.rightView = imageView
                self.rightViewMode = .always
            break
            case "Left":
                self.leftView = imageView
                self.leftViewMode = .always
            break
            default:
            break
        }
        
        
        /*
        let arrow = UIImageView(image: UIImage(named: icon))
        //if let size = arrow.image?.size {
        arrow.frame = CGRect(x: 10.0, y: 50.0, width: arrow.image!.size.width + 100.0, height: arrow.image!.size.height)
        //}
        //arrow.contentMode = .center
        self.leftView = arrow
        self.leftViewMode = .always*/
        /*
        let iconImage = UIImage(named: icon)
        let iconView = UIImageView(frame:
                                    CGRect(x: 0, y: 0, width: (iconImage?.size.width)!, height: (iconImage?.size.height)!))
        iconView.image = iconImage
        let iconContainerView: UIView = UIView(frame:
                       CGRect(x: 0, y: 0, width: (iconImage?.size.width)!, height: (iconImage?.size.height)!))
        iconContainerView.addSubview(iconView)
        leftView = iconContainerView
        leftViewMode = .always*/
        
    }
    
    func addPadding(direction: String, width: Double, heigth: Double){
        
        let invisibleRectangle = UIView(frame: CGRect(x: 0, y: 0, width: width, height: self.frame.height))
        self.leftView = invisibleRectangle
        self.leftViewMode = .always
        
        /*
        switch direction{
            case "Left":
                self.leftView = invisibleRectangle
                self.leftViewMode = .always
            break
            case "Right":
                self.rightView = invisibleRectangle
                self.rightViewMode = .always
            break
            default:
            break
        }*/
        
        
    }
}
