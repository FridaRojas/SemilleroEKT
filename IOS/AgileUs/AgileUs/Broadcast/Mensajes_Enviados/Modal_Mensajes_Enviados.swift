//
//  Modal_Mensajes_Enviados.swift
//  modulo_broadcast
//
//  Created by user205703 on 25/11/21.
//

import UIKit

class Modal_Mensajes_Enviados: UIViewController
{

    @IBOutlet weak var Asunto_Mensaje: UITextView!
    @IBOutlet weak var Mensaje_Enviado: UITextView!
    
    var accion_confirmacion: ((_ datos: [Any]) -> Void)?
    var asunto: String?
    var mensaje: String?
        
    override func viewDidLoad()
    {
        super.viewDidLoad()
        Asunto_Mensaje.text = nil
        Mensaje_Enviado.text = nil
        post_Enviar_Broadcast()
    }
    
    
    @IBAction func Cancelar_Envio(_ sender: Any)
    {
        dismiss(animated: true, completion: nil)
    }
    
    @IBAction func enviar(_ sender: Any)
    {
        Asunto_Mensaje.text = asunto
        Mensaje_Enviado.text = mensaje
        var arreglo: [Any] = [asunto, Mensaje_Enviado]
        self.accion_confirmacion? (arreglo)
        dismiss(animated: true, completion: nil)
    }
    
    func post_Enviar_Broadcast()
    {
        guard let Url_Post_a_Broadcast = URL(string: "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/broadCast/crearMensajeBroadcast/61ad370537670e5060dc060e") else
            {
            return
                
            }
        var solicitud_Enviar_Broadcast = URLRequest(url: Url_Post_a_Broadcast)
        solicitud_Enviar_Broadcast.httpMethod = "POST"
        solicitud_Enviar_Broadcast.setValue("application/json", forHTTPHeaderField: "Content-Type")
        let cuerpo_Enviar_Broadcast: (AnyHashable) = [
            "timestamp": "2021-12-06T01:40:54.321+00:00",
            "status": "400",
            "error": "Bad Request",
                "path": "/Servicios-0.0.1-SNAPSHOT/api/broadCast/crearMensajeBroadcast"
        ]
        solicitud_Enviar_Broadcast.httpBody = try? JSONSerialization.data(withJSONObject: cuerpo_Enviar_Broadcast, options: .fragmentsAllowed)
        let tarea = URLSession.shared.dataTask(with: solicitud_Enviar_Broadcast)
        {
            data, _, error in guard let data = data, error == nil else
            {
             return
            }
            do {
                let respuesta_a_broadcast = try JSONSerialization.jsonObject(with: data, options: .fragmentsAllowed)
                print("post exitoso: \(respuesta_a_broadcast)")
            }catch
            {
                print(error)
            }
        }
        tarea.resume()
    }
    
    
    func post_Enviar_Mensaje_Broadcast()
    {
        guard let Url_Post_a_Mensaje = URL(string: "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/broadCast/enviarMensaje/61ad370537670e5060dc060e") else
            {
            return
                
            }
        var solicitud_Enviar_Mensaje = URLRequest(url: Url_Post_a_Mensaje)
        solicitud_Enviar_Mensaje.httpMethod = "POST"
        solicitud_Enviar_Mensaje.setValue("application/json", forHTTPHeaderField: "Content-Type")
        let cuerpo_Enviar_Broadcast: (AnyHashable) = [
            "timestamp": "2021-12-06T01:40:54.321+00:00",
            "status": "400",
            "error": "Bad Request",
                "path": "/Servicios-0.0.1-SNAPSHOT/api/broadCast/enviarMensaje"
        ]
        solicitud_Enviar_Mensaje.httpBody = try? JSONSerialization.data(withJSONObject: cuerpo_Enviar_Broadcast, options: .fragmentsAllowed)
        let tarea = URLSession.shared.dataTask(with: solicitud_Enviar_Mensaje)
        {
            data, _, error in guard let data = data, error == nil else
            {
             return
            }
            do {
                let respuesta_a_mensaje = try JSONSerialization.jsonObject(with: data, options: .fragmentsAllowed)
                print("post exitoso: \(respuesta_a_mensaje)")
            }catch
            {
                print(error)
            }
        }
        tarea.resume()
    }

    
    
}
