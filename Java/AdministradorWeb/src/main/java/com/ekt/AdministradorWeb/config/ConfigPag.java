package com.ekt.AdministradorWeb.config;


import com.ekt.AdministradorWeb.entity.Group;
import com.ekt.AdministradorWeb.DAO.GroupDAO;
import com.ekt.AdministradorWeb.DAO.UserDAO;
import com.ekt.AdministradorWeb.entity.User;
import com.google.gson.Gson;
import okhttp3.*;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;
import com.ekt.AdministradorWeb.entity.Group;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ConfigPag {

    UserDAO userDAO = new UserDAO();
    GroupDAO groupDAO = new GroupDAO();

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



    @GetMapping("/añadirUsuarioPage")
    public String añadirUsuarioPage(){
        return "paginas/usuarios/AñadirUsuario";
    }


    @PostMapping("/eliminarUsuario")
    public String eliminarUsuario(@ModelAttribute(value = "id") String id){

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/user/delete/"+id)
                .method("DELETE", body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println("el id es: "+id+"  eliminado con exito");
        }catch (Exception e) {
            System.out.println("Error al eliminar usuario" +e);
        }
        return "redirect:/findAllUsuarios";
    }

    @GetMapping("/findAllUsuarios")
    public String findAllUsuarios( ModelMap model) {
         ArrayList<User> listaUsuarios = new ArrayList();
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
            //System.out.println(res);
            System.out.println("Peticion exitosa");
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

    @PostMapping("/añadirUsuario")
    public String añadirUsuario(@ModelAttribute User user){
        //realizar el guardado
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n  \"correo\":\""+user.getCorreo()+"\",\r\n    \"fechaInicio\":\"" +user.getFechaInicio()+"\",\r\n    \"fechaTermino\":\""+user.getFechaTermino()+"\",\r\n    \"numeroEmpleado\":\""+user.getNumeroEmpleado()+"\",\r\n    \"nombre\":\""+user.getNombre()+"\",\r\n    \"password\": \""+user.getPassword()+"\",\r\n    \"nombreRol\": \"\",\r\n    \"idGrupo\": \"\",\r\n    \"opcionales\": [],\r\n    \"token\": \"\",\r\n    \"telefono\":\" "+user.getTelefono()+"\",\r\n    \"idSuperiorInmediato\": \"\",\r\n    \"statusActivo\": \"true\",\r\n    \"curp\":\" "+user.getCurp()+"\",\r\n    \"rfc\":\" "+user.getRFC()+"\"\r\n}");
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/user/create")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();

        }catch (Exception e){
            System.out.println("Error al insertar usuario");
        }
        return "redirect:/findAllUsuarios";
    }

    @PostMapping("/editarUsuario")
    public String editarUsuario(@ModelAttribute(value = "id") String id,Model model){
        //buscar al usuario
        Gson gson = new Gson();
        User user = new User();

        System.out.println("el usuarios es: "+id);

        //buscar usuario
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/user/find/"+id)
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject= new JSONObject(response.body().string());
            //Separamos la parte de data
            JSONObject name1 = jsonObject.getJSONObject("data");
            user =gson.fromJson(name1.toString(), User.class);
            model.addAttribute("user",user);
        }catch (Exception e){
            System.out.println("error al castear el usuario");
        }

        return "/paginas/usuarios/EditarUsuario";
    }

    @PostMapping ("/editarUsuarioServicio")
    public String editarUsuarioServicio(@ModelAttribute User user){
        //validar que los datos no existan


        //si no existen editar

        //si existen retornar error
        return "";
    }

    @PostMapping("/CrearGrupo")
    public String CrearGrupo(@ModelAttribute Group gr) {
        System.out.println(gr.getNombre());
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
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return "redirect:/findAllUsuarios";

    }

    @PostMapping("/reasignaSuperior")
    public String reasignaSuperior(@ModelAttribute(value = "idUsuario") String idUsuario, Model modelMap, RedirectAttributes redirectAttributes){
        ArrayList<User> listaUsuarios = userDAO.muestraSubordinados(idUsuario);
        String idGrupo = "1234";
        modelMap.addAttribute("listaUsuarios",listaUsuarios);
        modelMap.addAttribute("idUsuario", idUsuario);
        //redirectAttributes.addFlashAttribute("idGrupo", idGrupo);
        return "paginas/usuarios/ReasignaSuperior";
    }

    @PostMapping("/EliminaActualiza")
    public String eliminaActualiza(@ModelAttribute(value = "idUsuario") String idUsuario){
        System.out.println(idUsuario);
        return "";
    }


    @GetMapping("/inicioGrupos")
    public String inicioGrupos() {
        return "paginas/organigramas/inicioOrganigramas";
    }


    @GetMapping("/buscarTodosGrupos")
    public String buscarTodosGrupos(@ModelAttribute ArrayList<Group> listaGrupos, ModelMap model) {
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/grupo/buscarTodo")
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String res = response.body().string();

            JSONObject jsonObject= new JSONObject(res);

            JSONArray name1 = jsonObject.getJSONArray("data");

            for (int i=0;i<name1.length();i++){
                listaGrupos.add(gson.fromJson(name1.getJSONObject(i).toString(), Group.class));
            }

        }catch (Exception e){
            System.out.println("Error al realizar la consulta");
        }
        model.addAttribute("listaGrupos",listaGrupos);

        return "paginas/organigramas/inicioOrganigramas.html";
    }

    @GetMapping("/editarGrupo")
    public String editarGrupos() {
        return "paginas/organigramas/editarOrganigrama";
    }


}
