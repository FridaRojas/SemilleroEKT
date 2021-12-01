//
//  Api.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 22/11/21.
//

import Foundation

final class Api {
    
    static let shared = Api()
    
    let url = "http://10.97.3.134:2021/api/tareas"
    let rangeStatusCode200 = 200...299
    let rangeStatusCode400 = 400...499
    let rangeStatusCode500 = 500...599
    
    func createTask(task: Task, file: URL?, success: @escaping (_ task: TaskResponse) -> (), failure: @escaping (_ error: String) -> ()){
        
        var newTask: Task
        
        if file != nil {
            var filename = "Tarea_\(HelpString.randomString(length: 20))"

            newTask = Task(
                        id_grupo: "GRUPOID1",
                        id_emisor: "EMIS1",
                        nombre_emisor: "JOSE",
                        id_receptor: "ReceptorAlexis",
                        nombre_receptor: "cristian",
                        fecha_ini: task.fecha_ini!,
                        fecha_fin: task.fecha_fin!,
                        titulo: task.titulo!,
                        descripcion: task.descripcion!,
                        prioridad: task.prioridad!,
                        estatus: "pendiente",
                        archivo: filename)
            
            ProvFirebase.storageInFirebase(file: file!, filename: filename)
        
        } else {
            newTask = Task(
                        id_grupo: "GRUPOID1",
                        id_emisor: "EMIS1",
                        nombre_emisor: "JOSE",
                        id_receptor: "ReceptorAlexis",
                        nombre_receptor: "cristian",
                        fecha_ini: task.fecha_ini!,
                        fecha_fin: task.fecha_fin!,
                        titulo: task.titulo!,
                        descripcion: task.descripcion!,
                        prioridad: task.prioridad!,
                        estatus: "pendiente")
            
            
        }
        
        let session = URLSession.shared
        let url = URL(string: "\(url)/agregarTarea")!
        var request = URLRequest(url: url)
        
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let dataToJson = try! JSONEncoder().encode(newTask)
        
        let taskSession = session.uploadTask(with: request, from: dataToJson) {
            (data, response, error) in
            
            if let httpResponse = response as? HTTPURLResponse,
               self.rangeStatusCode500.contains(httpResponse.statusCode) {
                if let dataSuccess = data {
                    let dataString = String(data: dataSuccess, encoding: .utf8)
                    failure("error \(dataString!)")
                }
                return;
            }
            
            if let httpResponse = response as? HTTPURLResponse,
               self.rangeStatusCode400.contains(httpResponse.statusCode){
                if let dataSuccess = data {
                    let dataString = String(data: dataSuccess, encoding: .utf8)
                    failure("error \(dataString!)")
                }
                return;
            }
            
            if error != nil {
                failure("error app")
                return;
            }
            if let data = data {
                print(data)
                do {
                    var task: TaskResponse
                    task = try JSONDecoder().decode(TaskResponse.self, from: data)
                    success(task)
                } catch {
                    failure("Error JSONDecoder")
                }
     
            }
            
        }

        taskSession.resume()
    }
    
    func editTask(id: String, success: @escaping (_ task: Task) -> (), failure: @escaping (_ error: String) -> ()) {
        
        let session = URLSession.shared
        let url = URL(string: "\(url)/obtenerTareaPorId/\(id)")!
        
        let task = session.dataTask(with: url) {
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
            
            if let dataSuccess = data {
                
                do {
                    var task: TaskResponse

                    task = try JSONDecoder().decode(TaskResponse.self, from: dataSuccess)
                    success(task.data!)
                } catch {
                    failure("Error JSONDecoder")
                }
            }
            
        }
        
        task.resume()
        
    }
    
    func updateTask(id: String, task: Task, success: @escaping (_ task: String) -> (), failure: @escaping (_ error: String) -> ()) {
        
        let session = URLSession.shared
        let url = URL(string: "\(url)/actualizarTarea/\(id)")!
        var request = URLRequest(url: url)
        
        request.httpMethod = "PUT"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let dataToJson = try! JSONEncoder().encode(task)
        
        
        let task = session.uploadTask(with: request, from: dataToJson) {
            (data, response, error) -> Void in
            
            
            if let httpResponse = response as? HTTPURLResponse,
               self.rangeStatusCode500.contains(httpResponse.statusCode) {
                if let dataSuccess = data {
                    let dataString = String(data: dataSuccess, encoding: .utf8)
                    failure("error \(dataString!)")
                }
                return;
            }
            
            if let httpResponse = response as? HTTPURLResponse,
               self.rangeStatusCode400.contains(httpResponse.statusCode){
                
                if let dataSuccess = data {
                    let dataString = String(data: dataSuccess, encoding: .utf8)
                    failure("error \(dataString!)")
                }
                
                return;
            }
            
            if error != nil {
                failure("error: \(error!.localizedDescription)")
                return;
            }
            if let dataSuccess = data {
                //                do {
                //                    task = try JSONDecoder().decode(Task.self, from: dataSuccess)
                //                    success(task)
                //                } catch {
                //                    failure("Error JSONDecoder")
                //                }
                let dataString = String(data: dataSuccess, encoding: .utf8)
                success(dataString!)
                print("Success")
            }
            
            
        }
        task.resume()
    }
    
    func cancelTask(id: String, success: @escaping (_ message: String) -> (), failure: @escaping (_ error: String) -> ()) {
        
        let session = URLSession.shared
        let url = URL(string: "\(url)/cancelarTarea/\(id)")!
        var request = URLRequest(url: url)
        
        request.httpMethod = "DELETE"
        
        let task = session.dataTask(with: request) {
            (data, response, error) in
            
            if let httpResponse = response as? HTTPURLResponse,
               self.rangeStatusCode500.contains(httpResponse.statusCode) {
                if let dataSuccess = data {
                    let dataString = String(data: dataSuccess, encoding: .utf8)
                    failure("error \(dataString!)")
                }
                return;
            }
            
            if let httpResponse = response as? HTTPURLResponse,
               self.rangeStatusCode400.contains(httpResponse.statusCode){
                
                if let dataSuccess = data {
                    let dataString = String(data: dataSuccess, encoding: .utf8)
                    failure("error \(dataString!)")
                }
                
                return;
            }
            
            if error != nil {
                failure("error: \(error!.localizedDescription)")
                return;
            }
            if let dataSuccess = data {
                
                let dataString = String(data: dataSuccess, encoding: .utf8)
                success(dataString!)
                print("Success")
            }
            
        }
        task.resume()
    }
    
    func changeStatus(id: String, status: String, success: @escaping (_ message: String) -> (), failure: @escaping (_ error: String) -> ()) {
        
        let session = URLSession.shared
        let url = URL(string: "\(url)/actulizarEstatus/\(id)&\(status)")!
        print(url)
        var request = URLRequest(url: url)
        
        request.httpMethod = "PUT"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let task = session.dataTask(with: request) {
            (data, response, error) in
            
            if let httpResponse = response as? HTTPURLResponse, self.rangeStatusCode500.contains(httpResponse.statusCode) {
                if let data = data {
                    let dataString = String(data: data, encoding: .utf8)
                    
                    failure("error: \(dataString)")
                }
                
                return;
            }
            
            if let httpResponse = response as? HTTPURLResponse, self.rangeStatusCode400.contains(httpResponse.statusCode) {
                if let data = data {
                    let dataString = String(data: data, encoding: .utf8)
                    
                    failure("error: \(dataString)")
                }
                
                return;
            }
            
            if error != nil {
                failure("error: \(error!.localizedDescription)")
                return;
            }
            
            if let dataSuccess = data {
                
                let dataString = String(data: dataSuccess, encoding: .utf8)
                success(dataString!)
                print("Success")
            }
            
        }
        
        task.resume()
        
    }
    
}
