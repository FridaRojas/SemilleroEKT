//
//  AsignarTareaViewController.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 22/11/21.
//

import UIKit

class AsignarTareaViewController: UIViewController, UITextFieldDelegate, UITextViewDelegate,UIPickerViewDataSource, UIPickerViewDelegate {

    

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
    var opciones_personalizadas =
    ["Hugo","Paco","Luis", "Pedro"]
    var selector_personalizado = UIPickerView()

    override func viewDidLoad() {
        super.viewDidLoad()
        navbarConfig()
        viewStyleConfig()
        inputStyleConfig()
        Configurar_teclado()
        configurar_DatePicker()
        Configurar_Picker_PersonasAsignadas()
        //prueba_peticion_get_personasAsignadas()
        prueba2()
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
    
    
    func initTextFieldDelegate()
    {
        nameTaskField.delegate = self
        descriptionText.delegate = self
        personSelectField.delegate = self
        priortyField.delegate = self
        dateStartField.delegate = self
        dateEndField.delegate = self
        fileField.delegate = self
    }

    func Configurar_teclado()
       {
           
           initTextFieldDelegate()
            //Gesto para cerrar teclado al tocar la pantalla
            let gesto_tap = UITapGestureRecognizer(target: self, action: #selector(Ocultar_Teclado(_:)))
           self.view.addGestureRecognizer(gesto_tap)
            //Eventos de teclado
            NotificationCenter.default.addObserver(self, selector: #selector(cambio_teclado(notification:)), name: UIResponder.keyboardWillShowNotification, object: nil)
            NotificationCenter.default.addObserver(self, selector: #selector(cambio_teclado(notification:)), name: UIResponder.keyboardWillHideNotification, object: nil)
            NotificationCenter.default.addObserver(self, selector: #selector(cambio_teclado(notification:)), name: UIResponder.keyboardWillChangeFrameNotification, object: nil)
            
           
       }
    
        @objc func Ocultar_Teclado(_ sender: UITapGestureRecognizer)
        {
        
         nameTaskField.resignFirstResponder()
         descriptionText.resignFirstResponder()
         personSelectField.resignFirstResponder()
         priortyField.resignFirstResponder()
         dateStartField.resignFirstResponder()
         dateEndField.resignFirstResponder()
         fileField.resignFirstResponder()
        
         
        }
        deinit
        {
        
        NotificationCenter.default.removeObserver(self, name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.removeObserver(self, name: UIResponder.keyboardWillHideNotification, object: nil)
        NotificationCenter.default.removeObserver(self, name: UIResponder.keyboardWillChangeFrameNotification, object: nil)
        }
    
    @objc func cambio_teclado(notification:Notification)
    {
         let info_teclado = notification.userInfo?[UIResponder.keyboardFrameBeginUserInfoKey] as? NSValue
        let dimensiones_teclado = info_teclado?.cgRectValue
        let altura_teclado = dimensiones_teclado?.height
        var nombre_notificacion = notification.name.rawValue

        switch nombre_notificacion
        {
        case "UIKeyboardWillHideNotification": view.frame.origin.y = 0
        case "UIKeyboardWillShowNotification":
            if nameTaskField.isFirstResponder
            {
               
                view.frame.origin.y = (-0.5 * altura_teclado!) * 0.1
            }
            else if descriptionText.isFirstResponder
            {
                
                view.frame.origin.y = (-0.5 * altura_teclado!) * 1.3
            }
            else if fileField.isFirstResponder
            {
                
                view.frame.origin.y = (-0.5 * altura_teclado!) * 1.7
            }

            else
            {
                
                view.frame.origin.y = (-0.5 * altura_teclado!) * 0.1
            }
            
        default: view.frame.origin.y = 0
        }
    }
    //pregunta al delegado si debe procesar la pulsación del botón Retorno para el campo de texto.
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        switch textField
        {
        case nameTaskField:
            personSelectField.becomeFirstResponder()
        case personSelectField:
            priortyField.becomeFirstResponder()
        case priortyField:
            dateStartField.becomeFirstResponder()
        case dateStartField:
            dateEndField.becomeFirstResponder()
        case dateEndField:
            descriptionText.becomeFirstResponder()
        default: textField.becomeFirstResponder()
        }
        return true
    }
    
    func configurar_DatePicker()
    {
            
            let loc = Locale(identifier: "es_mx")
            datePicker.preferredDatePickerStyle = .wheels
            datePicker.locale = loc
            datePicker.minimumDate = Date()
            dateStartField.textAlignment = .center
            dateEndField.textAlignment = .center
            let toolBar = UIToolbar()
            toolBar.sizeToFit()
            
            let botonDone = UIBarButtonItem(barButtonSystemItem: .done, target: nil, action: #selector(PresionarDone))
            
            
             toolBar.setItems([botonDone], animated: true)
            
            //Asignar toolbar
            dateStartField.inputAccessoryView = toolBar
            dateEndField.inputAccessoryView = toolBar
            
            //asignar el datepicker al campo de texto
            dateStartField.inputView = datePicker
            dateEndField.inputView = datePicker
            
            //Modo del datepicker
            datePicker.datePickerMode = .date
             
    }
        @objc func PresionarDone()
        {
            //dar formato a la fecha
            let formatter = DateFormatter()
            formatter.dateFormat = "yyyy-MM-dd";
            if dateStartField.isFirstResponder
            {
            dateStartField.text = formatter.string(from: datePicker.date)
            }
            if dateEndField.isFirstResponder
            {

            dateEndField.text = formatter.string(from: datePicker.date)
            }
            
            self.view.endEditing(true)
        }
    
    @IBAction func Asiginar_tarea(_ sender: UIButton) {


        if(nameTaskField.text!.EsVacio || personSelectField.text!.EsVacio || priortyField.text!.EsVacio || dateStartField.text!.EsVacio || dateEndField.text!.EsVacio || descriptionText.text!.EsVacio || fileField.text!.EsVacio)
        {
           Alerta_CamposVacios(title: "Error", Mensaje: "Faltan campos por llenar")
        }
            

        
    }
    
    func Configurar_Picker_PersonasAsignadas()
    {
        selector_personalizado.delegate = self
        selector_personalizado.dataSource = self
        //sub-vista del picker
        let barra_de_herramientaas = UIToolbar()
        barra_de_herramientaas.sizeToFit()
        let boton_listo = UIBarButtonItem(barButtonSystemItem: .done, target: nil, action: #selector(persona_seleccionada))
        barra_de_herramientaas.setItems([boton_listo], animated: true)
        personSelectField.inputAccessoryView = barra_de_herramientaas

        personSelectField.inputView = selector_personalizado

        
    }
    @objc func persona_seleccionada()
    {

       var indexSelected = selector_personalizado.selectedRow(inComponent: 0)
        print(indexSelected)
        if (indexSelected == 0)
        {
            
            personSelectField.text = "Hugo"
        }

        else
        {
        personSelectField.text = seleccionado_picker_persona
        }
        self.view.endEditing(true)
    }
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return opciones_personalizadas.count
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        seleccionado_picker_persona = opciones_personalizadas[row]

    }
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return opciones_personalizadas[row]
    }
    
    
    func prueba_peticion_get_personasAsignadas()
    {
        let session = URLSession(configuration: URLSessionConfiguration.default, delegate: nil, delegateQueue: nil)
        var request = URLRequest(url: URL(string:"http://10.97.0.165:3040/api/user/findByBossId/618e88acc613329636a769ae")!)
        request.httpMethod = "GET"
        request.httpBody = try! JSONSerialization.data(withJSONObject: [], options: .prettyPrinted)
        let task = session.dataTask(with: request) { (data, response, error) in
            print("*********")
            print(data)
            print(response)
            print(error)
        }
        task.resume()
    }
    func prueba2()
    {
        // Create URL

        let url = URL(string: "http://10.97.0.165:3040/api/user/findByBossId/618e88acc613329636a769ae")

        //let url = URL(string: "http://" + "10.0.1.6" + "/rpc/Shelly.GetInfo")

        guard let requestUrl = url else { fatalError() }



        // Create URL Request

        var request = URLRequest(url: requestUrl)



        // Specify HTTP Method to use

        request.httpMethod = "GET"



        // Send HTTP Request

        let task = URLSession.shared.dataTask(with: request) { (data, response, error) in

            

            // Check if Error took place

            if let error = error {

                print("Error took place \(error)")

                return

            }

            

            // Read HTTP Response Status code

            if let response = response as? HTTPURLResponse {

                print("Response HTTP Status code: \(response.statusCode)")

            }
            if let data = data, let dataString = String(data: data, encoding: .utf8) {
                
                print("\(dataString)")
            }
            

            //convert HTTP data - json to struct

            let decoder = JSONDecoder()



            

            

        }

        task.resume()
    }
}


