package com.ekt.AdministradorWeb.config;


import com.ekt.AdministradorWeb.DAO.GroupDAO;
import com.ekt.AdministradorWeb.DAO.UserDAO;
import com.ekt.AdministradorWeb.entity.User;
import com.google.gson.Gson;
import okhttp3.*;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;
import com.ekt.AdministradorWeb.entity.User;
import com.google.gson.Gson;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import com.ekt.AdministradorWeb.entity.User;
import com.google.gson.Gson;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import com.ekt.AdministradorWeb.entity.User;
import com.google.gson.Gson;
import okhttp3.*;
import com.ekt.AdministradorWeb.entity.Group;
import com.ekt.AdministradorWeb.entity.User;
import com.google.gson.Gson;
import okhttp3.*;
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
    public String muestraUsuariosGrupo(@ModelAttribute Group group, ModelMap model){
        User []usuarios = groupDAO.muestraUsuariosGrupo(group.getId());
        model.addAttribute("usuarios",usuarios);
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
                return "redirect:/Inicio";
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
    public String editarUsuarioServicio(@ModelAttribute User user,@ModelAttribute(value = "id") String id,RedirectAttributes redirectAttrs){
        Boolean bandera=false;
        user.setID(id);

        //validar que los datos no existan
       if (!userDAO.existusuario(user)) {
           //editar informacion
           if(userDAO.editarUsuario(user)){
                bandera=true;
           }
       }
        //si existen retornar error
        if (bandera){
            System.out.println("se modifico con exito");
            return "redirect:/findAllUsuarios";
        }else{
            System.out.println("Error al modificar usuario");
            redirectAttrs
                    .addFlashAttribute("mensaje", "Error al editar usuario, existen datos duplicasdos en la base de datos");
            return "redirect:/findAllUsuarios";
        }
    }

    @PostMapping("/CrearGrupo")
    public String CrearGrupo(@ModelAttribute Group gr, RedirectAttributes redirectAttrs) {
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
            JSONObject jsonObject= new JSONObject(response.body().string());

            if (jsonObject.get("status").toString().equals("OK")){
                return "redirect:/inicioGrupos";
            }else{
                redirectAttrs
                        .addFlashAttribute("mensaje", "Grupo ya existente");
                return "redirect:/inicioGrupos";
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "";
        }

    }

    @PostMapping("/reasignaSuperior")
    public String reasignaSuperior(@ModelAttribute(value = "idUsuario") String idUsuario, Model modelMap){
        ArrayList<User> listaSubordinados = userDAO.muestraSubordinados(idUsuario);
        User user = userDAO.buscaID(idUsuario);
        if(listaSubordinados != null) {
            ArrayList<User> listaUsuarios = new ArrayList<>();
            User[] usuarios;
            usuarios = groupDAO.muestraUsuariosGrupo(user.getIDGrupo());
            for (User usuario : usuarios) {
                if (!usuario.getID().equals(user.getID())) {
                    listaUsuarios.add(usuario);
                }
            }
            modelMap.addAttribute("listaSubordinados", listaSubordinados);
            modelMap.addAttribute("listaUsuarios", listaUsuarios);
            modelMap.addAttribute("idUsuario", idUsuario);
            return "paginas/usuarios/ReasignaSuperior";
        }else{
            groupDAO.eliminaUsuarioGrupo(idUsuario,user.getIDGrupo());
            return "paginas/organigramas/editarOrganigrama";
        }
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

    @PostMapping("/ActualizaElimina")
    public String actualizaElimina(@ModelAttribute(value = "idUsuario") String idUsuario, @ModelAttribute(value = "idUser") String idUser, @ModelAttribute(value = "idBoss") String idBoss, ModelMap modelMap){
        userDAO.actualizaIdSujperior(idUser,idBoss);

        ArrayList<User> listaSubordinados = userDAO.muestraSubordinados(idUsuario);
        User user = userDAO.buscaID(idUsuario);
        if(listaSubordinados != null) {
            ArrayList<User> listaUsuarios = new ArrayList<>();
            User[] usuarios;
            usuarios = groupDAO.muestraUsuariosGrupo(user.getIDGrupo());
            for (User usuario : usuarios) {
                if (!usuario.getID().equals(user.getID())) {
                    listaUsuarios.add(usuario);
                }
            }
            modelMap.addAttribute("listaSubordinados", listaSubordinados);
            modelMap.addAttribute("listaUsuarios", listaUsuarios);
            modelMap.addAttribute("idUsuario", idUsuario);
            return "paginas/usuarios/ReasignaSuperior";
        }else{
            groupDAO.eliminaUsuarioGrupo(idUsuario,user.getIDGrupo());
            return "paginas/organigramas/editarOrganigrama";
        }


    }

    @PostMapping("/buscarGrupo")
    public String buscarGrupo(String nombre){
        try {
            ArrayList<Group> listaGrupos=new ArrayList<>();
            Gson gson = new Gson();
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://localhost:3040/api/grupo/buscarPorNombre/"+nombre)
                    .method("GET", null)
                    .build();

            Response response = client.newCall(request).execute();

            String res = response.body().string();


            JSONObject jsonObject= new JSONObject(res);

            //JSONArray name1 = jsonObject.getJSONArray("data");

            //listaGrupos.add(gson.fromJson(name1.getJSONObject(0).toString(), Group.class));

            System.out.println(jsonObject.toString());

        }catch (Exception e){
            System.err.println("Exception"+e);
            return "/error1";
        }

        return null;
    }

    @GetMapping("/error1")
    public String error() {
        return "paginas/error.html";
    }



    @GetMapping("/Inicio")
    public String Inicio(){
        return "paginas/Inicio";
    }

    @PostMapping("/agregarUsuarioAGrupo")
    public String agregarUsuarioAGrupo(@ModelAttribute Group gr, RedirectAttributes redirectAttrs) {
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
            JSONObject jsonObject= new JSONObject(response.body().string());

            if (jsonObject.get("status").toString().equals("OK")){
                return "redirect:/inicioGrupos";
            }else{
                redirectAttrs
                        .addFlashAttribute("mensaje", "Grupo ya existente");
                return "redirect:/inicioGrupos";
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "";
        }

    }

    @GetMapping("/verUsuario")
    public String verUsuario(@ModelAttribute(value = "id") String id,Model model,RedirectAttributes redirectAttrs){
            User user= userDAO.buscaID("618b05c12d3d1d235de0ade0");
            if (user!=null){
                model.addAttribute("user", user);
                return "paginas/usuarios/verUsuario";
            }else{
                redirectAttrs
                        .addFlashAttribute("mensaje", "El usuario ya no existe");
                return "/findAllUsuarios";
            }

    }

}
