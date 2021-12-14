//
//  ChatsScreen.swift
//  AgileUs
// Modulo creado por Carlos_Adolfo_Hernandez_Moreno(C_A_H)
//  Created by user204412 on 11/18/21.
//

import UIKit

struct Datas_Conversaciones: Codable
{
    let status: String?
    let data: [Conversaciones]?
}

struct Conversaciones: Codable
{
    let idConversacion: String
    let idReceptor:String
    let nombreConversacionRecepto:String
    let nombreRol:String

}
struct Datas_Grupos:Codable
{
    let status:String?
    let data: [Grupos]?
}
struct Grupos:Codable
{
    let idConversacion:String
    let idReceptor:String
    let nombreConversacionRecepto:String

}


class ChatsScreen: UIViewController,UITableViewDelegate, UITableViewDataSource { //se importan las clases abstraptas

    @IBOutlet weak var tabla_chats: UITableView!
    var conversaciones = [Conversaciones]()
    var grupos = [Grupos]()
    var otrodatos = [Any]()
    //se crea un arreglo para poder simular los datos que nos proporcionara el web service

    //let controlador_modal2 = Adaptador_Modals()
    //constante de interfaz


    override func viewDidLoad()
    {
        super.viewDidLoad()
        //addLogoutButton()
        tabla_chats.delegate = self
        tabla_chats.dataSource = self
        tabla_chats.register(lista_chats.nib(), forCellReuseIdentifier: lista_chats.identificador)
        Servicio_web_grupos()
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2)
        {
            self.Servicio_web_conversaciones()
        }
        print("Este es un toke: \(tokenAuth) ")
    }
    
    override func viewWillAppear(_ animated: Bool) {
        navigationController?.navigationBar.barTintColor = UIColor(red: 76/255, green: 217/255, blue: 100/255, alpha: 1)
        hideNavBar()
        addLogoutButton()
    }

    @IBAction func cerrarSesion(_ sender: UIButton) {
        print("CERRAR SESION")
        UserDefaults.standard.setValue(String(), forKey: "userID")
        UserDefaults.standard.setValue(String(), forKey: "userName")
        UserDefaults.standard.setValue(String(), forKey: "email")
        UserDefaults.standard.setValue(String(), forKey: "employeeNumber")
        UserDefaults.standard.setValue(false, forKey: "isLogged")
        navigationController?.popViewController(animated: true)
        UserDefaults.standard.setValue(String(), forKey: "tokenAuth")
        UserDefaults.standard.setValue(String(), forKey: "idGrupo")
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

        var  titulo = otrodatos[index]
        var titulo_chat = titulo as! [Any]
        vc.title = "\(titulo_chat[3])"
        vc.Datos_chats = otrodatos[index]
        navigationController?.pushViewController(vc, animated: true)
    }

    func Servicio_web_grupos()
    {
        let servicio_grupos = server + "mensajes/listaGrupos/\(userID)"
        let url = URL(string: servicio_grupos)
        let requeste = NSMutableURLRequest(url: url! as URL)
        requeste.httpMethod = "GET";
        requeste.setValue("\(tokenAuth)", forHTTPHeaderField: "tokenAuth")
        URLSession.shared.dataTask(with: requeste as! URLRequest)
        {data,response,error in
            
            do
            {
                
                let resp = try JSONDecoder().decode(Datas_Grupos.self, from: data!)
               // print(String(data: dataSuccess, encoding: .utf8))
                if resp.data == nil
                                {
                    UserDefaults.standard.setValue(String(), forKey: "userID")
                    UserDefaults.standard.setValue(String(), forKey: "userName")
                    UserDefaults.standard.setValue(String(), forKey: "email")
                    UserDefaults.standard.setValue(String(), forKey: "employeeNumber")
                    UserDefaults.standard.setValue(false, forKey: "isLogged")
                    UserDefaults.standard.setValue(String(), forKey: "tokenAuth")
                    UserDefaults.standard.setValue(String(), forKey: "idGrupo")
                    
                                    DispatchQueue.main.async {
                                        let vt = LoginScreen()
                                        self.navigationController?.popViewController(animated: true)
                                    }
                                     
                }else
                {
                    self.grupos = resp.data!
                    DispatchQueue.main.async
                    {
                        var cadena = String()
                        var contador = 1
                        for item in self.grupos
                        {
                            self.otrodatos.append([contador,item.idConversacion,item.idReceptor,item.nombreConversacionRecepto,"grupo"])
                            contador = contador + 1
                        }
                    self.tabla_chats.reloadData()

                    }
                }
            }
            catch{print("Errorrrrrr\(error)")}
        }.resume()

    }



    func Servicio_web_conversaciones()
    {
        let servicio = server + "mensajes/listarConversaciones/\(userID)"
        //let servicio = "http://10.97.7.15:3040/api/mensajes/listarConversaciones/\(userID)"
        print("Este es un toke \(tokenAuth)")
        let url = URL(string: servicio)
        let requeste = NSMutableURLRequest(url: url! as URL)
        requeste.httpMethod = "GET";
        requeste.setValue("application/json", forHTTPHeaderField: "Content-Type")
        requeste.setValue("\(tokenAuth)", forHTTPHeaderField: "tokenAuth")
        URLSession.shared.dataTask(with: requeste as URLRequest)
        {data,response,error in
           
            do
            {
                let resp = try JSONDecoder().decode(Datas_Conversaciones.self, from: data!)
                
                if resp.data == nil
                                {
                    UserDefaults.standard.setValue(String(), forKey: "userID")
                    UserDefaults.standard.setValue(String(), forKey: "userName")
                    UserDefaults.standard.setValue(String(), forKey: "email")
                    UserDefaults.standard.setValue(String(), forKey: "employeeNumber")
                    UserDefaults.standard.setValue(false, forKey: "isLogged")
                    UserDefaults.standard.setValue(String(), forKey: "tokenAuth")
                    UserDefaults.standard.setValue(String(), forKey: "idGrupo")
                    
                                    DispatchQueue.main.async {
                                        let vt = LoginScreen()
                                        self.simpleAlertMessage(title: "AgileUs", message: "Tu sesión ha expirado")
                                        self.navigationController?.popViewController(animated: true)
                                    }
                                     
                }else
                {
                
                    self.conversaciones = resp.data!
                    DispatchQueue.main.async
                    {
                        var cadena = String()
                        var contador = -1
                        for item in self.conversaciones
                        {
                            self.otrodatos.append([contador,item.idConversacion,item.idReceptor,item.nombreConversacionRecepto,item.nombreRol])
                            contador = contador + 1
                        }
                    self.tabla_chats.reloadData()
                    }
                }
            }
            catch{print("Error\(error)")}
        }.resume()

    }


    @IBAction func Usuario_Envia_Broadcast(_ sender: Any)
    {
        /*
        let Modal_Broadcast_Usuario = controlador_modal2.crear_modal_mensajes_enviados(Accion_Confirmacion_Completion: {[self](Datos) -> Void in

            var asunto = [Datos] as! Any
            var mensaje = [Datos] as! Any

            self.dismiss(animated: true, completion: nil)

        })
        present(Modal_Broadcast_Usuario, animated: true)
         */
    }


    func cerrar_cesion()
    {
        UserDefaults.standard.setValue(String(), forKey: "userID")
        UserDefaults.standard.setValue(String(), forKey: "userName")
        UserDefaults.standard.setValue(String(), forKey: "email")
        UserDefaults.standard.setValue(String(), forKey: "employeeNumber")
        UserDefaults.standard.setValue(false, forKey: "isLogged")
        navigationController?.popViewController(animated: true)
        UserDefaults.standard.setValue(String(), forKey: "tokenAuth")
        UserDefaults.standard.setValue(String(), forKey: "idGrupo")
    }



}
