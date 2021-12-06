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
    guard let Url_Post_Comunicado = URL(string: "")
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        Asunto_Mensaje.text = nil
        Mensaje_Enviado.text = nil
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
    
    
    
}
