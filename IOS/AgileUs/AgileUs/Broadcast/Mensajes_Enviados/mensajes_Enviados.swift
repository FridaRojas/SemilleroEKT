//
//  mensajes_Enviados.swift
//  modulo_broadcast
//
//  Created by user205703 on 23/11/21.


import UIKit



class mensajes_Enviados: UIViewController, UITableViewDelegate, UITableViewDataSource
{
    let controlador_modal1 = Adaptador_Modal()
    var lista_mensajes = [Mensajes_Broacast]()
    //var usuarios = [Any]()
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return lista_mensajes.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        var Indice = indexPath.row
        let celda = tableView.dequeueReusableCell(withIdentifier: celda_msjs_enviados.identificador, for: indexPath) as! celda_msjs_enviados
        print(Indice)
        celda.Configurar_Celda_Mensajes(Mensaje_recibido: lista_mensajes[Indice] as! Mensajes_Broacast)
        
        return celda
    }
    
    @IBOutlet weak var lista_mensajes_eniados: UITableView!

    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        lista_mensajes_eniados.delegate = self
        lista_mensajes_eniados.dataSource = self
        lista_mensajes_eniados.register(celda_msjs_enviados.nib(), forCellReuseIdentifier: celda_msjs_enviados.identificador)
        consumir_mensajes_enviados()

    }
    
    
    @IBAction func Enviar_Mensaje(_ sender: Any)
    {
        let Modal_Mensajes = controlador_modal1.crear_modal_mensajes_enviados(Accion_Confirmacion_Completion: {[self](DatosModal) -> Void in
            
            var asunto = [DatosModal] as! Any
            var mensaje = [DatosModal] as! Any
        
            self.dismiss(animated: true, completion: nil)
            
        })
        present(Modal_Mensajes, animated: true)
    }
    
    
    @IBAction func Cerrar_Mensajes_Enviados(_ sender: Any)
    {
        navigationController?.popViewController(animated: true)
    }
    
    func consumir_mensajes_enviados()
    {
        var adaptador_SW =  Adaptadores_Web()
        
        adaptador_SW.Servicio_Lista_Mensajes_broadcast
        {
            Datos in
            
            self.lista_mensajes = Datos as! [Mensajes_Broacast]
            self.lista_mensajes_eniados.reloadData()
        }
        
    }
}
