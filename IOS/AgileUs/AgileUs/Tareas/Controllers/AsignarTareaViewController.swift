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
    @IBOutlet weak var addTask: UIButton!
    
    
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
        fileField.initStyle(placeholder: "Archivo Adjunto")
        descriptionText.initStyle(placeholder: "Descripcion")
        addTask.initStyle(text: "Asignar Tarea")
    }

}


