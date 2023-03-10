package com.ekt.AdministradorWeb.config;

import com.ekt.AdministradorWeb.DAO.GroupDAO;
import com.ekt.AdministradorWeb.DAO.UserDAO;
import com.ekt.AdministradorWeb.entity.*;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import com.ekt.AdministradorWeb.entity.User;
import com.ekt.AdministradorWeb.entity.Group;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.ArrayList;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;

@Controller
public class ConfigPag {

    UserDAO userDAO = new UserDAO();
    GroupDAO groupDAO = new GroupDAO();

    @GetMapping("/login")
    public String login() {
         return "paginas/login";
    }

    //Muestra los usuarios de un grupo
    @GetMapping("/eliminaUsuario")
    public String muestraUsuariosGrupo(@ModelAttribute Group group, ModelMap model, HttpSession session){
        try {
            //Valida si está activa la sesión
            if (session.getAttribute("user") != null && (boolean) session.getAttribute("user")) {
                User[] usuarios = groupDAO.muestraUsuariosGrupo(group.getId());
                model.addAttribute("usuarios", usuarios);
                return "paginas/modalEliminaUsuario";
            } else {
                return "redirect:/login";
            }
        }catch (Exception e){
            return "redirect:/error1";
        }
    }


    @PostMapping("/entrar")
    public String Valida(@ModelAttribute User us, RedirectAttributes redirectAttrs, HttpSession session) {
        boolean res=userDAO.validaCorreoPassword(us);
        try {
            //si es true, entra a inicio, si es false regresa a login con un mensaje de error
            if (res){
                session.setAttribute("user", res);
                session.setMaxInactiveInterval(10*60);
                return "redirect:/findAllUsuarios";
            }else{
                session.setAttribute("user", res);
                redirectAttrs
                        .addFlashAttribute("mensaje", "Usuario o contrasena incorrectos");
                return "redirect:/login";
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "redirect:/error1";
        }
    }

    /**
     *Busca a todos los usuarios y retorna a la pagina inicial de usuarios
     * @param model
     * @param session parametro correspondiente al manejo de la sesion
     * @return lista de usuarios en el Model
     * @return Pagina Inicial de Usuarios
     */
    @GetMapping("/findAllUsuarios")
    public String findAllUsuarios(ModelMap model, HttpSession session) {
        Gson gson = new Gson();
        ArrayList<User> listaUsuarios = new ArrayList<>();
        if (session.getAttribute("user")!= null && (boolean) session.getAttribute("user")) {
            try {
                JSONArray name1 = userDAO.buscarTodosUsuarios(listaUsuarios);
                for (int i = 0; i < name1.length(); i++) {
                    listaUsuarios.add(gson.fromJson(name1.getJSONObject(i).toString(), User.class));
                }
                model.addAttribute("listaUsuarios", listaUsuarios);
                return "paginas/usuarios/InicioUsuarios";
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return "redirect:/error1";
            }
        } else {
            return "redirect:/login";
        }
    }

    /**
     *Añade un usuario nuevo, unicamente con informacion personal
     * @param user Se recibe un Usuario para ser añadido
     * @param redirectAttrs parametro para añadir las alertas
     * @param session parametro para el manejo de la sesion
     * @return a la pagina inicial de Usuarios
     */
    @PostMapping("/añadirUsuario")//*
    public String añadirUsuario(@ModelAttribute User user, RedirectAttributes redirectAttrs, HttpSession session){
        try {
            if (session.getAttribute("user") != null && (boolean) session.getAttribute("user")) {
                if(userDAO.creaUsuario(user)) {
                    redirectAttrs
                            .addFlashAttribute("status", "success")
                            .addFlashAttribute("mensaje", "Usuario Creado Correctamente");
                    return "redirect:/findAllUsuarios";
                } else {
                    System.out.println("no creado :(");
                    redirectAttrs
                            .addFlashAttribute("status", "danger")
                            .addFlashAttribute("mensaje", "El usuario ya existe");
                    return "redirect:/findAllUsuarios";
                }
            }else {
                return "redirect:/login";
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "redirect:/error1";
        }
    }


    /**
     *Se llena un formulario con la informacion personal de un usuario y asi pueda ser editada
     * @param id parametro correspondiente al id de un Usuario
     * @param model
     * @param redirectAttrs parametro donde se añaden las alertas
     * @param session parametro correspondiente al manejo de la sesion
     * @return Se retorna a la pagina EditarUsuario
     */
    @RequestMapping(value="/editarUsuario",method = {RequestMethod.POST, RequestMethod.GET})
    @PostMapping("/editarUsuario")
    public String editarUsuario(@ModelAttribute(value = "id") String id,Model model,RedirectAttributes redirectAttrs, HttpSession session){
        User user;
        try {
            if (session.getAttribute("user")!= null && (boolean) session.getAttribute("user")) {
                user = userDAO.buscaID(id);
                if (user != null) {
                    model.addAttribute("user", user);
                    return "/paginas/usuarios/EditarUsuario";
                } else {
                    redirectAttrs
                            .addFlashAttribute("status", "danger")
                            .addFlashAttribute("mensaje", "El usuario no existe");
                    return "redirect:/findAllUsuarios";
                }
            }else {
                return "redirect:/login";
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "redirect:/error1";
        }


    }


    /**
     * Se edita la informacion personal en un usuario
     * @param user parametro con la informacion de un Usurio a editar
     * @param id parametro correspondiente al id de un Usuario
     * @param redirectAttrs parametro donde se añaden las alertas
     * @param session parametro correspondiente al manejo de la sesion
     * @return retorna a pagina Inicial de Usuarios
     * @return  retorna a la misma pagina en caso de ocurrir un error
     */
    @PostMapping ("/editarUsuarioServicio")//*
    public String editarUsuarioServicio(@ModelAttribute User user,@ModelAttribute(value = "id") String id,RedirectAttributes redirectAttrs, HttpSession session){
        Boolean bandera=false;
        User userDb;
        if (session.getAttribute("user")!= null && (boolean) session.getAttribute("user")) {
            //se guarda la informacion para que se mande completa
            userDb = userDAO.buscaID(id);
            user.setID(userDb.getID());
            user.setIDGrupo(userDb.getIDGrupo());
            user.setNombreRol(userDb.getNombreRol());
            user.setIDSuperiorInmediato(userDb.getIDSuperiorInmediato());

            //vefiricar si tiene un grupo asignado para editarlo tambien
            if (!userDb.getIDGrupo().equals("")) {
                if (userDAO.editarUsuario(user) && groupDAO.editarUsuarioGrupo(user)) {
                    bandera = true;
                }
            } else {
                if (userDAO.editarUsuario(user)) {
                    bandera = true;
                }
            }
            //si existen retornar error
            if (bandera) {
                System.out.println("se modifico usuario con exito");
                redirectAttrs
                        .addFlashAttribute("status", "success")
                        .addFlashAttribute("mensaje", "Usuario modificado con exito");
                return "redirect:/findAllUsuarios";
            } else {
                System.out.println("Error al modificar usuario");
                redirectAttrs
                        .addFlashAttribute("status", "danger")
                        .addFlashAttribute("mensaje", "Error al editar usuario, existen datos duplicados en la base de datos");
                return "redirect:/editarUsuario/?id=" + user.getID();
            }
        }else {
            return "redirect:/login";
        }
    }

    @PostMapping("/CrearGrupo")
    public String CrearGrupo(@ModelAttribute Group gr, RedirectAttributes redirectAttrs, HttpSession session) {
        try {
            if (session.getAttribute("user")!= null && (boolean) session.getAttribute("user")) {
                boolean res = groupDAO.crearGrupo(gr);
                //si es true regresa, si es false regresa con error de grupo ya existente
                if (res) {
                    redirectAttrs
                            .addFlashAttribute("status", "success")
                            .addFlashAttribute("mensaje", "Grupo creado correctamente");
                    return "redirect:/buscarTodosGrupos";
                } else {
                    redirectAttrs
                            .addFlashAttribute("status", "danger")
                            .addFlashAttribute("mensaje", "Grupo ya existente");
                    return "redirect:/buscarTodosGrupos";
                }
            }else {
                return "redirect:/login";
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "redirect:/error1";
        }
    }

    @PostMapping("/remplazaUsuario")
    public String remplazaUsuario(@ModelAttribute(value = "idUsuarioMenos") String idUsuarioMenos,@ModelAttribute(value = "idUsuarioMas") String idUsuarioMas, Model modelMap,RedirectAttributes redirectAttrs, HttpSession session){
        if (session.getAttribute("user")!= null && (boolean) session.getAttribute("user")) {
            User userMas,userMenos;
            //dos casos
            userMenos=userDAO.buscaID(idUsuarioMenos);
            userMas=userDAO.buscaID(idUsuarioMas);
            System.out.println("mas:"+userMas.toString());
            System.out.println("menos:"+userMenos.toString());
            //fuera del organigrama

                //añadir informacion inicial al que se queda
                //guardar en grupo y en lista genersl
                groupDAO.añadeUsuarioGrupo(idUsuarioMas, userMenos.getIDGrupo(), userMenos.getIDSuperiorInmediato(), userMenos.getNombreRol());
                //falta reasignar hijos
                if (userDAO.muestraSubordinados(idUsuarioMenos)!=null){
                    for (User us : userDAO.muestraSubordinados(idUsuarioMenos)) {
                        userDAO.actualizaIdSuperior(us.getID(), idUsuarioMas);
                    }
                }
                //eliminar al viejo
                groupDAO.eliminaUsuarioGrupo(userMenos.getID(), userMenos.getIDGrupo());
                redirectAttrs
                        .addFlashAttribute("status", "success")
                        .addFlashAttribute("mensaje", "Usuario remplazado correctamente");
                return "redirect:/editarGrupo?idGrupo=" + userMenos.getIDGrupo();

        }else {
            return "redirect:/login";
        }
    }

    //Reasigna un miembro del organigrama con otro superior inmediato (cuando se elimina su superior del organigrama)
    @PostMapping("/reasignaSuperior")
    public String reasignaSuperior(@ModelAttribute(value = "idUsuario") String idUsuario,@ModelAttribute(value = "origen") String origen, Model modelMap,RedirectAttributes redirectAttrs, HttpSession session){
        //valida si la sesión está activa
        if (session.getAttribute("user")!= null && (boolean) session.getAttribute("user")) {
            ArrayList<User> listaSubordinados = userDAO.muestraSubordinados(idUsuario);
            User user = userDAO.buscaID(idUsuario);
            if (listaSubordinados != null) {
                //valida si el usuario a eliminar tiene suboordinados a reasignar y redirecciona a ReasignaSuperior
                ArrayList<User> listaUsuarios = new ArrayList<>();
                User[] usuarios;
                usuarios = groupDAO.muestraUsuariosGrupo(user.getIDGrupo());
                for (User usuario : usuarios) {
                    if (!usuario.getID().equals(user.getID())) {
                        listaUsuarios.add(usuario);
                    }
                }
                if (user.getIDSuperiorInmediato().equals("")) {
                    modelMap.addAttribute("userDelete", true);
                } else {
                    modelMap.addAttribute("userDelete", false);
                }
                modelMap.addAttribute("listaSubordinados", listaSubordinados);
                modelMap.addAttribute("listaUsuarios", listaUsuarios);
                modelMap.addAttribute("idUsuario", idUsuario);
                modelMap.addAttribute("origen", origen);
                return "paginas/usuarios/ReasignaSuperior";
            } else {
                //si no tiene suboordinados, elimina y redirecciona a editarGrupo
                groupDAO.eliminaUsuarioGrupo(idUsuario, user.getIDGrupo());
                //se añade nuevo usuario al grupo
                if (origen.equals("0")) {
                    //Si el origen proviene de vistaUsuarios cambiar el status a false y redirecciona a findallusuarios
                    userDAO.desactivarUsuario(idUsuario);
                    redirectAttrs
                            .addFlashAttribute("status", "success")
                            .addFlashAttribute("mensaje", "Usuario eliminado correctamente");
                    return "redirect:/findAllUsuarios";
                } else {
                    //si origen proviene de grupos, redirecciona a edita grupo
                    redirectAttrs
                            .addFlashAttribute("status", "success")
                            .addFlashAttribute("mensaje", "Usuario eliminado correctamente");
                    return "redirect:/editarGrupo?idGrupo=" + user.getIDGrupo();
                }
            }
        }else {
            //si la sesión no está activa redirecciona al login
            return "redirect:/login";
        }
    }


    /**
     * Agrega un ArrayList a la pagina inicioOrganigramas para ser utilizada  con thymeleaf
     * -valida que la sesion este activa
     * -en caso de error redirige a la pagina de error
     * @return
     */
    @GetMapping("/buscarTodosGrupos")
    public String buscarTodosGrupos(@ModelAttribute ArrayList<Group> listaGrupos, ModelMap model, HttpSession session) {
        if (session.getAttribute("user")!= null && (boolean) session.getAttribute("user")) {

            try {
                Gson gson = new Gson();
                JSONArray name1 = groupDAO.buscarTodosGrupos();
                for (int i = 0; i < name1.length(); i++) {
                    listaGrupos.add(gson.fromJson(name1.getJSONObject(i).toString(), Group.class));
                }
            } catch (Exception e) {
                return "redirect:/error1";
            }
            model.addAttribute("listaGrupos", listaGrupos);
            return "paginas/organigramas/inicioOrganigramas.html";
        }else {
            return "redirect:/login";
        }
    }

    /**
     * Redirecciona a la pagina de edicion de grupos agregando el id del grupo a editar y
     * las listas de usuarios de dicho grupo y usuarios disponibles para agregar
     *
     * @param id string del id del grupo a editar
     *
     * @return
     */
    @RequestMapping(value="/editarGrupo",method = { RequestMethod.POST, RequestMethod.GET })
    @PostMapping("/editarGrupo")
    public String editarGrupos(@ModelAttribute User user,@ModelAttribute(value = "idGrupo") String id,Model model, HttpSession session) {
        if (session.getAttribute("user")!= null && (boolean) session.getAttribute("user")) {
            //añadir usuarios con los que se pueden sutituir
            ArrayList<User> posiblesSustituir = new ArrayList();
            posiblesSustituir.addAll(userDAO.listaUsuariosDisponibles());
            //posiblesSustituir.addAll(userDAO.listaUsuariosOrganigrama(id));
            model.addAttribute("listaSustitucion", posiblesSustituir);
            //añadir la lista de usuarios sin grupo
            model.addAttribute("listaDisponibles", userDAO.listaUsuariosDisponibles());
            //añadir lista de usuarios del organigrama
            model.addAttribute("listaUsuariosGrupo", userDAO.listaUsuariosOrganigrama(id));
            model.addAttribute("idGrupo", id);
            return "paginas/organigramas/editarOrganigrama";
        }else{
            return "redirect:/login";
        }
    }

    //Itera la lista de usuarios para reasiganrles su superior y elimina el usuario elegido.
    @PostMapping("/ActualizaElimina")
    public String actualizaElimina(@ModelAttribute(value = "idUsuario") String idUsuario,@ModelAttribute(value = "origen") String origen
            ,@ModelAttribute(value = "idUser") String idUser, @ModelAttribute(value = "idBoss") String idBoss, ModelMap modelMap
            ,Model model,RedirectAttributes redirectAttrs, HttpSession session){
        //valida si la sesión está activa
        if (session.getAttribute("user")!= null && (boolean) session.getAttribute("user")) {
            //Actualiza el el usuario con el id del superior inmediato
            userDAO.actualizaIdSuperior(idUser, idBoss);
            ArrayList<User> listaSubordinados = userDAO.muestraSubordinados(idUsuario);
            User user = userDAO.buscaID(idUsuario);
            //si el usuario a eliminar aún tiene subordinados extrae los posibles candidatos a ser su superior inmediato y redireeciona
            //a ReasignaSuperior
            if (listaSubordinados != null) {
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
            } else {
                //si no tiene suboordinados, elimina y redirecciona a editarGrupo
                groupDAO.eliminaUsuarioGrupo(idUsuario, user.getIDGrupo());
                if (origen.equals("0")) {
                    //Si el origen proviene de vistaUsuarios cambiar el status a false y redirecciona a findAllusuarios
                    userDAO.desactivarUsuario(idUsuario);
                    redirectAttrs
                            .addFlashAttribute("status", "success")
                            .addFlashAttribute("mensaje", "Usuario eliminado correctamente");
                    return "redirect:/findAllUsuarios";
                } else {
                    //si origen proviene de grupos, redirecciona a editarGrupo
                    redirectAttrs
                            .addFlashAttribute("status", "success")
                            .addFlashAttribute("mensaje", "Usuario eliminado correctamente");
                    return "redirect:/editarGrupo/?idGrupo=" + user.getIDGrupo();
                }
            }
        }else return "redirect:/login";
    }


    /**
     * Redireccion a la pagina de error general
     * @return
     */
    @GetMapping("/error1")
    public String error() {
        return "paginas/error.html";
    }

    @PostMapping("/agregarUsuarioAGrupo")
    public String agregarUsuarioAGrupo(@ModelAttribute BodyAddUserGroup bodyAdd, RedirectAttributes redirectAttrs, HttpSession session) {
        try{
            if (session.getAttribute("user")!= null && (boolean) session.getAttribute("user")) {
                boolean res = groupDAO.agregarUsuario(bodyAdd);
                if (res){
                    redirectAttrs
                            .addFlashAttribute("status", "success")
                            .addFlashAttribute("mensaje", "Usuario agregado correctamente");
                    return "redirect:/editarGrupo/?idGrupo=" + bodyAdd.getIdGrupo();
                }else{
                    redirectAttrs
                            .addFlashAttribute("status", "danger")
                            .addFlashAttribute("mensaje", "El usuario no pudo ser agregado");
                    return "redirect:/editarGrupo/?idGrupo=" + bodyAdd.getIdGrupo();
                }
            }else {
                return "redirect:/login";
            }
        }catch (Exception e){
            System.err.println("Exepcion: "+e);
            return "redirect: /error1";
        }
    }


    /**
     *Edita un usuario en un Organigrama
     * @param idGrupo parametro correspondiente a un id de Grupo
     * @param body Objeto con (idUsuario,idGrupo,idSuperior,nombreRol)
     * @param redirectAttrs parametro correspondiente a las alertas
     * @param session parametro correspondiente al manejo de sesion
     * @return retorna a la pagina general de Organigramas
     */
    @PostMapping("/editaUsuarioAGrupo")
    public String editaUsuarioAGrupo(@ModelAttribute(value = "idGrupo") String idGrupo,@ModelAttribute BodyAddUserGroup body
            ,RedirectAttributes redirectAttrs, HttpSession session){
        String respuesta;
        body.setIdGrupo(idGrupo);
        try {
            if (session.getAttribute("user")!= null && (boolean) session.getAttribute("user")) {
                respuesta = groupDAO.reasignausuariogrupo(body);
                if (respuesta.equals("OK")) {
                    redirectAttrs
                            .addFlashAttribute("status", "success")
                            .addFlashAttribute("mensaje", "Usuario editado correctamente");
                    return "redirect:/editarGrupo/?idGrupo=" + body.getIdGrupo();
                } else {
                    redirectAttrs
                            .addFlashAttribute("status", "danger")
                            .addFlashAttribute("mensaje", "Hubo un problema al editar al usuario");
                    return "redirect:/editarGrupo/?idGrupo=" + body.getIdGrupo();
                }
            }else {
                return "redirect:/login";
            }
        }catch (Exception e){
            System.err.println("Exepcion: "+e);
            redirectAttrs
                    .addFlashAttribute("mensaje", e);
            return "redirect:/editarGrupo/?idGrupo=" + body.getIdGrupo();
        }
    }

    @PostMapping("/verUsuario")
    public String verUsuario(@ModelAttribute(value = "id") String id,Model model,
                             RedirectAttributes redirectAttrs, HttpSession session){
        try {
            if (session.getAttribute("user")!= null && (boolean) session.getAttribute("user")) {
                User user = userDAO.buscaID(id);
                if (user != null) {
                    model.addAttribute("user", user);
                    return "paginas/usuarios/VerUsuario";
                } else {
                    redirectAttrs
                            .addFlashAttribute("mensaje", "El usuario ya no existe");
                    return "redirect:/findAllUsuarios";
                }
            }else {
                return "redirect:/login";
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "redirect:/error1";
        }
    }

    @RequestMapping(value="/borrarGrupo",method = { RequestMethod.POST})
    @PostMapping("/borrarGrupo")
    public String borrarGrupo(@ModelAttribute(value = "idGrupo") String id,Model model, HttpSession session,RedirectAttributes redirectAttrs) {
        try{
            if (session.getAttribute("user")!= null && (boolean) session.getAttribute("user")) {
                boolean res = groupDAO.borrarGrupo(id);
                if (res){
                    return "redirect:/buscarTodosGrupos";
                }else{
                    redirectAttrs
                            .addFlashAttribute("status", "danger")
                            .addFlashAttribute("mensaje", "Grupo no eliminado");
                    return "redirect:/buscarTodosGrupos";
                }

            }else {
                return "redirect:/login";
            }
        }catch (Exception e){
            System.err.println("Exepcion: "+e);
            return "redirect:/error1";
        }
    }

    //Busca a un usuario usando filtro por RFC, CURP, nombre, rol, correo o número de empleado.
    @PostMapping("/busquedaUsuario")
    public String busquedaUsuario(@ModelAttribute(value = "parametro") String parametro, ModelMap modelMap, HttpSession session){
        try {
            //valida si la sesión está activa, busca a los usuarios y redirecciona a InicioUsuarios
            if (session.getAttribute("user")!= null && (boolean) session.getAttribute("user")) {
                ArrayList<User> listaUsuarios = userDAO.busqedaUsuarios(parametro);
                modelMap.addAttribute("listaUsuarios", listaUsuarios);
                return "paginas/usuarios/InicioUsuarios";
            }else {
                //si la sesión no está activa redirecciona a login
                return "redirect:/login";
            }
        }catch (Exception e){
            return "redirect:/error1";
        }
    }

    //Busca a un organigrama por su nombre
    @PostMapping("/busquedaOrganigrama")
    public String busquedaOrganigrama(@ModelAttribute(value = "parametro") String parametro, ModelMap model, HttpSession session){
        ArrayList<Group> listaGrupos = new ArrayList<>();
        try {
            //Valida si la sesión está activa
            if (session.getAttribute("user")!= null && (boolean) session.getAttribute("user")) {
                if (groupDAO.busquedaGrupo(parametro) != null) {
                    listaGrupos.add(groupDAO.busquedaGrupo(parametro));
                    model.addAttribute("listaGrupos", listaGrupos);
                }
                return "paginas/organigramas/inicioOrganigramas.html";
            }else {
                return "redirect:/login";
            }
        }catch (Exception e){
            return "redirect:/error1";
        }
    }

    //Cierra sesión cambia estatus de la sesión a false.
    @PostMapping("/logout")
    public String logOut(HttpSession session){
        try {
            session.setAttribute("user", false);
            return "redirect:/login";
        }catch (Exception e){
            return "redirect:/error1";
        }
    }


}
