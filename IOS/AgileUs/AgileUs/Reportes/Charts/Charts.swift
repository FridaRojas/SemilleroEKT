//
//  Charts.swift
//  AgileUs
//
//  Created by Fernando González González on 25/11/21.
//

import Foundation
import UIKit
import Charts
/*
class configurationsChartB{
    
    var view_: UIView?
    var imgEcabezado_: UIImageView?
    var optionstAB_: UISegmentedControl?
    var indEnviados_:UIImageView
    var indRecibidos_:UIImageView
    var piechart_:PieChartView
    var barchart_:BarChartView
    var viewChart_: UIView
    var indLeidos_:UIImageView
    var cantEnviados_: UILabel
    var cantRecibidos_:UILabel
    var cantLeidos_:UILabel
    var cantTotales_:UILabel
    var lblTotales_:UILabel
    var lblLeidos_:UILabel
    var lblEnviados_:UILabel
    var lblRecibidos_:UILabel
    var lblTiempoLeido_:UILabel
    var lblTiempoRes_:UILabel
    var indTotales_: UIImageView
    
    
    func configureElementsScreen(view:UIView, imgEncabezado:UIImageView, optionstAB:UISegmentedControl, indEnviados:UIImageView, indRecibidos:UIImageView, piechart:PieChartView, barchart: BarChartView, viewChart: UIView, indLeidos:UIImageView,  cantEnviados: UILabel, cantRecibidos:UILabel, cantLeidos:UILabel, cantTotales:UILabel, lblTotales:UILabel, lblLeidos:UILabel, lblEnviados:UILabel,  lblRecibidos:UILabel, lblTiempoLeido:UILabel, lblTiempoRes:UILabel, indTotales: UIImageView ){
        
        
        view_ = view
        imgEcabezado_ = imgEncabezado
        optionstAB_ = optionstAB
        indEnviados_ = indEnviados
        indRecibidos_ = indRecibidos
        piechart_ = piechart
        barchart_ = barchart
        viewChart_ = viewChart
        indLeidos_ = indLeidos
        cantEnviados_ = cantEnviados
        cantRecibidos_ = cantRecibidos
        cantLeidos_ = cantLeidos
        cantTotales_ = cantTotales
        lblTotales_ = lblTotales
        lblLeidos_ = lblLeidos
        lblEnviados_ = lblEnviados
        lblRecibidos_ = lblRecibidos
        lblTiempoLeido_ = lblTiempoLeido
        lblTiempoRes_ = lblTiempoRes
        indTotales_ = indTotales
        
        
        configurar_pie_chart()
        configurar_bar_chart()
        configuracion_colores()
        //llenar_pie_chart(enviado: 46, recibido: 76, leido: 20)

        
        
    }
    
    
    func configuracion_colores() {
        view_.backgroundColor = Hexadecimal_Color(hex: "F5F5F5")
        imgEcabezado_.backgroundColor = Hexadecimal_Color(hex: "66BB6A")
        optionstAB_.tintColor = Hexadecimal_Color(hex: "66BB6A")
        optionstAB_.selectedSegmentTintColor = Hexadecimal_Color(hex: "66BB6A")
        indEnviados_.backgroundColor = Hexadecimal_Color(hex: "66BB6A")
        indRecibidos_.backgroundColor = Hexadecimal_Color(hex: "87D169")
    }
    
    func configurar_pie_chart() {
        //piechart = PieChartView(frame: CGRect(x: 0, y: 0, width: viewChart.frame.size.width, height: viewChart.frame.size.height))
                
        piechart_.removeFromSuperview()
        piechart_.isUserInteractionEnabled = false
        piechart_.legend.enabled = false
        
        //configurar descripcion
        indLeidos_.backgroundColor = UIColor.darkGray
        
    }
    
    func configurar_bar_chart() {
        //barchart = BarChartView(frame: CGRect(x: 0, y: 0, width: viewChart.frame.size.width, height: viewChart.frame.size.height))
        barchart_.legend.enabled = false
        barchart_.isUserInteractionEnabled = false
        
    }
    
    
func llenar_pie_chart(enviado: Double?, recibido: Double?, leido: Double?) {
    cantEnviados_.text = "\(enviado!)"
    cantRecibidos_.text = "\(recibido!)"
    cantLeidos_.text = "\(leido!)"
    cantTotales_.text = "\(enviado! + recibido! + leido!)"
    
    let enviados = PieChartDataEntry(value: enviado!)
    let recibidos = PieChartDataEntry(value: recibido!)
    let leidos = PieChartDataEntry(value: leido!)
    
    let entries = [enviados, recibidos, leidos]
    let dataset = PieChartDataSet(entries: entries, label: nil)
    let chartdata = PieChartData(dataSet: dataset)
    chartdata.setValueTextColor(UIColor.clear)

    let colors = [Hexadecimal_Color(hex: "66BB6A"), Hexadecimal_Color(hex: "87D169"), Hexadecimal_Color(hex: "7F8182")]
    
    dataset.colors = colors
    piechart_.data = chartdata
    piechart_.contentMode = .scaleToFill
    viewChart_.addSubview(piechart_)
    
    actualizar_datos_lista_grafica(enviado: enviado!, recibido: recibido!)
}

func llenar_bar_chart(datos: [Any]?) {
    
    let leidos = BarChartDataEntry(x: 1, y: Double("\(datos![0])")!)
    let contestados = BarChartDataEntry(x: 2, y: Double("\(datos![1])")!)
    let entries = [leidos, contestados]
    
    let dataset = BarChartDataSet(entries: entries)
    let chardata = BarChartData(dataSet: dataset)
    barchart_.data = chardata
    
    let colors = [Hexadecimal_Color(hex: "66BB6A"), Hexadecimal_Color(hex: "87D169")]
    dataset.colors = colors
    viewChart_.addSubview(barchart_)
}

func actualizar_datos_lista_grafica(enviado: Double?, recibido: Double?) -> [Any] {
    
    var datos = [["ic_PieChart", enviado!, recibido!, "pie"], ["ic_Bar", enviado!, recibido!, "bar"]]
    
    return datos
}

    
    func ocultar_etiquetas(tipo: Bool) {
        lblTotales_.isHidden = tipo
        lblLeidos_.isHidden = tipo
        lblEnviados_.isHidden = tipo
        lblRecibidos_.isHidden = tipo
        indLeidos_.isHidden = tipo
        indRecibidos_.isHidden = tipo
        //cantLeidos.isHidden = tipo
        cantTotales_.isHidden = tipo
        cantEnviados_.isHidden = tipo
        //cantRecibidos.isHidden = tipo
        lblTiempoLeido_.isHidden = !tipo
        lblTiempoRes_.isHidden = !tipo
        cambiar_color_indicador(tipo: tipo)
    }
    
    func datos_bar_chart(datos: [Int]) {
        lblTiempoLeido_.text = "Tiempo de lectura promedio"
        lblTiempoRes_.text = "Tiempo de respuesta promedio"
        cantLeidos_.text = "\(datos[0])"
        cantRecibidos_.text = "\(datos[1])"
        
    }
    
    func cambiar_color_indicador(tipo: Bool) {
        if tipo == true {
            indTotales_.backgroundColor = Hexadecimal_Color(hex: "87D169")
        } else {
            indTotales_.backgroundColor = UIColor.systemOrange
        }
    }
    
        
}
*/
