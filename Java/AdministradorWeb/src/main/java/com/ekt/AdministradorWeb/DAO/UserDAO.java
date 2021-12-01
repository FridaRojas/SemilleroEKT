package com.ekt.AdministradorWeb.DAO;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.ekt.AdministradorWeb.entity.User;
import com.google.gson.Gson;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserDAO {

    public ArrayList<User> muestraSubordinados(String idSuperior){
        Gson gson = new Gson();
        ArrayList<User> listaUsuarios = new ArrayList<>();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/user/findByBossId/" + idSuperior)
                .method("GET", null)
                .addHeader("Content-Type", "application/json")
                .build();
        try{
            Response response = client.newCall(request).execute();
            JSONObject jsonObject= new JSONObject(response.body().string());
            if (!jsonObject.get("data").equals("")){
                JSONArray usuarios = jsonObject.getJSONArray("data");

                for (int i=0;i<usuarios.length();i++){
                    listaUsuarios.add(gson.fromJson(usuarios.getJSONObject(i).toString(), User.class));
                }
                return listaUsuarios;
            }else{
                return null;
            }
        }catch (Exception e){
            System.out.println("No se puede realizar la petición");
            return null;
        }
    }

    public User buscaID(String idUser){
        Gson gson = new Gson();
        User usuario;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/user/find/" + idUser)
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject= new JSONObject(response.body().string());
            if (!jsonObject.get("data").equals("")){
                JSONObject usuarios = jsonObject.getJSONObject("data");
                System.out.println(usuarios);
                usuario = gson.fromJson(usuarios.toString(), User.class);
                System.out.println(usuario.toString());
                return usuario;
            }else{
                return null;
            }
        }catch (Exception e){
            System.out.println("Ocurrió un problema");
            return null;
        }
    }

    public boolean actualizaIdSujperior(String idUser, String idSuperior){

        return false;
    }
}
