package com.ekt.AdministradorWeb.DAO;

import com.ekt.AdministradorWeb.entity.User;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
                .url("http://localhost:3040/api/user/findByBossId/619bbf0623b2987cc6211172")
                .method("GET", null)
                .addHeader("Content-Type", "application/json")
                .build();
        try{
            Response response = client.newCall(request).execute();
            JSONObject jsonObject= new JSONObject(response.body().string());
            if (jsonObject.get("data")!=""){
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

    public ArrayList<User> muestraUsuariosGrupo(){

        return null;
    }
}
