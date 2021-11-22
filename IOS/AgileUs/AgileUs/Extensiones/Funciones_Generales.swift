//
//  Funciones_Generales.swift
//  AgileUs
//
//  Created by Luis Gregorio Ramirez Villalobos on 22/11/21.
//

import UIKit

extension UIViewController {
    
    /*
     Función que toma el valor hexadecimal de color para obtener su valor en RGB
     */
    func Hexadecimal_Color(hex:String) -> UIColor {
        var cString:String = hex.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()

        if (cString.hasPrefix("#")) {
            cString.remove(at: cString.startIndex)
        }

        if ((cString.count) != 6) {
            return UIColor.gray
        }

        var rgbValue:UInt64 = 0
        Scanner(string: cString).scanHexInt64(&rgbValue)

        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
    
    // Función que retorna un formato de fecha de acuerdo al estilo deseado
    func Obtener_valor_fecha(fecha: Date, estilo: String) -> String {
        
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "es_419")
        switch estilo {
        case "Fecha_Usuario":
            formatter.dateFormat = "dd/MMMM/yyyy"
        case "Fecha_Base_Datos":
            formatter.dateFormat = "yyyy/MM//dd"
        case "dia":
            formatter.dateFormat = "dd"
        case "mes":
            formatter.dateFormat = "MM"
        case "year":
            formatter.dateFormat = "yyyy"
        case "hora_completa_usuario":
            formatter.dateFormat = "hh:mm a"
        case "hora_completa_base_datos":
            formatter.dateFormat = "HH:mm:ss"
        case "am_pm":
            formatter.dateFormat = "a"
        case "hora":
            formatter.dateFormat = "hh"
        case "minutos":
            formatter.dateFormat = "mm"
        default:
            formatter.dateFormat = "dd:MM:yyyy hh:mm a"
        }
        
        return formatter.string(from: fecha)
    }
}


