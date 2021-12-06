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

struct lista_de_usuarios: Codable
{
    let nombre: String
    let token: String
}


class mensajes_Enviados: UIViewController, UITableViewDelegate, UITableViewDataSource
{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return lista_usuarios.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let indice = indexPath.row
        let celda = tableView.dequeueReusableCell(withIdentifier: celda_msjs_enviados.identificador, for: indexPath) as! celda_msjs_enviados
        celda.Configurar_Celda_Mensajes(Datos: usuarios [indice] as! [Any])
        
        return celda
    }
    
    let controlador_modal1 = Adaptador_Modals()
    var lista_usuarios = [lista_de_usuarios]()
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
        //consumir_mensajes_enviados()
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
        let servicio = "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/broadCast/listaUsuarios/61a101db174bcf469164d2fd"
        let url = URL(string: servicio)
        URLSession.shared.dataTask(with: url!)
        {(data, response, error) in
            do
            {
                self.lista_usuarios = try
                JSONDecoder().decode([lista_de_usuarios].self, from: data!)
                DispatchQueue.main.async {
                    var indice = 1
                    for item in self.lista_usuarios
                    {
                        print(item.nombre)
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
