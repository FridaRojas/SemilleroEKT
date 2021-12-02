//
//  mensajes_Enviados.swift
//  modulo_broadcast
//
//  Created by user205703 on 23/11/21.
// url para la lista de usuarios para el broadcast
// https://firebasestorage.googleapis.com/v0/b/nombre-7ec89.appspot.com/o/BroadCastListaDeUsuarios.json?alt=media&token=585ce09f-5972-4661-bcfe-73063b4aafaa
//mensajes del broadcast
// https://firebasestorage.googleapis.com/v0/b/nombre-7ec89.appspot.com/o/BroadCastTodosLosMensajes.json?alt=media&token=c987c8ce-37ed-46e9-8a28-89a09093d274



import UIKit

struct mensajes_Broadcast: Codable
{
    let id: String
    let Senderid: String
    let Receiverid: String
    let Asunto: String
    let Message: String
}


class mensajes_Enviados: UIViewController, UITableViewDelegate, UITableViewDataSource
{
    let controlador_modal1 = Adaptador_Modals()
    var usuarios = ["Adonay", "Georgina", "Israel", "Jorge"]
    var mensajes_Enviados = [mensajes_Broadcast]()
    
    @IBOutlet weak var lista_mensajes_eniados: UITableView!

    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        lista_mensajes_eniados.delegate = self
        lista_mensajes_eniados.dataSource = self

    }
    
    override func viewDidAppear(_ animated: Bool)
    {
        //consumir_mensajes_enviados()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return usuarios.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let celda = tableView.dequeueReusableCell(withIdentifier: "celda_msjs_enviados", for: indexPath)
        
        celda.textLabel?.text = usuarios[indexPath.row]
        
        return celda
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
    
    /*func consumir_mensajes_enviados()
    {
        let servicio = ""
        let url = URL(string: servicio)
        URLSession.shared.dataTask(with: url!)
        {(data, response, error) in
            do
            {
                self.usuarios = try
                JSONDecoder().decode([].self, from: data!)
                DispatchQueue.main.async {
                    var cadena = String()
                    for item in self.usuarios
                    {
                        cade
                    }
                    //aquí va donde llena la lista
                }
            }catch{print("Error")}
        }.resume()
    
    }*/

}
