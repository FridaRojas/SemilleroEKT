//
//  Charts.swift
//  AgileUs
//
//  Created by Fernando González González on 25/11/21.
//

import Foundation
import UIKit
import Charts

class ChartsB:UIViewController{
    
    var piechart = PieChartView()
    var barchart = BarChartView()

    func configuracion_colores(Vista: UIView, img:UIImageView, segmentedControl:UISegmentedControl, indEnviados:UIImageView, indRecibidos:UIImageView) {
        Vista.backgroundColor = Hexadecimal_Color(hex: "F5F5F5")
        img.backgroundColor = Hexadecimal_Color(hex: "66BB6A")
        segmentedControl.tintColor = Hexadecimal_Color(hex: "66BB6A")
        segmentedControl.selectedSegmentTintColor = Hexadecimal_Color(hex: "66BB6A")
        indEnviados.backgroundColor = Hexadecimal_Color(hex: "66BB6A")
        indRecibidos.backgroundColor = Hexadecimal_Color(hex: "87D169")
    }
    
    
    func configurar_pie_chart(piechartV:PieChartView, viewChart:UIView, indLeidos:UIImageView) {
        //piechartV = PieChartView(frame: CGRect(x: 0, y: 0, width: viewChart.frame.size.width, height: viewChart.frame.size.height))
                
        piechartV.removeFromSuperview()
        piechartV.isUserInteractionEnabled = false
        piechartV.legend.enabled = false
        
        //configurar descripcion
        indLeidos.backgroundColor = UIColor.darkGray
        
    }
    
    
    
}
