//
//  comunicados.swift
//  modulo_broadcast
//
//  Created by user205703 on 23/11/21.
//

import UIKit

class comunicados: UIViewController, UITableViewDelegate, UITableViewDataSource
{

    @IBOutlet weak var tableView: UITableView!
    let controlador_modal2 = Adaptador_Modals()
    
    var comunicados_enviados = ["Comunicado general 12/5/2021 ", "comunicado ventas 30/10/2021", "Comunicado general 6/9/2021", "comunicado de junta 14/2/2021"]
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        tableView.delegate = self
        tableView.dataSource = self
        
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return comunicados_enviados.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let celda = tableView.dequeueReusableCell(withIdentifier: "celda_comunicados", for: indexPath)
        
        celda.textLabel?.text = comunicados_enviados[indexPath.row]
        
        return celda
    }
    
    
    @IBAction func Nuevo_Comunicado(_ sender: Any)
    {
        let Modal_Mensajes = controlador_modal2.crear_modal_mensajes_enviados(Accion_Confirmacion_Completion: {[self](Datos) -> Void in
            
            var asunto = [Datos] as! Any
            var mensaje = [Datos] as! Any
        
            self.dismiss(animated: true, completion: nil)
            
        })
        present(Modal_Mensajes, animated: true)
    }
    
}

