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
        self.textColor = UIColor(red: 107/255, green: 107/255, blue: 107/255, alpha: 1)
        self.font = UIFont.systemFont(ofSize: 15.0, weight: .regular)
        
        // Border
        
        self.layer.cornerRadius = 5.0
        self.layer.borderColor = CGColor(red: 107/255, green: 107/255, blue: 107/255, alpha: 0.5)
        self.layer.borderWidth = 1.0
        self.clipsToBounds = true
        
        /* Shadow
        self.layer.masksToBounds = false
        self.layer.shadowRadius = 1.0
        self.layer.shadowColor = UIColor.gray.cgColor
        self.layer.shadowOffset = CGSize(width: 0.0, height: 2.0)
        self.layer.shadowOpacity = 0.2
         */

    }
    
    func initStyleEdit(fontSize: CGFloat = 15, fontWeight: UIFont.Weight = .regular, colorText: UIColor = .black, selected: Bool = false) {
        
        self.font = UIFont.systemFont(ofSize: fontSize, weight: fontWeight)
        self.backgroundColor = UIColor(red: 1, green: 1, blue: 1, alpha: 0)
        self.textColor = colorText
        
        self.layer.cornerRadius = 5.0
        self.layer.borderColor = CGColor(red: 107/255, green: 107/255, blue: 107/255, alpha: 0.5)
        self.layer.borderWidth = 1.0
        self.clipsToBounds = true
        
        if selected {
            // Border
            self.layer.cornerRadius = 5.0
            self.layer.borderColor = CGColor(red: 107/255, green: 107/255, blue: 107/255, alpha: 0.5)
            self.layer.borderWidth = 1.0
            self.clipsToBounds = true
            
            /* Shadow
            self.layer.masksToBounds = true
            self.layer.shadowRadius = 1.0
            self.layer.shadowColor = UIColor.gray.cgColor
            self.layer.shadowOffset = CGSize(width: 0.0, height: 2.0)
            self.layer.shadowOpacity = 0.2
             */
        }
        
        
    }
    
    func stopBackspaceIn(word: String, text: String) -> Bool {
        
        let char = text.cString(using: String.Encoding.utf8)!
        let isBackSpace = strcmp(char, "\\b")
        
        if self.text != word {
            return true
        } else {
            if (isBackSpace == -92) {
                return false
            } else {
                return true
            }
        }
        
    }
    
}
