//
//  mensajes_Enviados.swift
//  modulo_broadcast
//
//  Created by user205703 on 23/11/21.


import UIKit

struct lista_de_mensajes: Codable
{
    let id: String
    let asunto: String
    let descripcion: String
    let idEmisor: String
    let nombreEmisor: String
}

class mensajes_Enviados: UIViewController, UITableViewDelegate, UITableViewDataSource
{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return lista_mensajes.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        var Indice = indexPath.row
        let celda = tableView.dequeueReusableCell(withIdentifier: celda_msjs_enviados.identificador, for: indexPath) as! celda_msjs_enviados
        celda.Configurar_Celda_Mensajes(Datos: usuarios[Indice] as! [Any])
        return celda
    }
    
    /*func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath)
    {
        let index = indexPath.row
        let interfaz = mensajes_Enviados()
        
        var mensaje = usuarios[index]
        var Mensaje_Broadcast = mensaje as! [Any]
    }*/
    
    let controlador_modal1 = Adaptador_Modals()
    var lista_mensajes = [lista_de_mensajes]()
    var usuarios = [Any]()

    
    @IBOutlet weak var lista_mensajes_eniados: UITableView!

    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        lista_mensajes_eniados.delegate = self
        lista_mensajes_eniados.dataSource = self
        lista_mensajes_eniados.register(celda_msjs_enviados.nib(), forCellReuseIdentifier: celda_msjs_enviados.identificador)

    }
    
    override func viewDidAppear(_ animated: Bool)
    {
        consumir_mensajes_enviados()
    }
    
    
    @IBAction func Enviar_Mensaje(_ sender: Any)
    {
        let Modal_Mensajes = controlador_modal1.crear_modal_mensajes_enviados(Accion_Confirmacion_Completion: {[self](Datos) -> Void in
            
            var asunto = [Datos] as! Any
            var mensaje = [Datos] as! Any
        
            self.dismiss(animated: true, completion: nil)
            
        })
        present(Modal_Mensajes, animated: true)
    }
    
    func consumir_mensajes_enviados()
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
                self.lista_mensajes = try
                JSONDecoder().decode([lista_de_mensajes].self, from: data!)
                DispatchQueue.main.async {
                    var indice = 1
                    for item in self.lista_mensajes
                    {
                        print(item.nombreEmisor, item.asunto)
                        indice = indice + 1
                    }
                    self.lista_mensajes_eniados.reloadData()
                }
            }
            catch{print("Error")}
        }
        .resume()
    }
}
