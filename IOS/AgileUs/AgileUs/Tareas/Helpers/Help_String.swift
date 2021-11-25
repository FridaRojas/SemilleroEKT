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
            print(newPhrase)
            return newPhrase
            
        } else {
            return "error"
        }
    }
}
