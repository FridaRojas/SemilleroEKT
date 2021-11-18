//
//  LoginScreen.swift
//  AgileUs
//
//  Created by user204412 on 11/18/21.
//

import UIKit

class LoginScreen: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        
        //self.navigationController?.isNavigationBarHidden = true

        
        //navigationController?.setNavigationBarHidden(false, animated: false)
        //navigationController?.setN
        // Do any additional setup after loading the view.
    }
    @IBAction func iniciarSesion(_ sender: UIButton) {
        
        self.performSegue(withIdentifier: "Login_To_Home", sender: self)
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
