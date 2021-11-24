//
//  Api.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 22/11/21.
//

import Foundation

final class Api {
    
    static let shared = Api()
    
    let url = "http://10.97.0.165:2021/api/tareas"
    let rangeStatusCode200 = 200...299
    let rangeStatusCode400 = 400...499
    let rangeStatusCode500 = 500...599
    
    func createTask(task: Task, success: @escaping (_ task: String) -> (), failure: @escaping (_ error: String) -> ()){
       
        let session = URLSession.shared
        let url = URL(string: "\(url)/agregarTarea")!
        var request = URLRequest(url: url)
        
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let dataToJson = try! JSONEncoder().encode(task)
        
        
        let task = session.uploadTask(with: request, from: dataToJson) {
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
    
    func editTask(id: String) {
        
    }
        
}
