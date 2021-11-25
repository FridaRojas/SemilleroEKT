//
//  ViewControllerExtension.swift
//  AgileUs
//
//  Created by user204412 on 11/19/21.
//

import Foundation
import UIKit

extension UIViewController{
    
    func hideNavBar(){
        self.navigationController?.navigationBar.isHidden = true
    }

    func showNavBar(){
        self.navigationController?.navigationBar.isHidden = false
    }
    
    func simpleAlertMessage(title: String, message: String){
        let alertMessage = UIAlertController(title: title, message: message, preferredStyle: UIAlertController.Style.alert)
        alertMessage.addAction( UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil) )
        
        self.present(
            alertMessage,
            animated:true,
            completion:nil
        )
    }

}
