/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controllers.PontoJpaController;
import Controllers.UsuarioJpaController;
import Controllers.UsuarioProjetoJpaController;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Ponto;
import models.Projeto;
import models.Usuario;

/**
 *
 * @author Willians
 */
public class teste {

    public static void main(String[] args) throws ParseException {

        UsuarioJpaController controlerUsuario = new UsuarioJpaController();
        BeanUsuario beanUsuario = new BeanUsuario();
        BeanPonto pontoBean = new BeanPonto();
        UsuarioProjetoJpaController jpa = new UsuarioProjetoJpaController();
        PontoJpaController controlePonto = new PontoJpaController();
        BeanConverterProjeto a = new BeanConverterProjeto();
        BeanLogar logarBean = new BeanLogar();
        Ponto p = new Ponto(12345);
        Usuario us= new Usuario(11111);
        us.setTipoUsuario("Coordenador");
        Calendar calInicio = Calendar.getInstance();
        Calendar calFim = Calendar.getInstance();
        calInicio.set(2015, 8, 1);
        calFim.set(2015, 12, 1);
        String nome = "Administrador ";
//        String aux = nome.substring(0,nome.indexOf(" "));
        if(!nome.contains(" ")){
            
        System.out.println("Sem Sobrenome");
        }else{
        System.out.println("nome: "+nome.indexOf(" "));
        }
        
        
        
    }
}
