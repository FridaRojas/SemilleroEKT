//
//  Strings.swift
//  AgileUs
//
//  Created by Carlos nitsuga Hernandez hernandez on 23/11/21.
//

import Foundation
extension String {
    var EsVacio: Bool {
        return self.trimmingCharacters(in: .whitespaces).isEmpty
    }
}
