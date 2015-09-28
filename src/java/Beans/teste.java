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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import models.Projeto;
import models.Usuario;
import hash.Sha;

/**
 *
 * @author Willians
 */
public class teste {

    public static void main(String[] args) throws ParseException {

//        BeanProjeto beanProjeto = new BeanProjeto();
        BeanUsuario beanUsuario = new BeanUsuario();
//        BeanUsuario_Projeto beanUsuario_Projeto = new BeanUsuario_Projeto();
//        BeanPonto beanPonto = new BeanPonto();
        UsuarioProjetoJpaController jpa = new UsuarioProjetoJpaController();
//        PontoJpaController controlePonto = new PontoJpaController();
//        CalendarView calendarView = new CalendarView();
        BeanConverterProjeto a = new BeanConverterProjeto();
                
//        beanUsuario.resetSenha(new Usuario(12));
        System.out.println("beanConverte: "+a.getProjetosUsuarioLogado(new Usuario(11112)));
    }
}
