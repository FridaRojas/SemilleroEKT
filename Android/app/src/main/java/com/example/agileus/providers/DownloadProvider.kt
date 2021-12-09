package com.example.agileus.providers

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import java.io.File

class DownloadProvider {

    fun dowloadFile(context:Context, url:String, titulo:String){
        val file: File = File(context.getExternalFilesDir(null), "file")
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("${titulo}${(0..99999).random()}")
            .setDescription("Download")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationUri(Uri.fromFile(file))
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadManager.enqueue(request)
    }
}