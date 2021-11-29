package com.example.agileus.webservices.apis

import com.example.agileus.models.User
import retrofit2.http.GET
import retrofit2.http.Query

interface UserLoginApi {
        //REMOTE
        @GET("v0/b/pruebas-eqipo-admin.appspot.com/o/salida%20login%20exitoso.json")
        //fun userLogin
        fun recuperarDatosLogin(
            @Query("correo") correo: String,
            @Query("password") password: String
        ): retrofit2.Call<User>

    }

/*    companion object{
        operator fun invoke(): MyApiService {
            return Retrofit.Builder()
                .baseUrl("https://firebasestorage.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApiService::class.java)
        }
    }

 */


//Si quisiera hacerlo en @POST,lo haria así
/* @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("email") email:String,
        @Field("password") password:String
    )

 */