//
//  ViewControllerExtension.swift
//  AgileUs
//
//  Created by user204412 on 11/19/21.
//

import Foundation
import UIKit

extension UIViewController{
    
    //extenciones fecha
    func Obtener_valor_fecha(fecha: Date, stilo: String) -> String
    {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "es_419")
        switch stilo
        {
        case "Fecha_Usuario": formatter.dateFormat = "dd/MMMM/yyyy"
        case "Fecha_Base_datos": formatter.dateFormat = "yyyy/MM/dd"
        case "Fecha_mongo": formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ"
        default: formatter.dateFormat = "dd/MM/yyyy hh:mm a"
        }
        return formatter.string(from: fecha)
    }
    struct JSONStringEncoder {
        func encode(_ dictionary: [String: Any]) -> String?
        {
            guard JSONSerialization.isValidJSONObject(dictionary) else {assertionFailure("Invalid json object received.")
                return nil
        }
            let jsonObject: NSMutableDictionary = NSMutableDictionary()
            let jsonData: Data

            dictionary.forEach { (arg) in jsonObject.setValue(arg.value, forKey: arg.key)}

            do { jsonData = try JSONSerialization.data(withJSONObject: jsonObject, options: .prettyPrinted)}
            catch
            {
                assertionFailure("JSON data creation failed with error: \(error).")
                return nil
            }

            guard let jsonString = String.init(data: jsonData, encoding: String.Encoding.utf8) else {
                assertionFailure("JSON string creation failed.")
                return nil
            }
           // print("Valores_Mensaje: \(jsonString)")
           
            return jsonString
        }
    }
    func hideNavBar(){
        self.navigationController?.navigationBar.isHidden = true
    }

    func showNavBar(){
        self.navigationController?.navigationBar.isHidden = false
        self.navigationController?.navigationBar.barTintColor = UIColor(red: 76/255, green: 217/255, blue: 100/255, alpha: 1)
        self.navigationController?.navigationBar.titleTextAttributes = [NSAttributedString.Key.foregroundColor: UIColor.black]
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
        let button = UIButton(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        button.center.x = CGFloat(25.0)
        button.center.y = CGFloat(60.0)

        button.setImage(UIImage(named: "white_logout_icon"), for: .normal)
        button.setTitle("Cerrar Sesion", for: .normal)
        button.addTarget(self, action: #selector(logOut), for: .touchUpInside)

        self.view.addSubview(button)
    }
    
    @objc func logOut(sender: UIButton!) {
        print("CERRAR SESION")
        cleanLocalVariables()
    }
    
    //Validar si el token ha expirado, en caso de que si cerrar sesion
    func tokenValidator(variable: Any?, context: ChatsScreen){
        
        guard let variable = variable else
        {
            print("DEBERIA CERRAR SESION")
            cleanLocalVariables(context: context)
            return
        }
        print("I HERE TODO OK")
    }
    
    //Borrar variables locales
    func cleanLocalVariables(){
        UserDefaults.standard.setValue(String(), forKey: "userID")
        UserDefaults.standard.setValue(String(), forKey: "userName")
        UserDefaults.standard.setValue(String(), forKey: "email")
        UserDefaults.standard.setValue(String(), forKey: "employeeNumber")
        UserDefaults.standard.setValue(String(), forKey: "rolName")
        UserDefaults.standard.setValue(String(), forKey: "hierarchyLevel")
        UserDefaults.standard.setValue(false, forKey: "isLogged")
        UserDefaults.standard.setValue(String(), forKey: "tokenAuth")
        UserDefaults.standard.setValue(String(), forKey: "idGrupo")
        navigationController?.popViewController(animated: true)
        //navigationController?.popViewController(animated: true)
    }
    
    func cleanLocalVariables(context: ChatsScreen){
        UserDefaults.standard.setValue(String(), forKey: "userID")
        UserDefaults.standard.setValue(String(), forKey: "userName")
        UserDefaults.standard.setValue(String(), forKey: "email")
        UserDefaults.standard.setValue(String(), forKey: "employeeNumber")
        UserDefaults.standard.setValue(String(), forKey: "rolName")
        UserDefaults.standard.setValue(String(), forKey: "hierarchyLevel")
        UserDefaults.standard.setValue(false, forKey: "isLogged")
        UserDefaults.standard.setValue(String(), forKey: "tokenAuth")
        UserDefaults.standard.setValue(String(), forKey: "idGrupo")
        DispatchQueue.main.async
        {
            context.navigationController?.popViewController(animated: true)
        }
    }
}
