//
//  comunicados.swift
//  modulo_broadcast
//
//  Created by user205703 on 23/11/21.
//

import UIKit

struct comunicados_al_Broadcast: Codable
{
    let id: String
    let asunto: String
    let descripcion: String
    let idEmisor: String
    let nombreEmisor: String
}


class comunicados: UIViewController, UITableViewDelegate, UITableViewDataSource
{

    @IBOutlet weak var tableView: UITableView!
    let controlador_modal2 = Adaptador_Modals()
    var Lista_Comunicados = [comunicados_al_Broadcast]()
    var comunicados_enviados = [Any]()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        tableView.delegate = self
        tableView.dataSource = self
        tableView.register(Celda_Comunicados.nib(), forCellReuseIdentifier: Celda_Comunicados.identificador_celda_comunicados)
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return comunicados_enviados.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        var contador = indexPath.row
        let celda = tableView.dequeueReusableCell(withIdentifier: Celda_Comunicados.identificador_celda_comunicados, for: indexPath) as! Celda_Comunicados
        
        celda.Configurar_Celda_Comunicados(Datos: comunicados_enviados[contador] as! [Any])
        return celda
    }
    
    
    @IBAction func Nuevo_Comunicado(_ sender: Any)
    {
        /*let Modal_Mensajes = controlador_modal2.crear_modal_mensajes_enviados(Accion_Confirmacion_Completion: {[self](Datos) -> Void in
            
            var asunto = [Datos] as! Any
            var mensaje = [Datos] as! Any
        
            self.dismiss(animated: true, completion: nil)
            
        })
        present(Modal_Mensajes, animated: true)*/
    }
    
    func Consumir_Comunicados_Enviados()
    {
        let servicio =  "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/broadCast/mostarMensajesdelBroadcast/61ad370537670e5060dc060e"
        let url = URL(string: servicio)
        URLSession.shared.dataTask(with: url!)
        {(data, response, error) in
            if let error = error {
                print ("error: \(error)")
                self.simpleAlertMessage(title: "Error!", message: "Error al conectar con el servidor")
                return
            }

            guard let response = response as? HTTPURLResponse,
                (200...299).contains(response.statusCode) else {
                    print ("Error servidor: \(response)")
                    self.simpleAlertMessage(title: "Error!", message: "Error de respuesta del servidor")
                return
            }

            do
            {
                print(data)
                print(error)
                self.Lista_Comunicados = try
                JSONDecoder().decode([comunicados_al_Broadcast].self, from: data!)
                DispatchQueue.main.async {
                    var indice = 1
                    for item in self.Lista_Comunicados
                    {
                        print(item.nombreEmisor, item.asunto)
                        indice = indice + 1
                    }
                    self.tableView.reloadData()
                }
            }
            catch{print("Error")}
        }
        .resume()
    }
}
    

