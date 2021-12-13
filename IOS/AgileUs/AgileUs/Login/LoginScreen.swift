//
//  LoginScreen.swift
//  AgileUs
//
//  Created by user204412 on 11/18/21.
//

import UIKit

class LoginScreen: UIViewController {
    
    //Outlets de viewcontroller
    @IBOutlet weak var txtUser: UITextField!
    @IBOutlet weak var txtPassword: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavBar()
        initCustomUIKit()
        
        //borrar datos solo prueba*********
        
        txtUser.text = "rogelioL@gmail.com"
        txtPassword.text = "0000000000"
        
        //*******************************
        
        
    }

    //Inicializar interface
    func initCustomUIKit(){
        txtUser.addBackgroundColorAndTextColor(backgroundColor: UIColor(red: 245.0/255, green: 245.0/255, blue: 245.0/255, alpha: 1.0), textColor: UIColor(red: 156.0/255, green: 158.0/255, blue: 159.0/255, alpha: 1.0))
        txtUser.roundCorners(cornerRadius: 20.0)
        txtUser.addIcon(image: UIImage(named: "user_icon")!, direction: "Left")

        txtPassword.addBackgroundColorAndTextColor(backgroundColor: UIColor(red: 245.0/255, green: 245.0/255, blue: 245.0/255, alpha: 1.0), textColor: UIColor(red: 156.0/255, green: 158.0/255, blue: 159.0/255, alpha: 1.0))
        txtPassword.roundCorners(cornerRadius: 20.0)
        txtPassword.addIcon(image: UIImage(named: "vector_icon")!, direction: "Left")
        let visibilityButton = txtPassword.addButton(iconImage: UIImage(named: "visibility_off")!)
        visibilityButton.addTarget(self, action: #selector(showHideText), for: .touchUpInside)
    }

    //Funcion para mostrar u ocultar objetos
    @objc func showHideText(sender: UIButton!) {
        txtPassword.isSecureTextEntry = !txtPassword.isSecureTextEntry
        if txtPassword.isSecureTextEntry == true {
            sender.setImage(UIImage(named: "visibility_off"), for: .normal)

        } else {
            sender.setImage(UIImage(named: "visibility_btn"), for: .normal)
        }
    }

    @IBAction func iniciarSesion(_ sender: UIButton) {
        
        //Validar campos de entrada
        guard let text = txtUser.text, !text.isEmpty else {
            simpleAlertMessage(title: "Error!", message: "El campo usuario esta vacio")
            return
        }

        guard let text = txtPassword.text, !text.isEmpty else {
            simpleAlertMessage(title: "Error!", message: "El campo contraseña esta vacio")
            return
        }
        
        let loginService = LoginService()
        loginService.getCredentials(email: txtUser.text!, password: txtPassword.text!, context: self)
    }

}
