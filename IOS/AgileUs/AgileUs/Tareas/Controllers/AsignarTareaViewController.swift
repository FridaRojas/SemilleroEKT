//
//  AsignarTareaViewController.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 22/11/21.
//

import UIKit
import MobileCoreServices



class AsignarTareaViewController: UIViewController, UITextViewDelegate, UIDocumentPickerDelegate {
    
    var urlFile: URL?

    @IBOutlet weak var nameTaskField: UITextField!
    @IBOutlet weak var personSelectField: UITextField!
    @IBOutlet weak var priortyField: UITextField!
    @IBOutlet weak var dateStartField: UITextField!
    @IBOutlet weak var fileField: UIButton!
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
        fileField.styleTypeInput(title: "Archivo Adjunto")
        descriptionText.initStyle(placeholder: "Descripcion")
        addTaskBtn.initStyle(text: "Asignar Tarea")
    }
    
    @IBAction func addTask(_ sender: Any) {
        
        let task = Task(
                id_grupo: "GRUPOID1",
                id_emisor: "EMIS1",
                nombre_emisor: "JOSE",
                id_receptor: "ReceptorAlexis",
                nombre_receptor: "cristian",
                fecha_ini: dateEndField.text!,
                fecha_fin: dateEndField.text!,
                titulo: nameTaskField.text!,
                descripcion: descriptionText.text!,
                prioridad: priortyField.text!,
                estatus: "pendiente")
        
        Api.shared.createTask(task: task, file: urlFile) {
            (task) in
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
    @IBAction func uploadFile(_ sender: Any) {
        
        let fileTypes = [
            String(kUTTypePDF)
        ]
        
        let viewPickerFile = UIDocumentPickerViewController(documentTypes: fileTypes, in: .import)
        
        viewPickerFile.delegate = self
        
        present(viewPickerFile, animated: true, completion: nil)
    }
    
    func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentsAt urls: [URL]) {
        
        if controller.documentPickerMode == .import {
            
            guard let url = urls.first else {
                return
            }
            
            do {
                if let filename = urls.first?.lastPathComponent {
                    urlFile = url
                    
                    self.fileField.setTitle(filename, for: .normal)
                    
                    controller.dismiss(animated: true, completion: nil)


                    
                }
            } catch {
                let nserror = error as NSError
                fatalError("Unresolved error \(nserror), \(nserror.userInfo)")
            }
            
        }
    }
    
    func documentPickerWasCancelled(_ controller: UIDocumentPickerViewController) {
        controller.dismiss(animated: true, completion: nil)
    }
    
}





