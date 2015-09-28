/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controllers.UsuarioProjetoJpaController;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import models.Projeto;
import models.Usuario;

@ManagedBean(name = "beanConverterProjeto", eager = true)
@ApplicationScoped
public class BeanConverterProjeto {

    List<Projeto> listaAuxProjeto;
    UsuarioProjetoJpaController jpa;
    Projeto projeto;
    Usuario usuario;
    Date dataAtual = new Date();

    @PostConstruct
    public void init() {
        listaAuxProjeto = new ArrayList<Projeto>();
//        getProjetosUsuarioLogado(usuario);
    }

    //BUSCA PROJETOS CADASTRADOS DA ENTIDADE USUARIO CONFORME A DATA ATUAL
    public List<Projeto> getProjetosUsuarioLogado(Usuario us) {
        if (usuario == null) {
            usuario = new Usuario();
        }
        usuario = us;
        if (jpa == null) {
            jpa = new UsuarioProjetoJpaController();
        }
        if(listaAuxProjeto ==  null){
            listaAuxProjeto = new ArrayList<>();
        }
        listaAuxProjeto = new ArrayList<Projeto>();
        setListaAuxProjeto(jpa.getProjectByUser(us));
        return getListaAuxProjeto();
    }

    public List<Projeto> getListaAuxProjeto() {
        return listaAuxProjeto;
    }

    public void setListaAuxProjeto(List<Projeto> listaAuxProjeto) {
        this.listaAuxProjeto = listaAuxProjeto;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

}
