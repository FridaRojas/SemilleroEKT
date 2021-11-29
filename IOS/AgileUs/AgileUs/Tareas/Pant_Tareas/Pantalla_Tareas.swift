//
//  Pantalla_Tareas.swift
//  AgileUs
//
//  Created by user203844 on 29/11/21.
//

import UIKit


struct Datos:Codable{
    let fecha_ini:String?
    let id_receptor: String?
    let id_tarea: String?
    let titulo: String?
    let prioridad: String?
}

class Pantalla_Tareas: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    let servico = "https://firebasestorage.googleapis.com/v0/b/apis-de-prueba-1088e.appspot.com/o/TareasResponse.json?alt=media&token=be446017-4996-4545-8273-005b3f37a77d"
    
    @IBOutlet weak var Lista_tareas: UITableView!
    
    
    
    // variables de mi tabla
    var tarea = [Any]()
    var arrTareas = [Datos]()
    //variables de arreglo de json
    var datostarea = ("hola")
   
    
  
    
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
                    
                    print("iniciado el dispatch")
                   //print(self.arrTareas!.description)
                  //  print(type(of: self.arrTareas))
                    print("ddddd")
                
                    for i in self.arrTareas{
               
                        print(i.prioridad)
                        print(i.titulo)
                        print(i.id_receptor)
                        print(i.id_tarea)
                        print(i.id_tarea)
                 }
                    self.Lista_tareas.reloadData()
                 
                }
            }catch let error{
                print(error)
            }
            
        }.resume()
        
}

    //metodos para uso de la tabla
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return arrTareas.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let indice = indexPath.row
        let celda_personalizada = tableView.dequeueReusableCell(withIdentifier: List.identificador, for: indexPath) as! List
        celda_personalizada.configurar_celda(i: arrTareas[ indice])
        return celda_personalizada
        
        
    
    }
}

