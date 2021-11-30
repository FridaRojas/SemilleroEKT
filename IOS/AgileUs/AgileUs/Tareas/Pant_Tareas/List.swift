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
        super.awakeFromNib()}
    
        //  Metodos
    override func setSelected(_ selected: Bool, animated: Bool) {super.setSelected(selected, animated: animated)}
    // configur
    func configurar_celda(i:Any)
    {
        var info = i as! Datos
        Nametask.text = info.titulo
        Persona.text = info.id_receptor
        Fecha.text = info.fecha_ini
        Prioridad.text = info.prioridad
     
        
    }
    
}



