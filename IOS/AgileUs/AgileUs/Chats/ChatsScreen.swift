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
        [1,"Pancho"],
        [2,"Maria"],
        [3,"Peña Nieto"],
        [4,"Justin Bieber"],
        [5,"Maluma"],
        [6,"CR7"],
        [7,"Messi"],
        [8,"Pirlo"],
        [9,"Gera "],
        [10,"Babo"],
        [11,"Tio Salinas"],
        [12,"Coppel"],
        [13,"Elecktra"],
        [14,"Banco Azteca"],
        [15,"aaaaa"]
    ]
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return datos.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let indice = indexPath.row
        var celda_personalizada = tableView.dequeueReusableCell(withIdentifier: lista_chats.identificador, for: indexPath) as! lista_chats
        //celda_personalizada.textLabel?.text = "Aqui iran las conversaciones"
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
