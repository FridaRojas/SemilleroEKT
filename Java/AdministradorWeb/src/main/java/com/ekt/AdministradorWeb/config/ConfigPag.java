package com.ekt.AdministradorWeb.config;


import com.ekt.AdministradorWeb.entity.Group;
import com.ekt.AdministradorWeb.entity.Respuesta;
import com.ekt.AdministradorWeb.entity.User;
import com.google.gson.Gson;
import okhttp3.*;
import org.bson.json.JsonObject;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.lang.reflect.Type;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;


@Controller
public class ConfigPag {

        @GetMapping("/login")
        public String login() {
            //return "paginas/organigrama/InicioOrganigrama";
             return "paginas/login";
        }

    @GetMapping("/inicioUsuarios")
    public String inicioUsuarios() {
        //return "paginas/organigrama/InicioOrganigrama";
        return "paginas/usuarios/InicioUsuarios";
    }

    @PostMapping("/entrar")
    public String Valida(@ModelAttribute User us) {
             //codigo de postman
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        System.out.println("correo: "+us.getCorreo()+"  contras: "+us.getPassword());
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"correo\": \""+us.getCorreo()+"\",\r\n    \"password\": "+us.getPassword()+",\r\n    \"token\":\"wesasasa\"\r\n}\r\n\r\n\r\n");
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/user/validate")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println("Peticion exitosa");
            JSONObject jsonObject= new JSONObject(response.body().string());
            if (jsonObject.get("data")!=""){
                return "paginas/usuarios/InicioUsuarios";
            }else{
                return "paginas/login";
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "paginas/login";
    }
    @GetMapping("/findAllUsuarios")
    public String findAllUsuarios(@ModelAttribute ArrayList<User> listaUsuarios, ModelMap model) {
        Gson gson = new Gson();
        //ArrayList<User> listaUsuarios = new ArrayList();
            //se realiza la peticion al back
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/user/findAll")
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            System.out.println(res);
            System.out.println("Peticion exitosa");
            JSONObject jsonObject= new JSONObject(res);
            //Separamos la parte de data
            JSONArray name1 = jsonObject.getJSONArray("data");

            //prueba de casteo

            System.out.println(name1.getJSONObject(1));
            System.out.println(name1.length());

            for (int i=0;i<name1.length();i++){
                listaUsuarios.add(gson.fromJson(name1.getJSONObject(i).toString(), User.class));
            }

            //User a = gson.fromJson(name1.getJSONObject(1).toString(), User.class);
            //System.out.println(a.getCorreo());

        }catch (Exception e){
            System.out.println("Error al realizar la consulta");
        }
        model.addAttribute("listaUsuarios",listaUsuarios);
        return "paginas/usuarios/InicioUsuarios";
        //return listaUsuarios;
    }

    @PostMapping("/CrearGrupo")
    public String CrearGrupo(@ModelAttribute Group gr) {
        System.out.println(gr.getName());
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"nombre\": \""+gr.getName()+"\"\r\n}");
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/grupo/crear")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return "paginas/usuarios/InicioUsuarios";

    }
}
