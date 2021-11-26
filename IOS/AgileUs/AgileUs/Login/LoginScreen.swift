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
}

class LoginScreen: UIViewController {
    @IBOutlet weak var txtUser: UITextField!
    @IBOutlet weak var txtPassword: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        hideNavBar()
        
        txtUser.addBackgroundColorAndTextColor(backgroundColor: UIColor(red: 245.0/255, green: 245.0/255, blue: 245.0/255, alpha: 1.0), textColor: UIColor(red: 156.0/255, green: 158.0/255, blue: 159.0/255, alpha: 1.0))
        txtUser.roundCorners(cornerRadius: 20.0)
        //txtUser.addIcon(icon: "user_icon")
        //txtUser.withImage(direction: "Left", image: UIImage(named: "user_icon")!, colorSeparator: UIColor.black)
        txtUser.addIcon(image: UIImage(named: "user_icon")!, direction: "Left")
        
        txtPassword.addBackgroundColorAndTextColor(backgroundColor: UIColor(red: 245.0/255, green: 245.0/255, blue: 245.0/255, alpha: 1.0), textColor: UIColor(red: 156.0/255, green: 158.0/255, blue: 159.0/255, alpha: 1.0))
        txtPassword.roundCorners(cornerRadius: 20.0)
        txtPassword.addIcon(image: UIImage(named: "vector_icon")!, direction: "Left")
        
        
        /*let path = UIBezierPath(roundedRect:txtUser.bounds, byRoundingCorners:[UIRectCorner.topRight, .bottomLeft], cornerRadii: CGSize(width: 20, height: 20))
        
        //UIBezierPath(rou)
        let maskLayer = CAShapeLayer()
        maskLayer.path = path.cgPath
        txtUser.layer.mask = maskLayer*/

        

        
        //txtUser.layer.cornerRadius = 20.0
        //txtUser.clipsToBounds = true
        
        /*txtPassword.addBackgroundColor(bgColor: UIColor.green /*UIColor(red: 245.0/255, green: 245.0/255, blue: 245.0/255, alpha: 1.0)*/, placeholderTxt: "DOWN", placeholderColor: UIColor.black, txtColor: UIColor.black)*/
        //txtUser.addBackground(background: "login_txt_box")
        
        //txtUser.addPadding(direction: "Left", width: 50, heigth: 0)
        //txtUser.addIcon(icon: "user_icon", direction: "Left", paddingRight: 0, paddingLeft: 0)//.addIcon(icon: "user_icon", direction: "Left")//
        //txtUser.addPadding(direction: "Right", width: 25, heigth: 0)
        
        //txtUser.addBackgroundAndIcon(fondo: "login_txt_box", icono: "user_icon")
        
        //txtUser.addBackgroundAndLeftIcon(fondo: <#T##String#>, icono: <#T##String#>)
        // Do any additional setup after loading the view.
    }

    func setUserVariables(userInfo: User){
        userID = userInfo.id
        userName = userInfo.nombre
        email = userInfo.correo
        employeeNumber = userInfo.numeroEmpleado
        isLogged = true

        UserDefaults.standard.setValue(userID, forKey: "userID")
        UserDefaults.standard.setValue(userName, forKey: "userName")
        UserDefaults.standard.setValue(email, forKey: "email")
        UserDefaults.standard.setValue(employeeNumber, forKey: "employeeNumber")
        
        UserDefaults.standard.setValue(isLogged, forKey: "isLogged")
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
        var serverLogin = server+"user/validate"
        serverLogin = "https://firebasestorage.googleapis.com/v0/b/pruebas-eqipo-admin.appspot.com/o/login%20v2.json?alt=media&token=49f226f5-eb9f-4213-97ee-7f726d4db02b"
        
        let user = UserRequest(correo: txtUser.text!, password: txtPassword!.text!, token: "wefwfefwf121221fwe")
        
        guard let uploadData = try? JSONEncoder().encode(user) else {
            print("Error al crear JSON")
            return
        }
        
        let url = URL(string: serverLogin)!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let task = URLSession.shared.dataTask(with: url){ data, response, error in
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
                                self.performSegue(withIdentifier: "Login_To_Home", sender: nil)
                            }
                        
                        break
                        case "BAD_REQUEST":
                            self.simpleAlertMessage(title: "AgileUS", message: "Usuario o contrasena incorrectas")
                        
                        break
                        default:
                        break
                    }
                    
                    /*
                    if dataResponse.status == "ACCEPTED" {
                        
                    } else if data*/
                    
                    

                    
                    
                 }catch{
                    print("Error")
                    print(data)
                }
            }
        }
        task.resume()
        
        //self.performSegue(withIdentifier: "Login_To_Home", sender: self)
        
    }
    
    /*
     @IBAction func iniciarSesion(_ sender: UIButton) {
     }
     // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
