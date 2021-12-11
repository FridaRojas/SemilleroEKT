//
//  celda_msjs_enviados.swift
//  AgileUs
//
//  Created by user205703 on 04/12/21.
//

import UIKit

class celda_msjs_enviados: UITableViewCell
{
    
    @IBOutlet weak var nombre_lbl_celda: UILabel!
    
    @IBOutlet weak var area_Lbl_Celda: UILabel!
    
    static let identificador = "Identificador_Celda_Lista"
    var nombre = "Adonay Matehuala Corona"
    var area = "Sistemas"
    
    static func nib() -> UINib
    {
        return UINib(nibName: "celda_msjs_enviados", bundle: nil)
    }
    
    override func awakeFromNib()
    {
        super.awakeFromNib()
        // Initialization code
    }
    
    var id = -1
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    func Configurar_Celda_Mensajes (Mensaje_recibido: Mensajes_Broacast)
    {
        nombre_lbl_celda.text = Mensaje_recibido.nombreEmisor
        //id = Datos[0] as! Int
        //nombre = Datos[1] as! String
        //area = Datos[2] as! String
        print("\n**********************\n")
        print(Mensaje_recibido)
        print("\n**********************\n")
    }
    
    func regresar_id () -> Int
    {
        return id
    }
    
}






