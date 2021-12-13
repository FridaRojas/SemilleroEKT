//
//  LoginService.swift
//  AgileUs
//
//  Created by user204412 on 12/12/21.
//

import Foundation

class LoginService
{
    func getCredentials(email: String, password: String, context: LoginScreen)
    {
        var serverLogin = server + "user/validate"

        let user = UserRequest(correo: email, password: password, token: pushNotificationToken)

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
                var dataJson: Any?
                
                do {
                    let json = try JSONSerialization.jsonObject(with: data as Data, options: .allowFragments) as! NSDictionary
                    
                    DispatchQueue.main.async
                    {
                        if let dataJson = json["data"] as? String
                        {
                            context.simpleAlertMessage(title: "AgileUS", message: "Usuario o contrasena incorrectas")
                            return
                        }
                        else
                        {
                            do{
                                let dataResponse = try JSONDecoder().decode(Response.self, from: data)
                                switch dataResponse.status
                                {
                                    case "ACCEPTED":
                                        print(dataResponse.data.id)

                                        DispatchQueue.main.async {
                                            self.setUserVariables(userInfo: dataResponse.data, context: context)
                                            context.performSegue(withIdentifier: "Login_To_Home", sender: nil)
                                        }

                                    break
                                    case "BAD_REQUEST":
                                        context.simpleAlertMessage(title: "AgileUS", message: "Usuario o contrasena incorrectas")

                                    break
                                    default:
                                    break
                                }

                             }catch let error_catch {
                                print("Error  -----: \(error_catch)")
                                //print(data)
                            }
                            
                        }
                    }
                } catch let error_catch
                {
                    print("Error    * : \(error_catch)")
                }
            }
        }
        task.resume()
    }
    
    //Colocar variables globales
    func setUserVariables(userInfo: User, context: LoginScreen){
        userID = userInfo.id
        userName = userInfo.nombre
        email = userInfo.correo
        employeeNumber = userInfo.numeroEmpleado
        rolName = userInfo.nombreRol
        isLogged = true
        
        tokenAuth = userInfo.tokenAuth
        idGrupo = userInfo.idgrupo ?? String()

        UserDefaults.standard.setValue(userID, forKey: "userID")
        UserDefaults.standard.setValue(userName, forKey: "userName")
        UserDefaults.standard.setValue(email, forKey: "email")
        UserDefaults.standard.setValue(employeeNumber, forKey: "employeeNumber")
        UserDefaults.standard.setValue(rolName, forKey: "rolName")
        UserDefaults.standard.setValue(tokenAuth, forKey: "tokenAuth")
        UserDefaults.standard.setValue(idGrupo, forKey: "idGrupo")
        
        UserDefaults.standard.setValue(isLogged, forKey: "isLogged")
        
        let hierarchyLevelService = HierarchyLevelService()
        hierarchyLevelService.setHierarchyLevel(idsuperiorInmediato: userInfo.idsuperiorInmediato, context: context)
    }
}
