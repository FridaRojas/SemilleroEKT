//
//  ContactosScreen.swift
//  AgileUs
//Modulo creado por Carlos_Adolfo_Hernandez_Moreno(C_A_H)
//  
//

import UIKit

class ContactosScreen: UIViewController,UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var tabla_contactos: UITableView!
    
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
        let celda_personalizada = tableView.dequeueReusableCell(withIdentifier:  lista_contactos.identificador, for: indexPath) as! lista_contactos
       
        //celda_personalizada.lbl_nombre_contacto.text = "\(datos[cont])"
        
        celda_personalizada.configurar_celda(Datos: datos[indice] as! [Any])
        
        return celda_personalizada
    }
    

    override func viewDidLoad() {
        super.viewDidLoad()
        tabla_contactos.delegate = self
        tabla_contactos.dataSource = self
        tabla_contactos.register(lista_contactos.nib(), forCellReuseIdentifier: lista_contactos.identificador)
    }
    

}
