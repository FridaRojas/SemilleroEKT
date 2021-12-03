package com.ekt.AdministradorWeb.DAO;

import com.ekt.AdministradorWeb.entity.BodyAddUserGroup;
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

    public boolean crearGrupo(Group gr){
        boolean res=true;

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/grupo/crearGrupo/"+gr.getNombre())
                .method("POST", body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject= new JSONObject(response.body().string());

            //si status es "OK" creo el grupo y regresa true, si es diferente a "OK" el grupo ya existe y regresa false
            if (jsonObject.get("status").toString().equals("OK")){
                res=true;
            }else{
                res=false;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return res;
    }

    public boolean editarUsuarioGrupo(User user){
        Boolean res=false;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":\""+user.getID()+"\",\"correo\":\""+user.getCorreo()+"\",\"fechaInicio\":\""+user.getFechaInicio()+"\",\"fechaTermino\":\""+user.getFechaTermino()+"\",\"numeroEmpleado\":\""+user.getNumeroEmpleado()+"\",\"nombre\":\""+user.getNombre()+"\",\"password\":\""+user.getPassword()+"\",\"nombreRol\":\""+user.getNombreRol()+"\",\"idGrupo\":\""+user.getIDGrupo()+"\",\"opcionales\":[],\"token\":\""+user.getToken()+"\",\"telefono\":\""+user.getTelefono()+"\",\"idSuperiorInmediato\":\""+user.getIDSuperiorInmediato()+"\",\"statusActivo\":\""+user.getStatusActivo()+"\",\"curp\":\""+user.getCurp()+"\",\"rfc\":\""+user.getRFC()+"\"}");
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/grupo/actualizaUsuarioGrupo")
                .method("PUT", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject= new JSONObject(response.body().string());

            //si status es "OK" creo el grupo y regresa true, si es diferente a "OK" el grupo ya existe y regresa false
            if (jsonObject.get("status").toString().equals("OK")){
                res=true;
            }
        }catch (Exception e){
            System.out.println("Error al hacer la consulta, actualizar usuario e grupo");
        }
        return res;
    }

    public String reasignausuariogrupo(BodyAddUserGroup datos){
       String res ;

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"idUsuario\":\""+datos.getIdUsuario()+"\",\r\n    \"idSuperior\":\""+datos.getIdSuperior()+"\",\r\n    \"nombreRol\":\""+datos.getNombreRol()+"\"\r\n}");
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/grupo/reasignaUsuarioGrupo")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject= new JSONObject(response.body().string());

            //si status es "OK" creo el grupo y regresa true, si es diferente a "OK" el grupo ya existe y regresa false
            if (jsonObject.get("status").toString().equals("OK")){
                res="OK";
            }else{
                res=jsonObject.get("msj").toString();
            }
        }catch (Exception e){
            res="Error al realizar la consulta";
            System.out.println("Error al reasignar: "+e.getMessage());
        }
        return res;
    }
}
