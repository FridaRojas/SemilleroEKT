package com.example.agileus.webservices.notification

import android.util.Log
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
    }

    override fun onNewToken(token: String) {
        //   super.onNewToken(token)
        Log.d("token", "token = $token")
        preferenciasGlobal.guardarToken(token)
    }

}