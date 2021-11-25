//
//  TareasScreen.swift
//  AgileUs
//
//  Created by user204412 on 11/18/21.
//

import UIKit

class TareasScreen: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        
        self.navigationController?.navigationBar.topItem?.title = "hola"

        
    }
    override func viewDidDisappear(_ animated: Bool) {
        
    }
    @IBAction func editTaskBtn(_ sender: Any) {

        
        performSegue(withIdentifier: "viewEditTask", sender: self)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let destino = segue.destination as? EditarTareaViewController {
            destino.idTask = "619c036a755c956b81252e03"
        }
    }

}
