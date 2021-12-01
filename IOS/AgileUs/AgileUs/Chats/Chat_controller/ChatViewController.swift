//
//  ChatViewController.swift
//  AgileUs
//
//  autor: Carlos_Adolfo_Hernandez (C_A_H)
//
import UIKit
import MessageKit
import InputBarAccessoryView


//declaramos Structuras
//estructura para codear json de api del servicio web
//con los datos que vamos a ocupar
struct Chats: Codable
{
    let id: String
    let texto: String
    let idconversacion: String
    let idreceptor: String
    let idemisor: String
    let fechaCreacion = Date()
    let rutaDocumento: String
}

//estructura del mensaje
struct Sender: SenderType
{
    var senderId: String

    var displayName: String
    
}
//estrructura de los componentes del mensaje
struct Message: MessageType
{
    var sender: SenderType
    
    var messageId: String
    
    var sentDate: Date
    
    var kind: MessageKind
    
    
}


class ChatViewController:
    MessagesViewController,MessagesDataSource,MessagesLayoutDelegate,InputBarAccessoryViewDelegate,MessageLabelDelegate,MessagesDisplayDelegate,MessageCellDelegate {
    
    //declaramos variables globales
    let servicio = "http://10.97.6.83:3040/api/mensajes/verConversacion/618e8743c613329636a769aa_618b05c12d3d1d235de0ade0"
    var currentUser = Sender(senderId: "user" , displayName: "Carlos") //variables usuario emisor
    var otherUser = Sender(senderId: "other", displayName: "Bops") //variable usuario receptor
    var messages = [MessageType]() //variable del tipo d emensaje
    var usuarios = [Chats]() //variable de tipo arreglo
    var mensaje = ""
    var lastDisplayedSentDate: Date?
    var Boton_ver:UIButton?

    override func viewDidLoad()
    {
        super.viewDidLoad()
        //inicializamos las clases
        messagesCollectionView.messagesDataSource = self
        messagesCollectionView.messagesLayoutDelegate = self
        messagesCollectionView.messagesDisplayDelegate = self
        messageInputBar.delegate = self
        configureMessageInputBar()
        //create_json()
       carga_mensajes()
       
    }
  
    override func viewDidAppear(_ animated: Bool) {
        auto_scroll()
        showNavBar()
    }
    //creacion de funciones
    //funcion que retornar el usuario remitente
    func currentSender() -> SenderType
    {return currentUser}
    //funcion que retorna el index del mensaje
    func messageForItem(at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) -> MessageType
    {return messages[indexPath.section]}
    //funcion que retorna el numero de mensajes
    func numberOfSections(in messagesCollectionView: MessagesCollectionView) -> Int
    {return messages.count}
    //funcion para asignar eventos al bton de enviar mensaje y campo de texto
    func inputBar(_ inputBar: InputBarAccessoryView, didPressSendButtonWith text: String) {
       
        mensaje = inputBar.inputTextView.text
        messages.append(Message(sender: currentUser,
                                     messageId: "1",
                                     sentDate: Date(),
                                     kind: .text("\(mensaje)")))
        var fecha = Obtener_valor_fecha(fecha: Date(), stilo: "Fecha_mongo")
        inputBar.inputTextView.text = ""
        
        self.messagesCollectionView.reloadData()
        create_json(id_emisor: userID, id_receptor: "618e8743c613329636a769aa", mensaje: mensaje, rutaDocumento: "", fecha: fecha){
            (exito) in
            print("Exitoso \(userID)")}fallido:{ fallido in
            print("Registro Fallido")
        }
    }
    //detectamos los URL y los hashtag
    func enabledDetectors(for message: MessageType, at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) -> [DetectorType] {
        return [.url, .hashtag]
    }
    //pintamos de diferente color los links y los hashtag
    func detectorAttributes(for detector: DetectorType, and message: MessageType, at indexPath: IndexPath) -> [NSAttributedString.Key : Any] {
        switch detector {
              case .hashtag:
            return [.foregroundColor: UIColor.magenta]
                case .url:
                 return [.foregroundColor: UIColor.blue]
              default: return MessageLabel.defaultAttributes
              }
    }
    //funcion que se le asigna al pulsar el link
    func didSelectURL(_ url: URL) {
        UIApplication.shared.openURL(url)
    }
    
    //funcion para perzonalizar la barra de texto
    func configureMessageInputBar() {
        messageInputBar.delegate = self
        messageInputBar.inputTextView.tintColor = .black
        messageInputBar.sendButton.setTitleColor(.blue, for: .normal)
        messageInputBar.sendButton.setTitleColor(
            UIColor.green.withAlphaComponent(0.3),
            for: .highlighted
        )
       
        messageInputBar.sendButton.setTitle("Enviar", for: .normal)
        messageInputBar.inputTextView.backgroundColor = .lightGray
        messagesCollectionView.messageCellDelegate = self
       
    }
    
    
    
    func boton_cargar()
    {
        /*Boton_ver = UIButton(frame: CGRect(x: 0, y: 0, width: 25, height: 25))
        Boton_ver!.setTitle("MUL", for: .normal)
        Boton_ver!.setTitleColor(UIColor.blue, for: .normal)
        Boton_ver!.addTarget(self, action: #selector(ver_contra), for: .touchUpInside)
        Boton_ver!.backgroundColor = .black
         */
    }
    
    func didTapMessage(in cell: MessageCollectionViewCell) {
        print("adentro")
    }
    //funcion para modificar el avatar de la conversacion
    func configureAvatarView(_ avatarView: AvatarView, for message: MessageType, at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) {
        avatarView.isHidden = false
    }
    //funcion para modificar tamaño el avatar
    func avatarSize(for message: MessageType, at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) -> CGSize {
        return .zero
    }
    //funcion para modificar el estilo del mensaje
    func messageStyle(for message: MessageType, at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) -> MessageStyle {
        return .bubbleOutline(UIColor.white)
       
    }
   
    //funcion para mostrar los mensajes apartir del dia en que se carga la conversacion
    func cellTopLabelAttributedText(for message: MessageType, at indexPath: IndexPath) -> NSAttributedString? {
        if let lastDisplayedSentDate = lastDisplayedSentDate, Calendar.current.isDate(lastDisplayedSentDate, inSameDayAs: message.sentDate) { return nil}
                lastDisplayedSentDate = message.sentDate
                return NSAttributedString(
                        string: MessageKitDateFormatter.shared.string(from: message.sentDate),attributes: [
                            NSAttributedString.Key.font: UIFont.boldSystemFont(ofSize: 10),
                            NSAttributedString.Key.foregroundColor: #colorLiteral(red: 0.1176470588, green: 0.4470588235, blue: 0.8, alpha: 1)])
    }
   //funcion para poner fecha de envio del mensaje
    func messageBottomLabelAttributedText(for message: MessageType, at indexPath: IndexPath) -> NSAttributedString? {
        let dateString = Obtener_valor_fecha(fecha: Date(), stilo: "Fecha_Usuario")
        return NSAttributedString(string: dateString , attributes: [NSAttributedString.Key.font: UIFont.preferredFont(forTextStyle: .caption2)])
    }
  
    //funcion para  dar tamaño a la fecha de conversacion
    func cellTopLabelHeight(for message: MessageType, at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) -> CGFloat {
            if (indexPath.item) % 4 == 0 {
                return 10
            } else {
                return 0
            }
        }
    //funcion para dar tamaño en la fecha que pone cuando se envia el mensaje
    func messageBottomLabelHeight(for message: MessageType, at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) -> CGFloat {
            return 16
        }
    
    func auto_scroll()
    {
        scrollsToLastItemOnKeyboardBeginsEditing = true
        maintainPositionOnKeyboardFrameChanged = true
        showMessageTimestampOnSwipeLeft = true
        
    }
    
    
    func carga_mensajes()
    {
        let url = URL(string: servicio)
        URLSession.shared.dataTask(with: url!)
        {data,response,error in
            do
            {
                self.usuarios = try JSONDecoder().decode([Chats].self, from: data!)
                DispatchQueue.main.async
                {
                    var cont = 0
                    for item in self.usuarios
                    {
                        var current_usuario =  item.idemisor
                        if item.idemisor == userID
                        {
                        //self.cadena = self.cadena + "\(item.texto)"
                            if item.rutaDocumento != ""
                            {
                                self.messages.append(Message(sender: self.otherUser,messageId: "\(item.id)",sentDate: item.fechaCreacion,kind: .text("Documento: \(item.rutaDocumento)")  ))
                            }else{
                                self.messages.append(Message(sender: self.otherUser,messageId: "\(item.id)",sentDate: item.fechaCreacion,kind: .text("\(item.texto)")))
                            }
                        }else{
                            if item.rutaDocumento != ""
                            {
                                self.messages.append(Message(sender: self.currentUser,messageId: "\(item.id)",sentDate: item.fechaCreacion,kind: .text("Documento: \(item.rutaDocumento)")))
                            }else{
                                self.messages.append(Message(sender: self.currentUser,messageId: "\(item.id)",sentDate: item.fechaCreacion,kind: .text("\(item.texto)")))
                            }
                        }
                    }
                    self.messagesCollectionView.reloadData()
                }
            }
            catch{print("Servidor Abajo")}
        }.resume()
    }

    //funcion para hacer petcion post al servidor
    func registro_mensajes(mensaje_json: String, succes: @escaping (_ succes: String) ->(), fallo: @escaping (_ fallo: String) ->() )
    {
        //crea NSURL
        let requestURL = URL(string: "http://10.97.6.83:3040/api/mensajes/crearMensaje")
        
        //crea NSMutableURLRequest  10.97.6.83
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
                succes("successsss")
                
            }
        }
        //ejecuta la tarea
        task.resume()
    }


    //funcion para crear json personalizado
    func create_json(id_emisor: String, id_receptor: String, mensaje: String,rutaDocumento : String, fecha: String, exito: @escaping (_ exito: String) ->(), fallido: @escaping (_ fallido: String) ->() )
    {
        let exampleDict: [String: Any] = [
            "idEmisor" : userID,
                "idReceptor" : id_receptor,
                "texto" : "\(mensaje)",
                "rutaDocumento" : "\(rutaDocumento)",
                "fechaCreacion" : "\(Obtener_valor_fecha(fecha: Date(), stilo: "Fecha_mongo"))",
                                        ]


            if let jsonString = JSONStringEncoder().encode(exampleDict) {
                registro_mensajes(mensaje_json: jsonString) {
                    (succes) in
                        //print(succes)
                    DispatchQueue.main.async {
                       exito("Registro exitoso")
                        
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

}
