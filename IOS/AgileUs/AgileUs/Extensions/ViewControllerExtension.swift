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
    
    func addLogoutButton(){
        let button = UIButton(frame: CGRect(x: 0, y: 0, width: 15, height: 15))
        button.center.x = CGFloat(380.0)
        button.center.y = CGFloat(80.0)

        button.setImage(UIImage(named: "black_logout_icon"), for: .normal)
        button.setTitle("Cerrar Sesion", for: .normal)
        button.addTarget(self, action: #selector(logOut), for: .touchUpInside)

        self.view.addSubview(button)
    }
    
    @objc func logOut(sender: UIButton!) {
        UserDefaults.standard.setValue(String(), forKey: "userID")
        UserDefaults.standard.setValue(String(), forKey: "userName")
        UserDefaults.standard.setValue(String(), forKey: "email")
        UserDefaults.standard.setValue(String(), forKey: "employeeNumber")
        UserDefaults.standard.setValue(false, forKey: "isLogged")
        navigationController?.popViewController(animated: true)
    }
}
