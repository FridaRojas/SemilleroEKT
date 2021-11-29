package com.example.agileus.providers


import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseProvider {

    lateinit var mStorageInstance : FirebaseStorage
    lateinit var mStorageReference: StorageReference
      var obs= MutableLiveData<String>()

    fun subirPdfFirebase(uri: Uri,references:String,fileName:String){
        mStorageInstance = FirebaseStorage.getInstance()
        mStorageReference = mStorageInstance.getReference(references)

        try{
            var refenciaPdf = mStorageReference.child(fileName)
            var uploadTask = refenciaPdf.putFile(uri)

            uploadTask.addOnSuccessListener {
                it.storage.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("Url", "Archivo uri = ${task.result}")
                        obs.value=task.result.toString()

                    } else {

                    }
                }
            }


            uploadTask.addOnFailureListener{
                it.printStackTrace()
            }
        }catch(e:Exception){
            Log.e("mensaje",e.message.toString())
            e.printStackTrace()
        }
        finally {
        }
    }

}