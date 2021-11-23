//
//  ChatsScreen.swift
//  AgileUs
// Modulo creado por Carlos_Adolfo_Hernandez_Moreno(C_A_H)
//  Created by user204412 on 11/18/21.
//

import UIKit

class ChatsScreen: UIViewController,UITableViewDelegate, UITableViewDataSource { //se importan las clases abstraptas

    @IBOutlet weak var tabla_chats: UITableView!
    
    //se crea un arreglo para poder simular los datos que nos proporcionara el web service
    var datos = [
        [1,"27/09/2021","aqui en mi casa"],
        [2,"27/09/2021","aqui en mi otra casa"],
        [3,"27/09/2021","aqui en mis casas"],
        [4,"27/09/2021","aqui en mis casas"],
        [5,"27/09/2021","aqui en mis casas"],
        [6,"27/09/2021","aqui en mi casa"],
        [7,"27/09/2021","aqui en mi otra casa"],
        [8,"27/09/2021","aqui en mis casas"],
        [9,"27/09/2021","aqui en mis casas"],
        [10,"27/09/2021","aqui en mis casas"],
        [11,"27/09/2021","aqui en mi casa"],
        [12,"27/09/2021","aqui en mi otra casa"],
        [13,"27/09/2021","aqui en mis casas"],
        [14,"27/09/2021","aqui en mis casas"],
        [15,"27/09/2021","aqui en mis casas"]
    ]
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return datos.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let indice = indexPath.row
        var celda_personalizada = tableView.dequeueReusableCell(withIdentifier: lista_chats.identificador, for: indexPath) as! lista_chats
        celda_personalizada.textLabel?.text = "Aqui iran las conversaciones"
        celda_personalizada.configurar_celda(Datos: datos[indice] as! [Any])
        return celda_personalizada
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        //pasar a la pantalla de conversacion
        let vc = ChatViewController()
        vc.title = "Nombre del contacto"
        navigationController?.pushViewController(vc, animated: true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tabla_chats.delegate = self
        tabla_chats.dataSource = self
        tabla_chats.register(lista_chats.nib(), forCellReuseIdentifier: lista_chats.identificador)
    }
    
}
