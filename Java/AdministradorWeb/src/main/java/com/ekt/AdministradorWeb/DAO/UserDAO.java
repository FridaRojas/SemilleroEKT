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
                usuario = gson.fromJson(usuarios.toString(), User.class);
                return usuario;
            }else{
                return null;
            }
        }catch (Exception e){
            System.out.println("Ocurrió un problema");
            return null;
        }
    }

    public Boolean editarUsuario(User user){
        System.out.println("En editarUsuario "+user.getFechaInicio()+"  "+user.getRFC());
        Boolean res=false;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"id\":\""+user.getID()+"\",\r\n    \"correo\": \""+user.getCorreo()+"\",\r\n    \"fechaInicio\": \""+user.getFechaInicio()+"\",\r\n    \"fechaTermino\": \""+user.getFechaTermino()+"\",\r\n    \"numeroEmpleado\": \""+user.getNumeroEmpleado()+"\",\r\n    \"nombre\": \""+user.getNombre()+"\",\r\n    \"password\": \""+user.getPassword()+"\",\r\n    \"opcionales\": [],\r\n    \"nombreRol\":\""+user.getNombreRol()+"\" ,\r\n    \"idGrupo\":\""+user.getIDGrupo()+" \",\r\n    \"token\":\""+user.getToken()+" \",\r\n    \"telefono\": \""+user.getTelefono()+"\",\r\n    \"idSuperiorInmediato\": \""+user.getIDSuperiorInmediato()+"\",\r\n    \"statusActivo\": \""+user.getStatusActivo()+"\",\r\n    \"curp\": \""+user.getCurp()+"\",\r\n    \"rfc\": \""+user.getRFC()+"\"\r\n}");
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/user/update/")
                .method("PUT", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject= new JSONObject(response.body().string());
            System.out.println("res de editr: "+jsonObject.toString());
            if (jsonObject.get("status").equals("OK")){
                res=true;
            }
        }catch (Exception e){

            System.out.println("Error en la consulta");
        }
        return res;
    }

    public Boolean existusuario(User user){
        Boolean res=false;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"correo\" : \""+user.getCorreo()+"\",\r\n    \"curp\" : \""+user.getCurp()+"\",\r\n    \"rfc\" : \""+user.getRFC()+"\",\r\n    \"numeroEmpleado\" : \""+user.getNumeroEmpleado()+"\"\r\n}");
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/user/existUser")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());

            if (!jsonObject.get("data").toString().equals("true")) {
                res=true;

            }
        }
        catch (Exception e){
            System.out.println("Error al realizar la peticion");
        }

        return res;
    }

    public boolean actualizaIdSujperior(String idUser, String idSuperior){

        return false;
    }

    public ArrayList<User> listaUsuariosDisponibles(){
        Gson gson = new Gson();
        ArrayList<User> listaUsuarios = new ArrayList();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/user/findAll")
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            JSONObject jsonObject= new JSONObject(res);
            //Separamos la parte de data
            JSONArray name1 = jsonObject.getJSONArray("data");

            //prueba de casteo

            for (int i=0;i<name1.length();i++){
                if(gson.fromJson(name1.getJSONObject(i).toString(), User.class).getIDGrupo().equals("")){
                    listaUsuarios.add(gson.fromJson(name1.getJSONObject(i).toString(), User.class));
                }
            }
            System.out.println("La lista de disponibles es: "+listaUsuarios.size());
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
                .url("http://localhost:3040/api/grupo/buscar/"+id)
                .method("GET", null)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String res = response.body().string();

            JSONObject jsonObject= new JSONObject(res);

            JSONObject name1 = jsonObject.getJSONObject("data");
            System.out.println(name1.toString());
            JSONArray users = name1.getJSONArray("usuarios");
            System.out.println(users.toString());
            for (int i=0;i<users.length();i++){
                    listaUsuariosOrganigrama.add(gson.fromJson(users.getJSONObject(i).toString(), User.class));
            }
            System.out.println("Lista de usuarios en un grupo: "+listaUsuariosOrganigrama.size());

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
                .url("http://localhost:3040/api/user/validate")
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

}
