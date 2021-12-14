//
//  ContactosScreen.swift
//  AgileUs
//Modulo creado por Carlos_Adolfo_Hernandez_Moreno(C_A_H)
//  
//

import UIKit

struct Datas_Contactos: Codable
{
    let status: String?
    let data: [Contactos]?
}

struct Contactos: Codable
{
    let id: String
    let nombre:String
    let nombreRol:String
    let idgrupo:String

}

class ContactosScreen: UIViewController,UITableViewDelegate, UITableViewDataSource {
    var contactos = [Contactos]()
    var otroscontactos = [Any]()
    @IBOutlet weak var tabla_contactos: UITableView!

    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return otroscontactos.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let indice = indexPath.row
        let celda_personalizada = tableView.dequeueReusableCell(withIdentifier:  lista_contactos.identificador, for: indexPath) as! lista_contactos
        celda_personalizada.configurar_celda(Datos: otroscontactos[indice] as! [Any])
        return celda_personalizada
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let index = indexPath.row
        
        //pasar a la pantalla de conversacion
        let vc = ChatViewController()
        var  titulo = otroscontactos[index]
        var titulo_chat = titulo as! [Any]
        vc.title = "\(titulo_chat[3])"
        vc.Datos_contacto = otroscontactos[index]
        navigationController?.pushViewController(vc, animated: true)
         
    }
    

    override func viewDidLoad() {
        super.viewDidLoad()
        tabla_contactos.delegate = self
        tabla_contactos.dataSource = self
        tabla_contactos.register(lista_contactos.nib(), forCellReuseIdentifier: lista_contactos.identificador)
        Servicio_web_contactos()
    }
    override func viewDidAppear(_ animated: Bool) {
        navigationController?.navigationBar.barTintColor = UIColor(red: 76/255, green: 217/255, blue: 100/255, alpha: 1)
        showNavBar()
    }
    
    func Servicio_web_contactos()
    {
        let servicio = server + "mensajes/listaContactos/\(userID)"
        //let servicio = "http://10.97.7.15:3040/api/mensajes/listaContactos/\(userID)"
        let url = URL(string: servicio)
        let requeste = NSMutableURLRequest(url: url! as URL)
        requeste.httpMethod = "GET";
        requeste.setValue("\(tokenAuth)", forHTTPHeaderField: "tokenAuth")

        URLSession.shared.dataTask(with: requeste as! URLRequest)
        {data,response,error in
            do
            {
                let resp =  try JSONDecoder().decode(Datas_Contactos.self, from: data!)
                
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
                
                    self.contactos = resp.data!
                    DispatchQueue.main.async
                    {
                        var cadena = String()
                        var contador = 1
                        for item in self.contactos
                        {
                            self.otroscontactos.append([contador,item.id,item.idgrupo,item.nombre,item.nombreRol])
                            contador = contador + 1
                        }
                        self.tabla_contactos.reloadData()
                    }
                }
            }
            catch{print("Error")}
        }.resume()

    }
    
    

}