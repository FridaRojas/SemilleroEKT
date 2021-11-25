//
//  Ext_UITextField.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 22/11/21.
//

import Foundation
import UIKit

extension UITextField {
    
    func initStyle(placeholder: String, imageName: String = ""){
        
        // Placeholder
        self.attributedPlaceholder = NSAttributedString(string: placeholder, attributes: [NSAttributedString.Key.foregroundColor: UIColor(red: 137/255, green: 139/255, blue: 140/255, alpha: 1)])
 
        self.backgroundColor = UIColor.white
        // Border
        self.layer.cornerRadius = 12.0
        self.layer.borderColor = CGColor(red: 255, green: 255, blue: 255, alpha: 0)
        self.layer.borderWidth = 1.0
        self.clipsToBounds = true
        
        
        // Shadow
        self.layer.masksToBounds = false
        self.layer.shadowRadius = 1.0
        self.layer.shadowColor = UIColor.gray.cgColor
        self.layer.shadowOffset = CGSize(width: 0.0, height: 2.0)
        self.layer.shadowOpacity = 0.2
        
        if imageName != "" {
            self.setIcon(image: UIImage(named: imageName)!)
        }
        
        self.setPadding()

    }
    
    
    func setIcon(image: UIImage, inRight: Bool = true, positionUIImageView: CGRect = CGRect(x: -10, y: 0, width: 20, height: 20), positionUIView: CGRect = CGRect(x: 5, y: 0, width: 20, height: 20) ) {
        
        let icon = UIImageView(frame: positionUIImageView)
        icon.image = image
        
        let iconContainerView: UIView = UIView(frame: positionUIView)
        iconContainerView.addSubview(icon)
        
        if inRight {
            rightView = iconContainerView
            rightViewMode = .always
        } else {
            leftView = iconContainerView
            leftViewMode = .always
        }


    }
    
    func setPadding() {
        let paddingView = UIView(frame: CGRect(x: 0, y: 0, width: 10, height: self.frame.height))
        leftView = paddingView
        leftViewMode = .always
    }
        
}
