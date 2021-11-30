//
//  ListaGrafica.swift
//  ejemploCharts
//
//  Created by Luis Gregorio Ramirez Villalobos on 17/11/21.
//

import UIKit

class ListaGrafica: UITableViewCell {
    
    @IBOutlet weak var lblCantEnviados: UILabel!
    @IBOutlet weak var lblCantRecibidos: UILabel!
    @IBOutlet weak var imgGrafica: UIImageView!
    @IBOutlet weak var txtTitulo: UILabel!
    @IBOutlet weak var lblTiempo: UILabel!
    @IBOutlet weak var lblRecibidos: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    static let identificador = "id_celda_pie"
    
    static func nib() -> UINib {
        return UINib(nibName: "ListaGrafica", bundle: nil)
    }
        
    func configurar_celda(datos: [Any]) {
        //txtTitulo.text = "Tiempo de respuesta promedio"
        imgGrafica.image = UIImage(named: datos[0] as! String)
        lblCantEnviados.text = "\(datos[1])"
        lblCantRecibidos.text = "\(datos[2])"
        configurar_etiquetas(tipo: datos[3] as! String)
    }
    
    func configurar_etiquetas(tipo: String) {
        
        if (tipo == "pie") {
            txtTitulo.text = "Enviados"
            lblTiempo.isHidden = true
        } else if (tipo == "bar") {
            txtTitulo.isHidden = true
            lblCantEnviados.isHidden = true
            lblRecibidos.isHidden = true
            
        }
        
    }
    
}
