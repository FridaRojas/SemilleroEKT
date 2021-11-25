//
//  DatePicker.swift
//  AgileUs
//
//  Created by Carlos nitsuga Hernandez hernandez on 24/11/21.
//

import Foundation
import UIKit

extension AsignarTareaViewController
{
    
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
        

}
