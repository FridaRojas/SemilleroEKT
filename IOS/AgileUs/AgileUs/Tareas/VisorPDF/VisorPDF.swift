//
//  VisorPDF.swift
//  AgileUs
//
//  Created by Carlos nitsuga Hernandez hernandez on 29/11/21.
//

import UIKit
import PDFKit
class VisorPDF: UIViewController,CAAnimationDelegate {

    
    var link = "https://firebasestorage.googleapis.com/v0/b/agileus-55195.appspot.com/o/Tareas%2Ftarea827?alt=media&token=e35b1696-6ce3-4b6f-9173-d8c3c2cea95e"
    var PDF_URL: URL?
    let shapeLayer = CAShapeLayer()
    
    @IBOutlet weak var visor: PDFView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    Configurar_PDF_Visor()


    }
    func Configurar_PDF_Visor()
    {

            visor.autoScales = true
            PDF_URL = URL(string: self.link)
            visor.document = PDFDocument(url: self.PDF_URL!)
            visor.maxScaleFactor = 19.0
            visor.minScaleFactor = visor.scaleFactorForSizeToFit
            shapeLayer.removeAllAnimations()

        
    }

    
    @IBAction func Descargar(_ sender: UIButton) {
        DispatchQueue.main.async {
            do
            {
                if let pdf_envio = try? Data.init(contentsOf: self.PDF_URL!)
                {
                    let objetosCompartir = [pdf_envio]
                    let ventana = UIActivityViewController(activityItems: objetosCompartir, applicationActivities: nil)
                    if self.presentedViewController == nil
                    {
                        self.present(ventana, animated: true, completion: nil)
                    }
                    else
                    {
                        self.dismiss(animated: true, completion: nil)
                        self.present(ventana, animated: true, completion: nil)
                    }
                }
 
            }
            catch{print("Existe un error")}
        }
    }
    
}
