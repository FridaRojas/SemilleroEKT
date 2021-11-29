package com.ekt.AdministradorWeb.config;


import com.ekt.AdministradorWeb.entity.User;
import com.google.gson.Gson;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import com.ekt.AdministradorWeb.entity.Group;
import com.ekt.AdministradorWeb.entity.User;
import com.google.gson.Gson;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import com.ekt.AdministradorWeb.entity.Group;
import com.ekt.AdministradorWeb.entity.Respuesta;
import com.ekt.AdministradorWeb.entity.User;
import com.google.gson.Gson;
import okhttp3.*;
import org.bson.json.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/eliminaUsuario")
    public String eliminaUsuario(@ModelAttribute Group group, ModelMap model){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Gson gson = new Gson();
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/grupo/buscar/619d220c3cd67733b375db11")
                .method("GET", null)
                .build();
        try{
            Response response = client.newCall(request).execute();
            System.out.println(response);
            JSONObject jsonObject= new JSONObject(response.body().string());
            if (jsonObject.get("data")!=""){
                System.out.println("Aqui sistoy");
                JSONObject grupoObjeto = jsonObject.getJSONObject("data");
                System.out.println(grupoObjeto.toString());
                Group grupo  = gson.fromJson(grupoObjeto.toString(), Group.class);
                System.out.println(grupo.getUsuarios().length);
                User[] usuarios = grupo.getUsuarios();
                System.out.println(usuarios.length);
                for(User usuariooooo: usuarios){
                    System.out.println(usuariooooo.getNombre());
                }
                model.addAttribute("usuarios",usuarios);
                return "paginas/modalEliminaUsuario";
            }else{
                return "paginas/login";
            }
        }catch (Exception e){
            System.out.println("No se puede realizar la petición");
        }
        return "paginas/modalEliminaUsuario";
    }
    @PostMapping("/entrar")
    public String Valida(@ModelAttribute User us, RedirectAttributes redirectAttrs) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        System.out.println("correo: "+us.getCorreo()+"  contras: "+us.getPassword());
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"correo\": \""+us.getCorreo()+"\",\r\n    \"password\": \""+us.getPassword()+"\",\r\n    \"token\":\"wesasasa\"\r\n}\r\n\r\n\r\n");
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/user/validate")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject= new JSONObject(response.body().string());

            if (!jsonObject.get("data").toString().equals("")){
                return "redirect:/findAllUsuarios";
            }else{
                redirectAttrs
                        .addFlashAttribute("mensaje", "Usuario o contrasena incorrectos");
                return "redirect:/login";
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "";
        }
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
            JSONObject jsonObject= new JSONObject(res);
            //Separamos la parte de data
            JSONArray name1 = jsonObject.getJSONArray("data");

            //prueba de casteo

            for (int i=0;i<name1.length();i++){
                listaUsuarios.add(gson.fromJson(name1.getJSONObject(i).toString(), User.class));
            }

        }catch (Exception e){
            System.out.println("Error al realizar la consulta");
        }
        model.addAttribute("listaUsuarios",listaUsuarios);
        return "paginas/usuarios/InicioUsuarios";
        //return listaUsuarios;
    }

    @GetMapping("/añadirUsuario")
    public String añadirUsuario(@ModelAttribute User user){
        //realizar el guardado


            return "paginas/usuarios/AñadirUsuario";
    }


    @PostMapping("/CrearGrupo")
    public String CrearGrupo(@ModelAttribute Group gr) {
        System.out.println(gr.getNombre());
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"nombre\": \""+gr.getNombre()+"\"\r\n}");
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

        return "redirect:/findAllUsuarios";

    }
}
