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
            formatter.dateFormat = "dd/MM/yyyy"
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
    
    func Obtener_meses() -> [String] {
        
        let meses = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]
        
        return meses
        
    }
    
    func alerta_mensajes(title: String, Mensaje: String) {
        let mensaje_alerta = UIAlertController(title: title, message: Mensaje, preferredStyle: UIAlertController.Style.alert)
        
        let button_alerta = UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler:nil)
        
        mensaje_alerta.addAction(button_alerta)
        
        self.present(mensaje_alerta, animated: true, completion: nil)
    }
    
    func cantidadDeMensajes(mensaje: [Mensajes], idUsuario: String) -> [Int] {
        
        var leidos = 0
        var recibidos = 0
        var enviados = 0
        
        for i in mensaje {
            
            if i.idreceptor == idUsuario || i.idreceptor.contains(idUsuario) {
                recibidos += 1
            }
            
            if i.idreceptor == idUsuario && i.statusLeido == true {
                leidos += 1
            }
            
            if i.idemisor == idUsuario && i.statusEnviado == true {
                enviados += 1
            }
            
        }
        
        return [enviados, recibidos, leidos]
        
        
    }
    
    
    func cantidadDeTareas(tareas: [Tareas], idUsuario:String) -> [Int]{
        
        var pendientes = 0
        var terminadas = 0
        var iniciada = 0
        var revision = 0
        var arrTareas = [Int]()
        
        print("Tareas: \(tareas)")
        
        for i in tareas{
            
            if idUsuario == i.id_emisor {
                
                print(i.status)
                
                if "\(i.status)" == "pendiente"{
                    pendientes += 1
                }
                
                if "\(i.status)" == "iniciada"{
                    iniciada += 1
                }
                
                if "\(i.status)" == "revision"{
                    revision += 1
                }
                
                if "\(i.status)" == "terminada"{
                    terminadas += 1
                }
                
            }else{
                print("No se encuentran coincidencias de usuarios")
            }
        }
        
        arrTareas = [pendientes, iniciada, revision, terminadas]
        print(arrTareas)
        return arrTareas
    }
    
}

