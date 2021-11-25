//
//  ViewController.swift
//  AgileUs
//
//  Created by user204412 on 11/18/21.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        if isLogged == true {
            self.performSegue(withIdentifier: "Splash_To_Home", sender: nil)
        }
        
    }

}

