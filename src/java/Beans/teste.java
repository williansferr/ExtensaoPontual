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
        Ponto p = new Ponto(12345);
        Usuario us= new Usuario(1234);
        
        System.out.println("Con: "+controlePonto.getConnection());
        
    }
}
