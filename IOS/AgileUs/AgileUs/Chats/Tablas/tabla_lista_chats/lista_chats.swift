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
        //var check = "\(Datos[3])"
        if Datos[4] as! String == "grupo"
        {
            lbl_conversacion.textColor = UIColor(red:0, green:0.255,blue:0.100,alpha:0.5)
            //lbl_conversacion.bounds
            lbl_conversacion.text = "\(Datos[3])"
        }
        lbl_conversacion.text = "\(Datos[3])"
    }
    func regresa_id() -> Int
    {
        return id
    }
    
    
}
