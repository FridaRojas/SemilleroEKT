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
        Api.shared.editTask(id: "619c036a755c956b81252e03") {
            (task) in
            print(task)
        } failure: {
            (error) in
            print(error)
        }
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
