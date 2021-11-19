//
//  ViewControllerExtension.swift
//  AgileUs
//
//  Created by user204412 on 11/19/21.
//

import Foundation
import UIKit

extension UIViewController{
    
    func  hideNavBar(){
        self.navigationController?.navigationBar.isHidden = true
    }
        //poner barra de regreso
    func  showNavBar(){
        self.navigationController?.navigationBar.isHidden = false
    }
}
