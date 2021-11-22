//
//  AsignarTareaViewController.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 22/11/21.
//

import UIKit

class AsignarTareaViewController: UIViewController {

    @IBOutlet weak var nameTaskField: UITextField!
    @IBOutlet weak var personSelectField: UITextField!
    @IBOutlet weak var priortyField: UITextField!
    @IBOutlet weak var dateStartField: UITextField!
    @IBOutlet weak var fileField: UITextField!
    @IBOutlet weak var descriptionText: UITextView!
    @IBOutlet weak var dateEndField: UITextField!
    @IBOutlet weak var addTaskBtn: UIButton!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        navbarConfig()
        viewStyleConfig()
        inputStyleConfig()
    }
    
    
    func navbarConfig() {
    }
    
    func viewStyleConfig() {
        self.view.backgroundColor = UIColor(red: 245/255, green: 245/255, blue: 245/255, alpha: 1)
    }
    
    func inputStyleConfig() {
        nameTaskField.initStyle(placeholder: "Nombre de la Tarea")
        personSelectField.initStyle(placeholder: "Persona Asignada")
        priortyField.initStyle(placeholder: "Prioridad")
        dateStartField.initStyle(placeholder: "Fecha Inicio")
        dateEndField.initStyle(placeholder: "Fecha Fin")
        fileField.initStyle(placeholder: "Archivo Adjunto")
        descriptionText.initStyle(placeholder: "Descripcion")
        addTaskBtn.initStyle(text: "Asignar Tarea")
    }
    
    @IBAction func addTask(_ sender: Any) {
        let data = [
            "nombre": "Nombre Prueba",
            "personaAsignada": "Persona Prueba",
            "prioridad": "Prioridad Prueba",
            "fechaInicio": "Fecha Inicio Prueba",
            "fechaFin": "Fecha Fin Prueba",
            "descripcion": "Descripcion",
            "archivo": "Archivo Prueba"
        ]
        Api.shared.createTask(data: data)
    }
    
}


