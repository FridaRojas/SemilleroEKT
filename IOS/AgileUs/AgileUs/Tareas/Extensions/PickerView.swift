//
//  PickerView.swift
//  AgileUs
//
//  Created by Carlos nitsuga Hernandez hernandez on 24/11/21.
//

import Foundation
import UIKit

extension AsignarTareaViewController: UIPickerViewDataSource,UIPickerViewDelegate
{
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if priortyField.isFirstResponder
        {
            return prioridades.count
        }
        else if personSelectField.isFirstResponder
        {
            return PersonasAsignadas.count
        }
        return 0
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        
        if priortyField.isFirstResponder
        {
            seleccionado_picker_prioridad = prioridades[row]
        }
        else if personSelectField.isFirstResponder
        {
            seleccionado_picker_persona = PersonasAsignadas[row][0]
            seleccionado_picker_persona_id = PersonasAsignadas[row][1]
            
        }
        

    }
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if priortyField.isFirstResponder
        {
            return prioridades[row]
        }
        else if personSelectField.isFirstResponder
        {
            return PersonasAsignadas[row][0]
        }
        return ""
    }
 
    
    func Configurar_Picker_PersonasAsignadas()
    {
        personSelectField.textAlignment = .center
        selector_Persona.delegate = self
        selector_Persona.dataSource = self
        let barra_de_herramientaas = UIToolbar()
        barra_de_herramientaas.sizeToFit()
        let boton_listo = UIBarButtonItem(barButtonSystemItem: .done, target: nil, action: #selector(persona_seleccionada))
        barra_de_herramientaas.setItems([boton_listo], animated: true)
        personSelectField.inputAccessoryView = barra_de_herramientaas
        personSelectField.inputView = selector_Persona

        
    }
    func Configurar_Picker_Prioridades()
    {
        priortyField.textAlignment = .center
        selector_Prioridad.delegate = self
        selector_Prioridad.dataSource = self
        let barra_de_herramientaas = UIToolbar()
        barra_de_herramientaas.sizeToFit()
        let boton_listo = UIBarButtonItem(barButtonSystemItem: .done, target: nil, action: #selector(prioridad_seleccionada))
        barra_de_herramientaas.setItems([boton_listo], animated: true)
        priortyField.inputAccessoryView = barra_de_herramientaas
        priortyField.inputView = selector_Prioridad

        
    }
    @objc func prioridad_seleccionada()
    {

       var indexSelected = selector_Prioridad.selectedRow(inComponent: 0)
        print(indexSelected)
        if (indexSelected == 0)
        {
            
            priortyField.text = prioridades[0]
        }

        else
        {
        priortyField.text = seleccionado_picker_prioridad
        }
        self.view.endEditing(true)
    }
    
    @objc func persona_seleccionada()
    {

       var indexSelected = selector_Persona.selectedRow(inComponent: 0)
        if (indexSelected == 0)
        {
            
            personSelectField.text = PersonasAsignadas[0][0]
            seleccionado_picker_persona = PersonasAsignadas[0][0]
            seleccionado_picker_persona_id = PersonasAsignadas[0][1]
        }

        else
        {
        personSelectField.text = seleccionado_picker_persona
        }
        
        //print("VARIABLE SELECCIONADO PICKER PERSONA ID: \(seleccionado_picker_persona_id)")
        self.view.endEditing(true)
    }
}

