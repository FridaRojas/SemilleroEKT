//
//  ButtonExtension.swift
//  AgileUs
//
//  Created by user204412 on 11/25/21.
//

import Foundation
import UIKit

extension UIButton{
    func addBackgroundColorAndTextColor(backgroundColor: UIColor, textColor: UIColor) {
        self.backgroundColor = backgroundColor
        //self.textColor = textColor
        self.tintColor = textColor
    }
    
    func roundCorners(cornerRadius: Double){
        self.layer.borderColor = self.backgroundColor?.cgColor
        self.layer.borderWidth = CGFloat(0.5)
        self.layer.cornerRadius = cornerRadius
        self.clipsToBounds = true
    }
}
