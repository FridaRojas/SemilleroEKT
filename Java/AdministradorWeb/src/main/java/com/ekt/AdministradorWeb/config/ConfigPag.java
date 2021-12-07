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
import java.util.List;

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
                return "redirect:/findAllUsuarios";
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
                redirectAttrs
                        .addFlashAttribute("status", "success")
                        .addFlashAttribute("mensaje", "Usuario Creado Correctamente");
                return "redirect:/findAllUsuarios";
            }else {
                System.out.println("no creado :(");
                redirectAttrs
                        .addFlashAttribute("status", "danger")
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
                        .addFlashAttribute("status", "danger")
                        .addFlashAttribute("mensaje", "El usuario no existe");
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

        //se guarda la informacion para que se mande completa
        userDb=userDAO.buscaID(id);
        user.setID(userDb.getID());
        user.setIDGrupo(userDb.getIDGrupo());
        user.setNombreRol(userDb.getNombreRol());
        user.setIDSuperiorInmediato(userDb.getIDSuperiorInmediato());

        //editar informacion
        //editar usuario en grupo

        //vefiricar si tiene un grupo asignado para editarlo tambien
        if (!userDb.getIDGrupo().equals("")){
            System.out.println("Entra a grupo lleno");
            if(userDAO.editarUsuario(user) && groupDAO.editarUsuarioGrupo(user) ){
                bandera=true;
            }
        }else{
            System.out.println("Entra a grupo vacio");
            if(userDAO.editarUsuario(user)){
                bandera=true;
            }
        }

        //si existen retornar error
        if (bandera){
            System.out.println("se modifico usuario con exito");
            redirectAttrs
                    .addFlashAttribute("status", "success")
                    .addFlashAttribute("mensaje", "Usuario modificado con exito");
            return "redirect:/findAllUsuarios";
        }else{
            System.out.println("Error al modificar usuario");
            redirectAttrs
                    .addFlashAttribute("status", "danger")
                    .addFlashAttribute("mensaje", "Error al editar usuario, existen datos duplicados en la base de datos");
            return "redirect:/editarUsuario/?id="+user.getID();
        }
    }

    @PostMapping("/CrearGrupo")
    public String CrearGrupo(@ModelAttribute Group gr, RedirectAttributes redirectAttrs) {

        try {
            boolean res= groupDAO.crearGrupo(gr);
            //si es true regresa, si es false regresa con error de grupo ya existente
            if (res){
                redirectAttrs
                        .addFlashAttribute("status", "success")
                        .addFlashAttribute("mensaje", "Grupo creado correctamente");
                return "redirect:/buscarTodosGrupos";
            }else{
                redirectAttrs
                        .addFlashAttribute("status", "danger")
                        .addFlashAttribute("mensaje", "Grupo ya existente");
                return "redirect:/buscarTodosGrupos";
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "redirect:/error1";
        }

    }


    @PostMapping("/remplazaUsuario")
    public String remplazaUsuario(@ModelAttribute(value = "idUsuarioMenos") String idUsuarioMenos,@ModelAttribute(value = "idUsuarioMas") String idUsuarioMas, Model modelMap,RedirectAttributes redirectAttrs){

        System.out.println("USUARIOmENOS: "+idUsuarioMenos+"   mas: "+idUsuarioMas);
        User userNuevo,userViejo;
        //dos casos
        userViejo=userDAO.buscaID(idUsuarioMenos);
        userNuevo=userDAO.buscaID(idUsuarioMas);

        //fuera del organigrama

        if (userDAO.buscarOrigenUsuario(idUsuarioMas)) {
            System.out.println("entra a caso fuera del organigrama");
            //añadir informacion inicial al que se queda

            //guardar en grupo y en lista genersl
            groupDAO.añadeUsuarioGrupo(idUsuarioMas, userViejo.getIDGrupo(), userViejo.getIDSuperiorInmediato(), userViejo.getNombreRol());

            //falta reasignar hijos
            if (userDAO.muestraSubordinados(idUsuarioMenos)!=null){
                for (User us : userDAO.muestraSubordinados(idUsuarioMenos)) {
                    userDAO.actualizaIdSuperior(us.getID(), idUsuarioMas);
                }
            }
            //eliminar al viejo
            groupDAO.eliminaUsuarioGrupo(userViejo.getID(), userViejo.getIDGrupo());

            redirectAttrs
                    .addFlashAttribute("status", "success")
                    .addFlashAttribute("mensaje", "Usuario eliminado correctamente");
            return "redirect:/editarGrupo?idGrupo=" + userViejo.getIDGrupo();
        }else{
            //reasigna los hijos de los dos

            //falta reasignar hijos
            ArrayList<User> listUserMas=userDAO.muestraSubordinados(idUsuarioMas);
            ArrayList<User> lisyUserMenos=userDAO.muestraSubordinados(idUsuarioMenos);

            //falta reasignar hijos
            if (lisyUserMenos!=null){
                for (User us : userDAO.muestraSubordinados(idUsuarioMenos)) {
                    System.out.println("Entra a actualizar hijos de menos");
                    userDAO.actualizaIdSuperior(us.getID(), idUsuarioMas);
                }
            }

            if (listUserMas!=null){
                for (User us : userDAO.muestraSubordinados(idUsuarioMas)) {
                    System.out.println("Entra a actualizar hijos de mas");
                    userDAO.actualizaIdSuperior(us.getID(), idUsuarioMas);
                }
            }

            //eliminar
            groupDAO.eliminaUsuarioGrupo(idUsuarioMenos, userViejo.getIDGrupo());
            groupDAO.eliminaUsuarioGrupo(idUsuarioMas, userViejo.getIDGrupo());

            //guardar en grupo y en lista genersl
            if (idUsuarioMas.equals(userViejo.getIDSuperiorInmediato())){
                System.out.println("entra al caso 1");
                groupDAO.añadeUsuarioGrupo(idUsuarioMenos, userNuevo.getIDGrupo(), userNuevo.getIDSuperiorInmediato(), userNuevo.getNombreRol());
                groupDAO.añadeUsuarioGrupo(idUsuarioMas, userViejo.getIDGrupo(), idUsuarioMenos, userViejo.getNombreRol());
            }else{
                if(userNuevo.getIDSuperiorInmediato().equals(idUsuarioMenos)){
                    System.out.println("entra al caso 2");
                    groupDAO.añadeUsuarioGrupo(idUsuarioMenos, userNuevo.getIDGrupo(), userNuevo.getIDSuperiorInmediato(), userNuevo.getNombreRol());
                    groupDAO.añadeUsuarioGrupo(idUsuarioMas, userViejo.getIDGrupo(), idUsuarioMenos, userViejo.getNombreRol());
                }else{
                    System.out.println("entra al caso 3");
                    groupDAO.añadeUsuarioGrupo(idUsuarioMenos,userNuevo.getIDGrupo(),userNuevo.getIDSuperiorInmediato(),userNuevo.getNombreRol());
                    groupDAO.añadeUsuarioGrupo(idUsuarioMas,userNuevo.getIDGrupo(),userViejo.getIDSuperiorInmediato(),userViejo.getNombreRol());
                }
            }



            redirectAttrs
                    .addFlashAttribute("status", "success")
                    .addFlashAttribute("mensaje", "Usuario eliminado correctamente");
            return "redirect:/editarGrupo?idGrupo=" + userViejo.getIDGrupo();
        }
    }

    @PostMapping("/reasignaSuperior")
    public String reasignaSuperior(@ModelAttribute(value = "idUsuario") String idUsuario,@ModelAttribute(value = "origen") String origen, Model modelMap,RedirectAttributes redirectAttrs){
        System.out.println("origen: "+origen);
        ArrayList<User> listaSubordinados = userDAO.muestraSubordinados(idUsuario);
        User user = userDAO.buscaID(idUsuario);
        if(listaSubordinados != null) {
            //si tiene suboordinados, redirecciona a vista de reasignar superior
            ArrayList<User> listaUsuarios = new ArrayList<>();
            User[] usuarios;
            usuarios = groupDAO.muestraUsuariosGrupo(user.getIDGrupo());
            for (User usuario : usuarios) {
                if (!usuario.getID().equals(user.getID())) {
                    listaUsuarios.add(usuario);
                }
            }
            if(user.getIDSuperiorInmediato().equals("")){
                modelMap.addAttribute("userDelete",true);
            }else{
                modelMap.addAttribute("userDelete",false);
            }
            modelMap.addAttribute("listaSubordinados", listaSubordinados);
            modelMap.addAttribute("listaUsuarios", listaUsuarios);
            modelMap.addAttribute("idUsuario", idUsuario);
            modelMap.addAttribute("origen", origen);

            return "paginas/usuarios/ReasignaSuperior";
        }else{
            //si no tiene suboordinados, elimina y redirecciona a editarGrupo
            groupDAO.eliminaUsuarioGrupo(idUsuario,user.getIDGrupo());
            //se añade nuevo usuario al grupo
            if(origen.equals("0")){
                //Si el origen proviene de vistaUsuarios cambiar el status a false y redirecciona a findallusuarios
                userDAO.desactivarUsuario(idUsuario);
                redirectAttrs
                        .addFlashAttribute("status", "success")
                        .addFlashAttribute("mensaje", "Usuario eliminado correctamente");
                return "redirect:/findAllUsuarios";
            }else{
                //si origen proviene de grupos, redirecciona a edita grupo
                redirectAttrs
                        .addFlashAttribute("status", "success")
                        .addFlashAttribute("mensaje", "Usuario eliminado correctamente");
                return "redirect:/editarGrupo?idGrupo=" + user.getIDGrupo();
            }

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

        //añadir usuarios con los que se pueden sutituir
        ArrayList<User> posiblesSustituir = new ArrayList();
        posiblesSustituir.addAll(userDAO.listaUsuariosDisponibles());
        //posiblesSustituir.addAll(userDAO.listaUsuariosOrganigrama(id));

        model.addAttribute("listaSustitucion",posiblesSustituir);

        //añadir la lista de usuarios sin grupo
        model.addAttribute("listaDisponibles",userDAO.listaUsuariosDisponibles());

        //añadir lista de usuarios del organigrama
        model.addAttribute("listaUsuariosGrupo",userDAO.listaUsuariosOrganigrama(id));

        model.addAttribute("idGrupo",id);
        return "paginas/organigramas/editarOrganigrama";
    }

    @PostMapping("/ActualizaElimina")
    public String actualizaElimina(@ModelAttribute(value = "idUsuario") String idUsuario,@ModelAttribute(value = "origen") String origen,@ModelAttribute(value = "idUser") String idUser, @ModelAttribute(value = "idBoss") String idBoss, ModelMap modelMap, Model model,RedirectAttributes redirectAttrs){
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
            modelMap.addAttribute("origen", origen);
            return "paginas/usuarios/ReasignaSuperior";
        }else{
            //si no tiene suboordinados, elimina y redirecciona a editarGrupo
            System.out.println("Entra a desactivar");
            groupDAO.eliminaUsuarioGrupo(idUsuario,user.getIDGrupo());
            if(origen.equals("0")){
                //Si el origen proviene de vistaUsuarios cambiar el status a false y redirecciona a findallusuarios
                userDAO.desactivarUsuario(idUsuario);
                redirectAttrs
                        .addFlashAttribute("status", "success")
                        .addFlashAttribute("mensaje", "Usuario eliminado correctamente");
                return "redirect:/findAllUsuarios";
            }else{
                //si origen proviene de grupos, redirecciona a edita grupo
                redirectAttrs
                        .addFlashAttribute("status", "success")
                        .addFlashAttribute("mensaje", "Usuario eliminado correctamente");
                return "redirect:/editarGrupo?idGrupo=" + user.getIDGrupo();
            }
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
                        .addFlashAttribute("status", "success")
                        .addFlashAttribute("mensaje", "Usuario agregado correctamente");
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
                redirectAttrs
                        .addFlashAttribute("status", "success")
                        .addFlashAttribute("mensaje", "Usuario editado correctamente");
                return "redirect:/editarGrupo/?idGrupo=" + body.getIdGrupo();
            }else{
                redirectAttrs
                        .addFlashAttribute("status", "danger")
                        .addFlashAttribute("mensaje", "Hubo un problema al editar al usuario");
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
        if(groupDAO.busquedaGrupo(parametro)!=null) {
            listaGrupos.add(groupDAO.busquedaGrupo(parametro));
            model.addAttribute("listaGrupos", listaGrupos);
        }
        return "paginas/organigramas/inicioOrganigramas.html";
    }
}
