//
//  Api.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 22/11/21.
//

import Foundation

final class Api {
    
    static let shared = Api()
    
    let url = "http://10.97.5.172:2021/api/tareas"
    let rangeStatusCode200 = 200...299
    let rangeStatusCode400 = 400...499
    let rangeStatusCode500 = 500...599
    
    func createTask(data: Dictionary<String, String>, success: @escaping (_ task: String) -> (), failure: @escaping (_ error: String) -> ()){
       
        let session = URLSession.shared
        let url = URL(string: "\(url)/agregarTarea")!
        var request = URLRequest(url: url)
        var test = "";
        
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let jsonData = try! JSONSerialization.data(withJSONObject: data, options: [])
        
        
        let task = session.uploadTask(with: request, from: jsonData) {
            (data, response, error) in
            
            if let httpResponse = response as? HTTPURLResponse,
                  self.rangeStatusCode500.contains(httpResponse.statusCode) {
                  failure("error")
                  return;
            }
            
            if let httpResponse = response as? HTTPURLResponse,
                  self.rangeStatusCode400.contains(httpResponse.statusCode){
                  failure("error")
                  return;
            }
            
            if error != nil {
                failure("error")
                return;
            }
            if let data = data, let dataString = String(data: data, encoding: .utf8) {
                success(dataString)
                print("Success")
            }
       
        }
        task.resume()
    }
    
}
