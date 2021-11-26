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

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

    @IBAction func Visualizar_tarea(_ sender: UIButton) {
        
        if let infoViewController = storyboard?.instantiateViewController(identifier: "InfoViewController") {
            infoViewController.modalPresentationStyle = .overCurrentContext
            infoViewController.modalTransitionStyle = .crossDissolve
            present(infoViewController, animated: true)
                }
    }
   
        
    
    
}
