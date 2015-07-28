/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controllers.Usuario_ProjetoJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import models.Projeto;
import models.Usuario;
import models.UsuarioProjeto;

/**
 *
 * @author Willians
 */
@ManagedBean
@SessionScoped
public class BeanUsuario_Projeto implements Serializable {

    Projeto projeto;
    Usuario usuario;
    UsuarioProjeto usuario_Projeto = new UsuarioProjeto();
    Usuario_ProjetoJpaController jpa = new Usuario_ProjetoJpaController();
    List<Projeto> lista = new ArrayList();

    public Usuario_ProjetoJpaController getJpa() {
        return jpa;
    }

    public void setJpa(Usuario_ProjetoJpaController jpa) {
        this.jpa = jpa;
    }

    public BeanUsuario_Projeto() {
    }

    public void vincularUsuarioProjeto() {
        getUsuario_Projeto().setProjeto(getProjeto());
        getUsuario_Projeto().setUsuario(getUsuario());

        try {
            jpa.create(usuario_Projeto);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Usuário Incluido no Projeto!", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Erro, Usuário não incluso!", ""));

        }
    }

    public void insert(Usuario us, Projeto projeto) {
        UsuarioProjeto up = new UsuarioProjeto();
        up.setProjeto(projeto);
        up.setUsuario(us);
        try {
            jpa.create(up);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Usuário Incluido no Projeto!", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Erro, Usuário não incluso!", ""));
        }
    }

    //metodo que busca o nome dos Usuarios cadastrados em um projeto incluindo o parâmetro Projeto ou
    // busca o nome dos Projetos que o usuario pertence incluindo o parâmetro Projeto
    public String getNomeProjetoOuUsuario(Object obj) {
        List list = new ArrayList();
        List<Projeto> listaProjeto = new ArrayList<>();
        List<Usuario> listaUsuario = new ArrayList<>();
        String nomes = "";

        if (obj instanceof Projeto) {
            list = getListaEntidadeComParametro(obj);
            listaUsuario = (List<Usuario>) list;
            if (listaUsuario != null) {
                for (int i = 0; i < listaUsuario.size(); i++) {
                    nomes = nomes + listaUsuario.get(i).getNome() + "\n";
                }
                return nomes;
            }
        } else if (obj instanceof Usuario) {
            list = getListaEntidadeComParametro(obj);
            listaProjeto = (List<Projeto>) list;
            if (listaProjeto != null) {
                for (int i = 0; i < listaProjeto.size(); i++) {
                    nomes += listaProjeto.get(i).getNome() + "; ";
                }
                return nomes;
            }

        }
        return null;
    }

    //continuar o metodo de retornar o usuario para cada projeto.
    public List<Object> getListaEntidadeComParametro(Object obj) {
        System.out.println("teste: "+obj);
        try {
            if (obj instanceof Projeto) {
                Projeto projeto = new Projeto();
                projeto = (Projeto) obj;
                List lista = jpa.getEntityWithParameterProject(projeto.getIdProjeto());
                return lista;
            } else if (obj instanceof Usuario) {
                Usuario usuario = new Usuario();
                usuario = (Usuario) obj;
                List lista = jpa.getEntityWithParameterUser(usuario.getMatricula());
                return lista;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Usuario> getTodosUsuariosComProjeto() {

        List lista = jpa.getUserWithProjectAll();
        return lista;
    }

    public List<Projeto> getListProjetos(Usuario us) {
        try {
            setLista(jpa.getEntityWithParameterUser(us.getMatricula()));
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public UsuarioProjeto getUsuario_Projeto() {
        return usuario_Projeto;
    }

    public void setUsuario_Projeto(UsuarioProjeto usuario_Projeto) {
        this.usuario_Projeto = usuario_Projeto;
    }

//    public List<Projeto> getListaAllProjetos() {
//        setLista(getListProjetos());
//        return lista;
//    }
    public void setLista(List<Projeto> lista) {
        this.lista = lista;
    }

}
