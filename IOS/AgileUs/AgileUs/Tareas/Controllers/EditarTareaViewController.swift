//
//  EditarTareaViewController.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 24/11/21.
//

import UIKit

class EditarTareaViewController: UIViewController, UITextViewDelegate, UITextFieldDelegate {
    
    var idTask: String?
    var isEdit: Bool = false
    var isAddObservation: Bool = false
    var task: Task?
    
    @IBOutlet weak var nameTaskField: UITextField!
    @IBOutlet weak var personSelectField: UITextField!
    @IBOutlet weak var priortyField: UITextField!
    @IBOutlet weak var dateStartField: UITextField!
    @IBOutlet weak var fileField: UIButton!
    @IBOutlet weak var descriptionText: UITextView!
    @IBOutlet weak var dateEndField: UITextField!
    @IBOutlet weak var updateTaskBtn: UIButton!
    @IBOutlet weak var observationField: UITextView!
    @IBOutlet weak var cancelTaskBtn: UIButton!
    @IBOutlet weak var addObservationsBtn: UIButton!
    @IBOutlet weak var statusField: UITextField!
    @IBOutlet weak var loader: UIActivityIndicatorView!
    @IBOutlet weak var viewBack: UIView!
    let datePicker = UIDatePicker()
    var prioridades = ["Alta","Media","Baja"]
    var seleccionado_picker_estatus = String()
    var seleccionado_picker_prioridad = String()
    var selector_Estatus = UIPickerView()
    var selector_Prioridad = UIPickerView()
    
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
        
        observationField.tag = 1
        descriptionText.tag = 2

        observationField.delegate = self
        descriptionText.delegate = self
        
        priortyField.tag = 1
        statusField.tag = 2
        dateStartField.tag = 3
        dateEndField.tag = 4
        
        priortyField.delegate = self
        statusField.delegate = self
        dateStartField.delegate = self
        dateEndField.delegate = self
        
        personSelectField.isEnabled = false
        
        
        
    }
    
/*    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        switch textView.tag {
        case 1: return textView.stopBackspaceIn(word: "Observaciones: ", text: text)
        case 2: return textView.stopBackspaceIn(word: "Descripción: ", text: text)
        default: print("otro")
        }
        return true
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        
        switch textField.tag {
        case 1: return textField.stopBackspaceIn(word: "Prioridad: ", text: string)
        case 2: return textField.stopBackspaceIn(word: "Estatus: ", text: string)
        case 3: return textField.stopBackspaceIn(word: "Inicio: ", text: string)
        case 4: return textField.stopBackspaceIn(word: "Fin: ", text: string)

        default: print("otro")
        }
        return true
        
    }*/

    func getTask() {
        Api.shared.editTask(id: idTask!) {
            (task) in
            
            DispatchQueue.main.async {
                
                self.task = task
                print(task)
                self.nameTaskField.text = task.titulo!
                self.personSelectField.text = task.nombre_receptor!
                self.priortyField.text = "Prioridad: \(task.prioridad!)"
                self.descriptionText.text =  "\(task.descripcion!)"
                self.dateStartField.text = "Inicio: \(HelpString.formatDate(date: task.fecha_ini!))"
                self.dateEndField.text = "Fin: \(HelpString.formatDate(date: task.fecha_fin!))"
                self.statusField.text = "Estatus: \(task.estatus!)"

          
                if task.observaciones == nil || task.observaciones == "" {
                } else {
                    self.observationField.text = "\(task.observaciones!)"
                }
                
                if task.estatus == "Terminada" || task.estatus == "Cancelado" {
                    self.addObservationsBtn.isHidden = true
                    self.updateTaskBtn.isHidden = true
                    self.cancelTaskBtn.isHidden = true
                }
        
                
                if task.estatus != "revision" {
                    self.addObservationsBtn.isHidden = true
                }
                
                if task.observaciones != nil || task.observaciones != "" {
                } else {
                    self.observationField.text = "Observaciones: \(task.observaciones!)"
                }
                
                if task.archivo != nil {
                    self.fileField.styleTypeInput(title: "Tarea_\(task.id_tarea!)")
                } else {
                    self.fileField.styleTypeInput(title: "Archivo Adjunto")

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
        priortyField.initStyleEdit(fontWeight: .light, colorText: .darkGray, selected: isEdit)
        statusField.initStyleEdit(fontWeight: .light, colorText: .darkGray, selected: isEdit)
        descriptionText.initStyleEdit(fontSize: 15, fontWeight: .light, colorText: .black, selected: isEdit)
        dateStartField.initStyleEdit(fontSize: 12, fontWeight: .light, colorText: .darkGray, imageName: "calendarIcon", selected: isEdit)
        dateEndField.initStyleEdit(fontSize: 12, fontWeight: .light, colorText: .darkGray, imageName: "calendarIcon", selected: isEdit)
 
        
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
        priortyField.isEnabled = isEdit
        statusField.isEnabled = isEdit
        descriptionText.isEditable = isEdit
        dateStartField.isEnabled = isEdit
        dateEndField.isEnabled = isEdit
        fileField.isEnabled = isEdit
        
    }
    @IBAction func editBtn(_ sender: Any) {
        Configurar_Picker_Prioridades()
        configurar_DatePicker()
        isEdit = !isEdit
        self.inputsEdit(isEdit: isEdit)
        self.inputStyleConfig(isEdit: isEdit)
        
        if task!.estatus != "revision" {
            statusField.isEnabled = false
            statusField.initStyleEdit(fontWeight: .light, colorText: .darkGray, selected: false)
        } else {
            Configurar_Picker_Estatus()
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
                
                if self.task!.estatus != "revision"  {
                    self.statusField.isEnabled = false
                    self.statusField.initStyleEdit(fontWeight: .light, colorText: .darkGray, selected: false)
                }
                
                self.loader.stopAnimating()
                self.viewBack.isHidden = true
                
            }
            
        } else {
            self.isEdit = true
            addObservationsBtn.isEnabled = false
            updateTaskBtn.initStyle(text: "Guardar")
        }
        
        
        
    }
    
    func updateTask(idTask: String, updateObservation: Bool = false) {
        
        self.loader.startAnimating()
        self.viewBack.isHidden = false
        
        
        let dateStart = HelpString.removeWord(phrase: dateStartField.text!, word: "Inicio: ")
        let dateEnd = HelpString.removeWord(phrase: dateEndField.text!, word: "Fin: ")
        let priority = HelpString.removeWord(phrase: priortyField.text!, word: "Prioridad: ")
        let status = HelpString.removeWord(phrase: statusField.text!, word: "Estatus: ")

        print(observationField.text)
        let task = Task(id_grupo: "GRUPOID1", id_emisor: "EMIS1", nombre_emisor: "JOSE", fecha_ini: dateStart, fecha_fin: dateEnd, titulo: nameTaskField.text!, descripcion: descriptionText.text, prioridad: priority, estatus: status, observaciones: observationField.text!)
        
        Api.shared.updateTask(id: idTask, task: task) {
            (task) in
            DispatchQueue.main.async {
                
                if updateObservation {
                    Api.shared.changeStatus(id: self.idTask!, status: "Pendiente"){
                        message in
                        
                        DispatchQueue.main.async {
                            self.altertaMensaje(title: "Exito", message: "Se actualizo la tarea correctamente", confirmationMessage: "Ok")
                            self.loader.stopAnimating()
                            self.viewBack.isHidden = true
                            self.statusField.text = "Estatus: Pendiente"
                            self.addObservationsBtn.isHidden = true
                            
                        }
                   
                        
                    } failure: { error in
                        print("error cambio: \(error)")
                    }
                } else {
                    self.altertaMensaje(title: "Exito", message: "Se actualizo la tarea correctamente", confirmationMessage: "Ok")
                    self.loader.stopAnimating()
                    self.viewBack.isHidden = true
                }
                
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
                    self.addObservationsBtn.isHidden = true
                    self.updateTaskBtn.isHidden = true
                    self.cancelTaskBtn.isHidden = true
                    self.statusField.text = "Estatus: Cancelado"
                    self.altertaMensaje(title: "Exito", message: "Se cancelo la tarea correctamente", confirmationMessage: "Ok")
                }
            } failure: { error in
                print(error)
                
                self.loader.stopAnimating()
                self.viewBack.isHidden = true
                
                self.altertaMensaje(title: "Error", message: "Hubo un problema, intentelo mas tarde", confirmationMessage: "Ok")
            }
        } cancel: {
            print("No se cancelo tarea")
            self.loader.stopAnimating()
            self.viewBack.isHidden = true
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
                
                self.updateTask(idTask: self.idTask!, updateObservation: true)
                self.updateTaskBtn.isEnabled = true
                self.observationField.isEditable = false
                
                self.observationField.backgroundColor = UIColor(red: 1, green: 1, blue: 1, alpha: 0)
                
                self.addObservationsBtn.initStyle(text: "Observaciones")
                
                self.loader.startAnimating()
                self.viewBack.isHidden = false
                
        

                
            } cancel: {
                self.loader.stopAnimating()
                self.viewBack.isHidden = true
            }
        }
    }
    
    
}
