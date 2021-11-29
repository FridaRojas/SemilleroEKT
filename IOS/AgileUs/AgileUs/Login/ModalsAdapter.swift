import UIKit

class ModalsAdapter{
    
    func generateModalForgotPassword() -> ModalForgotPassword
    {
        return UIStoryboard(name: "ModalForgotPassword", bundle: .main).instantiateViewController(identifier: "Modal_ForgotPassword") as! ModalForgotPassword
        /*let storyboard =
        let modal = storyboard.instantiateViewController(identifier: "Modal_ForgotPassword") as! ModalForgotPassword
        return modal*/
    }
    
}
