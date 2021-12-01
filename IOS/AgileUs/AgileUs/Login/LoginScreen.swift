//
//  LoginScreen.swift
//  AgileUs
//
//  Created by user204412 on 11/18/21.
//

import UIKit

struct UserRequest: Codable {
    let correo: String
    let password: String
    let token: String
}

struct Response: Codable{
    let status: String
    let msj: String
    let data: User
}

struct User:Codable{
    let id: String
    let correo: String
    let numeroEmpleado: String
    let nombre: String
    let nombreRol: String
    let idsuperiorInmediato: String
}

struct ResponseHierarchy:Codable{
    let status: String
    let msj: String
}

class LoginScreen: UIViewController {
    @IBOutlet weak var txtUser: UITextField!
    @IBOutlet weak var txtPassword: UITextField!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        //self.navigationController?.isNavigationBarHidden = true


        //navigationController?.setNavigationBarHidden(false, animated: false)
        //navigationController?.setN
        // Do any additional setup after loading the view.

        hideNavBar()
        initCustomUIKit()
    }

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

    @objc func showHideText(sender: UIButton!) {
        txtPassword.isSecureTextEntry = !txtPassword.isSecureTextEntry
        if txtPassword.isSecureTextEntry == true {
            sender.setImage(UIImage(named: "visibility_off"), for: .normal)

        } else {
            sender.setImage(UIImage(named: "visibility_btn"), for: .normal)
        }
    }

    func setUserVariables(userInfo: User){
        userID = userInfo.id
        userName = userInfo.nombre
        email = userInfo.correo
        employeeNumber = userInfo.numeroEmpleado
        rolName = userInfo.nombreRol
        let idsuperiorInmediato = userInfo.idsuperiorInmediato
        isLogged = true

        UserDefaults.standard.setValue(userID, forKey: "userID")
        UserDefaults.standard.setValue(userName, forKey: "userName")
        UserDefaults.standard.setValue(email, forKey: "email")
        UserDefaults.standard.setValue(employeeNumber, forKey: "employeeNumber")
        UserDefaults.standard.setValue(String(), forKey: "rolName")

        UserDefaults.standard.setValue(isLogged, forKey: "isLogged")

        let serverGetHierarchy = server + "user/findByBossId/" + userID
        let url = URL(string: serverGetHierarchy)
        URLSession.shared.dataTask(with: url!)
        { (data, response, error) in

            if let error = error {
                print ("error: \(error)")
                return
            }

            guard let response = response as? HTTPURLResponse,
                (200...299).contains(response.statusCode) else {
                    print ("Error servidor: \(response)")
                return
            }

            if let mimeType = response.mimeType,
                mimeType == "application/json",
               let data = data
            {

                do{
                    let hierarchyData = try JSONDecoder().decode(ResponseHierarchy.self, from: data)
                    DispatchQueue.main.async {
                        switch hierarchyData.status{
                            case "OK":
                            if idsuperiorInmediato.isEmpty {
                                    hierarchyLevel = 1
                                } else {
                                    hierarchyLevel = 2
                                }
                                break
                            case "NOT_FOUND":
                                    hierarchyLevel = 3
                                break
                            default:
                                break
                        }

                        UserDefaults.standard.setValue(hierarchyLevel, forKey: "hierarchyLevel")
                    }
                }catch let error_S
                {
                    print("Error al conseguir hierarchy level\n\n\(error_S)\n\n********")
                }
            }
        }.resume()
    }

    @IBAction func iniciarSesion(_ sender: UIButton) {

        guard let text = txtUser.text, !text.isEmpty else {
            simpleAlertMessage(title: "Error!", message: "El campo usuario esta vacio")
            return
        }

        guard let text = txtPassword.text, !text.isEmpty else {
            simpleAlertMessage(title: "Error!", message: "El campo contrasena esta vacio")
            return
        }

        //Enviar JSON para inicio de sesion
        var serverLogin = server + "user/validate"

        let user = UserRequest(correo: txtUser.text!, password: txtPassword!.text!, token: pushNotificationToken)

        guard let uploadData = try? JSONEncoder().encode(user) else {
            print("Error al crear JSON")
            return
        }

        let url = URL(string: serverLogin)!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")

        let task = URLSession.shared.uploadTask(with: request, from: uploadData) { data, response, error in
            if let error = error {
                print ("error: \(error)")
                return
            }
            guard let response = response as? HTTPURLResponse,
                (200...299).contains(response.statusCode) else {
                    print ("Error servidor: \(response)")
                return
            }
            if let mimeType = response.mimeType,
                mimeType == "application/json",
                let data = data
            {
                //print ("got data: \(dataString)")
                do{
                    let dataResponse = try JSONDecoder().decode(Response.self, from: data)

                    switch dataResponse.status
                    {
                        case "ACCEPTED":
                            print(dataResponse.data.id)

                            DispatchQueue.main.async {
                                self.setUserVariables(userInfo: dataResponse.data)
                                self.txtUser.becomeFirstResponder()
                                self.txtUser.text = nil
                                self.txtPassword.text = nil
                                self.performSegue(withIdentifier: "Login_To_Home", sender: nil)
                            }

                        break
                        case "BAD_REQUEST":
                            self.simpleAlertMessage(title: "AgileUS", message: "Usuario o contrasena incorrectas")

                        break
                        default:
                        break
                    }

                 }catch{
                    print("Error")
                    print(data)
                }
            }
        }
        task.resume()

        //self.performSegue(withIdentifier: "Login_To_Home", sender: self)

    }

    @IBAction func forgotPassword(_ sender: UIButton) {

        //let modalAdapter = ModalsAdapter()
        //let modalForgotPassword = modalAdapter.generateModalForgotPassword()

        self.present(ModalsAdapter().generateModalForgotPassword(), animated: true)
    }

}
