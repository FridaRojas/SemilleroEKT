//
//  ViewController.swift
//  AgileUs
//
//  Created by user204412 on 11/18/21.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var logoView: UIView!
    
    override func viewWillAppear(_ animated: Bool) {
        /*
        //Fondo verde
        self.view.backgroundColor = UIColor(red: 100.0/255, green: 188.0/255.0, blue: 111.0/255, alpha: 1.0)
        
        //Crear circulo para el logo
        let layer = CAShapeLayer()
        layer.path =  UIBezierPath(arcCenter: CGPoint(x: self.view.center.x, y: self.view.center.y), radius: CGFloat(100), startAngle: CGFloat(0), endAngle: CGFloat(Double.pi * 2), clockwise: true).cgPath
        layer.fillColor = UIColor(red: 133.0/255, green: 210.0/255.0, blue: 112.0/255, alpha: 1.0).cgColor
        self.view.layer.addSublayer(layer)
        */
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2){ //
            self.performSegue(withIdentifier: "Splash_To_Login", sender: nil)
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        if isLogged == true {
            self.performSegue(withIdentifier: "Splash_To_Home", sender: nil)
        }
    }

}

