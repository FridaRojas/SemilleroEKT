//
//  Pantalla_Tareas.swift
//  AgileUs
//
//  Created by user203844 on 29/11/21.
//

import UIKit


class Pantalla_Tareas: UIViewController, UITableViewDelegate, UITableViewDataSource, UICollectionViewDelegate, UICollectionViewDataSource {
    
    @IBOutlet weak var menu_clasificador: UICollectionView!
    @IBOutlet weak var titleTaksField: UILabel!
    
    // nombre para las categorias de pendiente,final,
    var dataSource = [String]()
    
    
    //let servico = "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/tareas/"
    
//    let servico = "http://3.144.86.49:8080/Servicios-0.0.1-SNAPSHOT/api/tareas/"
    @IBOutlet weak var Lista_tareas: UITableView!
    
    
    var selectedIndex = Int()
    // variables de mi tabla
    var tarea = [Any]()
    var arrTareas = [Datos]()
    var selestatus: Status?
    //let idUser = "61b0e5b31e484f08fcbf594b"
    //var nivel = "intermedio"
    var select_estatus:String = ""
    var id_tarea:String = ""
    var url:String = ""
    let cellSpacingHeight: CGFloat = 5
    //let token = "cb4726124497b16bdaaa8d2bfbce5aba0782fa7d59d4fe873dabc55062743091"
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
        addLogoutButton()
        ValidarNivelUser()
        //llamar a mi boton
        botonflotante.addTarget(self, action: #selector(didTapButton), for: .touchUpInside)
        
        // Preparando lista
        Lista_tareas.delegate = self
        Lista_tareas.dataSource = self
        Lista_tareas.register(List.nib(),forCellReuseIdentifier: List.identificador)
        

        select_estatus = hierarchyLevel == 1 ? "Asignadas" : "pendiente"
     // llamar al servico de filtros
        // llamada de menu
        menu_clasificador.dataSource = self
        menu_clasificador.delegate = self
        menu_clasificador.register(ItemMenu.nib(), forCellWithReuseIdentifier: ItemMenu.identificador)
        
        titleTaksField.text = hierarchyLevel != 1 ? "Tareas Pendientes" : "Asignadas"
    }

    override func viewWillAppear(_ animated: Bool) {
        //llama al servico
        hideNavBar()
        if select_estatus == "Asignadas"
        {
            url =  "\(server)tareas/obtenerTareasQueAsignoPorId/\(userID)"
        }
        else
        {
            url = hierarchyLevel != 1 ? "\(server)tareas/obtenerTareasQueLeAsignaronPorIdYEstatus/\(userID)/\(select_estatus)" : "\(server)tareas/obtenerTareasQueAsignoPorId/\(userID)"
            
        }
        consumir_servicio(url: url)

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
        var request = URLRequest(url: url!)
        
        // Token Config
        request.setValue("\(tokenAuth)", forHTTPHeaderField: "tokenAuth")
        
        print(request)
        //print(self.token)
        self.MostrarSpinner(onView: self.view)
        
        URLSession.shared.dataTask(with: request){
            (data,response,error) in
            print(response)
            if let httpResponse = response as? HTTPURLResponse,
               (400...499).contains(httpResponse.statusCode){
                if let dataSuccess = data {
                    let dataString = String(data: dataSuccess, encoding: .utf8)
                    print("400-------\(dataString)")
                }
                return;
            }
            if let dataSuccess = data {
                print("************information: \(dataSuccess)")
                
                do{
                    
                    
                    self.selestatus = try JSONDecoder().decode(Status.self, from: dataSuccess)
                    DispatchQueue.main.async
                    {
                        
                        if self.selestatus?.data == nil {
                            self.arrTareas.removeAll()  // llenar loz datoz
                            self.Lista_tareas.reloadData() // recarga la lizta
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
                self.RemoverSpinner()
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
        //print("SELECTOR DE ESTATUS:\(select_estatus)")
        if select_estatus != "Asignadas"
        {
            if let infoViewController = storyboard?.instantiateViewController(identifier: "InfoViewController") as? InfoViewController {

                infoViewController.modalPresentationStyle = .fullScreen
                //infoViewController.modalPresentationStyle = .overCurrentContext
                //infoViewController.modalTransitionStyle = .crossDissolve
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
           celda.Configure_color(categoria: dataSource[indexPath.row])
            //celda.backgroundColor = UIColor.systemW
        
            //print("esta es la celda: \(selectedIndex)")
        }
        else{
        //celda.backgroundColor = UIColor .white
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
            url = "\(server)tareas/obtenerTareasQueLeAsignaronPorIdYEstatus/\(userID)/\(select_estatus)"
            titleTaksField.text = "Tareas Iniciadas"

            //print(estatus)
        case "Pendientes":
            select_estatus = "pendiente"
            url = "\(server)tareas/obtenerTareasQueLeAsignaronPorIdYEstatus/\(userID)/\(select_estatus)"
            titleTaksField.text = "Tareas Pendientes"

            //print(estatus)
        case "Revisión":
            select_estatus = "revision"
            url = "\(server)tareas/obtenerTareasQueLeAsignaronPorIdYEstatus/\(userID)/\(select_estatus)"
            titleTaksField.text = "Tareas En Revisión"

            //print(estatus)
        case "Terminadas":
            select_estatus = "terminada"
            url = "\(server)tareas/obtenerTareasQueLeAsignaronPorIdYEstatus/\(userID)/\(select_estatus)"
            titleTaksField.text = "Tareas Terminadas"

            //print(estatus)
        case "Asignadas":
            select_estatus = "Asignadas"
            url = "\(server)tareas/obtenerTareasQueAsignoPorId/\(userID)"
            titleTaksField.text = "Tareas Asignadas"

            
        default:
            print("ningun estatus seleccionado")
        }
        consumir_servicio(url: url)
        self.menu_clasificador.reloadData()
        
        

            }
    func ValidarNivelUser()
    {
        if hierarchyLevel == 1
        {
            dataSource.append("Asignadas")
            view.addSubview(botonflotante)

        }
        else if hierarchyLevel == 2
        {
            dataSource.append(contentsOf: ["Pendientes","Iniciadas","Revisión","Terminadas","Asignadas"])
            view.addSubview(botonflotante)

        }
        else
        {
            dataSource.append(contentsOf: ["Pendientes","Iniciadas","Revisión","Terminadas"])
        }
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        cell.contentView.layer.masksToBounds = true
    }

}


