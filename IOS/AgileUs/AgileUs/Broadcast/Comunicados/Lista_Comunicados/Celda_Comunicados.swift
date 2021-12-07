//
//  Celda_Comunicados.swift
//  AgileUs
//
//  Created by user205703 on 07/12/21.
//

import UIKit

class Celda_Comunicados: UITableViewCell {

    static let identificador = "Identificador_Celda_Comunicado"
    var asunto = "Comunicado general"
    var fecha = "28/09/2021"
    var id = -1
    
    static func nib() -> UINib
    {
        return UINib(nibName: "Identificador_Celda_Comunicado", bundle: nil)
    }
    
    override func awakeFromNib()
    {
        super.awakeFromNib()

    }

    override func setSelected(_ selected: Bool, animated: Bool)
    {
        super.setSelected(selected, animated: animated)

    }
    
    func Configurar_Celda_Comunicados (Datos: [Any])
    {
        asunto = Datos[0] as! String
        fecha = Datos[1] as! String
    }
    
    func regresar_id () -> Int
    {
        return id
    }
    
}
