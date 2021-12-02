//
//  Modulo_Broadcast.swift
//  AgileUs
//
//  Created by user205703 on 23/11/21.
//

import UIKit

class Modulo_Broadcast: UIViewController {

    override func viewDidLoad()
    {
        super.viewDidLoad()
    }
    
    @IBAction func Ir_a_Mensajes(_ sender: Any)
    {
        self.performSegue(withIdentifier: "buzon_a_mensajes_enviados", sender: nil)
    }
    
    @IBAction func Ir_a_Comunicados(_ sender: Any)
    {
        self.performSegue(withIdentifier: "buzon_a_comunicados", sender: nil)
    }
}
