//  autor: Carlos_Adolfo_Hernandez (C_A_H)
import Foundation
import UIKit


//extenciones fecha
func Obtener_valor_fecha(fecha: Date, stilo: String) -> String
{
    let formatter = DateFormatter()
    formatter.locale = Locale(identifier: "es_419")
    switch stilo
    {
    case "Fecha_Usuario": formatter.dateFormat = "dd/MMMM/yyyy"
    case "Fecha_Base_datos": formatter.dateFormat = "yyyy/MM/dd"
    case "Fecha_mongo": formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ"
    default: formatter.dateFormat = "dd/MM/yyyy hh:mm a"
    }
    return formatter.string(from: fecha)
}

//Estructura para crear un json personalizado
struct JSONStringEncoder {
    func encode(_ dictionary: [String: Any]) -> String?
    {
        guard JSONSerialization.isValidJSONObject(dictionary) else {assertionFailure("Invalid json object received.")
            return nil
    }
        let jsonObject: NSMutableDictionary = NSMutableDictionary()
        let jsonData: Data

        dictionary.forEach { (arg) in jsonObject.setValue(arg.value, forKey: arg.key)}

        do { jsonData = try JSONSerialization.data(withJSONObject: jsonObject, options: .prettyPrinted)}
        catch
        {
            assertionFailure("JSON data creation failed with error: \(error).")
            return nil
        }

        guard let jsonString = String.init(data: jsonData, encoding: String.Encoding.utf8) else {
            assertionFailure("JSON string creation failed.")
            return nil
        }
        print("Valores_Mensaje: \(jsonString)")
        return jsonString
    }
}

//funcion para hacer petcion post al servidor
func registro_mensajes(mensaje_json: String, succes: @escaping (_ succes: String) ->(), fallo: @escaping (_ fallo: String) ->() )
{
    //crea NSURL
    let requestURL = URL(string: "http://10.97.5.252:3040/api/mensajes/crearMensaje")
    //crea NSMutableURLRequest
    let request = NSMutableURLRequest(url: requestURL! as URL)
    //configura el método de envío
    request.httpMethod = "POST";
    //parámetros a enviar
    let postParameters = mensaje_json;
    //agrega los parámetros a la petición
    request.httpBody = postParameters.data(using: String.Encoding.utf8)
    request.setValue("application/json", forHTTPHeaderField: "Content-Type")
    //request.
    //crea una tarea que envía la petición post
    let task = URLSession.shared.dataTask(with:request as URLRequest){
        data, response, error in
        //si ocurre algún error sale
        if error != nil{
            fallo("Error")
            return;
        }
        else{
            succes("succes")
            
        }
    }
    //ejecuta la tarea
    task.resume()
}


//funcion para crear json personalizado
func create_json(id_emisor: String, id_receptor: String, mensaje: String, fecha: String, exito: @escaping (_ exito: String) ->(), fallido: @escaping (_ fallido: String) ->() )
{
    let exampleDict: [String: Any] = [
            "idEmisor" : id_emisor,
            "idReceptor" : id_receptor,
            "texto" : "\(mensaje)",
            "fechaCreacion" : "\(Obtener_valor_fecha(fecha: Date(), stilo: "Fecha_mongo"))",
                                    ]


        if let jsonString = JSONStringEncoder().encode(exampleDict) {
            registro_mensajes(mensaje_json: jsonString) {
                (succes) in
                    print(succes)
                DispatchQueue.main.async {
                   exito("Todo Salio Bien")
                }
                
            } fallo: {
                fallo in
                DispatchQueue.main.async {
                fallido("Servidor Abajo")
                }
               
            }
        } else {
            print("fallo la codificacion")
        }
    
}

