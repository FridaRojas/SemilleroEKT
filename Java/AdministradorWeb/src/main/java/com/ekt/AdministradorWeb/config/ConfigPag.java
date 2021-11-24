package com.ekt.AdministradorWeb.config;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ConfigPag {

        @GetMapping("/login")
        public String login() {
            //return "paginas/organigrama/InicioOrganigrama";
             return "elementos/menuLateral";
        }

    @GetMapping("/inicioUsuarios")
    public String inicioUsuarios() {
        //return "paginas/organigrama/InicioOrganigrama";
        return "paginas/usuarios/InicioUsuarios";
    }







}
