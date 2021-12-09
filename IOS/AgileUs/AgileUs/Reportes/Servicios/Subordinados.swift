//
//  Subordinados.swift
//  AgileUs
//
//  Created by Fernando González González on 07/12/21.
//

import Foundation

var arrIdSub:Subordinados?

class webServiceSubordinados{
    
    var webServiceSub: ((_ arrIdSubs:[Any]) -> Void)?
    
    func webServiceTaskBySub(idBoos:String){
        let sevice = true
        let serviceByID = "\(serviceUserBoos)\(idBoos)"
        print("id",idBoos)
        
        print(serviceByID)
        
        let url = URL(string: serviceByID)
        
        var request = URLRequest(url: url!)
        request.setValue("c9acb094036a82eb6dbac287b6dc437b87f25c95ee954db469a4c424eacdcaba", forHTTPHeaderField: "tokenAuth")
        
        
        
        print("WebService de tareas filtrando a subordinados")
        
        //Gernerar manejo de excepciones
        URLSession.shared.dataTask(with: request){
            
            (informacion, response, error) in
            
            do{
                
                arrIdSub = try JSONDecoder().decode(Subordinados.self, from: informacion!)
                DispatchQueue.main.async {
                                        
                    if sevice == true{
                        self.webServiceSub?(arrIdSub!.data!)
                    }
                    
                }
                
            }catch{
                print("Error al leer el archivo subordinados tareas")
            }
            
        }.resume()
             
        
    }

}
