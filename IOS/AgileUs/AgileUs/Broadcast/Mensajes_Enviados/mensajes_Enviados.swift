//
//  mensajes_Enviados.swift
//  modulo_broadcast
//
//  Created by user205703 on 23/11/21.


import UIKit

var arrListaMensajes = [Any]()

struct Mensajes: Codable
{
    let id: String?
    let asunto: String?
    let descripcion: String?
    let idEmisor: String?
    let nombreEmisor: String?
}

class mensajes_Enviados: UIViewController, UITableViewDelegate, UITableViewDataSource
{
    let controlador_modal1 = Adaptador_Modals()
    var lista_mensajes = [Mensajes]()
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
        celda.Configurar_Celda_Mensajes(Datos: lista_mensajes[Indice] as! [Any])
        print(" es el indice \(Indice)")
        print(lista_mensajes)
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
                
                return
            }

            guard let response = response as? HTTPURLResponse,
                (200...299).contains(response.statusCode) else {
                    print ("Error servidor: \(response)")
                    
                return
            }
            
            print(data)
            
            
            do
            { //error
                print("Entrando al do")
                //print(self.lista_mensajes)
                
                //Errores
                
                var variable_de_apoyo: [Mensajes]
                
                self.lista_mensajes = try
                JSONDecoder().decode([Mensajes].self, from: data!)
                // print("es el arreglo \(variable_de_apoyo)")
                
                
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
            catch{print("Error en el try \(error)")}
        }
        .resume()
    }
}
