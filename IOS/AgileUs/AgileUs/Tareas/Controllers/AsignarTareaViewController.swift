//
//  AsignarTareaViewController.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 22/11/21.
//

import UIKit

class AsignarTareaViewController: UIViewController, UITextViewDelegate {

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
        descriptionText.delegate = self
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
        personSelectField.initStyle(placeholder: "Persona Asignada", imageName: "arrowIcon")
        priortyField.initStyle(placeholder: "Prioridad", imageName: "arrowIcon")
        dateStartField.initStyle(placeholder: "Fecha Inicio", imageName: "calendarIcon")
        dateEndField.initStyle(placeholder: "Fecha Fin", imageName: "calendarIcon")
        fileField.initStyle(placeholder: "Archivo Adjunto", imageName: "fileIcon")
        descriptionText.initStyle(placeholder: "Descripcion")
        addTaskBtn.initStyle(text: "Asignar Tarea")
    }
    
    @IBAction func addTask(_ sender: Any) {
        let data = [
            "id_grupo": "GRUPOID1",
            "id_emisor": "EMIS1",
            "nombre_emisor": "JOSE",
            "id_receptor": "RECEPT1",
            "nombre_receptor": "FERNANDO",
            "fecha_ini": dateEndField.text!,
            "fecha_fin": dateEndField.text!,
            "titulo": nameTaskField.text!,
            "descripcion": descriptionText.text!,
            "prioridad": priortyField.text!,
            "createdDate": "2014-01-01T23:28:56.782Z",
            "estatus": "pendiente"
        ]
        
        Api.shared.createTask(data: data) {
            (task) in
            print(task)
            DispatchQueue.main.async {
                self.altertaMensaje(title: "Exito", message: "Se asigno la tarea correctamente", confirmationMessage: "Ok", popView: true)
            }
        } failure: { error in
            print(error)
            DispatchQueue.main.async {
                self.altertaMensaje(title: "Error", message: "Hubo un problema, intentelo mas tarde", confirmationMessage: "Ok")
            }
        }
        
        
    }
    
    public func textViewDidBeginEditing(_ textView: UITextView) {
        if textView.textColor == UIColor(red: 137/255, green: 139/255, blue: 140/255, alpha: 1) {
             textView.text = nil
             textView.textColor = UIColor.black
         }
    }
    
}


