//
//  TareasScreen.swift
//  AgileUs
//
//  Created by user204412 on 11/18/21.
//

import UIKit

class TareasScreen: UIViewController {
    
    private let botonflotante: UIButton = {
        let button = UIButton(frame: CGRect(x: 0, y: 0, width: 60, height: 60))
        button.backgroundColor = .systemGreen
        
        let image = UIImage(systemName: "plus", withConfiguration: UIImage.SymbolConfiguration(pointSize: 32, weight: .medium))
        
        button.setImage(image, for: .normal)
        button.tintColor = .white
        button.setTitleColor(.white, for: .normal)
        button.layer.shadowRadius = 10
        button.layer.shadowOpacity = 0.3
        //button.layer.masksToBounds = true
        button.layer.cornerRadius = 30
        return button
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.addSubview(botonflotante)
        botonflotante.addTarget(self, action: #selector(didTapButton), for: .touchUpInside)
    }
    
        override func viewDidLayoutSubviews() {
            super.viewDidLayoutSubviews()
            botonflotante.frame = CGRect(
                x: view.frame.size.width - 90,
                y: view.frame.size.height - 150,
                width: 60, height: 60)
        }

       @objc private func didTapButton() {
        let alert = UIAlertController(title: "Error",
                                      message: "Busca otra manera" ,
                                      preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Aceptar", style:  .cancel, handler: nil))
        present(alert, animated: true )
        
        
        
        self.navigationController?.navigationBar.topItem?.title = "hola"

        
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        
    }

}
