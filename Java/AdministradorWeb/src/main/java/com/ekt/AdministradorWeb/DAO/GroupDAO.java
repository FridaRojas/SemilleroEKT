package com.ekt.AdministradorWeb.DAO;

import com.ekt.AdministradorWeb.entity.Group;
import com.ekt.AdministradorWeb.entity.User;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

public class GroupDAO {

    public User[] muestraUsuariosGrupo(String idGrupo){
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/grupo/buscar/619d220c3cd67733b375db11")
                .method("GET", null)
                .build();
        try{
            Response response = client.newCall(request).execute();
            JSONObject jsonObject= new JSONObject(response.body().string());
            if (jsonObject.get("data")!=""){
                JSONObject grupoObjeto = jsonObject.getJSONObject("data");
                Group grupo  = gson.fromJson(grupoObjeto.toString(), Group.class);
                User []usuarios = grupo.getUsuarios();
                return usuarios;
            }else{
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }
}
