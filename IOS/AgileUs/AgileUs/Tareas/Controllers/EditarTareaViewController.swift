//
//  EditarTareaViewController.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 24/11/21.
//

import UIKit

class EditarTareaViewController: UIViewController {

    var idTask: String?
    
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
    
    override func viewDidLoad() {
        super.viewDidLoad()
        inputStyleConfig()
        viewStyleConfig()
        
        Api.shared.editTask(id: idTask!) {
            (task) in
            
            DispatchQueue.main.async {
                print(task)
                self.nameTaskField.text = task.titulo!
                self.personSelectField.text = task.nombre_receptor!
                self.priortyField.text = "Prioridad: \(task.prioridad!)"
//                self.dateStartField.text = task.fecha_ini!
//                self.dateEndField.text = task.fecha_fin!
                self.descriptionText.text =  task.descripcion!
                self.dateStartField.text = "Inicio: \(task.fecha_ini!)"
                self.dateEndField.text = "Fin: \(task.fecha_fin!)"
                
                if task.observaciones != "" {
                    self.observationField.isHidden = true
                }
                
            }
        } failure: {
            (error) in
            print(error)
        }
        
    }
    
    func inputStyleConfig() {
        
        nameTaskField.font = UIFont.systemFont(ofSize: 20, weight: .bold)
        
        personSelectField.font = UIFont.systemFont(ofSize: 15, weight: .bold)
        personSelectField.textColor = .darkGray
        
        priortyField.font = UIFont.systemFont(ofSize: 15, weight: .light)
        priortyField.textColor = .darkGray
        
        descriptionText.textColor = .black
        descriptionText.font = UIFont.systemFont(ofSize: 15, weight: .light)
        descriptionText.backgroundColor = UIColor(red: 1, green: 1, blue: 1, alpha: 0)
        
        dateStartField.backgroundColor = UIColor(red: 1, green: 1, blue: 1, alpha: 0)
        dateStartField.setIcon(image: UIImage(named: "calendarIcon")!, inRight: false, positionUIImageView: CGRect(x: 0, y: 0, width: 20, height: 20), positionUIView: CGRect(x: 10, y: 0, width: 20, height: 20))
        dateStartField.textColor = UIColor.darkGray
        dateStartField.font = UIFont.systemFont(ofSize: 12, weight: .light)
        
        dateEndField.setIcon(image: UIImage(named: "calendarIcon")!, inRight: false, positionUIImageView: CGRect(x: 0, y: 0, width: 20, height: 20), positionUIView: CGRect(x: 10, y: 0, width: 20, height: 20))
        dateEndField.backgroundColor = UIColor(red: 1, green: 1, blue: 1, alpha: 0)
        dateEndField.textColor = UIColor.darkGray
        dateEndField.font = UIFont.systemFont(ofSize: 12, weight: .light)
        
        observationField.textColor = .black
        observationField.font = UIFont.systemFont(ofSize: 15, weight: .light)
        observationField.backgroundColor = UIColor(red: 1, green: 1, blue: 1, alpha: 0)
        
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
    
//    @IBAction func updateTask(_ sender: Any) {
//
//        let task = Task(id_grupo: "GRUPOID1", id_emisor: "EMIS1", nombre_emisor: "JOSE", id_receptor: "RECEPT1", nombre_receptor: personSelectField.text!, fecha_ini: dateEndField.text!, fecha_fin: dateEndField.text!, titulo: nameTaskField.text!, descripcion: descriptionText.text!, prioridad: priortyField.text)
//
//        Api.shared.updateTask(id: idTask!, task: task) {
//            (task) in
//            DispatchQueue.main.async {
//                self.altertaMensaje(title: "Exito", message: "Se actualizo la tarea correctamente", confirmationMessage: "Ok", popView: true)
//            }
//        } failure: {
//            (error) in
//            print(error)
//            DispatchQueue.main.async {
//                self.altertaMensaje(title: "Error", message: "Hubo un problema, intentelo mas tarde", confirmationMessage: "Ok")
//            }
//        }
//    }
    
}
