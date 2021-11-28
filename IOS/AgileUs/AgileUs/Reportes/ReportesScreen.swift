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
    
    // arreglo de usuarios por lider
    var arrUsuarios:[Any]?
    var idUsuario:String?
    
    // arreglo de cantidad de mensajes
    var arrMensajes:[Any]?
    var cantidad_mensajes = [Int]()
    
    //arreglo de cantidad de tareas
    var arrTareas:[Any]?
    var arrCantidadDeTareas = [Int]()
    
    // adaptadores
    let adaptadorModal = Adaptador_Modals()
    var adaptadorServicios = AdaptadorServicios()
    
    //Variables del tipo de gráfico
    var graficoMensajes = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //Definir primeros datos de los elementos de la lista
        arrDatosLista = [["ic_PieChart", 0, 0, "pieM"], ["ic_Bar", 0, 0, "barM"]]
        
        //CONFIGURACIONES
        
        //Configurar los gráficos circulares
        configurar_pie_chart()
        
        //configurar los gráficos de barras
        configurar_bar_chart()
        
        //Configurar los elementos de la lista
        configurar_lista()
        
        //configurar los colores de los elementos a representar de los gráficos
        configuracion_colores()
        
        //LLenar los gráficos de barras
        llenar_pie_chartMensajes(mensajes: cantidad_mensajes)
        //llenar_pie_chartTareas(tareas: arrCantidadDeTareas)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        //Ejecutar los servicios web antes de cargar la pantalla principal
        serviciosUsuarios()
        serviciosTareas()
        serviciosMensajes()
    }
    
    //  SERVICIOS WEB       <--------------------------
    
    func serviciosUsuarios() {
        
        let _ = adaptadorServicios.serviciosWeb {
            [self] (Datos) -> Void in
            arrUsuarios = Datos
        }
    }
    
    func serviciosMensajes() {

        //let mensaje = MensajesService()
        
        let _ = adaptadorServicios.servicioWebMensajesAdapter {
                [self] (Datos) -> Void in

            arrMensajes = Datos
            cantidad_mensajes = cantidadDeMensajes(mensaje: arrMensajes! as! [Mensajes], idUsuario: "618e8821c613329636a769ac")
            llenar_pie_chartMensajes(mensajes: cantidad_mensajes)
        }
    }
    
    func serviciosTareas(){
        let _ = adaptadorServicios.servicioWebTareasAdapter{
            [self] (Datos) -> Void in
            
            arrTareas = Datos
            arrCantidadDeTareas = cantidadDeTareas(tareas: arrTareas! as! [Tareas], idUsuario: "EMIS5")
            llenar_pie_chartTareas(tareas: arrCantidadDeTareas)
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
        
        if sender.selectedSegmentIndex == 0 {
            llenar_pie_chartMensajes(mensajes: cantidad_mensajes)
            
            graficoMensajes = true
        }
        
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
            
            //Gráficar los datos que se están asignando
            piechart.data = chartdata
            piechart.contentMode = .scaleToFill
            
            viewChart.addSubview(piechart)
            cargar_animacion_pie()
            
            //
            actualizar_datos_lista_grafica(tareasCompletadas: Double(tareas[3]), tareasPendientes: Double(tareas[0]))
        }
        
    }
     
        
    //      Gráfico de Pastel de Mensajes
    
    func llenar_pie_chartMensajes(mensajes: [Int]) {
        
        //Remover el gráfico de barras de la vista
        if !barchart.isEmpty(){
            barchart.removeFromSuperview()
        }
        
        //Comprobar que el arreglo de datos que se va a interpretar no esté vacio
        if mensajes.isEmpty{
            print("No hay datos para mostrar MENSAJES")
        }else{

            configuracion_etiquetasPieMensajes()
            
            cantEnviados.text = "\(mensajes[0])"
            cantRecibidos.text = "\(mensajes[1])"
            cantLeidos.text = "\(mensajes[2])"
            cantTotales.text = "\(mensajes[0] + mensajes[1] + mensajes[2])"
            
            let enviados = PieChartDataEntry(value: Double(mensajes[0]))
            let recibidos = PieChartDataEntry(value: Double(mensajes[1]))
            let leidos = PieChartDataEntry(value: Double(mensajes[2]))
            
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
            
            //  No sé que hace                                  <---------------------------------------------------------
            actualizar_datos_lista_grafica(enviado: Double(mensajes[0]), recibido: Double(mensajes[1]))
            

        }
        
    }
    
    
    // LLENAR INFORMACION DE GRÁFICOS DE BARRAS
    
    //      Gráficos de barras de Mensajes
    
    func llenar_bar_chartMensajes(datos: [Any]?) {
        
        let leidos = BarChartDataEntry(x: 1, y: Double("\(datos![0])")!)
        let contestados = BarChartDataEntry(x: 2, y: Double("\(datos![1])")!)
        let entries = [leidos, contestados]
        
        let dataset = BarChartDataSet(entries: entries)
        let chardata = BarChartData(dataSet: dataset)
        barchart.data = chardata
        
        let colors = [Hexadecimal_Color(hex: "66BB6A"), Hexadecimal_Color(hex: "87D169")]
        dataset.colors = colors
        viewChart.addSubview(barchart)
        cargar_animacion_bar()
    }
    
    //      Gráficos de barras de tareas
    
    func llenar_bar_chartTareas(arrDatosT: [Any]){
        
        //Comprobar que el arreglo no esté vacio
        if !arrDatosT.isEmpty{
            let tareasCompletadas = BarChartDataEntry(x: 1, y: Double("\(arrDatosT[3])")!)
            //Por definir este campo                        <---------------------------------------------------------
            let tareasPendientes = BarChartDataEntry(x: 2, y: Double("\(arrDatosT[0])")!)
            
            let entrie = [tareasCompletadas, tareasPendientes]
            
            let dataSet = BarChartDataSet(entries: entrie)
            let chartData = BarChartData(dataSet: dataSet)
            barchart.data = chartData
            
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
    
    func actualizar_datos_lista_grafica(enviado: Double?, recibido: Double?) {
        
        print("Datos de la listá MENSAJES")
        
        if enviado == nil || recibido == nil {
            arrDatosLista = [["ic_PieChart", 0, 0, "pieM"], ["ic_Bar", 0, 0, "barM"]]
        } else {
            arrDatosLista = [["ic_PieChart", enviado!, recibido!, "pieM"], ["ic_Bar", enviado!, recibido!, "barM"]]
        }
        //datos = [["ic_PieChart", enviado!, recibido!, "pie"], ["ic_Bar", enviado!, recibido!, "bar"]]
        opcionesGrafica.reloadData()
    }
    
    
    //  ACTUALIZAR DATOS DE LAS GRÁFICAS DE TAREAS
    
    func actualizar_datos_lista_grafica(tareasCompletadas: Double?, tareasPendientes: Double?) {
        
        print("Datos de la listá TAREAS")
        
        if tareasCompletadas == nil || tareasPendientes == nil {
            arrDatosLista = [["ic_PieChart", 0, 0, "pieT"], ["ic_Bar", 0, 0, "barT"]]
        } else {
            arrDatosLista = [["ic_PieChart", tareasCompletadas!, tareasPendientes!, "pieT"], ["ic_Bar", tareasCompletadas!, tareasPendientes!, "barT"]]
            
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
        
        print("cell For Row At")
        
        let indice = indexPath.row
        print(indice)
        
        let celda_personalizada = tableView.dequeueReusableCell(withIdentifier: ListaGrafica.identificador, for: indexPath) as! ListaGrafica
        
        //Cambiar las etiquetas de la lista
        celda_personalizada.configurar_celda(datos: arrDatosLista[indice] as! [Any])
        print("Datos ls")
        print(arrDatosLista)
        return celda_personalizada
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if indexPath.row == 0 {
            
            //Comprobar que gráfico es el que está seleccionado
            barchart.removeFromSuperview()
            
            if graficoMensajes == true{
                
                llenar_pie_chartMensajes(mensajes: cantidad_mensajes)
                ocultar_etiquetas(tipo: false)
                
            }else{
                llenar_pie_chartTareas(tareas: arrCantidadDeTareas)
                ocultar_etiquetas(tipo: false)
            }
            
            
        } else if (indexPath.row == 1) {
            
            //Comprobar que gráfico es el que está seleccionado
            
            piechart.removeFromSuperview()
            
            if graficoMensajes == true{
                llenar_bar_chartMensajes(datos: [34, 60])
                ocultar_etiquetas(tipo: true)
                datos_bar_chart(datos: [34, 60])
            }else{
                
                llenar_bar_chartTareas(arrDatosT: arrCantidadDeTareas)
                
            }
        }
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
        lblTiempoLeido.text = "Tareas Completadas"
        lblTiempoRes.text = "Tareas Pendientes"
        cantLeidos.text = "\(arrCantTareas[3])"
        cantRecibidos.text = "\(arrCantTareas[0])"
    }
    
    //                  GRÁFICA DE BARRAS
    func configuracion_etiquetasPieTareas(){
        lblEnviados.text = "Pendientes"
        lblRecibidos.text = "Iniciadas"
        lblTotales.text = "Revisión"
        lblLeidos.text = "Terminadas"
    }
    
    func ocultar_etiquetas(tipo: Bool) {
        lblTotales.isHidden = tipo
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
        lblTiempoLeido.text = "Tiempo de lectura promedio"
        lblTiempoRes.text = "Tiempo de respuesta promedio"
        cantLeidos.text = "\(datos[0])"
        cantRecibidos.text = "\(datos[1])"
        
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
            [self](Datos) -> Void in
              
            lblNombreu.text = (Datos[3] as! String)
        })
        
        present(modal_form, animated: true)
    
    }
}
