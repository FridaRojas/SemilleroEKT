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
        [1,"Carlos"],
        [2,"Adolfo"],
        [3,"Hernandez"],
        [4,"Moreno"],
        [5,"Sofia"],
        [6,"Pedro"],
        [7,"Alondra"],
        [8,"Poncho"],
        [9,"Juan"],
        [10,"Isacc"],
        [11,"Aranxa"],
        [12,"ASDFGHJKL"],
        [13,"Holi"],
        [14,"lkjfvkr"],
        [15,"ñññ"]
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
    override func viewDidAppear(_ animated: Bool) {
        showNavBar()
    }
    

}
