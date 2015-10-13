/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controllers.PontoJpaController;
import Controllers.UsuarioJpaController;
import Controllers.UsuarioProjetoJpaController;
import hash.GenerateSenha;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import models.Projeto;
import models.Usuario;
import hash.Sha;
import java.text.ParseException;

/**
 *
 * @author Willians
 */
public class teste {

    public static void main(String[] args) throws ParseException  {

        UsuarioJpaController controlerUsuario = new UsuarioJpaController();
        BeanUsuario beanUsuario = new BeanUsuario();
        UsuarioProjetoJpaController jpa = new UsuarioProjetoJpaController();
        PontoJpaController controlePonto = new PontoJpaController();
        BeanConverterProjeto a = new BeanConverterProjeto();

        
        Calendar time =Calendar.getInstance();
        System.out.println("Time: "+time.getTime() );
        
//        String dataNas = "22/09/1987";
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        String teste = "22/09/1987";
//        Date date = (java.util.Date) formatter.parse(dataNas);
//        System.out.println("DataNova: " + date);
//       
//        System.out.println("SenhaData:"+beanUsuario.converterDateParaString(date));
//        beanUsuario.resetSenha(new Usuario(11127));
//       Usuario us = new Usuario(11133);
//       us.setNome("JOsé");
//       us.setDataNasc(date);
//       us.setTipoUsuario("Aluno");
//       us.setEmail("iiiik@kkk@kk");
//       controlerUsuario.create(us);
    }
}
