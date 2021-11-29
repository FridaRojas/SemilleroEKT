//
//  ItemMenu.swift
//  boton_tabla
//
//  Created by user203844 on 29/11/21.
//

import UIKit

class ItemMenu: UICollectionViewCell {
    

    @IBOutlet weak var Texto: UILabel!
    static let identificador = "Identificador_ItemMenu"
    static func nib() -> UINib { return UINib(nibName: "ItemMenu", bundle: nil) }
    

    override func awakeFromNib() { super.awakeFromNib() }
    
    // CONSTRUCTOR QUE CAMBIA LA IMAGEN DEPENDIENDO DE LA CATEGORIA
    func Configure(categoria: String) {
        Texto.text = categoria
    }
}


