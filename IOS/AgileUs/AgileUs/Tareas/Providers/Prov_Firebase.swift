//
//  Prov_Firebase.swift
//  AgileUs
//
//  Created by Andres Villanueva Sanchez on 30/11/21.
//

import Foundation
import Firebase
import FirebaseStorage

final class ProvFirebase {
    static func storageInFirebase(file: URL, filename: String, urlResponse: @escaping (_ url: String) -> () ) -> String
    {
        
        
        var urlS: String?
        let bucketRef = Storage.storage().reference(withPath: "Tareas/\(filename)")
        

        let tarea_subir = bucketRef.putFile(from: file, metadata: nil)
        {
            matadatos, error in
            guard let metadatos = matadatos else
            {
                print(error?.localizedDescription)
                return
            }
            
            bucketRef.downloadURL(completion: {
                url, error in
                
                if let urlText = url?.absoluteString {
                    urlResponse(urlText)
                    
                } else {
                     print("error subia: \(error) ")
                }
                
                
            })
            print("Se subio archivo")
            
 
        }
        return "\(urlS)"
        

        
    }

}
