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
            destino.idTask = "61a6a120516207029a580544"
        }
    }

    @IBAction func Visualizar_tarea(_ sender: UIButton) {

        if let infoViewController = storyboard?.instantiateViewController(identifier: "InfoViewController") {
            infoViewController.modalPresentationStyle = .overCurrentContext
            infoViewController.modalTransitionStyle = .crossDissolve
            present(infoViewController, animated: true)
                }
    }




}
