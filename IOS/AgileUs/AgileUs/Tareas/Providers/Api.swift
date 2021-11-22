//
//  Api.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 22/11/21.
//

import Foundation

final class Api {
    
    static let shared = Api()
    
    func createTask(data: Dictionary<String, String>) {
        let session = URLSession.shared
        let url = URL(string: "10.97.7.11:2021/api/tareas/addTarea")!
        var request = URLRequest(url: url)
        
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let jsonData = try! JSONSerialization.data(withJSONObject: data, options: [])
        
        let task = session.uploadTask(with: request, from: jsonData) {
            data, response, error in
            
            if let data = data, let dataString = String(data: data, encoding: .utf8) {
                print(dataString)
            }
            
            if let httpResponse = response as? HTTPURLResponse {
                print(httpResponse.statusCode)
            }
            
        }
        
        task.resume()
    }
    
}
