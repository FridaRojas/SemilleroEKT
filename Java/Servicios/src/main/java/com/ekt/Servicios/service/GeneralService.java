package com.ekt.Servicios.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class GeneralService {



    /**
     * Recibe un string.
     * Si el string esta vacio crea un sha aleatorio.
     * Si recibe un string con caracteres regresa un sha256 de dicho string.
     * @return
     */
    public  static String cifrar(String param){
        try {

            if (param.length()>0){
            }else{
                for (int i=0;i<20;i++){
                    param+=String.valueOf(Math.random());
                }
            }
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            BigInteger number = new BigInteger(1, md.digest(param.getBytes(StandardCharsets.UTF_8)));
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 32){
                hexString.insert(0, '0');
            }
            return hexString.toString();
        }catch (Exception e){
            return null;
        }
    }
}
