//
//  Ext_UITextView.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 22/11/21.
//

import Foundation
import UIKit

extension UITextView {
    
    func initStyle(placeholder: String) {
        self.text = placeholder
        self.textColor = UIColor(red: 137/255, green: 139/255, blue: 140/255, alpha: 1)
        // Border
        self.layer.cornerRadius = 12.0
        self.layer.borderColor = CGColor(red: 255, green: 255, blue: 255, alpha: 1)
        self.layer.borderWidth = 1.0
        self.clipsToBounds = true
        
        // Shadow
        self.layer.masksToBounds = false
        self.layer.shadowRadius = 1.0
        self.layer.shadowColor = UIColor.gray.cgColor
        self.layer.shadowOffset = CGSize(width: 0.0, height: 1.0)
        self.layer.shadowOpacity = 1.0
    }
    
    
}
