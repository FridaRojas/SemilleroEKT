//
//  Funciones_Date.swift
//  AgileUs
//
//  Created by Luis Gregorio Ramirez Villalobos on 23/11/21.
//

import Foundation
import UIKit

extension Date {
    
    // funcion que retorna un arreglo con todos los días entre dos fechas
    func obtener_dias(fechaInicio: Date?, fechaFin: Date?) -> [Date] {
        var fechas:[Date] = []
        var inicio = fechaInicio!

        while inicio <= fechaFin! {
            fechas.append(inicio)
            inicio = Calendar.current.date(byAdding: .day, value: 1, to: inicio)!
        }
        
        return fechas
        
    }
    
    // funcion que retornar un date a partir de un string
    func convertir_string_a_fecha(fecha: String) -> Date {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "es_419")
        dateFormatter.dateFormat = "dd/MM/yyyy"
        
        return dateFormatter.date(from:fecha)!
    }
    
    // funcion para convertir la fecha de los servicios a formato Date
    func convertir_string_servicio_a_fecha(fecha: String) -> Date {
        let dateFormatter = DateFormatter()
        //dateFormatter.locale = Locale(identifier: "es_419")
        dateFormatter.dateFormat = "yyyy-MM-dd"
        
        return dateFormatter.date(from:fecha)!
    }
    
    func fechaServiciosAFecha(fecha: String) -> Date {
        //let fecha = "\(i.fechaEnviado)".prefix(10)
        var fechaHora = String()
        let fechaD = fecha.prefix(10)
        
        let inicio = fecha.index(fecha.startIndex, offsetBy: 11)
        let fin = fecha.index(fecha.endIndex, offsetBy: -10)
        let rango = inicio..<fin

        let subFecha = fecha[rango]
        
        fechaHora = "\(fechaD) \(subFecha)"
        let fechaConvertida = convertir_string_servicio_a_fecha(fecha: fechaHora)
        
        return fechaConvertida
        
    }
    
    
    // funcion que retorna el rango de fechas de una semana a partir de una fecha
    func dias_por_semana(fecha_semana: Date) -> String {
        
        let calendar = Calendar.current
        
        
        let dayOfWeek = calendar.component(.weekday, from: fecha_semana) - 1
        let weekdays = calendar.range(of: .weekday, in: .weekOfYear, for: fecha_semana)!
        let days = (weekdays.lowerBound ..< weekdays.upperBound)
            .compactMap { calendar.date(byAdding: .day, value: $0 - dayOfWeek, to: fecha_semana) }
        
        print(type(of: days))
        
        var fecha_i = convertir_string_a_fecha(fecha: String("\(days[0])".prefix(10)))
        print(fecha_i)
        let fecha_inicio = "\(days[0])".prefix(10)
        let fecha_fin = "\(days[days.count - 1])".prefix(10)
        let rango = " \(fecha_inicio) - \(fecha_fin)"
        
        return rango
        
    }
    
    // obtiene todas las semanas por año
    func obtener_semanas_por_año() -> [String] {
        
        // variables
        var semanas_count = [String]()
        var semanas_anio = [String]()
        let calendar = Calendar.current
        
        // obtener año actual y primer y ultimo dia del año
        let year = Calendar.current.component(.year, from: Date())
        let fecha_inicio = "01/01/\(year)"
        let fecha_fin = "31/12/\(year)"

        // obtener fechas a parttir de string
        let fecha_1 = convertir_string_a_fecha(fecha: fecha_inicio)
        let fecha_2 = convertir_string_a_fecha(fecha: fecha_fin)
         
        let fechas = obtener_dias(fechaInicio: fecha_1, fechaFin: fecha_2)
        
        // crea arregle de semanas por año con fechas
        for i in fechas {
            let numSemana = calendar.component(.weekOfYear, from: i)
            let semana = "Semana \(numSemana)"
            
            if (!semanas_count.contains(semana)) {
                semanas_count.append(semana)
                semanas_anio.append(semana + dias_por_semana(fecha_semana: i))
            }
            
        }
        
        return semanas_anio
        
    }
    
    func obtener_primer_ultimo_dia_mes(mes: String) -> [String] {
        
        var fechas = [String]()
        let year = Calendar.current.component(.year, from: Date())
        
        switch mes {
        case "Enero":
            fechas = ["01/01/\(year)", "31/01/\(year)"]
        case "Febrero":
            
            if anio_biciesto(anio: year) {
                fechas = ["01/02/\(year)", "29/02/\(year)"]
            } else {
                fechas = ["01/02/\(year)", "28/02/\(year)"]
            }
        case "Marzo":
            fechas = ["01/03/\(year)", "31/03/\(year)"]
        case "Abril":
            fechas = ["01/04/\(year)", "30/04/\(year)"]
        case "Mayo":
            fechas = ["01/05/\(year)", "31/05/\(year)"]
        case "Junio":
            fechas = ["01/06/\(year)", "30/06/\(year)"]
        case "Julio":
            fechas = ["01/07/\(year)", "31/07/\(year)"]
        case "Agosto":
            fechas = ["01/08/\(year)", "31/08/\(year)"]
        case "Septiembre":
            fechas = ["01/09/\(year)", "30/09/\(year)"]
        case "Octubre":
            fechas = ["01/10/\(year)", "31/10/\(year)"]
        case "Noviembre":
            fechas = ["01/11/\(year)", "30/11/\(year)"]
        case "Diciembre":
            fechas = ["01/12/\(year)", "31/12/\(year)"]
        default:
            fechas = ["",""]
        }
        
        return fechas
 
    }
    
    func anio_biciesto(anio: Int) -> Bool {
        return (anio % 4 == 0 && anio % 100 != 0) || anio % 400 == 0
            
    }
    
    func obtener_primer_ultimo_dia_anio(anio: String) -> [String] {
        
        var fechas = [String]()
        
        let fecha_inicio = "01/01/\(anio)"
        let fecha_fin = "31/12/\(anio)"
        
        fechas = [fecha_inicio, fecha_fin]
        return fechas
    }
    

    // funcion para convertir la fecha de los servicios a formato Date

    func inicioDia(fecha: Date) -> Date {
        return Calendar.current.startOfDay(for: fecha)
    }
    
    func finDia(fecha: Date) -> Date {
        
        var inicio = inicioDia(fecha: fecha)
        var components = DateComponents()
        components.day = 1
        components.second = -1
        return Calendar.current.date(byAdding: components, to: inicio)!
    }
    

}
