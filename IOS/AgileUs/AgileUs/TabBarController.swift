//
//  TabBarController.swift
//  AgileUs
//
//  Created by user204412 on 12/2/21.
//

import UIKit

class TabBarController: UITabBarController {


    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
    }
    
    override func viewDidAppear(_ animated: Bool) {
        //print(rolName)
        //print("/*****************************/")
        var indexToRemove = 0
        if rolName == "BROADCAST" {
            viewControllers?.remove(at: 0)
        } else {
            viewControllers?.remove(at: 1)
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
