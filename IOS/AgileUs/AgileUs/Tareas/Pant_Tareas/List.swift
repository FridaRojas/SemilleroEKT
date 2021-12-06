//
//  List.swift
//  boton_tabla
//
//  Created by user203844 on 23/11/21.
//

import UIKit

class List: UITableViewCell{
   
    @IBOutlet weak var Nametask: UILabel!
    @IBOutlet weak var Persona: UILabel!
    @IBOutlet weak var Fecha: UILabel!
    @IBOutlet weak var Prioridad: UILabel!
    
   

    //variables de funcionamiento
   static let identificador = "identificador_List"
   static func nib() -> UINib { return UINib(nibName: "List", bundle: nil)}
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.clipsToBounds = false
//        
        self.layer.masksToBounds = true
        self.layer.shadowRadius = 1.0
        self.layer.shadowColor = UIColor.gray.cgColor
        self.layer.shadowOffset = CGSize(width: 0.0, height: 1.0)
        self.layer.shadowOpacity = 0.5
//        self.layer.cornerCurve = CALayerCornerCurve.circular

        
    }
    
        //  Metodos
    override func setSelected(_ selected: Bool, animated: Bool) {super.setSelected(selected, animated: animated)}
    // configur
    func configurar_celda(i:Any)
    {
        var info = i as! Datos
        Nametask.text = info.titulo
        
        Persona.text = info.nombre_receptor
        Fecha.text = HelpString.formatDate(date: info.fecha_ini!)
        Prioridad.text = "Prioridad: \(info.prioridad!)"
        if info.prioridad! == "Alta"
        {
            print("entre a la alta")
            Prioridad.textColor = UIColor(red: 255/255, green: 0/255, blue: 0/255, alpha: 1)
            
            
        }
        else if info.prioridad! == "Media"
        {
            Prioridad.textColor = UIColor(red: 255/255, green: 179/255, blue: 0/255, alpha: 1)
        }
        else
        {
            Prioridad.textColor = UIColor(red: 67/255, green: 160/255, blue: 71/255, alpha: 1)
        }
        
        
    }
    
    override open var frame: CGRect {
        get {
            return super.frame
        }
        set (newFrame) {
            var frame =  newFrame
            frame.origin.y += 10
            frame.origin.x += 10
            frame.size.height -= 15
            frame.size.width -= 2 * 10
            super.frame = frame
        }
    }

    
}



