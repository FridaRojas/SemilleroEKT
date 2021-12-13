
//
//  ViewController.swift
//  ejemploCharts
//
//  Created by Luis Gregorio Ramirez Villalobos on 17/11/21.
//

import UIKit
import Charts

class ReportesScreen: UIViewController, UITableViewDelegate, UITableViewDataSource, CAAnimationDelegate {

    // elementos
    @IBOutlet weak var optionstAB: UISegmentedControl!
    @IBOutlet weak var viewChart: UIView!
    @IBOutlet weak var imgEncabezado: UIImageView!
    @IBOutlet weak var opcionesGrafica: UITableView!
    @IBOutlet weak var viewIndi: UIView!
    @IBOutlet weak var viewBtn: UIView!
    @IBOutlet weak var btnShare: UIButton!


    var piechart = PieChartView()
    var barchart = BarChartView()
    var barchartGeneral = BarChartView()

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

    //Usuarios subordinados
    var arrCantTareasSubordinados = [Any]()

    // adaptadores
    let adaptadorModal = Adaptador_Modals()
    var adaptadorServicios = AdaptadorServicios()

    //Variables del tipo de gráfico
    var graficoMensajes = false

    // arreglo cantidad de mensajes broadcast
    var mensajesBroad:[Any]?
    var cantidad_mensajes_broad = [Int]()
    var cantidad_mensajes_broad_usu = [Int]()

    // variable para cambiar fondo de lista al seleccionar
    var seleccionado = 0

    var tipoUsuario = false
    var usuarios_cantidades = [Any]()
    var usuarios_cantidades_broad = [Any]()

    let shapeLayer = CAShapeLayer()
    var posIniViewI:Double?
    var posIniViewB:Double?

    override func viewDidLoad() {
        super.viewDidLoad()

        hideNavBar()
        addLogoutButton()
        //Ejecutar los servicios web
        //ejecucionServicios()

        //Configuraciones
        configuraciones()
        configuracionPosIndicadores()

    }

    override func viewDidAppear(_ animated: Bool) {
        ejecucionServicios()
        configuracion_cantidades()
        optionstAB.selectedSegmentIndex = 0
        configuracion_etiquetasPieMensajes()
        ocultar_etiquetas(tipo: false)
    }


    func configuracionPosIndicadores(){
        posIniViewI = viewIndi.frame.origin.x
        posIniViewB = viewBtn.frame.origin.x
    }

    func esconderBoton(SiNo: Bool){

            if SiNo == true{

                viewBtn.isHidden = SiNo
                let centerPos = viewBtn.frame.width / 1.5
                viewIndi.frame.origin.x = posIniViewB! + centerPos
            }else if SiNo == false{

                viewBtn.isHidden = SiNo
                viewIndi.frame.origin.x = posIniViewI!

            }

        }

    // Funcion para ejecutar servicios
    func ejecucionServicios(){
        
        //Validar si el usuario no cuenta con subordinados
       
        //serviciosUsuarios()
        
        animacion_espera()
        //serviciosMensajes(idUsuario: userID)
        //serviciosTareas(idUsuario: userID)
        //serviciosBroadcast(idUsuario: userID)
    }


    // Animacion espera

    func animacion_espera() {

        view.isUserInteractionEnabled = false

        let center = view.center
                let trackLayer = CAShapeLayer()
                let circularPath = UIBezierPath(arcCenter: center, radius: 100, startAngle: -CGFloat.pi / 2, endAngle: 2 * CGFloat.pi, clockwise: true)
                trackLayer.path = circularPath.cgPath

                trackLayer.strokeColor = Hexadecimal_Color(hex: "2EC1B3").cgColor
                trackLayer.lineWidth = 10
                trackLayer.fillColor = UIColor.clear.cgColor
                trackLayer.lineCap = CAShapeLayerLineCap.round
                shapeLayer.path = circularPath.cgPath
                //shapeLayer.strokeColor = UIColor.red.cgColor

                shapeLayer.strokeColor = Hexadecimal_Color(hex: "2EC1B3").cgColor
                shapeLayer.lineWidth = 10
                shapeLayer.fillColor = UIColor.clear.cgColor
                shapeLayer.lineCap = CAShapeLayerLineCap.round
                shapeLayer.strokeEnd = 0

                view.layer.addSublayer(shapeLayer)

                let basicAnimation = CABasicAnimation(keyPath: "strokeEnd")
                basicAnimation.toValue = 1
                basicAnimation.duration = 1.5
                basicAnimation.repeatCount = Float.infinity
                basicAnimation.fillMode = CAMediaTimingFillMode.forwards
                basicAnimation.isRemovedOnCompletion = true
                basicAnimation.delegate = self
                shapeLayer.add(basicAnimation, forKey: "urSoBasic")    }


    func animationDidStart(_ anim: CAAnimation) {
        //configurar_pdf_visro()
        serviciosUsuarios()
    }


    // Funciones de servicioes web --->
    func serviciosMensajes(idUsuario: String) {
        adaptadorServicios.servicioWebMensajesAdapter(idUsuario: idUsuario) {
                [self] (Datos) -> Void in
            mensajes = Datos
            cantidad_mensajes = cantidadDeMensajes(mensaje: mensajes! as! [Mensajes], idUsuario: idUsuario)
            
            shapeLayer.removeAllAnimations()
            view.isUserInteractionEnabled = true
            
            llenar_pie_chart(mensajes: cantidad_mensajes)
        }
    }

    func serviciosMensajesPorLider(idUsuario: String, nombre: String) {
        adaptadorServicios.servicioWebMensajesAdapter(idUsuario: idUsuario) {
                [self] (Datos) -> Void in
            mensajes = Datos
            cantidad_mensajes = cantidadDeMensajes(mensaje: mensajes! as! [Mensajes], cantidades: cantidad_mensajes, idUsuario: idUsuario)

            usuarios_cantidades.append(cantidadDeMensajesUsuario(mensaje: mensajes! as! [Mensajes], idUsuario: idUsuario, nombre: nombre))

            shapeLayer.removeAllAnimations()
            view.isUserInteractionEnabled = true
            llenar_pie_chart(mensajes: cantidad_mensajes)
        }
    }

    func serviciosTareasLider(nombre:String, idReceptor:String){
            adaptadorServicios.servicioWebTareasAdapterByBoss(idReceptor: idReceptor) {
                [self] (Datos) -> Void in

                arrTareas = Datos

                //Cantidad de tareas
                arrCantidadDeTareas = cantidaDeTareasUsuarios(tareas: arrTareas! as! [Tareas], arrCantidadTareas: arrCantidadDeTareas, idUsuario: idReceptor)

                arrCantTareasSubordinados.append(cantidadDeTareasUsuarios(tareas: arrTareas! as! [Tareas], idUsuario: idReceptor, nombre: nombre))

            }
        }
            
        func serviciosTareas(idUsuario: String){
            adaptadorServicios.servicioWebTareasAdapter(idUsuario: idUsuario){
                [self] (Datos) -> Void in
                arrTareas = Datos

                if arrTareas == nil {
                    arrCantidadDeTareas = [0,0,0,0,0,0]
                }else{
                    arrCantidadDeTareas = cantidadDeTareas(tareas: arrTareas! as! [Tareas], idUsuario: idUsuario)
                }
                //Cantidad de tareas

            }
        }

    
    func serviciosUsuarios() {
            adaptadorServicios.serviciosWeb(idUsuario: userID) {
                [self] (Datos) -> Void in
                
                    arrUsuarios = Datos

                    if arrUsuarios!.count > 0 {
                        configura_label_usuario(nombre: "Mi equipo")

                        if let lista_usuarios = (arrUsuarios as? [Usuario]) {
                           
                            usuarios_cantidades = [Any]()
                            usuarios_cantidades_broad = [Any]()
                            for i in lista_usuarios {
                                serviciosMensajesPorLider(idUsuario: i.id, nombre: i.nombre)
                                serviciosBroadcastPorLider(idUsuario: i.id, nombre: i.nombre)
                                serviciosTareasLider(nombre: i.nombre, idReceptor:i.id)

                            }
                        }
                    } else {
                        esconderBoton(SiNo: true)
                        serviciosBroadcast(idUsuario: userID)
                        serviciosMensajes(idUsuario: userID)
                        configura_label_usuario(nombre: userName)
                        serviciosTareas(idUsuario: userID)
                    }
                
                
            }
            
        
        
        
    }

    func serviciosUsuariosPorFecha(filtros: [String]) {
        if let lista_usuarios = (arrUsuarios as? [Usuario]){
            usuarios_cantidades = [Any]()
            usuarios_cantidades_broad = [Any]()
            
            for i in lista_usuarios {
                serviciosMensajesPorLiderFiltrado(filtros: filtros, idUsuario: i.id, nombre: i.nombre)

                servicioTareasUsuariosFiltradoFechas(filtro: filtros, idUsuario: i.id , nombre: i.nombre, token: i.tokenAuth!)

            }
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

    func serviciosBroadcastPorLider(idUsuario: String, nombre: String) {
        adaptadorServicios.servicioWebBroadcastAdapter(idUsuario: idUsuario) {
            [self] (Datos) -> Void in
            mensajesBroad = Datos
            cantidad_mensajes_broad = cantidadDeBroad(mensaje_broad: mensajesBroad! as! [Broadcast], cantidades: cantidad_mensajes_broad, idUsuario:
                                                     idUsuario)
            cantidad_mensajes_broad_usu = cantidadDeBroad(mensaje_broad: mensajesBroad! as! [Broadcast], idUsuario: idUsuario)
            serviciosBroadcastRecibidosLider(idUsuario: idUsuario)
            usuarios_cantidades_broad.append([nombre, cantidad_mensajes_broad_usu[0], cantidad_mensajes_broad_usu[1]])

        }
    }

    func serviciosBroadcastRecibidos(idUsuario: String) {
        adaptadorServicios.servicioWebMensajesAdapter(idUsuario: userBroadcastID) {
                [self] (Datos) -> Void in
            mensajes = Datos
            let recibidos = cantidadBroadRecibidos(mensajes: mensajes! as! [Mensajes], cantidades: cantidad_mensajes_broad, idUsuario: idUsuario)

            cantidad_mensajes_broad[1] = recibidos
            actualizar_datos_lista_grafica(mensajes: cantidad_mensajes, broadcast: cantidad_mensajes_broad)
        }
    }

    func serviciosBroadcastRecibidosLider(idUsuario: String) {
        adaptadorServicios.servicioWebMensajesAdapter(idUsuario: userBroadcastID) {
                [self] (Datos) -> Void in
            mensajes = Datos
            let recibidos = cantidadBroadRecibidos(mensajes: mensajes! as! [Mensajes], cantidades: cantidad_mensajes_broad, idUsuario: idUsuario)

            let recibidosUsu =  cantidadBroadRecibidos(mensajes: mensajes! as! [Mensajes], idUsuario: idUsuario)

            cantidad_mensajes_broad_usu[1] = recibidosUsu
            cantidad_mensajes_broad[1] = recibidos
            actualizar_datos_lista_grafica(mensajes: cantidad_mensajes, broadcast: cantidad_mensajes_broad)
        }
    }

    func serviciosTareasFiltrado(filtros: [String]) {
            adaptadorServicios.servicioWebTareasAdapter(idUsuario: filtros[2]){
                [self] (Datos) -> Void in
                arrTareas = Datos
                arrCantidadDeTareas = cantidadDeTareasPorFecha(tareas: arrTareas! as! [Tareas], idUsuario: filtros[2], fechaInicio: filtros[0], fechaFin: filtros[1])
                //print("-------------------------\(arrCantidadDeTareas)")
                //llenar_pie_chartTareas(tareas: arrCantidadDeTareas)
            }
        }

    func servicioTareasUsuariosFiltradoFechas(filtro:[String], idUsuario:String, nombre:String, token: String){
            adaptadorServicios.servicioWebTareasAdapter(idUsuario: idUsuario){
                [self] (Datos) -> Void in

                arrTareas = Datos
                arrCantidadDeTareas = cantidadDeTareasPorFecha(tareas: arrTareas as! [Tareas], idUsuario: idUsuario, fechaInicio: filtro[0], fechaFin: filtro[1])

                arrCantTareasSubordinados.append(cantidadDeTareasTodosUsuariosPorFecha(tareas: arrTareas as! [Tareas], idUsuario: idUsuario, nombre: nombre, fechaInicio: filtro[0], fechaFin: filtro[1]))
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

    func serviciosMensajesPorLiderFiltrado(filtros: [String], idUsuario: String, nombre: String) {
        adaptadorServicios.servicioWebMensajesAdapter(idUsuario: idUsuario) {
                [self] (Datos) -> Void in
            mensajes = Datos
            cantidad_mensajes = cantidadDeMensajesPorFecha(mensaje: mensajes! as! [Mensajes], cantidades: cantidad_mensajes, idUsuario: idUsuario, fechaIni: filtros[0], fechaFin: filtros[1])

            //(mensaje: mensajes! as! [Mensajes], cantidades: cantidad_mensajes, idUsuario: idUsuario)

            usuarios_cantidades.append(cantidadDeMensajesPorFechaUsuario(mensaje: mensajes! as! [Mensajes], idUsuario: idUsuario, nombre: nombre, fechaIni: filtros[0], fechaFin: filtros[1]))

            llenar_pie_chart(mensajes: cantidad_mensajes)
        }

    }
    // Fin funciones de servicioes web <---


    // Confiuraciones de graficos --->
    func configuraciones(){
        //      MENSAJES
        //Configuraciones de gráfico de pastel de mensajes
        configurar_pie_chart()
        //configurar los gráficos de barras de mensajes
        configurar_bar_chart()
        configurar_bar_chart_general()

        //      LISTA
        //Configurar lista
        configurar_lista()
        //Datos de la lista
        configuracion_cantidades()
        //      GRÁFICAS
        //configurar los colores de los elementos a representar de los gráficos
        configuracion_colores()
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
        barchart.isUserInteractionEnabled = true
    }

    func configurar_bar_chart_general() {
        barchartGeneral = BarChartView(frame: CGRect(x: 0, y: 0, width: viewChart.frame.size.width, height: viewChart.frame.size.height))
        barchartGeneral.legend.enabled = false
        //barchartGeneral.isUserInteractionEnabled = true
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

        //datos = [["ic_PieChart", 0, 0, "pie"], ["ic_Bar", 0, 0, "bar"]]
        cantidad_mensajes = [0,0,0]
        cantidad_mensajes_broad = [0,0]
        cantidad_mensajes_broad_usu = [0,0]
    } // --->


    // Lenado de graficas pie --->
    // 0:envidos 1:recibidos 2:leidos
    func llenar_pie_chart(mensajes: [Int]) {

        if !barchart.isEmpty(){
            barchart.removeFromSuperview()
        }

        if !barchartGeneral.isEmpty(){
            barchartGeneral.removeFromSuperview()
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

        if !barchartGeneral.isEmpty() {
            barchartGeneral.removeFromSuperview()
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

        if !piechart.isEmpty(){
            barchart.removeFromSuperview()
        }

        if !barchartGeneral.isEmpty(){
            barchartGeneral.removeFromSuperview()
        }

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

            barchart.noDataText = "No hay información"
            viewChart.addSubview(barchart)
            cargar_animacion_bar()

        }else{

            print("El arreglo de tareas está vacio")
        }
    }

    func llenar_bar_chart(datos: [Any]?) {
        if !piechart.isEmpty(){
            barchart.removeFromSuperview()
        }

        if !barchartGeneral.isEmpty(){
            barchartGeneral.removeFromSuperview()
        }

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

    func llenar_bar_chart_general(tipo: String, datos: [Any]) {

        let arrGeneral = datos

        if !piechart.isEmpty(){
            piechart.removeFromSuperview()
        }

        if !barchart.isEmpty(){
            barchart.removeFromSuperview()
        }

        // Inicia arreglos de valores
        var nombreUsuarios = [String]()
        var cantEnviados = [Int]()
        var cantRecibidos = [Int]()
        var cantLeidos = [Int]()

        // llena arreglos de acuerdo a las cantidades
        for i in datos {
            let dato = i as! [Any]
            nombreUsuarios.append(dato[0] as! String)
            cantEnviados.append(dato[1] as! Int)
            cantRecibidos.append(dato[2] as! Int)

            if tipo == "mensajes" {
                cantLeidos.append(dato[3] as! Int)
            }
        }

        barchartGeneral.noDataText = "No hay información"
        var enviados: [BarChartDataEntry] = []
        var recibidos: [BarChartDataEntry] = []
        var leidos: [BarChartDataEntry] = []

        for i in 0..<nombreUsuarios.count {
            let dataEnviados = BarChartDataEntry(x: Double(i), y: Double(cantEnviados[i]))
                    enviados.append(dataEnviados)

            let dataRecibidos = BarChartDataEntry(x: Double(i), y: Double(cantRecibidos[i]))
                    recibidos.append(dataRecibidos)

            if tipo == "mensajes" {
                let dataLeidos = BarChartDataEntry(x: Double(i), y: Double(cantLeidos[i]))
                        leidos.append(dataLeidos)
            }
        }

        let chartDataEnviados = BarChartDataSet(entries: enviados)
        let chartDataRecibidos = BarChartDataSet(entries: recibidos)
        let chartDataLeidos = BarChartDataSet(entries: leidos)

        var dataSets = [BarChartDataSet]()

        if tipo == "mensajes" {
            dataSets = [chartDataEnviados,chartDataRecibidos,chartDataLeidos]
        } else if tipo == "broadcast" {
            dataSets = [chartDataEnviados,chartDataRecibidos]
        }

        chartDataEnviados.colors = [Hexadecimal_Color(hex: "66BB6A")]
        chartDataRecibidos.colors = [Hexadecimal_Color(hex: "87D169")]
        chartDataLeidos.colors = [Hexadecimal_Color(hex: "7F8182")]

        let chartData = BarChartData(dataSets: dataSets)

        var groupSpace = 0.0
        var barSpace = 0.0
        var barWidth = 0.0

        if tipo == "mensajes" {
            groupSpace = 0.16
            barSpace = 0.03
            barWidth = 0.25
        } else if tipo == "broadcast" {
            groupSpace = 0.10
            barSpace = 0.02
            barWidth = 0.10
        }

        let groupCount = arrGeneral.count
        let inicio = 0

        chartData.barWidth = barWidth;
        barchartGeneral.xAxis.axisMinimum = Double(inicio)
        let gg = chartData.groupWidth(groupSpace: groupSpace, barSpace: barSpace)
        barchartGeneral.xAxis.axisMaximum = Double(inicio) + gg * Double(groupCount)

        chartData.groupBars(fromX: Double(inicio), groupSpace: groupSpace, barSpace: barSpace)

        barchartGeneral.notifyDataSetChanged()
        barchartGeneral.data = chartData

        if tipo == "mensajes" {
            barchartGeneral.setVisibleXRangeMaximum(3.0)
        } else if tipo == "broadcast" {
            barchartGeneral.setVisibleXRangeMaximum(2.0)
        }

        barchartGeneral.doubleTapToZoomEnabled = false

        let xaxis = barchartGeneral.xAxis
        xaxis.labelPosition = .bottom
        xaxis.centerAxisLabelsEnabled = true
        xaxis.valueFormatter = IndexAxisValueFormatter(values:nombreUsuarios)

        xaxis.granularity = 1

        /*if tipo == "mensajes" {
            xaxis.granularity = 1
        } else if tipo == "broadcast" {
            xaxis.granularity = 2
        }*/

        print(nombreUsuarios)

        viewChart.addSubview(barchartGeneral)
        barchartGeneral.animate(yAxisDuration: 0.8, easingOption: ChartEasingOption.easeInOutQuad)
    }

    func llenar_bar_chart_general_Tareas(tipo: String, datos: [Any]){

            if !piechart.isEmpty(){
                piechart.removeFromSuperview()
            }

            if !barchart.isEmpty(){
                barchart.removeFromSuperview()
            }


            let arrGeneral = datos

            var arrNombres = [String]()
            var arrPendientes = [Int]()
            var arrIniciados = [Int]()
            var arrRevisado = [Int]()
            var arrTerminados = [Int]()

            var arrTerminadosATiempo = [Int]()
            var arrTerminadosADestiempo = [Int]()

            for i in datos{

                let dato = i as! [Any]

                arrNombres.append(dato[0] as! String)
                arrPendientes.append(dato[1] as! Int)
                arrIniciados.append(dato[2] as! Int)
                arrRevisado.append(dato[3] as! Int)
                arrTerminados.append(dato[4] as! Int)

                if tipo.contains("atiempo"){
                    arrTerminadosATiempo.append(dato[5] as! Int)
                    arrTerminadosADestiempo.append(dato[6] as! Int)
                }

            }

            var datEntPend:[BarChartDataEntry] = []
            var datEntInic:[BarChartDataEntry] = []
            var datEntRev:[BarChartDataEntry] = []
            var datEntTer:[BarChartDataEntry] = []
            var datEntATiempo:[BarChartDataEntry] = []
            var datEntDesTiempo:[BarChartDataEntry] = []

            for i in 0..<arrNombres.count{

                let dataPend = BarChartDataEntry(x: Double(i), y: Double(arrPendientes[i]))
                datEntPend.append(dataPend)

                let dataInic = BarChartDataEntry(x: Double(i), y: Double(arrIniciados[i]))
                datEntInic.append(dataInic)

                let dataRev = BarChartDataEntry(x: Double(i), y: Double(arrRevisado[i]))
                datEntRev.append(dataRev)

                let dataTerm = BarChartDataEntry(x: Double(i), y: Double(arrTerminados[i]))
                datEntTer.append(dataTerm)

                if tipo.contains("atiempo"){
                    let dataATiempo = BarChartDataEntry(x: Double(i), y: Double(arrTerminadosATiempo[i]))
                    datEntATiempo.append(dataATiempo)
                    let dataADesTiempo = BarChartDataEntry(x: Double(i), y: Double(arrTerminadosADestiempo[i]))
                    datEntDesTiempo.append(dataADesTiempo)
                }
            }

            let chartDataPend = BarChartDataSet(entries: datEntPend)
            let chartDataIni = BarChartDataSet(entries: datEntInic)
            let chartDataRev = BarChartDataSet(entries: datEntRev)
            let chartDataTerm = BarChartDataSet(entries: datEntTer)
            let chartDataATiempo = BarChartDataSet(entries: datEntATiempo)
            let chartDataDesTiempo = BarChartDataSet(entries: datEntDesTiempo)

            var dataSet = [BarChartDataSet]()
            if tipo == "tareas"{
                dataSet = [chartDataPend, chartDataIni, chartDataRev, chartDataTerm]
            }else if tipo.contains("atiempo"){
                dataSet = [chartDataATiempo, chartDataDesTiempo]
            }


            chartDataPend.colors = [Hexadecimal_Color(hex: "66BB6A")]
            chartDataIni.colors = [Hexadecimal_Color(hex: "87D169")]
            chartDataRev.colors = [Hexadecimal_Color(hex: "66877F")]
            chartDataTerm.colors = [Hexadecimal_Color(hex: "7F8182")]

            let chartData = BarChartData(dataSets: dataSet)

            var groupSpace = 0.0
            var barSpace = 0.0
            var barWidth = 0.0

            if tipo == "tareas"{
                groupSpace = 0.16
                barSpace = 0.03
                barWidth = 0.25

            }else if tipo == "atiempo"{
                groupSpace = 0.16
                barSpace = 0.03
                barWidth = 0.25
            }


            let groupCount = arrGeneral.count

            chartData.barWidth = barWidth
            barchartGeneral.xAxis.axisMinimum = Double(0)
            let gg = chartData.groupWidth(groupSpace: groupSpace, barSpace: barSpace)
            barchartGeneral.xAxis.axisMaximum = Double(gg) * Double(groupCount)

            chartData.groupBars(fromX: Double(0), groupSpace: groupSpace, barSpace: barSpace)
            barchartGeneral.notifyDataSetChanged()

            barchartGeneral.data = chartData

            barchartGeneral.setVisibleXRangeMaximum(3.0)

            barchartGeneral.doubleTapToZoomEnabled = false

            let xaxis = barchartGeneral.xAxis
            xaxis.labelPosition = .bottom
            xaxis.centerAxisLabelsEnabled = true
            xaxis.valueFormatter = IndexAxisValueFormatter(values: arrNombres)
            xaxis.granularity = 1
            viewChart.addSubview(barchartGeneral)
            barchartGeneral.animate(yAxisDuration: 0.8, easingOption:ChartEasingOption.easeInQuad)

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
        if arrUsuarios == nil{
            print("El arreglo de los usuarios está vacio")
            arrUsuarios = ["SinUsuario"]
        }
        let modal_form = adaptadorModal.crear_modal_funcion(datos: arrUsuarios!, Accion_Confirmacion_completion: {
            [self](Filtro) -> Void in


            if Filtro.count > 3 {

                if Filtro[3] as! String == "Mi equipo" {
                    configuracion_cantidades()
                    lblNombreu.text = Filtro[3] as! String
                    serviciosUsuariosPorFecha(filtros: Filtro as! [String])
                    esconderBoton(SiNo: false)
                } else {
                    esconderBoton(SiNo: true)
                    serviciosMensajes(idUsuario: Filtro[0] as! String)
                    configuracion_cantidades()
                    serviciosMensajesFiltrado(filtros: Filtro as! [String])
                    serviciosTareasFiltrado(filtros: Filtro as! [String])
                    serviciosBroadcast(idUsuario: Filtro[2] as! String)
                    lblNombreu.text = (Filtro[3] as! String)
                }
            } else if Filtro.count <= 3 {

                if Filtro[1] as! String == "Mi equipo" {
                    configuracion_cantidades()
                    serviciosUsuarios()
                    esconderBoton(SiNo: false)
                } else {
                    configuracion_cantidades()
                    esconderBoton(SiNo: true)
                    serviciosMensajes(idUsuario: Filtro[0] as! String)
                    serviciosTareas(idUsuario: Filtro[0] as! String)
                    serviciosBroadcast(idUsuario: Filtro[0] as! String)
                    lblNombreu.text = (Filtro[1] as! String)
                }

            }

            //Regresar al menu de mensajes
            optionstAB.selectedSegmentIndex = 0
            llenar_pie_chart(mensajes: cantidad_mensajes)
            configuracion_etiquetasPieMensajes()
            ocultar_etiquetas(tipo: false)
            graficoMensajes = true

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


    // funcion boton mostrar graficas de usuarios
    @IBAction func btnMostrarGeneral(_ sender: Any) {
        if optionstAB.selectedSegmentIndex == 0 {
            if seleccionado == 0 {
                llenar_bar_chart_general(tipo: "mensajes", datos: usuarios_cantidades)
                //llenar_bar_chart_general(tipo: "broadcast", datos: usuarios_cantidades_broad)
            } else if seleccionado == 1 {
                llenar_bar_chart_general(tipo: "broadcast", datos: usuarios_cantidades_broad)
            }
        }else if optionstAB.selectedSegmentIndex == 1{
            if seleccionado == 0{
                llenar_bar_chart_general_Tareas(tipo: "tareas", datos:arrCantTareasSubordinados)
            }else{
                print("Tareas Culminadas a tiempo")
                llenar_bar_chart_general_Tareas(tipo: "atiempo", datos:arrCantTareasSubordinados)
            }

        }
    }

    @IBAction func btnCaptura(_ sender: Any) {
        compartir_pantalla()
    }

}
