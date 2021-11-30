//
//  ViewController.swift
//  ejemploCharts
//
//  Created by Luis Gregorio Ramirez Villalobos on 17/11/21.
//

import UIKit
import Charts
import simd

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
    var datos = [Any]()
    
    // arreglo de usuarios por lider
    var usuarios:[Any]?
    var idUsuario:String?
    
    // arreglo de cantidad de mensajes
    var mensajes:[Any]?
    var cantidad_mensajes = [Int]()
    
    // arreglo cantidad de mensajes broadcast
    var mensajesBroad:[Any]?
    var cantidad_mensajes_broad = [Int]()
    
    // adaptdores
    let adaptador = Adaptador_Modals()
    var adaptadorServicios = AdaptadorServicios()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configuracion_cantidades()
        serviciosMensajes(idUsuario: userID)
        serviciosUsuarios()
        //serviciosBroadcast()
        configurar_pie_chart()
        configurar_lista()
        configurar_bar_chart()
        configuracion_colores()
        configura_label_usuario(nombre: userName)
    }
    
    func configura_label_usuario(nombre: String) {
        lblNombreu.text = nombre
    }
    
    func configuracion_cantidades() {
        datos = [["ic_PieChart", 0, 0, "pie"], ["ic_Bar", 0, 0, "bar"]]
        cantidad_mensajes = [0,0,0]
        cantidad_mensajes_broad = [0,0]
    }
    
    func serviciosUsuarios() {
        adaptadorServicios.serviciosWeb(idUsuario: userID) {
            [self] (Datos) -> Void in
            usuarios = Datos
        }
    }
    
    func serviciosMensajes(idUsuario: String) {
        adaptadorServicios.servicioWebMensajesAdapter(idUsuario: idUsuario) {
                [self] (Datos) -> Void in
            mensajes = Datos
            cantidad_mensajes = cantidadDeMensajes(mensaje: mensajes! as! [Mensajes], idUsuario: userID)
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
    
    func serviciosBroadcast() {
        adaptadorServicios.servicioWebBroadcastAdapter(idUsuario: userID) {
            [self] (Datos) -> Void in
            
            mensajesBroad = Datos
            cantidad_mensajes_broad = cantidadDeBroad(mensaje_broad: mensajesBroad! as! [Broadcast], idUsuario:
                                                     userID)
            actualizar_datos_lista_grafica(mensajes: cantidad_mensajes, broadcast: cantidad_mensajes_broad)
        }
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
        
    @IBAction func cambio(_ sender: UISegmentedControl) {
        
        // pie chart mensajes
        if sender.selectedSegmentIndex == 0 {
            llenar_pie_chart(mensajes: cantidad_mensajes)
        }
        
        // pie chart tareas
        if sender.selectedSegmentIndex == 1 {
            llenar_pie_chart(mensajes: [10,10,10])
        }
        
    }
        
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
    
    func cargar_animacion_pie() {
        piechart.animate(yAxisDuration: 1, easingOption: ChartEasingOption.easeInOutQuad)
    }
    
    func cargar_animacion_bar() {
        barchart.animate(yAxisDuration: 0.8, easingOption: ChartEasingOption.easeInOutQuad)
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
    
    func actualizar_datos_lista_grafica(mensajes: [Int], broadcast: [Int]) {
        datos = [["ic_PieChart", mensajes[0], mensajes[1], "pie"], ["ic_Bar", cantidad_mensajes_broad[1], cantidad_mensajes_broad[0], "bar"]]
        
        opcionesGrafica.reloadData()
    }
    
    // funciones table view
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return datos.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let indice = indexPath.row
        let celda_personalizada = tableView.dequeueReusableCell(withIdentifier: ListaGrafica.identificador, for: indexPath) as! ListaGrafica
        celda_personalizada.configurar_celda(datos: datos[indice] as! [Any])
        return celda_personalizada
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if indexPath.row == 0 {
            barchart.removeFromSuperview()
            llenar_pie_chart(mensajes: cantidad_mensajes)
            ocultar_etiquetas(tipo: false)
        } else if (indexPath.row == 1) {
            piechart.removeFromSuperview()
            datos_bar_chart(datos: cantidad_mensajes_broad)
            llenar_bar_chart(datos: cantidad_mensajes_broad)
            ocultar_etiquetas(tipo: true)
            
        }
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
        lblTiempoLeido.text = "Enviados a broadcast"
        lblTiempoRes.text = "Recibidos a broadcast"
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
        let modal_form = adaptador.crear_modal_funcion(datos: usuarios!, Accion_Confirmacion_completion: {
            [self](filtros) -> Void in
            
            print(filtros)
            serviciosMensajesFiltrado(filtros: filtros as! [String])
            //serviciosMensajes(idUsuario: Datos[2] as! String)
              
            /*var mensajes_fecha = cantidadDeMensajesPorFecha(mensaje: mensajes! as! [Mensajes], idUsuario: Datos[2] as! String, fechaIni: Datos[0] as! String, fechaFin: Datos[1] as! String)
            
            print(mensajes_fecha)
            print(Datos)
            lblNombreu.text = Datos[3] as! String
            cantidad_mensajes = mensajes_fecha
            llenar_pie_chart(mensajes: mensajes_fecha)*/
        })
        present(modal_form, animated: true)
    }
}
