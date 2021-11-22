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
        self.backgroundColor = UIColor(red: 102/255, green: 187/255, blue: 106/255, alpha: 1)
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
}
