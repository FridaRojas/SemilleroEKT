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
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        getTask()
        inputStyleConfig(isEdit: isEdit)
        viewStyleConfig()
        
        inputsEdit(isEdit: isEdit)
        
        
        
    }
    
    func getTask() {
        Api.shared.editTask(id: idTask!) {
            (task) in
            
            DispatchQueue.main.async {
                print(task)
                self.task = task
                self.nameTaskField.text = task.titulo!
                self.personSelectField.text = task.nombre_receptor!
                self.priortyField.text = "Prioridad: \(task.prioridad!)"
                self.descriptionText.text =  task.descripcion!
                self.dateStartField.text = "Inicio: \(task.fecha_ini!)"
                self.dateEndField.text = "Fin: \(task.fecha_fin!)"
                self.statusField.text = "Estatus: \(task.estatus!)"
                
                //                if task.observaciones == nil && task.estatus == "Revision" {
                //                    self.addObservationsBtn.isHidden = false
                //                } else {
                //                    self.addObservationsBtn.isHidden = true
                //                }
                
                if let observaciones = task.observaciones {
                    if task.estatus == "Revision" {
                        self.addObservationsBtn.isHidden = false
                    } else {
                        self.addObservationsBtn.isHidden = true
                    }
                }
                
                if task.observaciones != "" {
                    self.observationField.isHidden = true
                }
                print(type(of: task.observaciones))
                
            }
        } failure: {
            (error) in
            print(error)
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
        observationField.initStyleEdit(fontSize: 15, fontWeight: .light, colorText: .black, selected: isEdit)
        
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
        observationField.isEditable = isEdit
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
            updateTaskBtn.initStyle(text: "Guardar")
        }
        
        
        
    }
    
    func updateTask(idTask: String) {
        
        let dateStart = HelpString.removeWord(phrase: dateStartField.text!, word: "Inicio: ")
        let dateEnd = HelpString.removeWord(phrase: dateEndField.text!, word: "Fin: ")
        let priority = HelpString.removeWord(phrase: priortyField.text!, word: "Prioridad: ")
        let status = HelpString.removeWord(phrase: statusField.text!, word: "Estatus: ")
        
        let task = Task(id_grupo: "GRUPOID1", id_emisor: "EMIS1", nombre_emisor: "JOSE", id_receptor: "RECEPT1", nombre_receptor: personSelectField.text!, fecha_ini: dateStart, fecha_fin: dateEnd, titulo: nameTaskField.text!, descripcion: descriptionText.text!, prioridad: priority, estatus: status)
        
        Api.shared.updateTask(id: idTask, task: task) {
            (task) in
            DispatchQueue.main.async {
                self.altertaMensaje(title: "Exito", message: "Se actualizo la tarea correctamente", confirmationMessage: "Ok", popView: true)
            }
        } failure: {
            (error) in
            print(error)
            DispatchQueue.main.async {
                self.altertaMensaje(title: "Error", message: "Hubo un problema, intentelo mas tarde", confirmationMessage: "Ok")
            }
        }
    }
    @IBAction func cancelTask(_ sender: Any) {
        
        alertaConfirmacion(title: "Cancelar", message: "Desea cancelar esta tarea?", confirmationMessage: "Ok"){
            Api.shared.cancelTask(id: self.idTask!) {
                message in
                print(message)
                DispatchQueue.main.async {
                    self.altertaMensaje(title: "Exito", message: "Se cancelo la tarea correctamente", confirmationMessage: "Ok", popView: true)
                }
            } failure: { error in
                print(error)
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
            
        } else {
            
        }
        
        
    }
    
    
    
}
