//
//  Modal_Comunicados.swift
//  modulo_broadcast
//
//  Created by user205703 on 29/11/21.
//

import UIKit

class Modal_Comunicados: UIViewController
{
    
    @IBOutlet weak var Asunto_Comunicado: UITextView!
    @IBOutlet weak var Mensaje_Comunicado: UITextView!
    
    var accion_confirmacion: ((_ datos: [Any]) -> Void)?
    var asunto_Com: String?
    var comunicado: String?
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
    }
    
    @IBAction func Cancelar_Comunicado(_ sender: Any)
    {
        dismiss(animated: true, completion: nil)
    }
    
    @IBAction func Enviar_Comunicado(_ sender: Any)
    {
        Asunto_Comunicado.text = asunto_Com!
        Mensaje_Comunicado.text = comunicado!
        var arreglo: [Any] = [asunto_Com, comunicado]
        self.accion_confirmacion? (arreglo)
    }
}
