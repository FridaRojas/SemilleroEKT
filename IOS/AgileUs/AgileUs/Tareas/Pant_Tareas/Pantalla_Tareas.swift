//
//  Pantalla_Tareas.swift
//  AgileUs
//
//  Created by user203844 on 29/11/21.
//

import UIKit


struct Status: Codable{
    let estatus: String
    let mensaje: String
    let data: [Datos]?

}

struct Datos:Codable{
    var id_tarea: String?
    var id_grupo: String?
    var id_emisor: String?
    var nombre_emisor: String?
    var id_receptor: String?
    var nombre_receptor: String?
    var fecha_ini: String?
    var fecha_BD: String?
    var fecha_fin: String?
    var titulo: String?
    var descripcion: String?
    var prioridad: String?
    var estatus: String?
    var leido: Bool?
    var fechaLeido: String?
    var createdDate: String?
    var observaciones: String?
    var archivo: String?
    var token: String?
    var fecha_iniR: String?
    var fecha_finR: String?

}


class Pantalla_Tareas: UIViewController, UITableViewDelegate, UITableViewDataSource, UICollectionViewDelegate, UICollectionViewDataSource {
    
    @IBOutlet weak var menu_clasificador: UICollectionView!
    
    // nombre para las categorias de pendiente,final,
    var dataSource = [String]()
    
    
    let servico = "http://18.218.7.148:3040/api/tareas/"
    
    @IBOutlet weak var Lista_tareas: UITableView!
    
    
    var selectedIndex = Int()
    // variables de mi tabla
    var tarea = [Any]()
    var arrTareas = [Datos]()
    var selestatus: Status?
    let idUser = "618e8743c613329636a769aa"
    var nivel = "intermedio"
    var select_estatus:String = ""
    var id_tarea:String = ""
    //variable para mostrar el colection view

    
    
    
    //para formar el boton con color e imagen
    private let botonflotante: UIButton = {
    let button = UIButton(frame: CGRect(x: 0, y: 0, width: 60, height: 60))
        button.backgroundColor = .systemGreen
    let image = UIImage(systemName: "plus", withConfiguration: UIImage.SymbolConfiguration(pointSize: 32, weight: .medium))
        button.setImage(image, for: .normal)
        button.tintColor = .white
        button.setTitleColor(.white, for: .normal)
        button.layer.shadowRadius = 10
        button.layer.shadowOpacity = 0.3
        //button.layer.masksToBounds = true
        button.layer.cornerRadius = 30
        return button
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        ValidarNivelUser()
        //llamar a mi boton
        botonflotante.addTarget(self, action: #selector(didTapButton), for: .touchUpInside)
        
        // Preparando lista
        Lista_tareas.delegate = self
        Lista_tareas.dataSource = self
        Lista_tareas.register(List.nib(),forCellReuseIdentifier: List.identificador)
        
        //llama al servico
        var url = nivel != "alto" ? "\(servico)obtenerTareasQueLeAsignaronPorIdYEstatus/\(idUser)&pendiente" : "\(servico)obtenerTareasQueAsignoPorId/\(idUser)"
        select_estatus = nivel == "alto" ? "Asignadas" : "pendiente"
        
        consumir_servicio(url: url)
        
     // llamar al servico de filtros
        
        
        // llamada de menu
        menu_clasificador.dataSource = self
        menu_clasificador.delegate = self
        menu_clasificador.register(ItemMenu.nib(), forCellWithReuseIdentifier: ItemMenu.identificador)
    }
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        botonflotante.frame = CGRect(
        x: view.frame.size.width - 90,
        y: view.frame.size.height - 150,
        width: 60, height: 60)
        }

       @objc private func didTapButton() {
        self.performSegue(withIdentifier: "Formulario", sender: nil)
    }
    
    //funcion  para consumir servicio
    func consumir_servicio(url: String)
    {
        
        let url = URL(string: url)
        
        print("URL: \(url)")
        URLSession.shared.dataTask(with: url!){
            (data,response,error) in
      

            //print(information)
            //print(request)
            //print(error)
            print("************error: \(error)")
            if let dataSuccess = data {
                print("************information: \(dataSuccess)")

                do{
                   // print("Iniciando la entrada al try")
                    
                    
                    
                    self.selestatus = try JSONDecoder().decode(Status.self, from: dataSuccess)
                    
                    print("data*******\(self.selestatus)")
77
                    DispatchQueue.main.async
                    {
                        //print(self.arrTareas)
                        
                        if self.selestatus?.data == nil {
                            self.arrTareas.removeAll()
                            self.Lista_tareas.reloadData()
                            self.Alerta_CamposVacios(title: "Sin tareas", Mensaje: "Vacia")
                            return
                        }
                        
                        self.arrTareas = (self.selestatus?.data)!

                        
                        for i in self.arrTareas {
                            
                            print(i)
                   
                            print(i.prioridad)
                            print(i.titulo)
                            print(i.id_receptor)
                            print(i.id_tarea)
                            
                            
                     }
                        self.Lista_tareas.reloadData()
                        
                    }
                }catch let error{
                    print(error)
                }

            }
            
            
        }.resume()
        
}
    
    //funcion del servicio de filtros
    

    //metodos para uso de la tabla
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return arrTareas.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let indice = indexPath.row
        let celda_personalizada = tableView.dequeueReusableCell(withIdentifier: List.identificador, for: indexPath) as! List
        celda_personalizada.configurar_celda(i: arrTareas[indice])
        
        
        return celda_personalizada
        
        
    
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        var selectindexLista = arrTareas[indexPath.row]
        id_tarea = selectindexLista.id_tarea!
        print("SELECTOR DE ESTATUS:\(select_estatus)")
        if select_estatus != "Asignadas"
        {
            if let infoViewController = storyboard?.instantiateViewController(identifier: "InfoViewController") as? InfoViewController {
                infoViewController.modalPresentationStyle = .overCurrentContext
                infoViewController.modalTransitionStyle = .crossDissolve
                infoViewController.id_tarea = id_tarea
                present(infoViewController, animated: true)
                    }
            
        }
        else
        {
            performSegue(withIdentifier: "trans_edit", sender: id_tarea)
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "trans_edit"
        {
            var ventana_edit = segue.destination as! EditarTareaViewController
            ventana_edit.idTask = id_tarea
        }

    }
    //Metodos para la categoria
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return dataSource.count
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let celda = collectionView.dequeueReusableCell(withReuseIdentifier: ItemMenu.identificador, for: indexPath) as! ItemMenu
        celda.Configure(categoria: dataSource[indexPath.row])
     
        
        //print(selectedIndex)
       
      
       if selectedIndex == indexPath.row
        {
            celda.backgroundColor = UIColor.systemGreen
        
            //print("esta es la celda: \(selectedIndex)")
        }
        else{
        celda.backgroundColor = UIColor .white
        }
        
        return celda
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
        //print("Selecccion: \(dataSource[indexPath.row])")
        
        
        selectedIndex = indexPath.row
        select_estatus = dataSource[indexPath.row]
        var url:String = ""
        //print("******* estatus seleccioando:\(estatus)")
        switch select_estatus {
        case "Iniciadas":
            select_estatus = "iniciada"
            url = "\(servico)obtenerTareasQueLeAsignaronPorIdYEstatus/\(idUser)&\(select_estatus)"
            //print(estatus)
        case "Pendientes":
            select_estatus = "pendiente"
            url = "\(servico)obtenerTareasQueLeAsignaronPorIdYEstatus/\(idUser)&\(select_estatus)"
            //print(estatus)
        case "Revisión":
            select_estatus = "revision"
            url = "\(servico)obtenerTareasQueLeAsignaronPorIdYEstatus/\(idUser)&\(select_estatus)"
            //print(estatus)
        case "Terminadas":
            select_estatus = "terminada"
            url = "\(servico)obtenerTareasQueLeAsignaronPorIdYEstatus/\(idUser)&\(select_estatus)"
            //print(estatus)
        case "Asignadas":
            select_estatus = "Asignadas"
            url = "\(servico)obtenerTareasQueAsignoPorId/\(idUser)"
            
        default:
            print("ningun estatus seleccionado")
        }
        consumir_servicio(url: url)
        self.menu_clasificador.reloadData()
        
        

            }
    func ValidarNivelUser()
    {
        if nivel == "alto"
        {
            dataSource.append("Asignadas")
            view.addSubview(botonflotante)

        }
        else if nivel == "intermedio"
        {
            dataSource.append(contentsOf: ["Pendientes","Iniciadas","Revisión","Terminadas","Asignadas"])
            view.addSubview(botonflotante)

        }
        else
        {
            dataSource.append(contentsOf: ["Pendientes","Iniciadas","Revisión","Terminadas"])
        }
    }
}


