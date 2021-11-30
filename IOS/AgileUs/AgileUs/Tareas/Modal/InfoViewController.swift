//
//  InfoViewController.swift
//  AgileUs
//
//  Created by Carlos nitsuga Hernandez hernandez on 25/11/21.
//
import UIKit

class InfoViewController: UIViewController {
    
    @IBOutlet weak var boton_estatus: UIButton!
    @IBOutlet weak var Titulo: UILabel!
    @IBOutlet weak var Nombre: UILabel!
    @IBOutlet weak var Prioridad: UILabel!
    @IBOutlet weak var Estatus: UILabel!
    @IBOutlet weak var Descripcion: UITextView!
    @IBOutlet weak var Fecha_inicio: UILabel!
    @IBOutlet weak var Fecha_fin: UILabel!
    @IBOutlet weak var Observacion: UITextView!
    
    var id_tarea:String = ""
    var estatus:String = ""

    lazy var blurredView: UIView = {
        // Crear containerView
        let containerView = UIView()
        // Crear blur view
        let blurEffect = UIBlurEffect(style: .light)
        let customBlurEffectView = CustomVisualEffectView(effect: blurEffect, intensity: 0.2)
        customBlurEffectView.frame = self.view.bounds
        // Crear
        let dimmedView = UIView()
        dimmedView.backgroundColor = .black.withAlphaComponent(0.6)
        dimmedView.frame = self.view.bounds
        
        //Agregar subView
        containerView.addSubview(customBlurEffectView)
        containerView.addSubview(dimmedView)
        return containerView
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupView()
        MostrarTareaModal()

    }
    
    func setupView() {
        //Agregar blur view y mandarlo de regreso
        view.addSubview(blurredView)
        view.sendSubviewToBack(blurredView)
    }
    
    @IBAction func Cerrar_modal(_ sender: UIButton) {
        dismiss(animated: true)
    }
    func MostrarTareaModal()
    {
        self.MostrarSpinner(onView: self.view)
        Api.shared.LoadTareaModal(idTarea: "61a6a120516207029a580544") {
            tarea in
            print("si jalo")
            DispatchQueue.main.async {
                self.Titulo.text = tarea.titulo
                self.Nombre.text = tarea.nombre_receptor
                self.Prioridad.text = "Prioridad: \(tarea.prioridad)"
                self.Estatus.text = "Estatus: \(tarea.estatus)"
                self.estatus = tarea.estatus
                self.id_tarea = tarea.id_tarea
                switch self.estatus
                {
                case "pendiente":
                    self.boton_estatus.setTitle("Cambiar a iniciada", for: .normal)
                    self.estatus = "iniciada"
                case "iniciada":
                    self.boton_estatus.setTitle("Cambiar a revision", for: .normal)
                    self.estatus = "revision"
                case "revision":
                    self.boton_estatus.isHidden = true
                case "terminada":
                    self.boton_estatus.isHidden = true
                default:
                    print("sin estatus")
                }
                self.Descripcion.text = "Descripcion: \(tarea.descripcion)"
                self.Fecha_inicio.text = "Fecha inicio: \(String(tarea.fecha_ini.prefix(10)))"
                self.Fecha_fin.text = "Fecha fin:\(String(tarea.fecha_fin.prefix(10)))"
                if (self.Observacion.text == "null")
                {
                    self.Observacion.text = String()
                }
                else{
                    self.Observacion.text = "Observaciones: \(tarea.observaciones!)"
                }
                self.RemoverSpinner()

            }
        } failure: { error in
            print("ERROR")
        }
    }

    
    func CambiarEstatus()
    {
        self.MostrarSpinner(onView: self.view)
        Api.shared.UpdateEstatus(idTarea: id_tarea, estatus: estatus)
        {
            tarea in
            
            DispatchQueue.main.async {
                self.RemoverSpinner()
                self.Alerta_CamposVacios(title: "Cambio Exitoso", Mensaje: "Se ha cambiado su estatus")
                self.Estatus.text = "Estatus: \(self.estatus)"
            }
        } failure: { error in

        }
    }
    
    
    @IBAction func BotonCambiarEstatus(_ sender: UIButton) {
        CambiarEstatus()
    }
    @IBAction func VisorPDF(_ sender: UIButton) {
        
    }
}
