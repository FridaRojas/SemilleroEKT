//
//  Funciones_Generales.swift
//  AgileUs
//
//  Created by Luis Gregorio Ramirez Villalobos on 22/11/21.
//

import UIKit

extension UIViewController {
        
    /*
     Funcion para compatir captura de pantalla
     */
    func compartir_pantalla() {
        let bounds = UIScreen.main.bounds
        UIGraphicsBeginImageContextWithOptions(bounds.size, true, 0.0)
        self.view.drawHierarchy(in: bounds, afterScreenUpdates: false)
        let img = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        let activityViewController = UIActivityViewController(activityItems: [img!], applicationActivities: nil)
        self.present(activityViewController, animated: true, completion: nil)
    }
    
    /*
     Función que toma el valor hexadecimal de color para obtener su valor en RGB*/
    
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
            

            if i.idreceptor.contains(idUsuario) {
                if i.idemisor != idUsuario {
                    recibidos += 1
                }
            }
            
            if i.idreceptor.contains(idUsuario) && i.statusLeido == true {
                if i.idemisor != idUsuario {
                    leidos += 1
                }
            }
            
            if i.idemisor == idUsuario && i.statusEnviado == true {
                enviados += 1
            }
            
        }
        
        print("esdkjfbvhsdfb------- \(enviados) \(recibidos) \(leidos)")
        
        return [enviados, recibidos, leidos]
        
    }
    
    func cantidadDeMensajes(mensaje: [Mensajes], cantidades: [Int], idUsuario: String) -> [Int] {
        
        var enviados = cantidades[0]
        var recibidos = cantidades[1]
        var leidos = cantidades[2]
        
        for i in mensaje {
            
            if i.idreceptor.contains(idUsuario) {
                if i.idemisor != idUsuario {
                    recibidos += 1
                }
            }
            
            if i.idreceptor.contains(idUsuario) && i.statusLeido == true {
                if i.idemisor != idUsuario {
                    leidos += 1
                }
            }
            
            if i.idemisor == idUsuario && i.statusEnviado == true {
                enviados += 1
            }
            
        }
        
        return [enviados, recibidos, leidos]
    }
    
    func cantidadDeMensajesUsuario(mensaje: [Mensajes], idUsuario: String, nombre: String) -> [Any] {
        
        var enviados = 0
        var recibidos = 0
        var leidos = 0
        
        for i in mensaje {
            
            if i.idreceptor.contains(idUsuario) {
                if i.idemisor != idUsuario {
                    recibidos += 1
                }
            }
            
            if i.idreceptor.contains(idUsuario) && i.statusLeido == true {
                if i.idemisor != idUsuario {
                    leidos += 1
                }
            }
            
            if i.idemisor == idUsuario && i.statusEnviado == true {
                enviados += 1
            }
            
        }
        
        return [nombre, enviados, recibidos, leidos]
        
    }
    
    func cantidadBroadRecibidos(mensajes: [Mensajes], idUsuario: String) -> Int {
        var recibidos = 0
        for i in mensajes {
            if i.idemisor == userBroadcastID && i.idreceptor == idUsuario {
                recibidos += 1
            }
        }
        
        return recibidos
    }
    
    func cantidadBroadRecibidos(mensajes: [Mensajes], cantidades: [Int], idUsuario: String) -> Int {
        var recibidos = cantidades[1]
        for i in mensajes {
            if i.idemisor == userBroadcastID && i.idreceptor == idUsuario {
                recibidos += 1
            }
        }
        
        return recibidos
    }
    
    func cantidadDeBroad(mensaje_broad: [Broadcast], idUsuario: String) -> [Int] {
        let recibidos = 0
        var enviados = 0
    
        for _ in mensaje_broad {
            enviados += 1
        }
        return [enviados, recibidos]
    }
    
    func cantidadDeBroad(mensaje_broad: [Broadcast], cantidades: [Int], idUsuario: String) -> [Int] {
        var enviados = cantidades[0]
        let recibidos = cantidades[1]
    
        for i in mensaje_broad {
            enviados += 1
        }
        return [enviados, recibidos]
    }
    
    func cantidadDeMensajesPorFecha(mensaje: [Mensajes], idUsuario: String, fechaIni: String, fechaFin: String) -> [Int] {
        
        // quitar por idUsuario
        //let usuario = "618b05c12d3d1d235de0ade0"
        // FECHAS
        var fechaEnviado:Date?
        let fechaInicial = Date().convertir_string_a_fecha(fecha: fechaIni)
        let fechaFinal = Date().convertir_string_a_fecha(fecha: fechaFin)

        var leidos = 0
        var recibidos = 0
        var enviados = 0
        
        for i in mensaje {
            let fechaEnv = "\(i.fechaEnviado)".prefix(10)
            fechaEnviado = Date().convertir_string_servicio_a_fecha(fecha: String(fechaEnv))
            if fechaEnviado! >= fechaInicial && fechaEnviado! <= fechaFinal {
                if i.idreceptor.contains(idUsuario) {
                    if i.idemisor != idUsuario {
                        recibidos += 1
                    }
                }
                
                if i.idreceptor.contains(idUsuario) && i.statusLeido == true {
                    if i.idemisor != idUsuario {
                        leidos += 1
                    }
                }
                
                if i.idemisor == idUsuario && i.statusEnviado == true {
                    enviados += 1
                }
            }
        }
        return [enviados, recibidos, leidos]
    }
    
    func cantidadDeMensajesPorFechaUsuario(mensaje: [Mensajes], idUsuario: String, nombre: String, fechaIni: String, fechaFin: String) -> [Any] {
        
        var fechaEnviado:Date?
        let fechaInicial = Date().convertir_string_a_fecha(fecha: fechaIni)
        let fechaFinal = Date().convertir_string_a_fecha(fecha: fechaFin)

        var leidos = 0
        var recibidos = 0
        var enviados = 0
        
        for i in mensaje {
            let fechaEnv = "\(i.fechaEnviado)".prefix(10)
            fechaEnviado = Date().convertir_string_servicio_a_fecha(fecha: String(fechaEnv))
            if fechaEnviado! >= fechaInicial && fechaEnviado! <= fechaFinal {
                if i.idreceptor.contains(idUsuario) {
                    if i.idemisor != idUsuario {
                        recibidos += 1
                    }
                }
                
                if i.idreceptor.contains(idUsuario) && i.statusLeido == true {
                    if i.idemisor != idUsuario {
                        leidos += 1
                    }
                }
                
                if i.idemisor == idUsuario && i.statusEnviado == true {
                    enviados += 1
                }
            }
        }
        
        return [nombre, enviados, recibidos, leidos]
        
    }
    
    
    func cantidadDeMensajesPorFecha(mensaje: [Mensajes], cantidades: [Int], idUsuario: String, fechaIni: String, fechaFin: String) -> [Int] {
        
        // quitar por idUsuario
        //let usuario = "618b05c12d3d1d235de0ade0"
        // FECHAS
        var fechaEnviado:Date?
        let fechaInicial = Date().convertir_string_a_fecha(fecha: fechaIni)
        let fechaFinal = Date().convertir_string_a_fecha(fecha: fechaFin)

        var enviados = cantidades[0]
        var recibidos = cantidades[1]
        var leidos = cantidades[2]
        
        for i in mensaje {
            let fechaEnv = "\(i.fechaEnviado)".prefix(10)
            fechaEnviado = Date().convertir_string_servicio_a_fecha(fecha: String(fechaEnv))
            if fechaEnviado! >= fechaInicial && fechaEnviado! <= fechaFinal {
                if i.idreceptor.contains(idUsuario) {
                    if i.idemisor != idUsuario {
                        recibidos += 1
                    }
                }
                
                if i.idreceptor.contains(idUsuario) && i.statusLeido == true {
                    if i.idemisor != idUsuario {
                        leidos += 1
                    }
                }
                
                if i.idemisor == idUsuario && i.statusEnviado == true {
                    enviados += 1
                }
            }
        }
        return [enviados, recibidos, leidos]
    }
    
    
    
    func cantidadDeTareas(tareas: [Tareas], idUsuario:String) -> [Int]{
                
            var pendientes = 0
            var terminadas = 0
            var iniciada = 0
            var revision = 0
            var canceladas = 0
            var tareasaTiempo = 0
            var tareasDesTimempo = 0
            var leidas = 0
            var sinLeer = 0
            
            var arrTareas = [Int]()
            
            //print("Tareas: \(tareas)")
            
            for i in tareas{
                
                //Tareas del usuario
                if idUsuario == i.id_receptor {
                    
                    if "\(i.estatus!.lowercased())" == "pendiente"{
                        pendientes += 1
                    }
                    
                    if "\(i.estatus!.lowercased())" == "iniciada"{
                        iniciada += 1
                        
                    }
                    
                    if "\(i.estatus!.lowercased())" == "revision"{
                        revision += 1
                    }
                    
                    if "\(i.estatus!.lowercased())" == "terminada"{
                        terminadas += 1
                    }
                    
                    if "\(i.estatus!.lowercased())" == "cancelado"{
                        canceladas += 1
                    }
                    
                    if i.fecha_finR == nil || i.fecha_iniR == nil{
                        print("LAs fechas están vacias, no se puede obtener la tareas a tiempo")
                    }else{
                        
                        if i.fecha_fin! < i.fecha_finR!{
                            tareasDesTimempo += 1
                        }else if i.fecha_fin! >= i.fecha_finR!{
                            tareasaTiempo += 1
                        }
                    }
                    
                    //Comprobar que los mensajes son leidos por el usuario
                    if i.leido == true{
                        leidas += 1
                    }else{
                        sinLeer += 1
                        }
                }else{
                    print("No se encuentran coincidencias de usuarios")
                }
            }
            //arrTareas = [pendientes, iniciada, revision, terminadas]
            arrTareas = [pendientes, iniciada, revision, terminadas, tareasaTiempo, tareasDesTimempo]
            
            print("\nPendientes[0]: \(pendientes) \nIniciadas[1]: \(iniciada) \nRevision[2]: \(revision) \nTerminadas[3]: \(terminadas) \n\nA tiempo[4]: \(tareasaTiempo) \nFuera de tiempo[5]: \(tareasDesTimempo)\n")
            
            return arrTareas
        }
            
            
            
        //ESTADISTICAS DE FECHAS PERSONALIZADAS, O ESTADISTICAS DE FECHAS POR DEFECTO
        func cantidadDeTareasPorFecha(tareas:[Tareas], idUsuario:String, fechaInicio:String, fechaFin:String ) -> [Int]{
            
            var pendientes = 0
            var terminadas = 0
            var iniciada = 0
            var revision = 0
            var canceladas = 0

            var tareasaTiempo = 0
            var tareasDesTimempo = 0

            var leidas = 0
            var sinLeer = 0
            
            var arrTareas = [Int]()
            
            let fechaInicial = Date().convertir_string_a_fecha(fecha: fechaInicio)
            let fechaFinal = Date().convertir_string_a_fecha(fecha: fechaFin)
                    
            //Recorrer el arreglo de Fechas
            for i in tareas{
                
                let fechaI = Date().convertir_string_a_fecha(fecha: fechaInicio)
                let fechaF = Date().convertir_string_a_fecha(fecha: fechaFin)
                let fechaini = "\(i.fecha_ini!)".prefix(10)
                let fechafin = "\(i.fecha_fin!)".prefix(10)
                
                if Date().convertir_string_servicio_a_fecha(fecha: String(fechaini)) >= fechaI && Date().convertir_string_servicio_a_fecha(fecha: String(fechaini)) <= fechaF {
                    
                    if idUsuario == i.id_receptor!{
                        
                        if !fechaInicio.isEmpty || !fechaFin.isEmpty{
                            //Cantidad de Tareas por el rango de fechas

                            if "\(i.estatus!.lowercased())" == "pendiente"{
                                pendientes += 1
                            }
                            
                            if "\(i.estatus!.lowercased())" == "iniciada"{
                                iniciada += 1
                            }
                            
                            if "\(i.estatus!.lowercased())" == "revision"{
                                revision += 1
                            }
                            
                            if "\(i.estatus!.lowercased())" == "terminada"{
                                terminadas += 1
                            }
                            
                            if "\(i.estatus!.lowercased())" == "cancelado"{
                                canceladas += 1
                            }
                        }
                        if i.fecha_finR == nil || i.fecha_iniR == nil{
                            print("Las fechas de terminos están vacias")
                        }else{
                            
                            var fechaIReal = "\(i.fecha_iniR!)".prefix(10)
                            var fechaFReal = "\(i.fecha_finR!)".prefix(10)
                            
                            if Date().convertir_string_servicio_a_fecha(fecha: String(fechafin)) < Date().convertir_string_servicio_a_fecha(fecha: String(fechaFReal)){
                                tareasDesTimempo += 1
                            }else if Date().convertir_string_servicio_a_fecha(fecha: String(fechafin)) > Date().convertir_string_servicio_a_fecha(fecha: String(fechaFReal)){
                                tareasaTiempo += 1
                            }
                        }
                    }else{
                        print("Los usuarios no coinciden")
                    }
                }
            }
            arrTareas = [pendientes, iniciada, revision, terminadas, tareasaTiempo, tareasDesTimempo]
            return arrTareas
        }
        
        func cantidaDeTareasUsuarios(tareas: [Tareas], arrCantidadTareas:[Int], idUsuario:String ) -> [Int]{
            
            var pendientes = arrCantidadTareas[0]
            var iniciada = arrCantidadTareas[1]
            var revision = arrCantidadTareas[2]
            var terminadas = arrCantidadTareas[3]
            var canceladas = 0
            var tareasaTiempo = arrCantidadTareas[4]
            var tareasDesTimempo = arrCantidadTareas[5]
            var leidas = 0
            var sinLeer = 0
            
            var arrTareas = [Int]()
            var arrTareasUsuarios = [Int]()
            
            for i in tareas{
                if i.id_receptor == idUsuario{
                    if "\(i.estatus!.lowercased())" == "pendiente"{
                        pendientes += 1
                    }
                    
                    if "\(i.estatus!.lowercased())" == "iniciada"{
                        iniciada += 1
                        
                    }
                    
                    if "\(i.estatus!.lowercased())" == "revision"{
                        revision += 1
                    }
                    
                    if "\(i.estatus!.lowercased())" == "terminada"{
                        terminadas += 1
                    }
                    
                    if "\(i.estatus!.lowercased())" == "cancelado"{
                        canceladas += 1
                    }

                    if i.fecha_iniR == nil || i.fecha_finR == nil{
                        print("Hay fechas nulas, no se pueden determinar la cantidad de tareas realizadas")
                    }else {
                        //Filtrar por rango de fechas
                        if i.fecha_ini! <= i.fecha_iniR!  && i.fecha_fin! <= i.fecha_finR!{
                            //cantidad de tareas en fechas de término
                            if i.fecha_fin! < i.fecha_finR!{
                                tareasDesTimempo += 1
                            }else if i.fecha_fin! >= i.fecha_finR!{
                                tareasaTiempo += 1
                            }
                            //Contar las tareas leidas por el rango de fechas
                            if i.leido == true{
                                leidas += 1
                            }else{
                                sinLeer += 1
                            }
                        }
                    }
                }else{
                    print("Los usuarios no coinciden")
                }
            }
            arrTareasUsuarios = [pendientes, iniciada, revision, terminadas, tareasaTiempo, tareasDesTimempo]

            return arrTareasUsuarios
        }
        
        //ESTADISTICAS DE FECHAS PERSONALIZADAS, O ESTADISTICAS DE FECHAS POR DEFECTO
        func cantidadDeTareasTodosUsuariosPorFecha(tareas:[Tareas], idUsuario:String, nombre:String, fechaInicio:String, fechaFin:String ) -> [Any]{
            
            var pendientes = 0
            var iniciada = 0
            var revision = 0
            var terminadas = 0
            
            var tareasaTiempo = 0
            var tareasDesTimempo = 0

            var canceladas = 0
            var leidas = 0
            var sinLeer = 0
            
            var arrTareas = [Int]()
                    
            //Recorrer el arreglo de Fechas
            for i in tareas{
                
                let fechaI = Date().convertir_string_a_fecha(fecha: fechaInicio)
                let fechaF = Date().convertir_string_a_fecha(fecha: fechaFin)
                let fechaini = "\(i.fecha_ini!)".prefix(10)
                let fechafin = "\(i.fecha_fin!)".prefix(10)
               
                if Date().convertir_string_servicio_a_fecha(fecha: String(fechaini)) >= fechaI && Date().convertir_string_servicio_a_fecha(fecha: String(fechafin)) <= fechaF{
                    if idUsuario == i.id_receptor!{
                        
                        if !fechaInicio.isEmpty && !fechaFin.isEmpty{
                            //Cantidad de Tareas por el rango de fechas

                            if "\(i.estatus!.lowercased())" == "pendiente"{
                                pendientes += 1
                            }
                            
                            if "\(i.estatus!.lowercased())" == "iniciada"{
                                iniciada += 1
                            }
                            
                            if "\(i.estatus!.lowercased())" == "revision"{
                                revision += 1
                            }
                            
                            if "\(i.estatus!.lowercased())" == "terminada"{
                                terminadas += 1
                            }
                            
                            if "\(i.estatus!.lowercased())" == "cancelado"{
                                canceladas += 1
                            }
                        }
                        if i.fecha_iniR == nil || i.fecha_finR == nil{
                            print("Hay fechas reales nulas")
                        }else{
                            //let fechaIReal = "\(i.fecha_iniR!)".prefix(10)
                            let fechaFReal = "\(i.fecha_finR!)".prefix(10)
                            
                            if Date().convertir_string_servicio_a_fecha(fecha: String(fechafin)) < Date().convertir_string_servicio_a_fecha(fecha: String(fechaFReal)){
                                tareasDesTimempo += 1
                            }else if Date().convertir_string_servicio_a_fecha(fecha: String(fechafin)) >= Date().convertir_string_servicio_a_fecha(fecha: String(fechaFReal)){
                                tareasaTiempo += 1
                            }
                        }
                        
                        
                    }else{
                        print("Los usuarios no coinciden")
                    }
                }
            }
            arrTareas = [pendientes, iniciada, revision, terminadas, tareasaTiempo, tareasDesTimempo]
            
            return arrTareas
        }

        func cantidadDeTareasUsuarios(tareas: [Tareas], idUsuario: String, nombre: String) -> [Any] {
            
            var pendientes = 0
            var terminadas = 0
            var iniciada = 0
            var revision = 0
            var canceladas = 0

            var tareasaTiempo = 0
            var tareasDesTimempo = 0

            var leidas = 0
            var sinLeer = 0
            
            for i in tareas {
                
                if "\(i.estatus!.lowercased())" == "pendiente"{
                    pendientes += 1
                }
                
                if "\(i.estatus!.lowercased())" == "iniciada"{
                    iniciada += 1
                    
                }
                
                if "\(i.estatus!.lowercased())" == "revision"{
                    revision += 1
                }
                
                if "\(i.estatus!.lowercased())" == "terminada"{
                    terminadas += 1
                }
                
                if "\(i.estatus!.lowercased())" == "cancelado"{
                    canceladas += 1
                }
                
                if i.fecha_finR == nil || i.fecha_iniR == nil{
                    print("LAs fechas están vacias, no se puede obtener la tareas a tiempo")
                }else{
                    
                    if i.fecha_fin! < i.fecha_finR!{
                        tareasDesTimempo += 1
                    }else if i.fecha_fin! >= i.fecha_finR!{
                        tareasaTiempo += 1
                    }
                }
                
                //Comprobar que los mensajes son leidos por el usuario
                if i.leido == true{
                    leidas += 1
                }else{
                    sinLeer += 1
                }
            }
            
            let arrTareas = [nombre, pendientes, iniciada, revision, terminadas, tareasaTiempo, tareasDesTimempo] as [Any]
            
            return arrTareas
            
        }

}

//Fecha de creacion -> Fecha de registro en la BD
//Fecha inical -> Cuando el jefe quiere que el usuario quiere que inicie la tarea
//Fecha inicial Real -> El usuario subordinado, cambia el estado de la tarea.

//Fecha fin -> Tiempo que asigna el jefe para terminar la tarea
//Fecha fin real -> El usuario subordinado Termina la tarea
