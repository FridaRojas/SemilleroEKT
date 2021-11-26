//
//  InfoViewController.swift
//  AgileUs
//
//  Created by Carlos nitsuga Hernandez hernandez on 25/11/21.
//
import UIKit

struct Tareas: Codable
{
    let titulo:String
    let nombre_receptor:String
    let prioridad:String
    let estatus:String
    let descripcion:String
    let fecha_ini:String
    let fecha_fin:String
    let observaciones:String?
}

class InfoViewController: UIViewController {
    
    @IBOutlet weak var Titulo: UILabel!
    @IBOutlet weak var Nombre: UILabel!
    @IBOutlet weak var Prioridad: UILabel!
    @IBOutlet weak var Estatus: UILabel!
    @IBOutlet weak var Descripcion: UITextView!
    @IBOutlet weak var Fecha_inicio: UILabel!
    @IBOutlet weak var Fecha_fin: UILabel!
    @IBOutlet weak var Observacion: UITextView!
    lazy var blurredView: UIView = {
        // 1. create container view
        let containerView = UIView()
        // 2. create custom blur view
        let blurEffect = UIBlurEffect(style: .light)
        let customBlurEffectView = CustomVisualEffectView(effect: blurEffect, intensity: 0.2)
        customBlurEffectView.frame = self.view.bounds
        // 3. create semi-transparent black view
        let dimmedView = UIView()
        dimmedView.backgroundColor = .black.withAlphaComponent(0.6)
        dimmedView.frame = self.view.bounds
        
        // 4. add both as subviews
        containerView.addSubview(customBlurEffectView)
        containerView.addSubview(dimmedView)
        return containerView
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupView()
        LoadTareaModal()
    }
    
    func setupView() {
        // 6. add blur view and send it to back
        view.addSubview(blurredView)
        view.sendSubviewToBack(blurredView)
    }
    
    @IBAction func Cerrar_modal(_ sender: UIButton) {
        dismiss(animated: true)
    }
    
    
    func LoadTareaModal()
            {
                let urlStr = "http://10.97.3.134:2021/api/tareas/obtenerTareaPorId/619c036a755c956b81252e03"
                if let url = URL(string: urlStr) {
                    URLSession.shared.dataTask(with: url) { (data, response , error) in
                        if let data = data {
                            do {
                                let AtribTarea = try JSONDecoder().decode(Tareas.self, from: data)
                                DispatchQueue.main.async {
                                    self.Titulo.text = AtribTarea.titulo
                                    self.Nombre.text = AtribTarea.nombre_receptor
                                    self.Prioridad.text = "Prioridad: \(AtribTarea.prioridad)"
                                    self.Estatus.text = "Estatus: \(AtribTarea.estatus)"
                                    self.Descripcion.text = AtribTarea.descripcion
                                    self.Fecha_inicio.text = AtribTarea.fecha_ini
                                    self.Fecha_fin.text = AtribTarea.fecha_fin
                                    if (self.Observacion.text == "null")
                                    {
                                        self.Observacion.text = String()
                                    }
                                    else{
                                        self.Observacion.text = AtribTarea.observaciones
                                    }
                                    

                                }
                                
                                
                            } catch {
                                print("error")
                            }
                        } else {
                            print("error")
                        }
                    }.resume()
                }
                

            }

}
