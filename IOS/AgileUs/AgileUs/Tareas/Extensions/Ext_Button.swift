//
//  Ext_Button.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 22/11/21.
//

import Foundation
import UIKit

extension UIButton {
    func initStyle(text: String) {
        
        self.setTitle(text, for: .normal)
        self.titleLabel?.font = UIFont.systemFont(ofSize: 30, weight: .bold)
        
        // Border
        self.layer.cornerRadius = 12.0
        self.layer.borderColor = CGColor(red: 102, green: 187, blue: 106, alpha: 1)
        self.layer.borderWidth = 1.0
        self.clipsToBounds = true
        
        // Shadow
        self.layer.masksToBounds = true
        self.layer.shadowRadius = 1.0
        self.layer.shadowColor = UIColor.gray.cgColor
        self.layer.shadowOffset = CGSize(width: 0.0, height: 0.8)
        self.layer.shadowOpacity = 1.0
    }
    
    func styleTypeInput(title: String) {
        
        self.setTitle(title, for: .normal)
        
        
        self.setTitleColor(UIColor(red: 137/255, green: 139/255, blue: 140/255, alpha: 1), for: .normal)

       self.backgroundColor = UIColor.white

       self.layer.cornerRadius = 12.0
       self.layer.borderColor = CGColor(red: 255, green: 255, blue: 255, alpha: 0)
       self.layer.borderWidth = 1.0
       self.clipsToBounds = true
       
       
       self.layer.masksToBounds = false
       self.layer.shadowRadius = 1.0
       self.layer.shadowColor = UIColor.gray.cgColor
       self.layer.shadowOffset = CGSize(width: 0.0, height: 2.0)
       self.layer.shadowOpacity = 0.2
        
        
       
    }
    
    
    
}
