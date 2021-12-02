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
    
    @IBOutlet weak var buttonFile: UIButton!
    var id_tarea:String?
    var estatus:String = ""
    var url_file:String = ""
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
        MostrarTareaModal(idtask: id_tarea!)

    }
    
    func setupView() {
        //Agregar blur view y mandarlo de regreso
        view.addSubview(blurredView)
        view.sendSubviewToBack(blurredView)
    }
    
    @IBAction func Cerrar_modal(_ sender: UIButton) {
        dismiss(animated: true)
    }
    func MostrarTareaModal(idtask: String)
    {
        self.MostrarSpinner(onView: self.view)
        Api.shared.LoadTareaModal(idTarea: idtask) {
            tarea in
            print("si jalo")
            DispatchQueue.main.async {
                self.Titulo.text = tarea.titulo
                self.Nombre.text = tarea.nombre_receptor
                self.Prioridad.text = "Prioridad: \(tarea.prioridad!)"
                self.Estatus.text = "Estatus: \(tarea.estatus!)"
                self.estatus = tarea.estatus!
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
                self.Descripcion.text = "Descripcion: \(tarea.descripcion!)"
                self.Fecha_inicio.text = "Fecha inicio: \(HelpString.formatDate(date: tarea.fecha_ini!))"
                self.Fecha_fin.text = "Fecha fin:\(HelpString.formatDate(date: tarea.fecha_fin!))"
                if (tarea.observaciones == nil || tarea.observaciones == "")
                {
                    self.Observacion.text = "Sin observaciones"
                }
                else{
                    self.Observacion.text = "Observaciones: \(tarea.observaciones!)"
                }
                if (tarea.archivo == nil || tarea.archivo == "")
                {
                    self.buttonFile.isHidden = true
                }
                else{
                    self.url_file = tarea.archivo!
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
         if (estatus == "iniciada")
        {
             print("Entro a Actualizar ********************")
             Api.shared.UpdateFecha(idTask: id_tarea!, ban: true)
             {
                 tarea in
                 print("Se guardo fecha de inicio real")
             } failure: { error in
                 print("Error \(error)")
             }
        }

        Api.shared.UpdateEstatus(idTarea: id_tarea!, estatus: estatus)
        {
            tarea in
            
            DispatchQueue.main.async {
                self.RemoverSpinner()
                self.Alerta_CamposVacios(title: "Cambio Exitoso", Mensaje: "Se ha cambiado su estatus")
                self.Estatus.text = "Estatus: \(self.estatus)"
            }
        } failure: { error in
            print("Error")
        }
    }
    
    
    @IBAction func BotonCambiarEstatus(_ sender: UIButton) {
        CambiarEstatus()
    }
    @IBAction func VisorPDF(_ sender: UIButton) {
        performSegue(withIdentifier: "trans_visor", sender: url_file)
    }
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        var ventana_visor = segue.destination as? VisorPDF
        ventana_visor?.urlFile = url_file
    }
}
