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
    
    func addBackgroundColorAndTextColor(backgroundColor: UIColor, textColor: UIColor) {
        self.backgroundColor = backgroundColor
        self.textColor = textColor
    }
    
    func roundCorners(cornerRadius: Double){
        self.layer.borderColor = self.backgroundColor?.cgColor
        self.layer.borderWidth = CGFloat(0.5)
        self.layer.cornerRadius = cornerRadius
        self.clipsToBounds = true
    }
    
    func addIcon(image: UIImage, direction: String){
        let mainView = UIView(frame: CGRect(x: 0, y: 0, width: 50, height: 45))
        let imageView = UIImageView(image: image)
        imageView.contentMode = .scaleAspectFit
        imageView.frame = CGRect(x: 15.0, y: 10.0, width: 24.0, height: 24.0)
        mainView.addSubview(imageView)

        switch direction {
            case "Left":
                self.leftViewMode = .always
                self.leftView = mainView
                break
            case "Right":
                self.rightViewMode = .always
                self.rightView = mainView
                break
            default:
                break
        }
    }
    
    func addButton(iconImage: UIImage) -> UIButton{
        let button = UIButton(type: .custom)
        button.setImage(UIImage(named: "visibility_off"), for: .normal)
        button.imageEdgeInsets = UIEdgeInsets(top: 0, left: -20, bottom: 0, right: 0)
        button.frame = CGRect(x: CGFloat(self.frame.size.width - 25), y: CGFloat(5), width: CGFloat(50), height: CGFloat(45))
        self.rightView = button
        self.rightViewMode = .always
        return button
    }
    
}
