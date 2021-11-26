//
//  lista_contactos.swift
//  AgileUs
//
//Modulo creado por Carlos_Adolfo_Hernandez_Moreno(C_A_H)
//

import UIKit

class lista_contactos: UITableViewCell {
    
    var id = -1
    static let identificador = "Identificador_Celda"
    
    static func nib() -> UINib
    {
        return UINib(nibName: "lista_contactos", bundle: nil)
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
        //lbl_nombre_contacto.text = "\(Datos[1])"
    }
    
    func regresa_id() -> Int
    {
        return id
    }
    
}
