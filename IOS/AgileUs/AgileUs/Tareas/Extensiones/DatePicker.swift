//
//  Validaciones.swift
//  AgileUs
//
//  Created by Carlos nitsuga Hernandez hernandez on 22/11/21.
//

import UIKit
extension UIViewController
{
    let datePicker = UIDatePicker()
    func CreacionDatePicker()
    {
        //campo_fecha.textAlignment = .center
        
        let toolBar = UIToolbar()
        toolBar.sizeToFit()
        
        let botonDone = UIBarButtonItem(barButtonSystemItem: .done, target: nil, action: #selector(PresionarDone))
        
        /*
         toolBar.setItems([botonDone], animated: true)
        
        Asignar toolbar
        campo_fecha.inputAccessoryView = toolBar
        
        asignar el datepicker al campo de texto
        campo_fecha.inputView = datePicker
        
        Modo del datepicker
        datePicker.datePickerMode = .date
         */
    }
    @objc func PresionarDone()
    {
        /* dar formato a la fecha
        let formatter = DateFormatter()
        formatter.dateStyle = .medium
        formatter.timeStyle = .none
        
        
        campo_fecha.text = formatter.string(from: datePicker.date)
        self.view.endEditing(true) */
    }
}

