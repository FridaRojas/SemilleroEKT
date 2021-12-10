package com.ekt.AdministradorWeb.DAO;

import com.ekt.AdministradorWeb.entity.User;
import com.google.gson.Gson;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class UserDAO {

    //String servidor = "http://3.144.86.49:8080/Servicios-0.0.1-SNAPSHOT";
    String servidor = "http://localhost:3040";

    public ArrayList<User> muestraSubordinados(String idSuperior){
        Gson gson = new Gson();
        ArrayList<User> listaUsuarios = new ArrayList<>();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(servidor+"/api/user/findByBossId/" + idSuperior)
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
                .url(servidor+"/api/user/find/" + idUser)
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject= new JSONObject(response.body().string());
            if (!jsonObject.get("data").equals("")){
                JSONObject usuarios = jsonObject.getJSONObject("data");
                usuario = gson.fromJson(usuarios.toString(), User.class);
                return usuario;
            }else{
                return null;
            }
        }catch (Exception e){
            System.out.println("Ocurrió un problema en UserDao busca por id");
            return null;
        }
    }

    public Boolean editarUsuario(User user){
        Boolean res=false;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"id\":\""+user.getID()+"\",\r\n    \"correo\": \""+user.getCorreo()+"\",\r\n    \"fechaInicio\": \""+user.getFechaInicio()+"\",\r\n    \"fechaTermino\": \""+user.getFechaTermino()+"\",\r\n    \"numeroEmpleado\": \""+user.getNumeroEmpleado()+"\",\r\n    \"nombre\": \""+user.getNombre()+"\",\r\n    \"password\": \""+user.getPassword()+"\",\r\n    \"opcionales\": [],\r\n    \"nombreRol\":\""+user.getNombreRol()+"\" ,\r\n    \"idGrupo\":\""+user.getIDGrupo()+" \",\r\n    \"token\":\""+user.getToken()+" \",\r\n    \"telefono\": \""+user.getTelefono()+"\",\r\n    \"idSuperiorInmediato\": \""+user.getIDSuperiorInmediato()+"\",\r\n    \"statusActivo\": \""+user.getStatusActivo()+"\",\r\n    \"curp\": \""+user.getCurp()+"\",\r\n    \"rfc\": \""+user.getRFC()+"\"\r\n}");
        Request request = new Request.Builder()
                .url(servidor+"/api/user/update/")
                .method("PUT", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject= new JSONObject(response.body().string());
            if (jsonObject.get("status").equals("OK")){
                System.out.println("Usuario editado correctamente");
                res=true;
            }
        }catch (Exception e){
            System.out.println("Error en la consulta");
        }
        return res;
    }

    public boolean actualizaIdSuperior(String idUser, String idSuperior){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"idUsuarios\" : [\""+idUser+"\"],\r\n    \"idSuperiores\" : [\""+idSuperior+"\"]\r\n}");
        Request request = new Request.Builder()
                .url(servidor+"/api/user/updateIdBoss")
                .method("PUT", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public ArrayList<User> listaUsuariosDisponibles(){
        Gson gson = new Gson();
        User user;
        ArrayList<User> listaUsuarios = new ArrayList();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(servidor+"/api/user/findAll")
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            JSONObject jsonObject= new JSONObject(res);
            JSONArray name1 = jsonObject.getJSONArray("data");
            for (int i=0;i<name1.length();i++){
                user=gson.fromJson(name1.getJSONObject(i).toString(), User.class);
                if(user.getIDGrupo().equals("") && user.getStatusActivo().equals("true")){
                    listaUsuarios.add(gson.fromJson(name1.getJSONObject(i).toString(), User.class));
                }
            }
        }catch (Exception e){
            System.out.println("Error al lista usuarios");
        }
        return listaUsuarios;
    }

    public ArrayList<User> listaUsuariosOrganigrama(String id){
        Gson gson = new Gson();
        ArrayList<User> listaUsuariosOrganigrama = new ArrayList();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(servidor+"/api/grupo/buscar/"+id)
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            JSONObject jsonObject= new JSONObject(res);
            JSONObject name1 = jsonObject.getJSONObject("data");
            JSONArray users = name1.getJSONArray("usuarios");
            for (int i=0;i<users.length();i++){
                //omite al BROADCAST ya que solo se puede realizar la operacion de eliminar
                listaUsuariosOrganigrama.add(gson.fromJson(users.getJSONObject(i).toString(), User.class));
            }
        }catch (Exception e){
            System.out.println("Error al hacer la peticion");
        }
        return listaUsuariosOrganigrama;
    }

    public boolean validaCorreoPassword(User us){
        boolean resp;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"correo\": \""+us.getCorreo()+"\",\r\n    \"password\": \""+us.getPassword()+"\",\r\n    \"token\":\"wesasasa\"\r\n}\r\n\r\n\r\n");
        Request request = new Request.Builder()
                .url(servidor+"/api/admin/validate")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            //hace la peticion
            Response response = client.newCall(request).execute();
            //convierte la respuesta en Json
            JSONObject jsonObject= new JSONObject(response.body().string());
            //si data es diferente de "" --> si coincide, si es igual a "" --> no coincide
            if (!jsonObject.get("data").toString().equals("")){
                resp=true;
            }else{
                resp=false;
            }
        }catch (Exception e){
            resp=false;
            System.out.println(e.getMessage());
        }
        return resp;
    }

    public JSONArray buscarTodosUsuarios(ArrayList<User> listaUsuarios){
        //se realiza la peticion al back
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(servidor+"/api/user/findAll")
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            JSONObject jsonObject= new JSONObject(res);
            if(!jsonObject.get("data").toString().equals("")){
                JSONArray name1 = jsonObject.getJSONArray("data");
                return name1;
            }else{
                return null;
            }
        }catch (Exception e){
            System.out.println("Error al realizar la consulta");
            return null;
        }
    }

    public boolean creaUsuario(User user){
        boolean res;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n  \"correo\":\""+user.getCorreo()+"\",\r\n    \"fechaInicio\":\""+user.getFechaInicio()+"\",\r\n    \"fechaTermino\":\""+user.getFechaTermino()+"\",\r\n    \"numeroEmpleado\":\""+user.getNumeroEmpleado()+"\",\r\n    \"nombre\":\""+user.getNombre()+"\",\r\n    \"password\": \""+user.getPassword()+"\",\r\n    \"nombreRol\": \"\",\r\n    \"idGrupo\": \"\",\r\n    \"opcionales\": [],\r\n    \"token\": \"\",\r\n    \"telefono\":\""+user.getTelefono()+"\",\r\n    \"idSuperiorInmediato\": \"\",\r\n    \"statusActivo\": \"true\",\r\n    \"curp\":\""+user.getCurp()+"\",\r\n    \"rfc\":\""+user.getRFC()+"\"\r\n}");
        Request request = new Request.Builder()
                .url(servidor+"/api/user/create")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject= new JSONObject(response.body().string());
            //si data es diferente de "" --> si coincide, si es igual a "" --> no coincide
            if (!jsonObject.get("data").toString().equals("")){
                res=true;
            }else{
                res=false;
            }
        }catch (Exception e){
            System.out.println("Error al insertar usuario");
            res=false;
        }
        return res;
    }

    public boolean desactivarUsuario(String id){
        Boolean res=false;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(servidor+"/api/user/delete/"+id)
                .method("DELETE", body)
                .build();
        try {
            //limpia informacion del usuario en la db
            Response response = client.newCall(request).execute();
            res=true;
        }catch (Exception e){
            System.out.println("Error  al eliminar usuario : "+e.getMessage());
        }
        return res;
    }

    public ArrayList<User> busqedaUsuarios(String parametro) {
        Gson gson = new Gson();
        ArrayList<User> listaUsuarios = new ArrayList<>();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(servidor+"/api/user/busquedaUsuario/" + parametro)
                .method("GET", null)
                .build();
        try {
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
            return null;
        }
    }

    public boolean buscarOrigenUsuario(String id){
        Boolean res=false;
        //dos casos
        ArrayList<User> listaDisponibles =listaUsuariosDisponibles();
        //fuera del organigrama
        for (User use :listaDisponibles) {
            if (use.getID().equals(id)){
                res=true;
            }
        }
        return res;
    }



}
