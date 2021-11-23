
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
        /*
        print("///////////////////////////")
        print(response)
        print("///////////////////////////")
        print(data!)
        print("///////////////////////////")
       */
    }
    //ejecuta la tarea
    task.resume()
}

