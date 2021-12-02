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
    

    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.backgroundColor = .white
        self.layer.cornerRadius = 12.0
        self.layer.borderColor = CGColor(red: 229/255, green: 229/255, blue: 229/255, alpha: 1)
        self.layer.borderWidth = 1.0
        self.clipsToBounds = true
        
        
       /* self.layer.masksToBounds = false
        self.layer.shadowRadius = 1.0
        self.layer.shadowColor = UIColor.gray.cgColor
        self.layer.shadowOffset = CGSize(width: 0.0, height: 2.0)
        self.layer.shadowOpacity = 0.2*/
        
    }
    
    // CONSTRUCTOR QUE CAMBIA LA IMAGEN DEPENDIENDO DE LA CATEGORIA
    func Configure(categoria: String) {
        Texto.text = categoria
        Texto.textColor = UIColor.black
        Texto.backgroundColor = UIColor.clear
    
    }
    func Configure_color(categoria: String) {
        Texto.text = categoria
        Texto.textColor = UIColor(red: 102/255, green: 187/255, blue: 107/255, alpha: 1)
        Texto.backgroundColor = UIColor.clear
    
    }
}


