//
//  LoginScreen.swift
//  AgileUs
//
//  Created by user204412 on 11/18/21.
//

import UIKit

struct User: Codable {
    let correo: String
    let password: String
    let token: String
}

class LoginScreen: UIViewController {
    @IBOutlet weak var txtUser: UITextField!
    @IBOutlet weak var txtPassword: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavBar()
        //txtUser.addBackgroundAndLeftIcon(fondo: <#T##String#>, icono: <#T##String#>)
        // Do any additional setup after loading the view.
    }
    
    @IBAction func iniciarSesion(_ sender: UIButton) {
        
        //Enviar JSON para inicio de sesion
        let serverLogin = server+"user/validate"
        
        self.performSegue(withIdentifier: "Login_To_Home", sender: self)
        let user = User(correo: txtUser.text!, password: txtPassword!.text!, token: "wefwfefwf121221fwe")
        
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
                let data = data,
                let dataString = String(data: data, encoding: .utf8) {
                print ("got data: \(dataString)")
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
