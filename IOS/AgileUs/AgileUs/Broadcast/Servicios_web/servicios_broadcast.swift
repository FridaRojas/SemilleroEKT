//
//  Mensajes_Broadcast.swift
//  AgileUs
//
//  Created by user205703 on 10/12/21.
//

import Foundation
import UIKit

class servicios_broadcast
{
    //variables que guardan las funciones lambda

    var sw_lista_de_mensajes: ((_ Datos_Respuesta: Any) -> Void)?
    
    var sw_enviar_mensaje: ((_ Enviar_Datos: Any) -> Void)?
    
    
    //url servicios web
    
    var url_lista_mensajes = "http://3.144.86.49:8080/Servicios-0.0.1-SNAPSHOT/api/broadCast/mostarMensajesdelBroadcast/api/broadCast/mostarMensajesdelBroadcast/"
    
    var url_enviar_mensaje_broadcast = "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/broadCast/crearMensajeBroadcast/"
    
    
    //Servicios web
    
    func servicio_listar_mensajes()
    {
        
        url_lista_mensajes = "\(url_lista_mensajes)\(userID)/"
        let url = URL(string: url_lista_mensajes)
        var request = URLRequest(url: url!)
        request.setValue(tokenAuth, forHTTPHeaderField: "tokenAuth")
        
        URLSession.shared.dataTask(with: request)
        {
            (datos_respuesta, respuesta, error) in
            
            if datos_respuesta == nil
            {
                print("No hay datos de respuesta")
            }
            else
            {
                if let error = error
                {
                    print ("error: \(error)")
                    return
                }

                guard let response = respuesta as? HTTPURLResponse,
                    (200...299).contains(response.statusCode) else
                {
                    print ("Error servidor: \(respuesta)")
                    return
                }
                
                DispatchQueue.main.async
                {
                  do
                  {
                      var info_SW = try JSONDecoder().decode(Respuesta_Lista_Mensajes.self, from: datos_respuesta!)
                      
                      var arreglo_de_Datos =  info_SW.data
                      
                      self.sw_lista_de_mensajes?(arreglo_de_Datos)
                  }
                  catch let error_casteo_sw
                  {
                      print("Error en el try \(error_casteo_sw)")
                      
                  }

                }
            }
            
        }.resume()
    }
    
    func servicio_mensaje_a_broadcast()
    {
        url_enviar_mensaje_broadcast = "\(url_enviar_mensaje_broadcast)\(userID)/"
        let url_mensajes_usuarios = URL(string: url_enviar_mensaje_broadcast)
        var solicitud_mensaje = URLRequest(url: url_mensajes_usuarios!)
        solicitud_mensaje.setValue(tokenAuth, forHTTPHeaderField: "tokenAuth")
        
        URLSession.shared.dataTask(with: solicitud_mensaje)
        {
            (datos_mensaje, respuesta, error_mensaje) in
            
            if datos_mensaje == nil
            {
                print("No hay datos de respuesta")
            }
            else
            {
                if let error = error_mensaje
                {
                    print ("error: \(error)")
                    return
                }
                
                guard let response = respuesta as? HTTPURLResponse,
                    (200...299).contains(response.statusCode) else
                {
                    print ("Error servidor: \(respuesta)")
                    return
                }
                /*
                DispatchQueue.main.async
                
                {
                  do
                  {
                      //var info_SW = try JSONDecoder().decode(.self, from: datos_respuesta!)
                      
                      //var arreglo_de_Datos =  info_SW.data
                      
                      self.sw_lista_de_mensajes?(arreglo_de_Datos)
                  }
                  catch let error_casteo_sw
                  {
                      print("Error en el try \(error_casteo_sw)")
                      
                  }
                */
        
                }
            }
        }
}
