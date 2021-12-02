//
//  ChatsScreen.swift
//  AgileUs
// Modulo creado por Carlos_Adolfo_Hernandez_Moreno(C_A_H)
//  Created by user204412 on 11/18/21.
//

import UIKit

struct Conversaciones: Codable
{
    let idConversacion: String
    let idReceptor:String
    let nombreConversacionRecepto:String
    let nombreRol:String

}

class ChatsScreen: UIViewController,UITableViewDelegate, UITableViewDataSource { //se importan las clases abstraptas

    @IBOutlet weak var tabla_chats: UITableView!
    var conversaciones = [Conversaciones]()
    var otrodatos = [Any]()
    //se crea un arreglo para poder simular los datos que nos proporcionara el web service

    override func viewDidLoad()
    {
        super.viewDidLoad()
        addLogoutButton()

        tabla_chats.delegate = self
        tabla_chats.dataSource = self
        tabla_chats.register(lista_chats.nib(), forCellReuseIdentifier: lista_chats.identificador)
        Servicio_web_conversaciones()
    }
    override func viewDidAppear(_ animated: Bool) {
        showNavBar()
       
    }


    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return otrodatos.count
    }


    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let indice = indexPath.row
        var celda_personalizada = tableView.dequeueReusableCell(withIdentifier: lista_chats.identificador, for: indexPath) as! lista_chats
        celda_personalizada.configurar_celda(Datos: otrodatos[indice] as! [Any])
        return celda_personalizada
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let index = indexPath.row
        //pasar a la pantalla de conversacion
        let vc = ChatViewController()
        
        vc.title = "\(otrodatos[index])"
        navigationController?.pushViewController(vc, animated: true)
    }
    
    
    func Servicio_web_conversaciones()
    {
        let servicio = "http://10.97.6.83:3040/api/mensajes/listarConversaciones/618e878ec613329636a769ab"
        let url = URL(string: servicio)

        URLSession.shared.dataTask(with: url!)
        {data,response,error in
            do
            {
                self.conversaciones = try JSONDecoder().decode([Conversaciones].self, from: data!)
                DispatchQueue.main.async
                {
                    var cadena = String()
                    var contador = 1
                    for item in self.conversaciones
                    {
                        self.otrodatos.append([contador,item.idConversacion,item.idReceptor,item.nombreConversacionRecepto,item.nombreRol])
                        contador = contador + 1
                    }
                    self.tabla_chats.reloadData()
                }

            }
            catch{print("Error")}
        }.resume()

    }






}
