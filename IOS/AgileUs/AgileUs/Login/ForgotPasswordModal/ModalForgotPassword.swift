//
//  ModalForgotPassword.swift
//  AgileUs
//
//  Created by user204412 on 11/26/21.
//

import UIKit

class ModalForgotPassword: UIViewController {

    @IBOutlet weak var modalView: UIView!
    @IBOutlet weak var txtEmail: UITextField!
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        
        txtEmail.addBackgroundColorAndTextColor(backgroundColor: UIColor(red: 245.0/255, green: 245.0/255, blue: 245.0/255, alpha: 1.0), textColor: UIColor(red: 156.0/255, green: 158.0/255, blue: 159.0/255, alpha: 1.0))
        txtEmail.roundCorners(cornerRadius: 20.0)
        txtEmail.addIcon(image: UIImage(named: "user_icon")!, direction: "Left")
        
    }
    
    @IBAction func closeModal(_ sender: UIButton) {
        dismiss(animated: true, completion: nil)
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?)
    {
        let touch = touches.first
        if touch?.view != self.modalView
        { self.dismiss(animated: true, completion: nil) }
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
