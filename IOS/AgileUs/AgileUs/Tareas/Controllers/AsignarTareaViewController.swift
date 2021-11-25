//
//  AsignarTareaViewController.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 22/11/21.
//

import UIKit

struct FindPersons: Codable {
let data: [DatosPersons]
}
struct DatosPersons: Codable {
    let nombre:String
}


class AsignarTareaViewController: UIViewController, UITextViewDelegate {



    @IBOutlet weak var nameTaskField: UITextField!
    @IBOutlet weak var personSelectField: UITextField!
    @IBOutlet weak var priortyField: UITextField!
    @IBOutlet weak var dateStartField: UITextField!
    @IBOutlet weak var fileField: UITextField!
    @IBOutlet weak var descriptionText: UITextView!
    @IBOutlet weak var dateEndField: UITextField!
    @IBOutlet weak var addTaskBtn: UIButton!
    
    let datePicker = UIDatePicker()
    var seleccionado_picker_persona = String()
    var seleccionado_picker_prioridad = String()
    var PersonasAsignadas = [String]()
    var prioridades = ["Alta","Media","Baja"]
    var selector_Persona = UIPickerView()
    var selector_Prioridad = UIPickerView()

    override func viewDidLoad() {
        super.viewDidLoad()
        navbarConfig()
        viewStyleConfig()
        inputStyleConfig()
        Configurar_teclado()
        configurar_DatePicker()
        Configurar_Picker_PersonasAsignadas()
        Configurar_Picker_Prioridades()
        LoadPersonsAsig()
        nameTaskField.textAlignment = .center
        fileField.textAlignment = .center
        
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
    
    


    
    
    @IBAction func Asiginar_tarea(_ sender: UIButton) {


        if(nameTaskField.text!.EsVacio || personSelectField.text!.EsVacio || priortyField.text!.EsVacio || dateStartField.text!.EsVacio || dateEndField.text!.EsVacio || descriptionText.text!.EsVacio || fileField.text!.EsVacio)
        {
           Alerta_CamposVacios(title: "Error", Mensaje: "Faltan campos por llenar")
        }
            

        
    }
    

    func LoadPersonsAsig()
        {
            let urlStr = "http://10.97.0.165:3040/api/user/findByBossId/618e88acc613329636a769ae"
            if let url = URL(string: urlStr) {
                URLSession.shared.dataTask(with: url) { (data, response , error) in
                    if let data = data {
                        do {
                            let Datapersons = try JSONDecoder().decode(FindPersons.self, from: data)
                            for item in Datapersons.data
                            {
                                self.PersonasAsignadas.append(item.nombre)
                            }
                        } catch {
                            print("error")
                        }
                    } else {
                        print("error")
                    }
                }.resume()
            }
            

        }
    

    
}


