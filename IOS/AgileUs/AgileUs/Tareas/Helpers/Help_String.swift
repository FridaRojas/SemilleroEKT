//
//  Help_String.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 25/11/21.
//

import Foundation

class HelpString {
    static func removeWord(phrase: String, word: String) -> String{
        
        var newPhrase = phrase
        
        if let range = newPhrase.range(of: word) {
            newPhrase.removeSubrange(range)
            return newPhrase
            
        } else {
            return "error"
        }
    }
    
    static func randomString(length: Int) -> String {
      let letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
      return String((0..<length).map{ _ in letters.randomElement()! })
    }
}
