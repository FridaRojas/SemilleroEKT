//
//  Services.swift
//  AgileUs
//
//  Created by Carlos nitsuga Hernandez hernandez on 26/11/21.
//

import Foundation
import UIKit
/*final class Api {

    static let shared = Api()
    //para mostrar tarea
    let url = "http://10.97.3.134:2021/api/tareas"
   // para obtener las personas asignadas let url = "https://firebasestorage.googleapis.com/v0/b/proyectop-50f0b.appspot.com/o/busquedaPorIdJefe2.json?alt=media&token=3a996bbf-398d-4785-8bbf-9931f5eecfe2"
    let rangeStatusCode200 = 200...299
    let rangeStatusCode400 = 400...499
    let rangeStatusCode500 = 500...599
    
    func LoadTareaModal(idTarea: String, success: @escaping (_ tarea: Tareas) -> (), failure: @escaping (_ error: String) -> ()){
       
        let session = URLSession.shared
        let url = URL(string: "\(url)/obtenerTareaPorId/\(idTarea)")!
        var request = URLRequest(url: url)
        
        request.httpMethod = "GET"
        
        let task = session.dataTask(with: request) {
            (data, response, error) in
            var tarea: Tareas
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
            if let dataSuccess = data {
                do
                {
                tarea = try JSONDecoder().decode(Tareas.self, from: dataSuccess)
                success(tarea)
                print("Success")
                } catch {failure("Error JSONDecoder")}
            }
       
        }
        task.resume()
    }

    
    
    func LoadPersonasAsignadas(idLider: String, success: @escaping (_ persona: FindPersons) -> (), failure: @escaping (_ error: String) -> ()){
       
        
        let session = URLSession.shared
        let url = URL(string: url)!
        var request = URLRequest(url: url)
        
        request.httpMethod = "GET"
        
        let task = session.dataTask(with: request) {
            (data, response, error) in
            var persona:FindPersons
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
            if let dataSuccess = data {
                //print("Success")
                do
                    {
                        persona = try JSONDecoder().decode(FindPersons.self, from: dataSuccess)
                        success(persona)
                    }
                    catch {failure("Error JSONDecoder")}
            }
       
        }
        task.resume()
    }
    func UpdateEstatus(idTarea: String, estatus:String, success: @escaping (_ respuesta: String) -> (), failure: @escaping (_ error: String) -> ()){
       
        
        let session = URLSession.shared
        let url = URL(string: "\(url)/actulizarEstatus/\(idTarea)&\(estatus)")!
        var request = URLRequest(url: url)
        
        request.httpMethod = "PUT"
        
        let task = session.dataTask(with: request) {
            (data, response, error) in
            var persona:FindPersons
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
            if let dataSuccess = data,let dataString = String(data: dataSuccess, encoding: .utf8) {
                success(dataString)

                //print("Success")
            }
       
        }
        task.resume()
    }
    
}
*/
