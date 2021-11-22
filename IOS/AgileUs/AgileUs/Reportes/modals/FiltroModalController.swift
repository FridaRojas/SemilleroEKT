//
//  FiltroModalController.swift
//  ejemploCharts
//
//  Created by Luis Gregorio Ramirez Villalobos on 19/11/21.
//

import UIKit

class FiltroModalController: UIViewController {
    
    var accion_confirmacion: ((_ datos: [Any]) -> Void)?
    var selector_semana = UIPickerView()
    let selector_fecha = UIDatePicker()
    var opciones_periodo = ["Día", "Semana", "Mes", "Año"]
    
    @IBOutlet weak var opPeriodos: UISegmentedControl!
    
    // elementos
    @IBOutlet weak var txtFecha: UITextField!
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configuraciones()
        configura_data_picker()
        //selector_periodo.delegate = self
        //selector_periodo.dataSource = self
        // Do any additional setup after loading the view.
    }
    
    func configuraciones() {
        opPeriodos.selectedSegmentTintColor = Hexadecimal_Color(hex: "66BB6A")
    }
    
    @IBAction func cancelar(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    /*func configura_picker_view() {
        //sub-vista para picker
        let barra_de_herramientas = UIToolbar()
        barra_de_herramientas.sizeToFit()
        
        //let boton_listo = UIBarButtonItem(barButtonSystemItem: .done, target: nil, action: #selector(fecha_elegida))
        
        barra_de_herramientas.setItems([boton_listo], animated: true)
        
        txtEdad.inputAccessoryView = barra_de_herramientas
        //txtEdad.inputView = selector_fecha
        
        txtEdad.inputView = selector_personalizado
        
        
        selector_fecha.datePickerMode = .date
        
    }*/
    
    // funciones datapicker
    
    func configura_data_picker() {
        if #available(iOS 13.4, *) {
            selector_fecha.preferredDatePickerStyle = .wheels
        }
        
        selector_fecha.locale = Locale(identifier: "es_419")
        
        //sub-vista para picker
        let barra_de_herramientas = UIToolbar()
        barra_de_herramientas.sizeToFit()
        
        let boton_listo = UIBarButtonItem(barButtonSystemItem: .done, target: nil, action: #selector(fecha_elegida))
        
        barra_de_herramientas.setItems([boton_listo], animated: true)
        
        txtFecha.inputAccessoryView = barra_de_herramientas
        txtFecha.inputView = selector_fecha
        selector_fecha.datePickerMode = .date
        
    }
    
    @objc func fecha_elegida() {
        var fecha = selector_fecha.date
        print(fecha)
        txtFecha.text = "\(Obtener_valor_fecha(fecha: fecha, estilo: "Fecha_Usuario"))"
        //print(selector_personalizado.selectedRow(inComponent: 0))
        //print(selector_personalizado.selectedRow(inComponent: 1))
        
        self.view.endEditing(true)
    }
 

}
