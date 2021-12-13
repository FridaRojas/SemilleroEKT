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
    let controlador_modal2 = Adaptador_Modal()
    var comunicados_enviados = 0
    var arrComunicados = ["Comunicado general 12/12/1912", "Corte al suministro eléctrico", "Reparación de la red", "Mantenimiento de los baños del piso 14"]
    
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        tableView.delegate = self
        tableView.dataSource = self
        tableView.register(Celda_Comunicados.nib(), forCellReuseIdentifier: Celda_Comunicados.identificador_celda_comunicados)
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return comunicados_enviados
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        var contador = indexPath.row
        let celda = tableView.dequeueReusableCell(withIdentifier: Celda_Comunicados.identificador_celda_comunicados, for: indexPath) as! Celda_Comunicados
        
        //celda.Configurar_Celda_Comunicados(Datos: comunicados_enviados[contador] as! [Any])
        celda.Configurar_Celda_Comunicados(Datos: arrComunicados)
        
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
    
    @IBAction func Cerrar_Mensajes_Enviados(_ sender: Any)
    {
        navigationController?.popViewController(animated: true)
    }
    
    
        
}
