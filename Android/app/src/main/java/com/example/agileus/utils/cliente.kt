package com.example.agileus.utils

import okhttp3.OkHttpClient
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

fun cliente(tiempo:Long): OkHttpClient {
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(tiempo, TimeUnit.SECONDS)
        .readTimeout(tiempo, TimeUnit.SECONDS)
        .writeTimeout(tiempo, TimeUnit.SECONDS)
        .build()
    return okHttpClient
}

fun isConnectedToThisServer(host: String): Boolean {

    val runtime = Runtime.getRuntime()
    try {
        val ipProcess = runtime.exec("/system/bin/ping -c 1 $host")
        val exitValue = ipProcess.waitFor()
        ipProcess.destroy()
        return exitValue == 0
    } catch (e: UnknownHostException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }

    return false
}