package com.ekt.AdministradorWeb.config;


import com.ekt.AdministradorWeb.DAO.GroupDAO;
import com.ekt.AdministradorWeb.DAO.UserDAO;
import com.ekt.AdministradorWeb.entity.*;
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

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.swing.text.Document;

@Controller
public class ConfigPag {

    UserDAO userDAO = new UserDAO();
    GroupDAO groupDAO = new GroupDAO();

    @GetMapping("/login")
    public String login() {
        //return "paginas/organigrama/InicioOrganigrama";
         return "paginas/login";
    }

    @GetMapping("/eliminaUsuario")
    public String muestraUsuariosGrupo(@ModelAttribute Group group, ModelMap model){
        User []usuarios = groupDAO.muestraUsuariosGrupo(group.getId());
        model.addAttribute("usuarios",usuarios);
        return "paginas/modalEliminaUsuario";
    }

    @PostMapping("/eliminarUsuarioGeneral")
    public String eliminarUsuario(@ModelAttribute(value = "id") String id,Model model){
        ArrayList<User> listaSubordinados = userDAO.muestraSubordinados(id);
        User user = userDAO.buscaID(id);


        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("http://localhost:3040/api/user/delete/"+id)
                .method("DELETE", body)
                .build();

        try {
            //limpia informacion del usuario en la db
            Response response = client.newCall(request).execute();
            System.out.println("el id es: "+id+"  eliminado con exito");
            //Verificar si tiene hijos
            if(listaSubordinados != null) {
                ArrayList<User> listaUsuarios = new ArrayList<>();
                User[] usuarios;
                usuarios = groupDAO.muestraUsuariosGrupo(user.getIDGrupo());
                for (User usuario : usuarios) {
                    if (!usuario.getID().equals(user.getID())) {
                        listaUsuarios.add(usuario);
                    }
                }
                model.addAttribute("listaSubordinados", listaSubordinados);
                model.addAttribute("listaUsuarios", listaUsuarios);
                model.addAttribute("idUsuario", id);
                return "paginas/usuarios/ReasignaSuperior";
            }else{

                return "redirect:/buscarTodosGrupos";
            }

        }catch (Exception e) {
            System.out.println("Error al eliminar usuario" +e);
        }
        return "redirect:/findAllUsuarios";
    }


    @PostMapping("/entrar")
    public String Valida(@ModelAttribute User us, RedirectAttributes redirectAttrs) {
        boolean res=userDAO.validaCorreoPassword(us);
        try {
            //si es true, entra a inicio, si es false regresa a login con un mensaje de error
            if (res){
                return "redirect:/Inicio";
            }else{
                redirectAttrs
                        .addFlashAttribute("mensaje", "Usuario o contrasena incorrectos");
                return "redirect:/login";
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "redirect:/error1";
        }
    }

    @GetMapping("/findAllUsuarios")
    public String findAllUsuarios(ModelMap model) {
        Gson gson = new Gson();
        ArrayList<User> listaUsuarios = new ArrayList<>();
        try {
            JSONArray name1 = userDAO.buscarTodosUsuarios(listaUsuarios);
            for (int i = 0; i < name1.length(); i++) {
                listaUsuarios.add(gson.fromJson(name1.getJSONObject(i).toString(), User.class));
            }
            model.addAttribute("listaUsuarios", listaUsuarios);
            return "paginas/usuarios/InicioUsuarios";
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "redirect:/error1";
        }
    }

    @PostMapping("/añadirUsuario")//*
    public String añadirUsuario(@ModelAttribute User user,RedirectAttributes redirectAttrs){
        System.out.println(user.getRFC());
        try {
            if(userDAO.creaUsuario(user)){
                System.out.println("creadoo");
                return "redirect:/findAllUsuarios";
            }else {
                System.out.println("no creado :(");
                redirectAttrs
                        .addFlashAttribute("mensaje", "El usuario ya existe");
                return "redirect:/findAllUsuarios";
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
            return "redirect:/error1";
        }
    }

    @RequestMapping(value="/editarUsuario",method = {RequestMethod.POST, RequestMethod.GET})
    @PostMapping("/editarUsuario")//*
    public String editarUsuario(@ModelAttribute(value = "id") String id,Model model,RedirectAttributes redirectAttrs){
        User user;
        try {
            user=userDAO.buscaID(id);
            if(user!=null) {
                model.addAttribute("user", user);
                return "/paginas/usuarios/EditarUsuario";
            }else {
                redirectAttrs
                        .addFlashAttribute("mensaje", "El usuario ya existe");
                return "redirect:/findAllUsuarios";
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "redirect:/error1";
        }


    }

    @PostMapping ("/editarUsuarioServicio")//*
    public String editarUsuarioServicio(@ModelAttribute User user,@ModelAttribute(value = "id") String id,RedirectAttributes redirectAttrs){
        Boolean bandera=false;
        User userDb;

        userDb=userDAO.buscaID(id);
        user.setID(id);
        user.setIDGrupo(userDb.getIDGrupo());
        System.out.println("en edita usuarioservicio:"+user.getIDGrupo());

        //editar informacion
        //editar usuario en grupo

       if(userDAO.editarUsuario(user) && groupDAO.editarUsuarioGrupo(user) ){
            bandera=true;
       }

        //si existen retornar error
        if (bandera){
            System.out.println("se modifico con exito");
            return "redirect:/findAllUsuarios";
        }else{
            System.out.println("Error al modificar usuario");
            redirectAttrs
                    .addFlashAttribute("mensaje", "Error al editar usuario, existen datos duplicasdos en la base de datos");
            return "redirect:/editarUsuario/?id="+user.getID();
        }
    }

    @PostMapping("/CrearGrupo")
    public String CrearGrupo(@ModelAttribute Group gr, RedirectAttributes redirectAttrs) {

        try {
            boolean res= groupDAO.crearGrupo(gr);
            //si es true regresa, si es false regresa con error de grupo ya existente
            if (res){
                return "redirect:/buscarTodosGrupos";
            }else{
                redirectAttrs
                        .addFlashAttribute("mensaje", "Grupo ya existente");
                return "redirect:/buscarTodosGrupos";
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "redirect:/error1";
        }

    }

    @PostMapping("/reasignaSuperior")
    public String reasignaSuperior(@ModelAttribute(value = "idUsuario") String idUsuario,@ModelAttribute(value = "origen") String origen, Model modelMap){
        System.out.println("origen: "+origen);
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
            //Si el origen proviene de vistaUsuarios cambiar el status a false
            return "paginas/usuarios/ReasignaSuperior";
        }else{
            System.out.println("Entra a desactivar");
            if(origen.equals("0")){
                userDAO.desactivarUsuario(idUsuario);
            }
            groupDAO.eliminaUsuarioGrupo(idUsuario,user.getIDGrupo());
            return "redirect:/editarGrupo?idGrupo=" + user.getIDGrupo();
        }
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

    @RequestMapping(value="/editarGrupo",method = { RequestMethod.POST, RequestMethod.GET })
    @PostMapping("/editarGrupo")
    public String editarGrupos(@ModelAttribute User user,@ModelAttribute(value = "idGrupo") String id,Model model) {
        System.out.println("id en editar: "+id);
        //añadir la lista de usuarios sin grupo
        model.addAttribute("listaDisponibles",userDAO.listaUsuariosDisponibles());

        //añadir lista de usuarios del organigrama
        model.addAttribute("listaUsuariosGrupo",userDAO.listaUsuariosOrganigrama(id));

        model.addAttribute("idGrupo",id);
        return "paginas/organigramas/editarOrganigrama";
    }

    @PostMapping("/ActualizaElimina")
    public String actualizaElimina(@ModelAttribute(value = "idUsuario") String idUsuario, @ModelAttribute(value = "idUser") String idUser, @ModelAttribute(value = "idBoss") String idBoss, ModelMap modelMap, Model model){
        userDAO.actualizaIdSuperior(idUser,idBoss);
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
            //Si el origen proviene de vistaUsuarios cambiar el status a false
            userDAO.desactivarUsuario(idUsuario);
            groupDAO.eliminaUsuarioGrupo(idUsuario,user.getIDGrupo());
            return "redirect:/editarGrupo?idGrupo=" + user.getIDGrupo();
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
    public String agregarUsuarioAGrupo(@ModelAttribute BodyAddUserGroup bodyAdd, RedirectAttributes redirectAttrs) {
        try{
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,
                     "{\r\n    \"idUsuario\":\""+bodyAdd.getIdUsuario()+
                            "\",\r\n    \"idGrupo\":\""+bodyAdd.getIdGrupo()+
                             "\",\r\n    \"idSuperior\":\""+bodyAdd.getIdSuperior()+
                             "\",\r\n    \"nombreRol\":\""+bodyAdd.getNombreRol()+"\"\r\n}\r\n\r\n");
            Request request = new Request.Builder()
                    .url("http://localhost:3040/api/grupo/agregarUsuario")
                    .method("PUT", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();

            JSONObject jsonObject= new JSONObject(response.body().string());

            if (jsonObject.get("status").toString().equals("OK")){
                return "redirect:/editarGrupo/?idGrupo=" + bodyAdd.getIdGrupo();
            }else{
                redirectAttrs
                        .addFlashAttribute("mensaje", jsonObject.get("msj").toString());
                return "redirect:/editarGrupo/?idGrupo=" + bodyAdd.getIdGrupo();
            }
        }catch (Exception e){
            System.err.println("Exepcion: "+e);
            return "redirect: /error1";
        }
    }

    @PostMapping("/editaUsuarioAGrupo")
    public String editaUsuarioAGrupo(@ModelAttribute(value = "idGrupo") String idGrupo,@ModelAttribute BodyAddUserGroup body, RedirectAttributes redirectAttrs){
        String respuesta;
        body.setIdGrupo(idGrupo);
        System.out.println("config: "+body.getIdGrupo()+"  "+body.getIdUsuario()+"  "+body.getIdSuperior()+"  "+body.getNombreRol());
        try {
            respuesta=groupDAO.reasignausuariogrupo(body);
            if(respuesta.equals("OK")){
                return "redirect:/editarGrupo/?idGrupo=" + body.getIdGrupo();
            }else{
                redirectAttrs
                        .addFlashAttribute("mensaje", respuesta);
                return "redirect:/editarGrupo/?idGrupo=" + body.getIdGrupo();
            }
        }catch (Exception e){
            System.err.println("Exepcion: "+e);
            redirectAttrs
                    .addFlashAttribute("mensaje", e);
            return "redirect:/editarGrupo/?idGrupo=" + body.getIdGrupo();

        }
    }


    @PostMapping("/verUsuario")
    public String verUsuario(@ModelAttribute(value = "id") String id,Model model,RedirectAttributes redirectAttrs){
        try {
            System.out.println("id:"+id);
        User user= userDAO.buscaID(id);
            if (user!=null){
                model.addAttribute("user", user);
                return "paginas/usuarios/verUsuario";
            }else{
                redirectAttrs
                        .addFlashAttribute("mensaje", "El usuario ya no existe");
                return "/findAllUsuarios";
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "redirect:/error1";
        }
    }

    @RequestMapping(value="/borrarGrupo",method = { RequestMethod.POST})
    @PostMapping("/borrarGrupo")
    public String borrarGrupo(@ModelAttribute(value = "idGrupo") String id,Model model) {
        //traza
        System.out.println("Entrando a borrar grupo");
        try{
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("http://localhost:3040/api/grupo/borrar/"+id)
                    .method("DELETE", body)
                    .build();
            Response response = client.newCall(request).execute();

            return "redirect:/buscarTodosGrupos";

        }catch (Exception e){
            System.err.println("Exepcion: "+e);
            return "redirect:/error1";
        }
    }

    @PostMapping("/busquedaUsuario")
    public String busquedaUsuario(@ModelAttribute(value = "parametro") String parametro, ModelMap modelMap){
        ArrayList<User> listaUsuarios = userDAO.busqedaUsuarios(parametro);
        modelMap.addAttribute("listaUsuarios",listaUsuarios);
        return "paginas/usuarios/InicioUsuarios";
    }

    @PostMapping("/busquedaOrganigrama")
    public String busquedaOrganigrama(@ModelAttribute(value = "parametro") String parametro, ModelMap model){
        ArrayList<Group> listaGrupos = new ArrayList<>();
        listaGrupos.add(groupDAO.busquedaGrupo(parametro));
        model.addAttribute("listaGrupos",listaGrupos);
        return "paginas/organigramas/inicioOrganigramas.html";
    }
}
