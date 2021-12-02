package com.ekt.AdministradorWeb.DAO;

import com.ekt.AdministradorWeb.entity.Group;
import com.ekt.AdministradorWeb.entity.User;
import com.google.gson.Gson;
import okhttp3.*;
import org.json.JSONObject;

public class GroupDAO {

    public User[] muestraUsuariosGrupo(String idGrupo){
        System.out.println(idGrupo);
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/grupo/buscar/" + idGrupo)
                .method("GET", null)
                .build();
        try{
            Response response = client.newCall(request).execute();
            JSONObject jsonObject= new JSONObject(response.body().string());
            if (!jsonObject.get("data").equals("")){
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

    public boolean eliminaUsuarioGrupo(String idUser, String idGroup){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"idUsuario\":\""+idUser+"\",\r\n    \"idGrupo\":\""+idGroup+"\"\r\n}");
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/grupo/borrarUsuarioDeGrupo")
                .method("DELETE", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject= new JSONObject(response.body().string());
            System.out.println(jsonObject);
            return true;
        }catch (Exception e){
            return false;
        }

    }
}
