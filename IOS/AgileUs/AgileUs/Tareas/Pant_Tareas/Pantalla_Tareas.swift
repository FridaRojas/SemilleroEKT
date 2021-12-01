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
    let fecha_ini:String?
    let id_receptor: String?
    let id_tarea: String?
    let titulo: String?
    let prioridad: String?
}


class Pantalla_Tareas: UIViewController, UITableViewDelegate, UITableViewDataSource, UICollectionViewDelegate, UICollectionViewDataSource {
    
    @IBOutlet weak var menu_clasificador: UICollectionView!
    
    // nombre para las categorias de pendiente,final,
    let dataSource = ["Pendientes","Iniciadas","Revisión","Terminada"]
    
    
    let servico = "https://firebasestorage.googleapis.com/v0/b/apis-de-prueba-1088e.appspot.com/o/TareasResponse.json?alt=media&token=be446017-4996-4545-8273-005b3f37a77d"
    
    @IBOutlet weak var Lista_tareas: UITableView!
    
    
    var selectedIndex = Int()
    // variables de mi tabla
    var tarea = [Any]()
    var arrTareas = [Datos]()
    var selestatus = [Status]()
   
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
        
        
        //llamar a mi boton
        view.addSubview(botonflotante)
        botonflotante.addTarget(self, action: #selector(didTapButton), for: .touchUpInside)
        
        // Preparando lista
        Lista_tareas.delegate = self
        Lista_tareas.dataSource = self
        Lista_tareas.register(List.nib(),forCellReuseIdentifier: List.identificador)
        
        //llama al servico
        consumir_servicio()
        
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
    func consumir_servicio()
    {
        
        let url = URL(string: servico)
        
        URLSession.shared.dataTask(with: url!){
            (information,request,error) in
            
            print(information!)
            print(request)
            print(error)
            
            do{
                print("Iniciando la entrada al try")
                
                self.arrTareas = try JSONDecoder().decode([Datos].self, from: information!)
                DispatchQueue.main.async
                {
                    print(self.arrTareas)
                  
                    for i in self.arrTareas {
                        
               
                        print(i.prioridad)
                        print(i.titulo)
                        print(i.id_receptor)
                        print(i.id_tarea)
                        
                        print()
                        
                 }
                    self.Lista_tareas.reloadData()
                 
                }
            }catch let error{
                print(error)
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
    //Metodos para la categoria
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return dataSource.count
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let celda = collectionView.dequeueReusableCell(withReuseIdentifier: ItemMenu.identificador, for: indexPath) as! ItemMenu
        celda.Configure(categoria: dataSource[indexPath.row])
     
        
        print(selectedIndex)
       
      
       if selectedIndex == indexPath.row
        {
            celda.backgroundColor = UIColor.systemGreen
        
            print("esta es la celda: \(selectedIndex)")
        }
        else{
        celda.backgroundColor = UIColor .white
        }
        
        return celda
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
        print("Selecccion: \(dataSource[indexPath.row])")
        
        
        selectedIndex = indexPath.row
        self.menu_clasificador.reloadData()
        
        

            }
    
}


