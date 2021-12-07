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
        if rolName == "BROADCAST" {
            viewControllers?.remove(at: 0)
        } else {
            viewControllers?.remove(at: 1)
        }        // Do any additional setup after loading the view.
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
