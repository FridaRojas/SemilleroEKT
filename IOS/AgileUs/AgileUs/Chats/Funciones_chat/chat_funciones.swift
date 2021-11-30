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
       // print("Valores_Mensaje: \(jsonString)")
       
        return jsonString
    }
}

