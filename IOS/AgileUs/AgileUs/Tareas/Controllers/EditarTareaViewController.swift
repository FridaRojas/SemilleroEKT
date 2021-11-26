//
//  EditarTareaViewController.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 24/11/21.
//

import UIKit

class EditarTareaViewController: UIViewController {
    
    var idTask: String?
    var isEdit: Bool = false
    var isAddObservation: Bool = false
    var task: Task?
    
    @IBOutlet weak var nameTaskField: UITextField!
    @IBOutlet weak var personSelectField: UITextField!
    @IBOutlet weak var priortyField: UITextField!
    @IBOutlet weak var dateStartField: UITextField!
    @IBOutlet weak var fileField: UITextField!
    @IBOutlet weak var descriptionText: UITextView!
    @IBOutlet weak var dateEndField: UITextField!
    @IBOutlet weak var updateTaskBtn: UIButton!
    @IBOutlet weak var observationField: UITextView!
    @IBOutlet weak var cancelTaskBtn: UIButton!
    @IBOutlet weak var addObservationsBtn: UIButton!
    @IBOutlet weak var statusField: UITextField!
    @IBOutlet weak var loader: UIActivityIndicatorView!
    @IBOutlet weak var viewBack: UIView!
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        getTask()
        inputStyleConfig(isEdit: isEdit)
        viewStyleConfig()
        
        inputsEdit(isEdit: isEdit)
        
        observationField.initStyleEdit(fontSize: 15, fontWeight: .light, colorText: .black, selected: isAddObservation)
        observationField.isEditable = isAddObservation
        
        loader.hidesWhenStopped = true
        viewBack.backgroundColor = UIColor(red: 245/255, green: 245/255, blue: 245/255, alpha: 1)
        loader.startAnimating()
        
        
        
    }
    
    
    func getTask() {
        Api.shared.editTask(id: idTask!) {
            (task) in
            
            DispatchQueue.main.async {
                
                
                print("Entro a task \(task)")
                
                
                self.task = task
                self.nameTaskField.text = task.titulo!
                self.personSelectField.text = task.nombre_receptor!
                self.priortyField.text = "Prioridad: \(task.prioridad!)"
                self.descriptionText.text =  "Descripción: \(task.descripcion!)"
                self.dateStartField.text = "Inicio: \(task.fecha_ini!)"
                self.dateEndField.text = "Fin: \(task.fecha_fin!)"
                self.statusField.text = "Estatus: \(task.estatus!)"

          
                if task.observaciones == nil {
                    print("Holi")
                }
                
        
                
                if task.estatus != "Revision" {
                    self.addObservationsBtn.isHidden = true
                }

                self.loader.stopAnimating()
                self.viewBack.isHidden = true
            }
        } failure: {
            (error) in
            self.loader.stopAnimating()
            self.viewBack.isHidden = true
            
            self.altertaMensaje(title: "Error", message: "Hubo un problema, intentelo mas tarde", confirmationMessage: "Ok", popView: true)
        }
    }
    func inputStyleConfig(isEdit: Bool) {
        
        nameTaskField.initStyleEdit(fontSize: 20, fontWeight: .bold, selected: isEdit)
        personSelectField.initStyleEdit(fontWeight: .bold, colorText: .darkGray, selected: isEdit)
        priortyField.initStyleEdit(fontWeight: .light, colorText: .darkGray, selected: isEdit)
        statusField.initStyleEdit(fontWeight: .light, colorText: .darkGray, selected: isEdit)
        descriptionText.initStyleEdit(fontSize: 15, fontWeight: .light, colorText: .black, selected: isEdit)
        dateStartField.initStyleEdit(fontSize: 12, fontWeight: .light, colorText: .darkGray, imageName: "calendarIcon", selected: isEdit)
        dateEndField.initStyleEdit(fontSize: 12, fontWeight: .light, colorText: .darkGray, imageName: "calendarIcon", selected: isEdit)
 
        
        fileField.initStyle(placeholder: "Archivo Adjunto", imageName: "fileIcon")
        updateTaskBtn.initStyle(text: "Editar")
        cancelTaskBtn.initStyle(text: "Cancelar")
        
        addObservationsBtn.initStyle(text: "Observaciones")
        addObservationsBtn.titleLabel?.numberOfLines = 2; // Dynamic number of lines
        addObservationsBtn.titleLabel?.lineBreakMode = NSLineBreakMode.byWordWrapping;
    }
    
    func viewStyleConfig() {
        self.view.backgroundColor = UIColor(red: 245/255, green: 245/255, blue: 245/255, alpha: 1)
    }
    
    func inputsEdit(isEdit: Bool) {
        
        nameTaskField.isEnabled = isEdit
        personSelectField.isEnabled = isEdit
        priortyField.isEnabled = isEdit
        statusField.isEnabled = isEdit
        descriptionText.isEditable = isEdit
        dateStartField.isEnabled = isEdit
        dateEndField.isEnabled = isEdit
        fileField.isEnabled = isEdit
        
    }
    @IBAction func editBtn(_ sender: Any) {
        
        isEdit = !isEdit
        self.inputsEdit(isEdit: isEdit)
        self.inputStyleConfig(isEdit: isEdit)
        
        if task!.estatus != "Revision" {
            statusField.isEnabled = false
            statusField.initStyleEdit(fontWeight: .light, colorText: .darkGray, selected: false)
        }
        if !isEdit {
            
            addObservationsBtn.isEnabled = true

            
            alertaConfirmacion(title: "Actualización", message: "Desea continuar ?", confirmationMessage: "Ok") {
              
                self.isEdit = false
                
                self.updateTask(idTask: self.idTask!)
                self.updateTaskBtn.initStyle(text: "Editar")
                self.inputsEdit(isEdit: false)
                self.inputStyleConfig(isEdit: false)
                
                
                
            } cancel: {
                self.isEdit = true
                
                self.inputsEdit(isEdit: true)
                self.inputStyleConfig(isEdit: true)
                self.updateTaskBtn.initStyle(text: "Guardar")
                
                if self.task!.estatus != "Revision"  {
                    self.statusField.isEnabled = false
                    self.statusField.initStyleEdit(fontWeight: .light, colorText: .darkGray, selected: false)
                }
                print("No se actualizo")
                
            }
            
        } else {
            self.isEdit = true
            addObservationsBtn.isEnabled = false
            updateTaskBtn.initStyle(text: "Guardar")
        }
        
        
        
    }
    
    func updateTask(idTask: String) {
        
        self.loader.startAnimating()
        self.viewBack.isHidden = false
        
        let dateStart = HelpString.removeWord(phrase: dateStartField.text!, word: "Inicio: ")
        let dateEnd = HelpString.removeWord(phrase: dateEndField.text!, word: "Fin: ")
        let priority = HelpString.removeWord(phrase: priortyField.text!, word: "Prioridad: ")
        let status = HelpString.removeWord(phrase: statusField.text!, word: "Estatus: ")
        let description = HelpString.removeWord(phrase: descriptionText.text!, word: "Descripción: ")
        let observation = HelpString.removeWord(phrase: observationField.text!, word: "Observaciones: ")
        
        let task = Task(id_grupo: "GRUPOID1", id_emisor: "EMIS1", nombre_emisor: "JOSE", id_receptor: "RECEPT1", nombre_receptor: personSelectField.text!, fecha_ini: dateStart, fecha_fin: dateEnd, titulo: nameTaskField.text!, descripcion: description, prioridad: priority, estatus: status, observaciones: observation)
        
        Api.shared.updateTask(id: idTask, task: task) {
            (task) in
            DispatchQueue.main.async {
                self.altertaMensaje(title: "Exito", message: "Se actualizo la tarea correctamente", confirmationMessage: "Ok")
                self.loader.stopAnimating()
                self.viewBack.isHidden = true
            }
        } failure: {
            (error) in
            print(error)
            DispatchQueue.main.async {
                
                self.loader.stopAnimating()
                self.viewBack.isHidden = true
                
                self.altertaMensaje(title: "Error", message: "Hubo un problema, intentelo mas tarde", confirmationMessage: "Ok")
            }
        }
    }
    @IBAction func cancelTask(_ sender: Any) {
        self.loader.startAnimating()
        self.viewBack.isHidden = false
        alertaConfirmacion(title: "Cancelar", message: "Desea cancelar esta tarea?", confirmationMessage: "Ok"){
            Api.shared.cancelTask(id: self.idTask!) {
                message in
                print(message)
                DispatchQueue.main.async {
                    
                    self.loader.stopAnimating()
                    self.viewBack.isHidden = true
                    
                    self.altertaMensaje(title: "Exito", message: "Se cancelo la tarea correctamente", confirmationMessage: "Ok", popView: true)
                }
            } failure: { error in
                print(error)
                
                self.loader.stopAnimating()
                self.viewBack.isHidden = true
                
                self.altertaMensaje(title: "Error", message: "Hubo un problema, intentelo mas tarde", confirmationMessage: "Ok")
            }
        } cancel: {
            print("No se cancelo tarea")
        }
        
    }
    
    @IBAction func addObservations(_ sender: Any) {
        
        isAddObservation = !isAddObservation
        
        
        if isAddObservation {
            observationField.isHidden = false
            observationField.isEditable = true
            observationField.initStyleEdit(fontSize: 15, fontWeight: .light, colorText: .black, selected: true)
            addObservationsBtn.initStyle(text: "Guardar")
            
            updateTaskBtn.isEnabled = false
            
        } else {
            
        
            alertaConfirmacion(title: "Observaciones", message: "Desea agregar las observaciones a la tarea?", confirmationMessage: "Ok") {
                
                self.updateTask(idTask: self.idTask!)
                self.updateTaskBtn.isEnabled = true
                self.observationField.isEditable = false
                
                self.observationField.backgroundColor = UIColor(red: 1, green: 1, blue: 1, alpha: 0)
                
                self.addObservationsBtn.initStyle(text: "Observaciones")
                

                
            } cancel: {
                
            }
        }
        
        
    }
    
    
    
}
