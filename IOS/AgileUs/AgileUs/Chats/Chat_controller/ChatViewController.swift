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
struct User: Codable
{
    let id: String
    let texto: String
    let idconversacion: String
    let idreceptor: String
    let idemisor: String
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
    MessagesViewController,MessagesDataSource,MessagesLayoutDelegate,InputBarAccessoryViewDelegate,MessageLabelDelegate,MessagesDisplayDelegate {
    
    //declaramos variables globales
    var currentUser = Sender(senderId: "user" , displayName: "Carlos") //variables usuario emisor
    var otherUser = Sender(senderId: "other", displayName: "Bops") //variable usuario receptor
    var messages = [MessageType]() //variable del tipo d emensaje
    var usuarios = [User]() //variable de tipo arreglo
    var mensaje = ""
    var lastDisplayedSentDate: Date?

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
       
        //let Date = Obtener_valor_fecha(fecha: Date(), stilo: "Fecha_mongo")
        mensaje = inputBar.inputTextView.text
        messages.append(Message(sender: currentUser,
                                     messageId: "1",
                                     sentDate: Date(),
                                     kind: .text("\(mensaje)")))
             var fecha = Obtener_valor_fecha(fecha: Date(), stilo: "Fecha_mongo")
        inputBar.inputTextView.text = ""
        self.messagesCollectionView.reloadData()
        create_json(id_emisor: "id_emisor", id_receptor: "id_receptor", mensaje: mensaje, fecha: fecha){
            (exito) in DispatchQueue.main.async { print("Todo Bien") }
            
        }fallido:
        {
            fallo in DispatchQueue.main.async { print("Algo fallo chAT") }
        }
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
        messageInputBar.inputTextView.backgroundColor = .brown
        
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
        if let lastDisplayedSentDate = lastDisplayedSentDate, Calendar.current.isDate(lastDisplayedSentDate, inSameDayAs: message.sentDate) {
                    return nil
                }
                lastDisplayedSentDate = message.sentDate
                
                return NSAttributedString(
                        string: MessageKitDateFormatter.shared.string(from: message.sentDate),
                        attributes: [
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
    
    
    func carga_mensajes()
    {
        let servicio = "https://firebasestorage.googleapis.com/v0/b/nombre-7ec89.appspot.com/o/VerConversacion.json?alt=media&token=5535d8d8-52d5-4a70-8222-b30838f0b19e"
        let url = URL(string: servicio)
        
        
        URLSession.shared.dataTask(with: url!)
        {data,response,error in
            do
            {
                self.usuarios = try JSONDecoder().decode([User].self, from: data!)
                DispatchQueue.main.async
                {
                    var cont = 0
                    for item in self.usuarios
                    {
                        var current_usuario =  item.idemisor
                        
                        if item.idemisor == current_usuario
                        {
                        //self.cadena = self.cadena + "\(item.texto)"
                        self.messages.append(Message(sender: self.currentUser,
                                                     messageId: "\(item.id)",
                                                     sentDate: Date(),
                                                     kind: .text("\(item.texto)")))
                        }else{
                        self.messages.append(Message(sender: self.otherUser,
                                                     messageId: "\(item.id)",
                                                     sentDate: Date(),
                                                     kind: .text("\(item.texto)")))
                        }
                    }
                    self.messagesCollectionView.reloadData()
                    
                }
               
            }
            catch{print("Error")}
        }.resume()
    }

    

}
