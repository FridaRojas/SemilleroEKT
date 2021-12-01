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
    var datos = [Any]()
    
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
    
    //CONFIGURACIONES
    
    //Configurar los gráficos circulares
    // arreglo cantidad de mensajes broadcast
    var mensajesBroad:[Any]?
    var cantidad_mensajes_broad = [Int]()
    
    // variable para cambiar fondo de lista al seleccionar
    var seleccionado = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configuracion_cantidades()
        serviciosMensajes(idUsuario: userID)
        serviciosUsuarios()
        serviciosBroadcast(idUsuario: userID)
        configurar_pie_chart()
        
        //configurar los gráficos de barras
        configurar_bar_chart()
        
        //Configurar los elementos de la lista
        configurar_lista()
        
        //configurar los colores de los elementos a representar de los gráficos
        configuracion_colores()
        
        //Definir primeros datos de los elementos de la lista
        arrDatosLista = [["ic_PieChart", 0, 0, "pieM", 0], ["ic_Bar", 0, 0, "barM", 0]]
        
        //LLenar los gráficos de barras
        
        //llenar_pie_chartTareas(tareas: arrCantidadDeTareas)
        
        //Ejecutar los servicios web
        serviciosMensajes(idUsuario: userID)
        ejecucionServicios()
        
        configura_label_usuario(nombre: userName)
    }
    
    func ejecucionServicios(){
        serviciosUsuarios()
        serviciosTareas()
        
    }
    
    func configura_label_usuario(nombre: String) {
        lblNombreu.text = nombre
    }
    
    func configuracion_cantidades() {
        datos = [["ic_PieChart", 0, 0, "pie"], ["ic_Bar", 0, 0, "bar"]]
        cantidad_mensajes = [0,0,0]
        cantidad_mensajes_broad = [0,0]
        
    }
    
    //  SERVICIOS WEB       <--------------------------
    
    func serviciosUsuarios() {
        
        adaptadorServicios.serviciosWeb(idUsuario: userID) {
            [self] (Datos) -> Void in
            arrUsuarios = Datos
        }
    }
    
    func serviciosTareas(){
        let _ = adaptadorServicios.servicioWebTareasAdapter{
            [self] (Datos) -> Void in
            
            arrTareas = Datos
            
            print(type(of: Datos))
            
            //print(arrTareas as Any)
            
            //Cantidad de tareas
            arrCantidadDeTareas = cantidadDeTareas(tareas: arrTareas! as! [Tareas], idUsuario: "RECEPT1")
            
            //Tareas terminadas a tiempo y a desatiempo
            //arrTareasTerminadas = estadisticasTareasTerminadas(fechas: arrTareas! as [Tareas], idUsuario: "")
            
            //Gráfica de pastel
            llenar_pie_chartTareas(tareas: arrCantidadDeTareas)
            
            //gráfica de barras
            //actualizar_datos_lista_grafica(arrCountTask: arrTareasTerminadas)
        }
    }
    
    //  FILTRADO DE TAREAS
    func serviciosTareasFiltrado(filtros: [String]) {
        
        adaptadorServicios.servicioWebTareasAdapter(idUsuario: filtros[2]){
            [self] (Datos) -> Void in
            arrTareas = Datos
            arrCantidadDeTareas = cantidadDeTareas(tareas: arrTareas! as! [Tareas], idUsuario: filtros[2], fechaInicio: filtros[0], fechaFin: filtros[1])
            
            llenar_pie_chartTareas(tareas: arrCantidadDeTareas)
        }
    }

    func serviciosMensajes(idUsuario: String) {
        adaptadorServicios.servicioWebMensajesAdapter(idUsuario: idUsuario) {
                [self] (Datos) -> Void in
            mensajes = Datos
            cantidad_mensajes = cantidadDeMensajes(mensaje: mensajes! as! [Mensajes], idUsuario: idUsuario)
            llenar_pie_chart(mensajes: cantidad_mensajes)
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
    }
    
    func serviciosBroadcastRecibidos(idUsuario: String) {
        adaptadorServicios.servicioWebMensajesAdapter(idUsuario: userBroadcastID) {
                [self] (Datos) -> Void in
            mensajes = Datos
            let recibidos = cantidadBroadRecibidos(mensajes: mensajes! as! [Mensajes], idUsuario: idUsuario)
            
            cantidad_mensajes_broad[1] = recibidos
            actualizar_datos_lista_grafica(mensajes: cantidad_mensajes, broadcast: cantidad_mensajes_broad)
            
            //cantidad_mensajes = cantidadDeMensajes(mensaje: mensajes! as! [Mensajes], idUsuario: idUsuario)
            //llenar_pie_chart(mensajes: cantidad_mensajes)
        }
    }
    
    func serviciosBroadcast(idUsuario: String) {
        adaptadorServicios.servicioWebBroadcastAdapter(idUsuario: idUsuario) {
            [self] (Datos) -> Void in
            
            mensajesBroad = Datos
            cantidad_mensajes_broad = cantidadDeBroad(mensaje_broad: mensajesBroad! as! [Broadcast], idUsuario:
                                                     idUsuario)
            //actualizar_datos_lista_grafica(mensajes: cantidad_mensajes, broadcast: cantidad_mensajes_broad)
            serviciosBroadcastRecibidos(idUsuario: idUsuario)
        }

    }


    //  CONFIGURACIONES DE GRÁFICOS        <--------------------------
    
    func configuracion_colores() {
        view.backgroundColor = Hexadecimal_Color(hex: "F5F5F5")
        imgEncabezado.backgroundColor = Hexadecimal_Color(hex: "66BB6A")
        optionstAB.tintColor = Hexadecimal_Color(hex: "66BB6A")
        optionstAB.selectedSegmentTintColor = Hexadecimal_Color(hex: "66BB6A")
        indEnviados.backgroundColor = Hexadecimal_Color(hex: "66BB6A")
        indRecibidos.backgroundColor = Hexadecimal_Color(hex: "87D169")
        indLeidos.backgroundColor = UIColor.darkGray
    }
    
    func cargar_animacion_pie() {
        piechart.animate(yAxisDuration: 1, easingOption: ChartEasingOption.easeInOutQuad)
    }
    
    func cargar_animacion_bar() {
        barchart.animate(yAxisDuration: 0.8, easingOption: ChartEasingOption.easeInOutQuad)
    }
    
    func configurar_pie_chart() {
        piechart = PieChartView(frame: CGRect(x: 0, y: 0, width: viewChart.frame.size.width, height: viewChart.frame.size.height))
        piechart.removeFromSuperview()
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
        
    
    //  GENERAR CAMBIO DE ESTADISTICAS DE MENSAJES A ESTADISTICAS DE TAREAS
    
    
    @IBAction func cambio(_ sender: UISegmentedControl) {
        
        // pie chart mensajes
        if sender.selectedSegmentIndex == 0 {
            llenar_pie_chart(mensajes: cantidad_mensajes)
            
            graficoMensajes = true
        }
        
        // pie chart tareas
        if sender.selectedSegmentIndex == 1 {

            llenar_pie_chartTareas(tareas: arrCantidadDeTareas)
            
            graficoMensajes = false

        }
        
    }
    
    // LLENAR INFORMACION DE GRÁFICOS DE PASTEL
    
    //      Gráfico de pastel de Tareas
    
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
            configuracion_etiquetasPieTareas()
            
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
            
            //LLenar gráfica de barras
            actualizar_datos_lista_grafica(tareasCompletadas: Double(tareas[3]), tareasPendientes: Double(tareas[0]), tareasATiempo: Double(tareas[4]))
            
             
        }
        
    }
    //      Gráfico de Pastel de Mensajes

    // 0:envidos 1:recibidos 2:leidos
    func llenar_pie_chart(mensajes: [Int]) {
        
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
     
    
    // LLENAR INFORMACION DE GRÁFICOS DE BARRAS
    
    //      Gráficos de barras de Mensajes
    
 

    //      Gráficos de barras de tareas
    
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
            configuracion_etiquetasBarTareas(arrCantTareas: arrCantidadDeTareas)
            ocultar_etiquetas(tipo: true)
        }else{
            print("El arreglo de tareas está vacio")
        }
        
    }
    
    //  ACTUALIZAR DATOS DE LAS GRÁFICAS DE MENSAJES
    

    func actualizar_datos_lista_grafica(mensajes: [Int], broadcast: [Int]) {
        datos = [["ic_PieChart", mensajes[0], mensajes[1], "pie"], ["ic_Bar", cantidad_mensajes_broad[1], cantidad_mensajes_broad[0], "bar"]]

        opcionesGrafica.reloadData()
    }
    
    
    //  ACTUALIZAR DATOS DE LAS GRÁFICAS DE TAREAS
    
    func actualizar_datos_lista_grafica(tareasCompletadas: Double?, tareasPendientes: Double?, tareasATiempo: Double?) {
        
        print("Datos de la listá TAREAS")
        
        if tareasCompletadas == nil || tareasPendientes == nil {
            arrDatosLista = [["ic_PieChart", 0, 0, "pieT"], ["ic_Bar", 0, 0, "barT"]]
        } else {
            arrDatosLista = [["ic_PieChart", tareasCompletadas!, tareasPendientes!, "pieT", tareasATiempo!], ["ic_Bar", tareasCompletadas!, tareasPendientes!, "barT", tareasATiempo!]]
            
        }
        
        opcionesGrafica.reloadData()
    }
    
    
    //  FUNCIONALIDADES DE TABLAS
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        //print("CumberOfRowInSection")
        //print(tableView)
        //print(section)
        
        return arrDatosLista.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        //print("cell For Row At")
        
        let indice = indexPath.row
        //print(indice)
        
        let celda_personalizada = tableView.dequeueReusableCell(withIdentifier: ListaGrafica.identificador, for: indexPath) as! ListaGrafica

        
        //Cambiar las etiquetas de la lista
        celda_personalizada.configurar_celda(datos: arrDatosLista[indice] as! [Any])
        //print("Datos ls")
        //print(arrDatosLista)

        celda_personalizada.configurar_celda(datos: datos[indice] as! [Any])
        
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
            
            if graficoMensajes == true{
                
                llenar_pie_chart(mensajes: cantidad_mensajes)
                ocultar_etiquetas(tipo: false)
                
            }else{
                llenar_pie_chartTareas(tareas: arrCantidadDeTareas)
                ocultar_etiquetas(tipo: false)
            }
            
            
        } else if (indexPath.row == 1) {
            
            //Comprobar que gráfico es el que está seleccionado
            
            piechart.removeFromSuperview()
            
            if graficoMensajes == true{
                datos_bar_chart(datos: cantidad_mensajes_broad)
                ocultar_etiquetas(tipo: true)
            }else{
                
                llenar_bar_chartTareas(arrDatosT: arrCantidadDeTareas)
                
            }
        }
        tableView.reloadData()
    }
    
    
    //              CONFIGURACION DE ELEMENTOS GRÁFICOS
    
    //      CONFIGURACIONES DE ELEMENTOS EN GRÁFICAS DE MENSAJES
    //                  GRÁFICA DE PASTEL
    
    func configuracion_etiquetasBarMensajes(){
        
    }
    
    //                  GRÁFICA DE BARRAS
    
    func configuracion_etiquetasPieMensajes(){
        lblEnviados.text = "Enviados"
        lblRecibidos.text = "Recibidos"
        lblTotales.text = "Totales"
        lblLeidos.text = "Leidos"
    }
    
    //      CONFIGURACIONES DE ELEMENTOS EN GRÁFICAS DE TAREAS
    //                  GRÁFICA DE PASTEL
    
    func configuracion_etiquetasBarTareas(arrCantTareas: [Any]){
        lblTiempoLeido.text = "Finalizadas a tiempo"
        cantRecibidos.text = "\(arrCantTareas[4])"
        lblTotales.text = "Finalizadas fuera de tiempo"
        cantLeidos.text = "\(arrCantTareas[5])"
        
    }
    
    //                  GRÁFICA DE BARRAS
    func configuracion_etiquetasPieTareas(){
        lblEnviados.text = "Pendientes"
        lblRecibidos.text = "Iniciadas"
        lblTotales.text = "Revisión"
        lblLeidos.text = "Terminadas"
    }
    
    func ocultar_etiquetas(tipo: Bool) {
        lblTiempoRes.isHidden = tipo
        lblLeidos.isHidden = tipo
        lblEnviados.isHidden = tipo
        lblRecibidos.isHidden = tipo
        indLeidos.isHidden = tipo
        indRecibidos.isHidden = tipo
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
    
    func cambiar_color_indicador(tipo: Bool) {
        if tipo == true {
            indTotales.backgroundColor = Hexadecimal_Color(hex: "87D169")
        } else {
            indTotales.backgroundColor = UIColor.systemOrange
        }
    }
    
    @IBAction func abrirFiltros(_ sender: Any) {
        
        let modal_form = adaptadorModal.crear_modal_funcion(datos: arrUsuarios!, Accion_Confirmacion_completion: {
            [self](Filtro) -> Void in
              
            lblNombreu.text = (Filtro[3] as! String)
            
            print(Filtro)
            
        serviciosTareasFiltrado(filtros: Filtro as! [String])
        serviciosMensajesFiltrado(filtros: Filtro as! [String])
        serviciosBroadcast(idUsuario: Filtro[2] as! String)
            
        })
        present(modal_form, animated: true)
    }
}
    
