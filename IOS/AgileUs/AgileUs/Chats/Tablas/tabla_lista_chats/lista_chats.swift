//
//  lista_chats.swift
//  AgileUs
//  autor: Carlos_Adolfo_Hernandez (C_A_H)
//
//

import UIKit

class lista_chats: UITableViewCell {
    
    //se crea el id de las celdas y su identificador
    @IBOutlet weak var lbl_conversacion: UILabel!
    
    var id = -1
    static let identificador = "Identificador_Celda_Lista"
    
    static func nib() -> UINib
    {
        return UINib(nibName: "lista_chats", bundle: nil)
    }


    override func awakeFromNib() {
        super.awakeFromNib()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    func configurar_celda(Datos : [Any])
    {
        id = Datos[0] as! Int
        lbl_conversacion.text = "\(Datos[1])"
    }
    func regresa_id() -> Int
    {
        return id
    }
    
    
}
