//
//  ViewController.swift
//  ejemploCharts
//
//  Created by Luis Gregorio Ramirez Villalobos on 17/11/21.
//

import UIKit
import Charts

class ReportesScreen: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    // elementos
    @IBOutlet weak var optionstAB: UISegmentedControl!
    @IBOutlet weak var viewChart: UIView!
    @IBOutlet weak var imgEncabezado: UIImageView!
    @IBOutlet weak var opcionesGrafica: UITableView!
    var piechart = PieChartView()
    var barchart = BarChartView()
    
    // etiqueta cantidades
    @IBOutlet weak var cantEnviados: UILabel!
    @IBOutlet weak var cantRecibidos: UILabel!
    @IBOutlet weak var cantLeidos: UILabel!
    @IBOutlet weak var cantTotales: UILabel!
    
    //indicadores
    @IBOutlet weak var indEnviados: UIImageView!
    @IBOutlet weak var indRecibidos: UIImageView!
    @IBOutlet weak var indLeidos: UIImageView!
    @IBOutlet weak var indTotales: UIImageView!
    
    // etiquetas
    @IBOutlet weak var lblTotales: UILabel!
    @IBOutlet weak var lblLeidos: UILabel!
    @IBOutlet weak var lblEnviados: UILabel!
    @IBOutlet weak var lblRecibidos: UILabel!
    @IBOutlet weak var lblTiempoLeido: UILabel!
    @IBOutlet weak var lblTiempoRes: UILabel!
    @IBOutlet weak var lblNombreu: UILabel!
    
    // variables para lista
    var arrDatosLista = [Any]()
    //var datos = [Any]()
    
    // arreglo de usuarios por lider
    var arrUsuarios:[Any]?
    var idUsuario:String?
    
    // arreglo de cantidad de mensajes
    var mensajes:[Any]?
    var cantidad_mensajes = [Int]()
    
    //arreglo de cantidad de tareas
    var arrTareas:[Any]?
    var arrCantidadDeTareas = [Int]()
    var arrTareasTerminadas = [Int]()
    
    // adaptadores
    let adaptadorModal = Adaptador_Modals()
    var adaptadorServicios = AdaptadorServicios()
    
    //Variables del tipo de gráfico
    var graficoMensajes = false
        
    // arreglo cantidad de mensajes broadcast
    var mensajesBroad:[Any]?
    var cantidad_mensajes_broad = [Int]()
    
    // variable para cambiar fondo de lista al seleccionar
    var seleccionado = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //Ejecutar los servicios web
        //ejecucionServicios()
       
        //Configuraciones
        configuraciones()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        ejecucionServicios()
    }
    
    // Funcion para ejecutar servicios
    func ejecucionServicios(){
        serviciosMensajes(idUsuario: userID)
        serviciosTareas()
        serviciosUsuarios()
        serviciosBroadcast(idUsuario: userID)
    }
    
    
    // Funciones de servicioes web --->
    func serviciosMensajes(idUsuario: String) {
        adaptadorServicios.servicioWebMensajesAdapter(idUsuario: idUsuario) {
                [self] (Datos) -> Void in
            mensajes = Datos
            cantidad_mensajes = cantidadDeMensajes(mensaje: mensajes! as! [Mensajes], idUsuario: idUsuario)
            llenar_pie_chart(mensajes: cantidad_mensajes)
        }
    }
    
    func serviciosTareas(){
        let _ = adaptadorServicios.servicioWebTareasAdapter{
            [self] (Datos) -> Void in
            
            arrTareas = Datos

            //Cantidad de tareas
            arrCantidadDeTareas = cantidadDeTareas(tareas: arrTareas! as! [Tareas], idUsuario: "ReceptorAlexis")
        }
    }
    
    func serviciosUsuarios() {
        adaptadorServicios.serviciosWeb(idUsuario: userID) {
            [self] (Datos) -> Void in
            arrUsuarios = Datos
        }
    }
    
    func serviciosBroadcast(idUsuario: String) {
        adaptadorServicios.servicioWebBroadcastAdapter(idUsuario: idUsuario) {
            [self] (Datos) -> Void in
            
            mensajesBroad = Datos
            cantidad_mensajes_broad = cantidadDeBroad(mensaje_broad: mensajesBroad! as! [Broadcast], idUsuario:
                                                     idUsuario)
            serviciosBroadcastRecibidos(idUsuario: idUsuario)
        }
    }
    
    func serviciosBroadcastRecibidos(idUsuario: String) {
        adaptadorServicios.servicioWebMensajesAdapter(idUsuario: userBroadcastID) {
                [self] (Datos) -> Void in
            mensajes = Datos
            let recibidos = cantidadBroadRecibidos(mensajes: mensajes! as! [Mensajes], idUsuario: idUsuario)
            
            cantidad_mensajes_broad[1] = recibidos
            actualizar_datos_lista_grafica(mensajes: cantidad_mensajes, broadcast: cantidad_mensajes_broad)
        }
    }
    
    func serviciosTareasFiltrado(filtros: [String]) {
        adaptadorServicios.servicioWebTareasAdapter(idUsuario: filtros[2]){
            [self] (Datos) -> Void in
            arrTareas = Datos
            arrCantidadDeTareas = cantidadDeTareas(tareas: arrTareas! as! [Tareas], idUsuario: filtros[2], fechaInicio: filtros[0], fechaFin: filtros[1])
            llenar_pie_chartTareas(tareas: arrCantidadDeTareas)
        }
    }
    
    func serviciosMensajesFiltrado(filtros: [String]) {
        adaptadorServicios.servicioWebMensajesAdapter(idUsuario: filtros[2]) {
                [self] (Datos) -> Void in
            mensajes = Datos
            cantidad_mensajes = cantidadDeMensajesPorFecha(mensaje: mensajes! as! [Mensajes], idUsuario: filtros[2], fechaIni: filtros[0], fechaFin: filtros[1])
            configura_label_usuario(nombre: filtros[3])
            llenar_pie_chart(mensajes: cantidad_mensajes)
        }
    } // Fin funciones de servicioes web <---
    
        
    // Confiuraciones de graficos --->
    func configuraciones(){
        //      MENSAJES
        //Configuraciones de gráfico de pastel de mensajes
        configurar_pie_chart()
        //configurar los gráficos de barras de mensajes
        configurar_bar_chart()
        
        //      LISTA
        //Configurar lista
        configurar_lista()
        //Datos de la lista
        configuracion_cantidades()
        //      GRÁFICAS
        //configurar los colores de los elementos a representar de los gráficos
        configuracion_colores()
        configura_label_usuario(nombre: userName)
    }
     
    func configuracion_colores() {
        view.backgroundColor = Hexadecimal_Color(hex: "F5F5F5")
        imgEncabezado.backgroundColor = Hexadecimal_Color(hex: "66BB6A")
        optionstAB.tintColor = Hexadecimal_Color(hex: "66BB6A")
        optionstAB.selectedSegmentTintColor = Hexadecimal_Color(hex: "66BB6A")
        indEnviados.backgroundColor = Hexadecimal_Color(hex: "66BB6A")
        indRecibidos.backgroundColor = Hexadecimal_Color(hex: "87D169")
        indLeidos.backgroundColor = UIColor.darkGray
    }
    
    func configurar_pie_chart() {
        piechart = PieChartView(frame: CGRect(x: 0, y: 0, width: viewChart.frame.size.width, height: viewChart.frame.size.height))
        //piechart.removeFromSuperview()
        piechart.isUserInteractionEnabled = false
        piechart.legend.enabled = false
    }
    
    func configurar_bar_chart() {
        barchart = BarChartView(frame: CGRect(x: 0, y: 0, width: viewChart.frame.size.width, height: viewChart.frame.size.height))
        barchart.legend.enabled = false
        barchart.isUserInteractionEnabled = false
    }
    
    func configurar_lista() {
        opcionesGrafica.delegate = self
        opcionesGrafica.dataSource = self
        opcionesGrafica.register(ListaGrafica.nib(), forCellReuseIdentifier: ListaGrafica.identificador)
    }

    func configura_label_usuario(nombre: String) {
        lblNombreu.text = nombre
    }
    
    func configuracion_cantidades() {
        //Definir primeros datos de los elementos de la lista
        arrDatosLista = [["ic_PieChart", 0, 0, "pieM"], ["ic_Bar", 0, 0, "barM"]]
        arrCantidadDeTareas = [0,0,0,0,0,0]
        arrTareasTerminadas = [0,0]
        
        //datos = [["ic_PieChart", 0, 0, "pie"], ["ic_Bar", 0, 0, "bar"]]
        cantidad_mensajes = [0,0,0]
        cantidad_mensajes_broad = [0,0]
    } // --->
    
    
    // Lenado de graficas pie --->
    // 0:envidos 1:recibidos 2:leidos
    func llenar_pie_chart(mensajes: [Int]) {
        
        if !barchart.isEmpty(){
            barchart.removeFromSuperview()
        }
        
        let enviado = mensajes[0]
        let recibido = mensajes[1]
        let leido = mensajes[2]
        
        cantEnviados.text = "\(enviado)"
        cantRecibidos.text = "\(recibido)"
        cantLeidos.text = "\(leido)"
        cantTotales.text = "\(enviado + recibido + leido)"
        
        let enviados = PieChartDataEntry(value: Double(enviado))
        let recibidos = PieChartDataEntry(value: Double(recibido))
        let leidos = PieChartDataEntry(value: Double(leido))
        
        let entries = [enviados, recibidos, leidos]
        let dataset = PieChartDataSet(entries: entries, label: nil)
        let chartdata = PieChartData(dataSet: dataset)
        chartdata.setValueTextColor(UIColor.clear)

        let colors = [Hexadecimal_Color(hex: "66BB6A"), Hexadecimal_Color(hex: "87D169"), Hexadecimal_Color(hex: "7F8182")]
        
        dataset.colors = colors
        piechart.data = chartdata
        piechart.contentMode = .scaleToFill
        viewChart.addSubview(piechart)
        cargar_animacion_pie()
        actualizar_datos_lista_grafica(mensajes: cantidad_mensajes, broadcast: cantidad_mensajes_broad)
    }
    
    func llenar_pie_chartTareas(tareas: [Int]){
        //Remover el gráfico de barras de la vista
        if !barchart.isEmpty(){
            barchart.removeFromSuperview()
        }
        
        //Comprobar que el arreglo de datos que se va a interpretar no esté vacio
        if tareas.isEmpty{
            print("No hay datos para mostrar TAREAS")
        }else{
            
            print("llenando gráfico de barras de Tareas")
     
            cantEnviados.text = "\(tareas[0])"
            cantRecibidos.text = "\(tareas[1])"
            cantTotales.text = "\(tareas[2])"
            cantLeidos.text = "\(tareas[3])"
            
            let pend = PieChartDataEntry(value: Double(tareas[0]))
            let inic = PieChartDataEntry(value: Double(tareas[1]))
            let rev = PieChartDataEntry(value: Double(tareas[2]))
            let term = PieChartDataEntry(value: Double(tareas[3]))
            
            let entries = [pend, inic, rev, term]
            let dataSet = PieChartDataSet(entries: entries)
            let chartdata = PieChartData(dataSet: dataSet)
            chartdata.setValueTextColor(UIColor.clear)
            
            let colors = [Hexadecimal_Color(hex: "66BB6A"), Hexadecimal_Color(hex: "87D169"), Hexadecimal_Color(hex: "7F8182"), Hexadecimal_Color(hex: "AAAABB")]
            
            dataSet.colors = colors
            
            //Gráficar los datos que se están asignando
            piechart.data = chartdata
            piechart.contentMode = .scaleToFill
            
            viewChart.addSubview(piechart)
            cargar_animacion_pie()
            actualizar_datos_lista_grafica(tareas: arrCantidadDeTareas)

        }
    }
    // Fin llenado de graficas pie <---
    
    //Llenado de graficas de barras --->
    
    func llenar_bar_chartTareas(arrDatosT: [Any]){
        //Comprobar que el arreglo no esté vacio
        if !arrDatosT.isEmpty{
            let tareasATiempo = BarChartDataEntry(x: 1, y: Double("\(arrDatosT[4])")!)
            //Por definir este campo                        <---------------------------------------------------------
            let tareasFueraTiempo = BarChartDataEntry(x: 2, y: Double("\(arrDatosT[5])")!)
            
            let entrie = [tareasATiempo, tareasFueraTiempo]
            
            let dataSet = BarChartDataSet(entries: entrie)
            let chartData = BarChartData(dataSet: dataSet)
            barchart.data = chartData
            
            let colors = [Hexadecimal_Color(hex: "66BB6A"), Hexadecimal_Color(hex: "87D169")]
            dataSet.colors = colors
            
            viewChart.addSubview(barchart)
            cargar_animacion_bar()
            
            //********* Añadir la configuracion de etiquetas *********
            //configuracion_etiquetasBarTareas(arrCantTareas: arrCantidadDeTareas)
            //ocultar_etiquetas(tipo: true)
        }else{
            print("El arreglo de tareas está vacio")
        }
    }
    
    func llenar_bar_chart(datos: [Any]?) {
        let enviados = BarChartDataEntry(x: 1, y: Double("\(datos![0])")!)
        let recibidos = BarChartDataEntry(x: 2, y: Double("\(datos![1])")!)
        let entries = [enviados, recibidos]
        
        let dataset = BarChartDataSet(entries: entries)
        let chardata = BarChartData(dataSet: dataset)
        barchart.data = chardata
        
        let colors = [Hexadecimal_Color(hex: "66BB6A"), Hexadecimal_Color(hex: "87D169")]
        dataset.colors = colors
        viewChart.addSubview(barchart)
        cargar_animacion_bar()
    }
    // Fin llenado graficas de barras <---
    
    //  ACTUALIZAR DATOS DE LAS GRÁFICAS DE MENSAJES --->
    func actualizar_datos_lista_grafica(mensajes: [Int], broadcast: [Int]) {
        arrDatosLista = [["ic_PieChart", mensajes[0], mensajes[1], "pieM"], ["ic_Bar", cantidad_mensajes_broad[1], cantidad_mensajes_broad[0], "barM"]]
        opcionesGrafica.reloadData()
    }
    
    func actualizar_datos_lista_grafica(tareas: [Int]) {
        arrDatosLista = [["ic_PieChart", tareas[0], tareas[1], "pieT"], ["ic_Bar", arrCantidadDeTareas[4], arrCantidadDeTareas[5], "barT"]]
        opcionesGrafica.reloadData()
    } // <--
    
    // Animaciones en graficas --->
    func cargar_animacion_pie() {
        piechart.animate(yAxisDuration: 1, easingOption: ChartEasingOption.easeInOutQuad)
    }
    
    func cargar_animacion_bar() {
        barchart.animate(yAxisDuration: 0.8, easingOption: ChartEasingOption.easeInOutQuad)
    } // <---
    
    
    // Modal filtros
    @IBAction func abrirFiltros(_ sender: Any) {
        let modal_form = adaptadorModal.crear_modal_funcion(datos: arrUsuarios!, Accion_Confirmacion_completion: {
            [self](Filtro) -> Void in
              
            lblNombreu.text = (Filtro[3] as! String)
                    
        serviciosTareasFiltrado(filtros: Filtro as! [String])
        serviciosMensajesFiltrado(filtros: Filtro as! [String])
        serviciosBroadcast(idUsuario: Filtro[2] as! String)
            
        })
        present(modal_form, animated: true)
    } // --->
    
    
    //  FUNCIONALIDADES DE TABLAS
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return arrDatosLista.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let indice = indexPath.row
        let celda_personalizada = tableView.dequeueReusableCell(withIdentifier: ListaGrafica.identificador, for: indexPath) as! ListaGrafica
        
        //Cambiar las etiquetas de la lista
        celda_personalizada.configurar_celda(datos: arrDatosLista[indice] as! [Any])
        //celda_personalizada.configurar_celda(datos: datos[indice] as! [Any])
        if (seleccionado == indexPath.row) {
            celda_personalizada.configurar_fondo(fondo: "Card_2")
        } else {
            celda_personalizada.configurar_fondo(fondo: "Card")
        }
        return celda_personalizada
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        seleccionado = indexPath.row
        
        if indexPath.row == 0 {
            //Comprobar que gráfico es el que está seleccionado
            barchart.removeFromSuperview()
            if optionstAB.selectedSegmentIndex == 0 {
                llenar_pie_chart(mensajes: cantidad_mensajes)
                ocultar_etiquetas(tipo: false)
            } else if optionstAB.selectedSegmentIndex == 1 {
                llenar_pie_chartTareas(tareas: arrCantidadDeTareas)
                ocultar_etiquetas(tipo: false)
            }
        } else if (indexPath.row == 1) {
            //Comprobar que gráfico es el que está seleccionado
            piechart.removeFromSuperview()
            
            if optionstAB.selectedSegmentIndex == 0 {
                print("ejecuta mensajes")
                llenar_bar_chart(datos: cantidad_mensajes_broad)
                datos_bar_chart(datos: cantidad_mensajes_broad)
                ocultar_etiquetas(tipo: true)
            } else if optionstAB.selectedSegmentIndex == 1 {
                print("ejecuta tareas")
                configuracion_etiquetasBarTareas(arrCantTareas: arrCantidadDeTareas)
                ocultar_etiquetas(tipo: true)
                llenar_bar_chartTareas(arrDatosT: arrCantidadDeTareas)
            }
            
        }
        tableView.reloadData()
    } // <---
    
    // Funcionalidades de elementos
    @IBAction func cambio(_ sender: UISegmentedControl) {
        // pie chart mensajes
        if sender.selectedSegmentIndex == 0 {
            seleccionado = 0
            llenar_pie_chart(mensajes: cantidad_mensajes)
            configuracion_etiquetasPieMensajes()
            ocultar_etiquetas(tipo: false)
            graficoMensajes = true
        }
        // pie chart tareas
        if sender.selectedSegmentIndex == 1 {
            seleccionado = 0
            llenar_pie_chartTareas(tareas: arrCantidadDeTareas)
            configuracion_etiquetasPieTareas()
            ocultar_etiquetas(tipo: false)
            graficoMensajes = false

        }
    } // <---
    
    // Funciones de configuracion de etiquetas
    func configuracion_etiquetasPieTareas(){
        lblEnviados.text = "Pendientes"
        lblRecibidos.text = "Iniciadas"
        lblTotales.text = "Revisión"
        lblLeidos.text = "Terminadas"
    }
    
    func configuracion_etiquetasPieMensajes(){
        lblEnviados.text = "Enviados"
        lblRecibidos.text = "Recibidos"
        lblTotales.text = "Totales"
        lblLeidos.text = "Leidos"
    }
    
    func ocultar_etiquetas(tipo: Bool) {
        lblTiempoRes.isHidden = tipo
        lblLeidos.isHidden = tipo
        lblEnviados.isHidden = tipo
        lblRecibidos.isHidden = tipo
        indLeidos.isHidden = tipo
        indRecibidos.isHidden = tipo
        lblTotales.isHidden = tipo
        cantTotales.isHidden = tipo
        cantEnviados.isHidden = tipo
        lblTiempoLeido.isHidden = !tipo
        lblTiempoRes.isHidden = !tipo
        cambiar_color_indicador(tipo: tipo)
    }
    
    func datos_bar_chart(datos: [Int]) {
        lblTiempoLeido.text = "Enviados a broadcast"
        lblTiempoRes.text = "Recibidos a broadcast"
        cantLeidos.text = "\(datos[1])"
        cantRecibidos.text = "\(datos[0])"
    }
    
    func configuracion_etiquetasBarTareas(arrCantTareas: [Any]){
        lblTiempoLeido.text = "Finalizadas a tiempo"
        cantRecibidos.text = "\(arrCantTareas[4])"
        lblTiempoRes.text = "Finalizadas fuera de tiempo"
        cantLeidos.text = "\(arrCantTareas[5])"
    }
    
    func cambiar_color_indicador(tipo: Bool) {
        if tipo == true {
            indTotales.backgroundColor = Hexadecimal_Color(hex: "87D169")
        } else {
            indTotales.backgroundColor = UIColor.systemOrange
        }
    } // <---

}
    
