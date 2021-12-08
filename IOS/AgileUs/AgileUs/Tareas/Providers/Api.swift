//
//  Api.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 22/11/21.
//

import Foundation
import Firebase
import FirebaseStorage

final class Api {
    
    static let shared = Api()
    
    let url = "http://10.97.3.24:3040/api/tareas"
    let token = "5ae7d87c088ea2187b0531d7172616f3147d7736a7a125dac893d8de2bc5068a";
//    let url_personas = "http://3.144.86.49:8080/Servicios-0.0.1-SNAPSHOT/api/user/findByBossId/618b05c12d3d1d235de0ade0"
//    let url = "http://10.97.3.24:3040/Servicios-0.0.1-SNAPSHOT/api/tareas"
//    let url_personas = "http://10.97.3.24:3040/Servicios-0.0.1-SNAPSHOT/api/user/findByBossId/618b05c12d3d1d235de0ade0"
    let url_personas = "http://10.97.3.24:3040/api/user/findByBossId/618b05c12d3d1d235de0ade0"

    let rangeStatusCode200 = 200...299
    let rangeStatusCode400 = 400...499
    let rangeStatusCode500 = 500...599
    let idUser = "61a83bbad036090b8e8db3c2"
    
    func createTask(task: Task, file: URL?, success: @escaping (_ task: TaskResponse) -> (), failure: @escaping (_ error: String) -> ()){
        
        var newTask: Task?
        
        if file != nil {
        
            var filename = "Tarea_Archivo"
            let bucketRef = Storage.storage().reference(withPath: "Tareas/\(filename)")
            var urlFile = ""

            let tarea_subir = bucketRef.putFile(from: file!, metadata: nil)
            {
                matadatos, error in
                guard let metadatos = matadatos else
                {
                    print(error?.localizedDescription)
                    return
                }
                
                bucketRef.downloadURL(completion: {
                    url, error in
                    
                    if let urlText = url?.absoluteString {
                       urlFile = "\(urlText)"
                        
                        newTask = Task(
                                    id_grupo: "GRUPOID1",
                                    id_emisor: "61a83bbad036090b8e8db3c2",
                                    nombre_emisor: "Armando Manzanero",
                                    id_receptor: task.id_receptor,
                                    nombre_receptor: task.nombre_receptor,
                                    fecha_ini: task.fecha_ini!,
                                    fecha_fin: task.fecha_fin!,
                                    titulo: task.titulo!,
                                    descripcion: task.descripcion!,
                                    prioridad: task.prioridad!,
                                    estatus: "pendiente",
                                    archivo: "\(urlText)")
                        
                        let session = URLSession.shared
                        let urlB = URL(string: "\(self.url)/agregarTarea")!
                        var request = URLRequest(url: urlB)
                        
                        
                        request.httpMethod = "POST"
                        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
                        
                        // Token Config
                        request.setValue("\(self.token)", forHTTPHeaderField: "token_sesion")
                        let dataToJson = try! JSONEncoder().encode(newTask!)
                        
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
                                failure("error \(error?.localizedDescription)")
                                return;
                            }
                            if let data = data {
                                print(data)
                                do {
                                    
                                    Api.shared.sendMessage(message: "", title: (newTask?.titulo)!, person: (newTask?.nombre_receptor)!)
                                    var task: TaskResponse
                                    
                                    task = try JSONDecoder().decode(TaskResponse.self, from: data)
                                    success(task)
                                } catch {
                                    failure("Error JSONDecoder \(error)")
                                }
                     
                            }
                            
                        }

                        taskSession.resume()
                        
                        
                    } else {
                         print("error subia: \(error) ")
                    }
                    
                    
                })
                print("Se subio archivo")
                
     
            }
            
                
 

          
        
        } else {
            newTask = Task(
                        id_grupo: "GRUPOID1",
                        id_emisor: "61a83bbad036090b8e8db3c2",
                        nombre_emisor: "Armando Manzanero",
                        id_receptor: task.id_receptor,
                        nombre_receptor: task.nombre_receptor,
                        fecha_ini: task.fecha_ini!,
                        fecha_fin: task.fecha_fin!,
                        titulo: task.titulo!,
                        descripcion: task.descripcion!,
                        prioridad: task.prioridad!,
                        estatus: "pendiente")
            
            let session = URLSession.shared
            let url = URL(string: "\(url)/agregarTarea")!
            var request = URLRequest(url: url)
            
            request.httpMethod = "POST"
            request.setValue("application/json", forHTTPHeaderField: "Content-Type")
            
            // Token Config
            request.setValue("\(self.token)", forHTTPHeaderField: "token_sesion")

            let dataToJson = try! JSONEncoder().encode(newTask!)
            
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
                        Api.shared.sendMessage(message: "", title: task.titulo!, person: task.nombre_receptor!)

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
        
        
    }
    
    func editTask(id: String, success: @escaping (_ task: Task) -> (), failure: @escaping (_ error: String) -> ()) {
        
        let session = URLSession.shared
        let url = URL(string: "\(url)/obtenerTareaPorId/\(id)/\(idUser)")!
        var request = URLRequest(url: url)
        
        // Token Config
        request.setValue("\(self.token)", forHTTPHeaderField: "token_sesion")

        let task = session.dataTask(with: request) {
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
        let url = URL(string: "\(url)/actualizarTarea/\(id)/\(idUser)")!
        var request = URLRequest(url: url)
        
        request.httpMethod = "PUT"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        // Token Config
        request.setValue("\(self.token)", forHTTPHeaderField: "token_sesion")

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
                print(dataString!)
            }
            
            
        }
        task.resume()
    }
    
    func cancelTask(id: String, success: @escaping (_ message: String) -> (), failure: @escaping (_ error: String) -> ()) {
        
        let session = URLSession.shared
        let url = URL(string: "\(url)/cancelarTarea/\(id)/\(idUser)")!
        var request = URLRequest(url: url)
        
        request.httpMethod = "DELETE"
        
        // Token Config
        request.setValue("\(self.token)", forHTTPHeaderField: "token_sesion")
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
        let url = URL(string: "\(url)/actulizarEstatus/\(id)/\(status)")!
        var request = URLRequest(url: url)
        
        // Token Config
        request.setValue("\(self.token)", forHTTPHeaderField: "token_sesion")

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
    func LoadTareaModal(idTarea: String, success: @escaping (_ tarea: Task) -> (), failure: @escaping (_ error: String) -> ()){
       
        let session = URLSession.shared
        let url = URL(string: "\(url)/obtenerTareaPorId/\(idTarea)")!
        var request = URLRequest(url: url)
     
        request.httpMethod = "GET"
        // Token Config
        request.setValue("\(self.token)", forHTTPHeaderField: "token_sesion")
        let task = session.dataTask(with: request) {
            (data, response, error) in
            var tarea: TaskResponse
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
                tarea = try JSONDecoder().decode(TaskResponse.self, from: dataSuccess)
                    success(tarea.data!)
                print("Success")
                } catch {failure("Error JSONDecoder")}
            }
       
        }
        task.resume()
    }

    
    
    func LoadPersonasAsignadas(idLider: String, success: @escaping (_ persona: FindPersons) -> (), failure: @escaping (_ error: String) -> ()){
       
        
        let session = URLSession.shared
        let url = URL(string: url_personas)!
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        // Token Config
        request.setValue("\(self.token)", forHTTPHeaderField: "token_sesion")
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
        let url = URL(string: "\(url)/actulizarEstatus/\(idTarea)/\(estatus)")!
        var request = URLRequest(url: url)
        
        request.httpMethod = "PUT"
        // Token Config
        request.setValue("\(self.token)", forHTTPHeaderField: "token_sesion")
        let task = session.dataTask(with: request) {
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
            if let dataSuccess = data,let dataString = String(data: dataSuccess, encoding: .utf8) {
                success(dataString)

                //print("Success")
            }
       
        }
        task.resume()
    }
    
    func sendMessage(message: String, title: String, person: String) {
        
        let session = URLSession.shared
        let url = URL(string: "http://3.144.86.49:8080/Servicios-0.0.1-SNAPSHOT/api/mensajes/crearMensaje")!
        var request = URLRequest(url: url)
        
        let date = Date()

        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        
        var messageS = MessageTask(idEmisor: "618e8743c613329636a769aa", idReceptor: "618e878ec613329636a769ab", texto: "Se asigno la tarea \(title) a la persona: \(person)", rutaDocumento: "", fechaCreacion: "\(dateFormatter.string(from: date))")
        
        
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        // Token Config
        request.setValue("\(self.token)", forHTTPHeaderField: "token_sesion")

        let dataToJson = try! JSONEncoder().encode(messageS)
        
        let taskSession = session.uploadTask(with: request, from: dataToJson) {
            (data, response, error) in
            
            if let httpResponse = response as? HTTPURLResponse,
               self.rangeStatusCode500.contains(httpResponse.statusCode) {
                if let dataSuccess = data {
                    let dataString = String(data: dataSuccess, encoding: .utf8)
                    print("error \(dataString!)")
                }
                return;
            }
            
            if let httpResponse = response as? HTTPURLResponse,
               self.rangeStatusCode400.contains(httpResponse.statusCode){
                if let dataSuccess = data {
                    let dataString = String(data: dataSuccess, encoding: .utf8)
                    print("error \(dataString!)")
                }
                return;
            }
            
            if error != nil {
                print("error message")
                return;
            }
            if let data = data {
                print(data)
                do {
                    var message: messageTaskResponse
                    message = try JSONDecoder().decode(messageTaskResponse.self, from: data)
                    print(message)
                } catch {
                    print("Error JSONDecoder mensaje")
                }
     
            }
            
        }

        taskSession.resume()
    }
    func UpdateFecha(idTask: String,ban:Bool, success: @escaping (_ respuesta: String) -> (), failure: @escaping (_ error: String) -> ()){
       
        let date = Date()

        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        
        let session = URLSession.shared
        let url = ban != false ? URL(string: "\(url)/actualizarTareaFechaRealIni/\(idTask)")! : URL(string: "\(url)/actualizarTareaFechaRealFin/\(idTask)")!
        var request = URLRequest(url: url)

        request.httpMethod = "PUT"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        // Token Config
        request.setValue("\(self.token)", forHTTPHeaderField: "token_sesion")
        var arreglo: Dictionary<String,String>
        if ban {
             arreglo = ["fecha_iniR":"\(dateFormatter.string(from: date))"]
        } else {
             arreglo = ["fecha_finR":"\(dateFormatter.string(from: date))"]
        }
                
        let dataToJson = try! JSONEncoder().encode(arreglo)
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
                failure("error actualizar fecha\(error?.localizedDescription)")
                return;
            }
            if let dataSuccess = data,let dataString = String(data: dataSuccess, encoding: .utf8) {
                print("Todo cool desde actualizacion fechas \(dataString)")
                success(dataString)
                
                //print("Success")
            }
       
        }
        taskSession.resume()
    }

}
