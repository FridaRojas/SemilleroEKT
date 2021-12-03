//
//  FiltroModalController.swift
//  ejemploCharts
//
//  Created by Luis Gregorio Ramirez Villalobos on 19/11/21.
//

import UIKit

class FiltroModalController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource {
    
    // variables
    var accion_confirmacion: ((_ datos: [Any]) -> Void)?
    var usuarios:[Any]?
    var idUsuario:String?
    
    var selector_periodo = UIPickerView()
    var selector_usuario = UIPickerView()
    let selector_fecha = UIDatePicker()
    
    var opciones_usuario = [Any]()
    var opciones_pIcker = [String]()
    
    // elementos
    @IBOutlet weak var opPeriodos: UISegmentedControl!
    @IBOutlet weak var txtFecha: UITextField!
    @IBOutlet weak var txtPeriodo: UITextField!
    @IBOutlet weak var lbOpcion: UILabel!
    @IBOutlet weak var lbFechaIni: UILabel!
    @IBOutlet weak var lbFechaFin: UILabel!
    @IBOutlet weak var txtFechaIni: UITextField!
    @IBOutlet weak var txtFechaFin: UITextField!
    @IBOutlet weak var txtUsuario: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configuraciones()
        configura_data_picker()
        configura_picker_view()
        configura_picker_fecha_i()
        configura_picker_fecha_f()
        ocultar_campos(tipo: "periodo")
    }
    
    func configuraciones() {
        opPeriodos.selectedSegmentTintColor = Hexadecimal_Color(hex: "66BB6A")
    }
    
    @IBAction func cancelar(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
        
    // funciones datepicker
    func configura_data_picker() {
        if #available(iOS 13.4, *) {
            selector_fecha.preferredDatePickerStyle = .wheels
        }
        
        selector_fecha.locale = Locale(identifier: "es_419")
        let boton_listo = UIBarButtonItem(barButtonSystemItem: .done, target: nil, action: #selector(fecha_elegida))
        let barra_de_herramientas = UIToolbar()
        barra_de_herramientas.sizeToFit()
        barra_de_herramientas.setItems([boton_listo], animated: true)
        
        txtFecha.inputAccessoryView = barra_de_herramientas
        txtFecha.inputView = selector_fecha
        selector_fecha.datePickerMode = .date
        
    }
    
    func configura_picker_view() {
        
        //periodos
        selector_periodo.delegate = self
        selector_periodo.dataSource = self
        selector_periodo.restorationIdentifier = "periodo"
        
        // usuarios
        selector_usuario.delegate = self
        selector_usuario.dataSource = self
        selector_usuario.restorationIdentifier = "usuario"
        configura_picker_usuarios()
    
    }
        
    @objc func valor_elegido() {
        txtPeriodo.text = opciones_pIcker[selector_periodo.selectedRow(inComponent: 0)]
        self.view.endEditing(true)
    }
    
    @objc func fecha_inicial_elegida() {
        let fecha = selector_fecha.date
        
        if !txtFechaFin.text!.isEmpty {
            let fechaFin = Date().convertir_string_a_fecha(fecha: txtFechaFin.text!)
            if fecha > fechaFin {
                alerta_mensajes(title: "Error", Mensaje: "Revise las fechas")
                txtFechaIni.text = ""
                return
            }
        }
            
        txtFechaIni.text = "\(Obtener_valor_fecha(fecha: fecha, estilo: "Fecha_Usuario"))"
        self.view.endEditing(true)
    }
    
    @objc func fecha_final_elegida() {
        let fecha = selector_fecha.date
        
        if !txtFechaIni.text!.isEmpty {
            let fechaIni = Date().convertir_string_a_fecha(fecha: txtFechaIni.text!)
            if fecha < fechaIni {
                alerta_mensajes(title: "Error", Mensaje: "Revise las fechas")
                txtFechaFin.text = ""
                return
            }
            
        }
        txtFechaFin.text = "\(Obtener_valor_fecha(fecha: fecha, estilo: "Fecha_Usuario"))"
        self.view.endEditing(true)
    }
    
    @objc func fecha_elegida() {
        let fecha = selector_fecha.date
        txtFecha.text = "\(Obtener_valor_fecha(fecha: fecha, estilo: "Fecha_Usuario"))"
        self.view.endEditing(true)
    }
    
    @objc func usuario_elegido() {
        var usuario = opciones_usuario[selector_usuario.selectedRow(inComponent: 0)] as! Usuario
        
        txtUsuario.text = usuario.nombre
        idUsuario = usuario.id
        self.view.endEditing(true)
    }
    
    func configura_picker_usuarios() {
        let boton_listo = UIBarButtonItem(barButtonSystemItem: .done, target: nil, action: #selector(usuario_elegido))
        let barra_de_herramientas = UIToolbar()
        barra_de_herramientas.sizeToFit()
        barra_de_herramientas.setItems([boton_listo], animated: true)
        
        txtUsuario.inputAccessoryView = barra_de_herramientas
        txtUsuario.inputView = selector_usuario
        obtener_usuarios()
    }
    
    func configura_picker_fecha_i() {
        let boton_listo = UIBarButtonItem(barButtonSystemItem: .done, target: nil, action: #selector(fecha_inicial_elegida))
        let barra_de_herramientas = UIToolbar()
        barra_de_herramientas.sizeToFit()
        barra_de_herramientas.setItems([boton_listo], animated: true)
        
        txtFechaIni.inputAccessoryView = barra_de_herramientas
        txtFechaIni.inputView = selector_fecha
        selector_fecha.datePickerMode = .date
    }
    
    func configura_picker_fecha_f() {
        let boton_listo = UIBarButtonItem(barButtonSystemItem: .done, target: nil, action: #selector(fecha_final_elegida))
        let barra_de_herramientas = UIToolbar()
        barra_de_herramientas.sizeToFit()
        barra_de_herramientas.setItems([boton_listo], animated: true)
        
        txtFechaFin.inputAccessoryView = barra_de_herramientas
        txtFechaFin.inputView = selector_fecha
        selector_fecha.datePickerMode = .date
    }
        
    func cambiar_picker_view_periodo() {
        let boton_listo = UIBarButtonItem(barButtonSystemItem: .done, target: nil, action: #selector(valor_elegido))
        let barra_de_herramientas = UIToolbar()
        barra_de_herramientas.sizeToFit()
        barra_de_herramientas.setItems([boton_listo], animated: true)
        
        txtPeriodo.inputAccessoryView = barra_de_herramientas
        txtPeriodo.inputView = selector_periodo
    }
    
    // funciones pickerview
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        
        var opciones = Int()
        
        if pickerView.restorationIdentifier == "periodo" {
            opciones =  opciones_pIcker.count
        } else if pickerView.restorationIdentifier == "usuario"{
            opciones = opciones_usuario.count
        }
        
        return opciones
    }

    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        
        var opcion = String()
        
        if pickerView.restorationIdentifier == "periodo" {
            opcion = opciones_pIcker[row]
        
        } else if pickerView.restorationIdentifier == "usuario" {
            var usuario = opciones_usuario[row] as! Usuario
            
            opcion = usuario.nombre
        }
        return opcion
    }
    
    // funciones para llenar picker view
    func obtener_meses_picker() {
        opciones_pIcker = Obtener_meses()
    }
    
    func obtener_anios() {
        opciones_pIcker = ["2020", "2021", "2022"]
    }
    
    func obtener_usuarios() {
        opciones_usuario = [String]()
        let usuarioL = Usuario(id: userID, nombre: userName, fechaInicio: "", fechaTermino: "", nombreRol: "", idgrupo: "", idsuperiorInmediato: "")
     
        opciones_usuario.append(usuarioL)
        let lista_usuarios = usuarios as! [Usuario]
        for i in lista_usuarios {
            opciones_usuario.append(i)
        }
    }
    
    @IBAction func cambioPeriodo(_ sender: UISegmentedControl) {
        txtFecha.text = ""
        txtPeriodo.text = ""
        txtUsuario.text = ""
        txtFechaFin.text = ""
        txtFechaIni.text = ""
        
        switch sender.selectedSegmentIndex {
        case 0:
            ocultar_campos(tipo: "periodo")
        case 1:
            ocultar_campos(tipo: "fecha")
            cambiar_picker_view_periodo()
            obtener_meses_picker()
        case 2:
            ocultar_campos(tipo: "fecha")
            cambiar_picker_view_periodo()
            obtener_anios()
        case 3:
            ocultar_campos(tipo: "custom")
        default:
            ocultar_campos(tipo: "periodo")
        }
    }
    
    func ocultar_campos(tipo: String) {
        if tipo == "fecha" {
            txtFecha.isHidden = true
            txtPeriodo.isHidden = false
            lbOpcion.isHidden = false
            txtFechaFin.isHidden = true
            txtFechaIni.isHidden = true
            lbFechaFin.isHidden = true
            lbFechaIni.isHidden = true
        }
        if tipo == "periodo" {
            txtFecha.isHidden = false
            txtPeriodo.isHidden = true
            lbOpcion.isHidden = false
            txtFechaFin.isHidden = true
            txtFechaIni.isHidden = true
            lbFechaFin.isHidden = true
            lbFechaIni.isHidden = true
        }
        if tipo == "custom" {
            lbOpcion.isHidden = true
            txtFechaFin.isHidden = false
            txtFechaIni.isHidden = false
            txtFecha.isHidden = true
            txtPeriodo.isHidden = true
            lbFechaFin.isHidden = false
            lbFechaIni.isHidden = false
        }
        
    }
    
    @IBAction func btnAceptar(_ sender: UIButton) {
        
        var info = [Any]()
        
       switch opPeriodos.selectedSegmentIndex {
        case 0:
           if txtFecha.text == "" || txtUsuario.text == "" {
               alerta_mensajes(title: "Error", Mensaje: "Faltan campos por llenar")
               return
           }
           
           info = [txtFecha.text!, txtFecha.text!, idUsuario!, txtUsuario.text!]
                      
       case 1:
           if txtPeriodo.text == "" || txtUsuario.text == "" {
               alerta_mensajes(title: "Error", Mensaje: "Faltan campos por llenar")
               return
           }
           
           let fechas_mes = Date().obtener_primer_ultimo_dia_mes(mes: txtPeriodo.text!)
           
           info = [fechas_mes[0], fechas_mes[1], idUsuario!, txtUsuario.text!]
       case 2:
           if txtPeriodo.text == "" || txtUsuario.text == "" {
               alerta_mensajes(title: "Error", Mensaje: "Faltan campos por llenar")
               return
           }
           
           let fechas_anio = Date().obtener_primer_ultimo_dia_anio(anio: txtPeriodo.text!)
           
           info = [fechas_anio[0], fechas_anio[1], idUsuario!, txtUsuario.text!]
       case 3:
           
           if txtFechaIni.text == "" || txtFechaFin.text == "" || txtUsuario.text == "" {
               alerta_mensajes(title: "Error", Mensaje: "Faltan campos por llenar")
               return
           }
           
           info = [txtFechaIni.text!, txtFechaFin.text!, idUsuario!, txtUsuario.text!]
           
        default:
            info = ["","","",""]
        }
        
        self.accion_confirmacion?(info)
        
        dismiss(animated: true, completion: nil)
        
    }
    
}
