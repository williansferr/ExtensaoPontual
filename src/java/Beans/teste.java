/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controllers.Usuario_ProjetoJpaController;
import models.Projeto;
import models.Usuario;
import models.UsuarioProjeto;

/**
 *
 * @author Willians
 */
public class teste{

    public static void main(String[] args) {
        BeanProjeto beanProjeto = new BeanProjeto();
//        BeanUsuario beanUsuario = new BeanUsuario();
        BeanUsuario_Projeto beanUsuario_Projeto = new BeanUsuario_Projeto();
        Usuario_ProjetoJpaController jpa = new Usuario_ProjetoJpaController();
        UsuarioProjeto up = new UsuarioProjeto();

        Usuario us = new Usuario();
        Projeto p = new Projeto();
        
//        UsuarioProjeto up = new UsuarioProjeto();
        
        us.setNome("Ichigo");
        us.setTipoUsuario("Coordenador");
        us.setMatricula(86);
        p.setIdProjeto(2);
//        up.setProjeto(p);
//        up.setUsuario(us);
//        beanUsuario_Projeto.insert(us,p);
//        System.out.println("projeto teste: "+ jpa.getUsersOfProject(2));
//        System.out.println("projeto teste: "+ jpa.findAll().toString());
        System.out.println("");
//        System.out.println("\n"+"Nome dos Usuarios no projeto: "+beanUsuario_Projeto.getNomeProjetoOuUsuario(new Projeto(6)));
        System.out.println("");
//        System.out.println("Nome dos Usuarios no projeto: "+beanUsuario_Projeto.getNomeProjetoOuUsuario(new Projeto(2)));
        
        System.out.println("\n"+"Nome dos Projetos desse Usuário: "+
                beanUsuario_Projeto.getListProjetos(new Usuario(87)));
        System.out.println("");
//        System.out.println("Nome dos Projetos desse Usuário: "+
//                beanUsuario_Projeto.getNomeProjetoOuUsuario(new Usuario(86)));
        System.out.println("");
//        System.out.println("Nome dos Projetos desse Usuário: "+
//                beanUsuario_Projeto.getNomeProjetoOuUsuario(new Usuario(75)));
    }

}
