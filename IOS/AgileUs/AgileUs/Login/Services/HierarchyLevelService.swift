//
//  HierarchyLevelService.swift
//  AgileUs
//
//  Created by user204412 on 12/12/21.
//

import Foundation

class HierarchyLevelService
{
    func setHierarchyLevel(idsuperiorInmediato: String?, context: LoginScreen){
        //Obtener jerarquia de usuario
        let url = URL(string: server + "user/findByBossId/" + userID)!
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue(tokenAuth, forHTTPHeaderField: "tokenSession")
        URLSession.shared.dataTask(with: request) //URLSession.shared.dataTask(with: url!)
        { (data, response, error) in

            if let error = error {
                print ("error: \(error)")
                context.simpleAlertMessage(title: "Error!", message: "Error al conectar con el servidor")
                return
            }

            guard let response = response as? HTTPURLResponse,
                (200...299).contains(response.statusCode) else {
                    print ("Error servidor: \(response)")
                    context.simpleAlertMessage(title: "Error!", message: "Error de respuesta del servidor")
                return
            }

            if let mimeType = response.mimeType,
                mimeType == "application/json",
               let data = data
            {

                do{
                    let hierarchyData = try JSONDecoder().decode(ResponseHierarchy.self, from: data)
                    //context.tokenValidator(status: hierarchyData.status, message: hierarchyData.status)
                    DispatchQueue.main.async {
                        print("idSuperiorInmediato: \(idsuperiorInmediato)")
                        switch hierarchyData.status{
                            case "OK":
                                if let idSuperiorInmediato = idsuperiorInmediato {
                                    
                                    if idSuperiorInmediato.isEmpty{
                                        hierarchyLevel = 1
                                    } else {
                                        hierarchyLevel = 2
                                    }
                                    
                                } else {
                                    hierarchyLevel = 1
                                }
                                break
                            case "BAD_REQUEST":
                                    hierarchyLevel = 3
                                break
                            default:
                                break
                        }
                        UserDefaults.standard.setValue(hierarchyLevel, forKey: "hierarchyLevel")
                        print("hierarchyLevel: \(hierarchyLevel)")
                    }
                }catch let error_S
                {
                    print("Error al conseguir hierarchy level\n\n\(error_S)\n\n**")
                }
            }
        }.resume()
    }
}
