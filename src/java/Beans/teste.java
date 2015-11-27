/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controllers.PontoJpaController;
import Controllers.UsuarioJpaController;
import Controllers.UsuarioProjetoJpaController;
import java.text.ParseException;
import java.util.Calendar;
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
        calInicio.set(2015, 10, 1);
        calFim.set(2015, 11, 1);
        
//        System.out.println("TotalHoras: "+controlePonto.getTotalHorasRealizadas(
//                new Usuario(11126), new Projeto(14), calInicio.getTime(), calFim.getTime()));
//        System.out.println("");
//        System.out.println("horaInicial: "+calInicio.getTime());
//        System.out.println("horaFim: "+calFim.getTime());
        
//        System.out.println(controlePonto.getHorasTotal(calInicio.getTime(), calFim.getTime(), 14, 11126));
        System.out.println("teste: "+pontoBean.buscaHorasTotal(new Usuario(11126),new Projeto(14),calInicio, calFim));
        
        
        System.out.println("senha: ");
        
    }
}
