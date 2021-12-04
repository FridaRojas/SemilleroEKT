//
//  celda_msjs_enviados.swift
//  AgileUs
//
//  Created by user205703 on 04/12/21.
//

import UIKit

class celda_msjs_enviados: UITableViewCell
{

    static let identificador = "Identificador_Celda_Lista"
    var nombre = "Adonay Matehuala Corona"
    var area = "Sistemas"
    
    static func nib() -> UINib
    {
        return UINib(nibName: "celda_msjs_enviados", bundle: nil)
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    var id = -1
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    func Configurar_Celda_Mensajes (Datos: [Any])
    {
        id = Datos[0] as! Int
        nombre = Datos[1] as! String
        area = Datos[2] as! String
    }
    
    func regresar_id () -> Int
    {
        return id
    }
    
}






