package com.example.agileus.webservices

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.*
import androidx.lifecycle.LiveData

class NetworkConnection(private val context: Context): LiveData<Boolean>() {

    private var connectivityManager: ConnectivityManager=
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var  networkCallback: ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()
        updateConnection()

                connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback())

    }


    private fun connectivityManagerCallback(): ConnectivityManager.NetworkCallback{

        networkCallback = object : ConnectivityManager.NetworkCallback(){

            override fun onLost(network: Network) {
                super.onLost(network)
                postValue(false)
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                postValue(true)
            }
        }
        return networkCallback
    }

    private val netWorkReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            updateConnection()
        }

    }

    private fun updateConnection() {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        postValue(activeNetwork?.isConnected == true)

    }

}