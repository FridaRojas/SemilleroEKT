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
    
    // variables para lista
    var datos = [Any]()
    var cont = 0
    
    //Adaptadores
    // modal
    let adaptador = Adaptador_Modals()
    //Adaptador de la ejecución de los servicios web
    let adaptadorServicios = AdaptadorServicios()
    
    //let configureC = ChartsB()

    
    override func viewDidLoad() {
        super.viewDidLoad()
        //configurar_pie_chart()
        //configurar_lista()
        //configurar_bar_chart()
        //configuracion_colores()
        //llenar_pie_chart(enviado: 46, recibido: 76, leido: 20)
        
        /*configureC.configureElementsScreen(view: viewChart, imgEncabezado: imgEncabezado, optionstAB: optionstAB, indEnviados: indEnviados, indRecibidos: indRecibidos, piechart: piechart, barchart: barchart, viewChart: indRecibidos, indLeidos: indLeidos, cantEnviados: cantEnviados, cantRecibidos: cantRecibidos, cantLeidos: cantLeidos, cantTotales: cantTotales, lblTotales: lblTotales, lblLeidos: lblLeidos, lblEnviados: lblEnviados, lblRecibidos: lblRecibidos, lblTiempoLeido: lblTiempoLeido, lblTiempoRes: lblTiempoRes, indTotales: indTotales)
        */
        
        
        print("mensaje prueba")
        
       
        //serviciosTareas()
        serviciosMensajes()

    }
    
    func serviciosMensajes(){
        adaptadorServicios.servicioWebMensajesAdapter{
            [] (Datos) -> Void in
            
            for i in Datos{
                print("\n\(i)")
            }
        }
    }
    
    func serviciosTareas(){
        adaptadorServicios.servicioWebTareasAdapter{
            [] (Datos) -> Void in
            
            for i in Datos{
                print("\n\(i)")
            }
        }
    }
    
    func servicioUsuarios(){
        //Servicio web de Usuarios
        adaptadorServicios.serviciosWeb{
            [] (Datos) -> Void in
            
            for i in Datos{
                print("\n\(i)")
            }

        }
    }
    
    func configuracion_colores() {
        view.backgroundColor = Hexadecimal_Color(hex: "F5F5F5")
        imgEncabezado.backgroundColor = Hexadecimal_Color(hex: "66BB6A")
        optionstAB.tintColor = Hexadecimal_Color(hex: "66BB6A")
        optionstAB.selectedSegmentTintColor = Hexadecimal_Color(hex: "66BB6A")
        indEnviados.backgroundColor = Hexadecimal_Color(hex: "66BB6A")
        indRecibidos.backgroundColor = Hexadecimal_Color(hex: "87D169")
    }
    
    func configurar_pie_chart() {
        piechart = PieChartView(frame: CGRect(x: 0, y: 0, width: viewChart.frame.size.width, height: viewChart.frame.size.height))
                
        piechart.removeFromSuperview()
        piechart.isUserInteractionEnabled = false
        piechart.legend.enabled = false
        
        //configurar descripcion
        indLeidos.backgroundColor = UIColor.darkGray
        
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
        
        if sender.selectedSegmentIndex == 0 {
            llenar_pie_chart(enviado: 46, recibido: 76, leido: 20)
        }
        
        if sender.selectedSegmentIndex == 1 {
            llenar_pie_chart(enviado: 78, recibido: 10, leido: 80)
        }
        
    }
        
    func llenar_pie_chart(enviado: Double?, recibido: Double?, leido: Double?) {
        cantEnviados.text = "\(enviado!)"
        cantRecibidos.text = "\(recibido!)"
        cantLeidos.text = "\(leido!)"
        cantTotales.text = "\(enviado! + recibido! + leido!)"
        
        let enviados = PieChartDataEntry(value: enviado!)
        let recibidos = PieChartDataEntry(value: recibido!)
        let leidos = PieChartDataEntry(value: leido!)
        
        let entries = [enviados, recibidos, leidos]
        let dataset = PieChartDataSet(entries: entries, label: nil)
        let chartdata = PieChartData(dataSet: dataset)
        chartdata.setValueTextColor(UIColor.clear)

        let colors = [Hexadecimal_Color(hex: "66BB6A"), Hexadecimal_Color(hex: "87D169"), Hexadecimal_Color(hex: "7F8182")]
        
        dataset.colors = colors
        piechart.data = chartdata
        piechart.contentMode = .scaleToFill
        viewChart.addSubview(piechart)
        
        actualizar_datos_lista_grafica(enviado: enviado!, recibido: recibido!)
    }
    
    func llenar_bar_chart(datos: [Any]?) {
        
        let leidos = BarChartDataEntry(x: 1, y: Double("\(datos![0])")!)
        let contestados = BarChartDataEntry(x: 2, y: Double("\(datos![1])")!)
        let entries = [leidos, contestados]
        
        let dataset = BarChartDataSet(entries: entries)
        let chardata = BarChartData(dataSet: dataset)
        barchart.data = chardata
        
        let colors = [Hexadecimal_Color(hex: "66BB6A"), Hexadecimal_Color(hex: "87D169")]
        dataset.colors = colors
        viewChart.addSubview(barchart)
    }
    
    func actualizar_datos_lista_grafica(enviado: Double?, recibido: Double?) {
        datos = [["ic_PieChart", enviado!, recibido!, "pie"], ["ic_Bar", enviado!, recibido!, "bar"]]
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
            llenar_pie_chart(enviado: 46, recibido: 76, leido: 20)
            ocultar_etiquetas(tipo: false)
        } else if (indexPath.row == 1) {
            piechart.removeFromSuperview()
            llenar_bar_chart(datos: [34, 60])
            ocultar_etiquetas(tipo: true)
            datos_bar_chart(datos: [34, 60])
        }
    }
    
    func ocultar_etiquetas(tipo: Bool) {
        lblTotales.isHidden = tipo
        lblLeidos.isHidden = tipo
        lblEnviados.isHidden = tipo
        lblRecibidos.isHidden = tipo
        indLeidos.isHidden = tipo
        indRecibidos.isHidden = tipo
        //cantLeidos.isHidden = tipo
        cantTotales.isHidden = tipo
        cantEnviados.isHidden = tipo
        //cantRecibidos.isHidden = tipo
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
        
        let modal_form = adaptador.crear_modal_funcion(Accion_Confirmacion_completion: {
            [self](Datos) -> Void in
            
            print(Datos)
            /*lblNombre.text = "Nombre: \(Datos[0] as! String)"
            lblApellido.text = "Apellido \(Datos[1] as! String)"
            lblEdad.text = "Edad \(Datos[2] as! String)"*/
            
            //var numero_cervezas = Datos[1] as! Int
            
            
            //dismiss(animated: true, completion: nil)
            //alerta_mensajes(title: "Mensaje", Mensaje: "Cervezas: \(numero_cervezas)")
        })
        
        present(modal_form, animated: true)
    
    }
    
    
    
}
