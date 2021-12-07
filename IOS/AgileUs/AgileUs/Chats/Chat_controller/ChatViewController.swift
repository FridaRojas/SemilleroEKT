//
//  ChatViewController.swift
//  AgileUs
//
//  autor: Carlos_Adolfo_Hernandez (C_A_H)

import UIKit
import MessageKit
import InputBarAccessoryView
import MobileCoreServices
import Firebase
import FirebaseStorage

//declaramos Structuras
//estructura para codear json de api del servicio web
//con los datos que vamos a ocupar
struct Chats: Codable
{
    let id:String
    let texto: String
    let idconversacion: String
    let idreceptor: String
    let idemisor: String
    let fechaCreacion = Date()
    let rutaDocumento: String
    let fechaEnviado: String
    let statusLeido: Bool
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
    MessagesViewController,MessagesDataSource,MessagesLayoutDelegate,InputBarAccessoryViewDelegate,MessageLabelDelegate,MessagesDisplayDelegate,MessageCellDelegate, UIDocumentPickerDelegate {
    
    //declaramos variables
   
    var currentUser = Sender(senderId: "user" , displayName: "Carlos") //variables usuario emisor
    var otherUser = Sender(senderId: "other", displayName: "Bops") //variable usuario receptor
    var messages = [MessageType]() //variable del tipo d emensaje
    var usuarios = [Chats]() //variable de tipo arreglo
    var mensaje = ""
    var lastDisplayedSentDate: Date?
    var Boton_ver:UIButton?
    var Datos_chats: Any?
    var Datos_contacto: Any?
    var url_Documento = ""
    var fecha_mensaje = ""
    var urlFile: URL?
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        //inicializamos las clases
        
        messagesCollectionView.messagesDataSource = self
        messagesCollectionView.messagesLayoutDelegate = self
        messagesCollectionView.messagesDisplayDelegate = self
        messageInputBar.delegate = self
        
        configureMessageInputBar()
        //carga_mensajes()
        showNavBar()
    }
  
    override func viewDidAppear(_ animated: Bool) {
        DispatchQueue.global(qos: .background).async {
            self.carga_mensajes()

        }
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

        DispatchQueue.main.async {
            self.messagesCollectionView.scrollToItem(at: IndexPath(row:0, section: self.messages.count - 1 ),at: .top, animated: false)
        }
        var receptor = ""
        if Datos_chats != nil
        {
             var  dato_chat = Datos_chats as! [Any]
             receptor = "\(dato_chat[2])"
        }else{
            var  dato_chat = Datos_contacto as! [Any]
            receptor = "\(dato_chat[1])"
        }
        
        if urlFile != nil {
            uploadFile(id_receptor: receptor, mensaje: mensaje, fecha: fecha)
        } else {
            create_json(id_emisor: userID, id_receptor: receptor, mensaje: mensaje, rutaDocumento: "", fecha: fecha){
                (exito) in
               
            }fallido:{ fallido in
                self.simpleAlertMessage(title: "Atencion", message: "Verifica tu Conexion a internet")
            }
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
        messageInputBar.sendButton.setTitleColor(UIColor.gray.withAlphaComponent(0.3),for: .highlighted)
        messageInputBar.sendButton.setTitle("Enviar", for: .normal)
        messageInputBar.inputTextView.backgroundColor = UIColor(red: 0.10, green: 0.20, blue: 0.10, alpha: 0.1)
        messageInputBar.inputTextView.layer.cornerRadius = 10
        messagesCollectionView.messageCellDelegate = self
        let items = [makeButton(named: "adjunto_archivo").onTextViewDidChange{ button, textView in
            button.tintColor = UIColor.gray
                button.isEnabled = textView.text.isEmpty
            }
        ]
        items.forEach{$0.tintColor = .lightGray}
        messageInputBar.setStackViewItems(items, forStack: .left, animated: false)
        messageInputBar.setLeftStackViewWidthConstant(to: 45, animated: false)
        auto_scroll()
    }
    
    
    
    func didTapMessage(in cell: MessageCollectionViewCell) {
        let fileUrl = URL(string: self.url_Documento)
        UIApplication.shared.openURL(fileUrl!)
        
    }
    //funcion para modificar el avatar de la conversacion
    func configureAvatarView(_ avatarView: AvatarView, for message: MessageType, at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) {
        avatarView.isHidden = true
    }
    //funcion para modificar tamaño el avatar
    func avatarSize(for message: MessageType, at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) -> CGSize {
        return .zero
    }
    //funcion para modificar el estilo del mensaje
    func messageStyle(for message: MessageType, at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) -> MessageStyle {
        
        return .bubbleOutline(UIColor.white)
    }
    
    
    
    func makeButton(named: String) -> InputBarButtonItem{
        return InputBarButtonItem()
            .configure{    $0.spacing = .fixed(10)
                           $0.image = UIImage(named: named)?.withRenderingMode(.alwaysTemplate)
                           $0.setSize(CGSize(width: 30, height: 30), animated: true)
            }.onSelected{
                $0.tintColor = UIColor.lightGray
            }.onDeselected{
                $0.tintColor = UIColor.gray
            }.onTouchUpInside{ _ in
                print("DOCUMENT PICKER")
                let fileTypes = [
                    String(kUTTypePDF)
                ]

                let viewPickerFile = UIDocumentPickerViewController(documentTypes: fileTypes, in: .import)

                viewPickerFile.delegate = self

                self.present(viewPickerFile, animated: true, completion: nil)
                //self.simpleAlertMessage(title: "Confirmacion", message: "Archivo Adjunto")
            }
        
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
        let dateString = self.fecha_mensaje
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
    //funcion para agregar label cuando el mensaje ha sido leido
    func cellBottomLabelAttributedText(for message: MessageType, at indexPath: IndexPath) -> NSAttributedString? {
        return NSAttributedString(string: "Read", attributes: [NSAttributedString.Key.font: UIFont.boldSystemFont(ofSize: 10), NSAttributedString.Key.foregroundColor: UIColor.darkGray])
    }
    //tamaño de la celda de leido
    func cellBottomLabelHeight(for message: MessageType, at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) -> CGFloat {
        return 10
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
    
    func ultimo_mensaje(){
        DispatchQueue.global().sync
        {
            self.messagesCollectionView.reloadData()
            if self.messages.count == 0
            {
                
            }else{
                self.messagesCollectionView.scrollToItem(at: IndexPath(row:0, section: self.messages.count - 1 ),at: .top, animated: false)
                
            }
            
        
        }
    }
    
    
    func carga_mensajes()
    {
        var servicio = ""
        if Datos_chats != nil
        {
            
             let dato_chat = Datos_chats as! [Any]
             servicio = server + "mensajes/verConversacion/\(userID)_\(dato_chat[2])"
            servicio = server + "mensajes/verConversacion/\(dato_chat[1])"
        }else{
            let dato_contacto = Datos_contacto as! [Any]
            servicio = server + "mensajes/verConversacion/\(userID)_\(dato_contacto[1])"
        }

        let url = URL(string: servicio)
        URLSession.shared.dataTask(with: url!)
        {data,response,error in
            do
            {
                
                if data == nil
                {
                    self.simpleAlertMessage(title: "Aviso", message: "Aun no ha Iniciado conversacion")
                }
                self.usuarios = try JSONDecoder().decode([Chats].self, from: data!)
                DispatchQueue.main.async
                {
                    var cont = 0
                    for item in self.usuarios
                    {
                        self.fecha_mensaje = item.fechaEnviado
                        if item.idemisor == userID
                        {
                            
                            if item.rutaDocumento != ""
                            {
                                self.messages.append(Message(sender: self.currentUser,messageId: "\(item.id)",sentDate: item.fechaCreacion ,kind: .text("Documento: 📄📝")  ))
                                self.url_Documento = item.rutaDocumento
                               
                            }else{
                                self.messages.append(Message(sender: self.currentUser,messageId: "\(item.id)",sentDate: item.fechaCreacion,kind: .text("\(item.texto)")))
                            }
                            
                        }else{
                            if item.rutaDocumento != ""
                            {
                                
                                self.messages.append(Message(sender: self.otherUser,messageId: "\(item.id)",sentDate: item.fechaCreacion ,kind: .text("Documento: 📄📝")))
                                self.url_Documento = item.rutaDocumento
                            }else
                            {
                                self.messages.append(Message(sender: self.otherUser,messageId: "\(item.id)",sentDate: item.fechaCreacion,kind: .text("\(item.texto)")))
                                if item.statusLeido == false
                                    {
                                        self.jsonMensajeLeido(id: item.id)
                                            {
                                                (exito) in
                                                print("mensaje:\(item.id) status\(item.statusLeido) ")
                                            }
                                            fallido:{ fallido in
                                                        print("Fallo La Carga de Mensajes: \(fallido)")
                                                    }
                                    }
                                
                            }
                           
                        }
                       
                    }
                    self.messagesCollectionView.reloadData()
                    self.ultimo_mensaje()
                }
            }
            catch{print("Servidor Abajo\(error)")}
        }.resume()
    }

    //funcion para registrar mensajes leidos
    func mensajesLeidos(mensaje_leido: String, succes: @escaping (_ succes: String) ->(), fallo: @escaping (_ fallo: String) ->() )
    {
        //crea NSURL
        let requestURL = URL(string: server + "mensajes/actualizarLeido")
        
        //crea NSMutableURLRequest
        let requeste = NSMutableURLRequest(url: requestURL! as URL)
        //configura el método de envío
        requeste.httpMethod = "PUT";
        //parámetros a enviar
        let postParameters = mensaje_leido;
        //agrega los parámetros a la petición
        requeste.httpBody = postParameters.data(using: String.Encoding.utf8)
        requeste.setValue("application/json", forHTTPHeaderField: "Content-Type")
        //crea una tarea que envía la petición post
        let task = URLSession.shared.dataTask(with:requeste as URLRequest){
            data, response, error in
            //si ocurre algún error sale
            if error != nil{
                fallo("Error")
                return;
            }
            else{
                succes("successsss")
                //print(response)
                //print(postParameters)
            }
        }
        //ejecuta la tarea
        task.resume()
    }
    
    //funcion para hacer petcion post al servidor
    func registro_mensajes(mensaje_json: String, succes: @escaping (_ succes: String) ->(), fallo: @escaping (_ fallo: String) ->() )
    {
        //crea NSURL
        let requestURL = URL(string: server + "mensajes/crearMensaje")
        
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
//creacion de json para marcar como leido
    func jsonMensajeLeido(id: String, exito: @escaping (_ exito: String) ->(), fallido: @escaping (_ fallido: String) ->() )
    {
        let exampleLeido: [String: Any] = [
            "id" : id,
            "fechaLeido" : "\(Obtener_valor_fecha(fecha: Date(), stilo: "Fecha_mongo"))",
                                        ]
            if let jsonLeido = JSONStringEncoder().encode(exampleLeido) {
                mensajesLeidos(mensaje_leido: jsonLeido){
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
    
    func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentsAt urls: [URL]) {
        if controller.documentPickerMode == .import {
            guard let url = urls.first else {
                return
            }
            do {
                if let filename = urls.first?.lastPathComponent {
                    urlFile = url
                    print(filename)
                    messageInputBar.inputTextView.text = filename
                    controller.dismiss(animated: true, completion: nil)
                }
            } catch {
                let nserror = error as NSError
                fatalError("Unresolved error \(nserror), \(nserror.userInfo)")
            }
        }
    }
    
    func uploadFile(id_receptor: String, mensaje: String, fecha: String) {
        var documentRoute = ""
        
        var file = urlFile
        var filename = "archivo"
        let storageReference = Storage.storage().reference(withPath: "Chats/\(filename)")
        
        let tarea_subir = storageReference.putFile(from: file!, metadata: nil)
        {
            matadatos, error in
            guard let metadatos = matadatos else
            {
                print(error?.localizedDescription)
                return
            }
            
            storageReference.downloadURL(completion: {
                url, error in
                
                if let urlText = url?.absoluteString {
                    documentRoute = urlText
                    
                    
                    self.create_json(id_emisor: userID, id_receptor: id_receptor, mensaje: mensaje, rutaDocumento: documentRoute, fecha: fecha){
                        (exito) in
                        print("/****************************************************************/")
                        print("Exitoso \(userID)")}fallido:{ fallido in
                        print("Registro Fallido")
                    }
                    
                    print(urlText)
                    print("Se subio archivo")
                } else {
                     print("error subia: \(error) ")
                }
                
            })

        }
    }

}

