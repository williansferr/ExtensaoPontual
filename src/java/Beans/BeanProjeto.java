package Beans;

import Controllers.ProjetoJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.Action;
import models.Projeto;
import models.Usuario;

/**
 *
 * @author Willians
 */
@ManagedBean
@ViewScoped
public class BeanProjeto{

    private int idProjeto, matricula;
    private Date data;
    private String nome, estado, colegiado;
    private Projeto projeto  = new Projeto();
    List<Projeto> listaProjeto;
    ProjetoJpaController jpa = new ProjetoJpaController();

    public String novoUsuario(Action submit) {
        getProjeto().setNome(getNome());
        getProjeto().setDataInicio(getData());
        getProjeto().setColegiado(getColegiado());
        getProjeto().setEstado(getEstado());

        try {
            jpa.create(getProjeto());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Novo projeto cadastrado", ""));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cadastro não foi realizado!", ""));
        }

        return null;
    }

    public void insert() {
        if (validaProjeto(projeto)) {
            jpa.create(this.projeto);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Cadastro realizado com sucesso!", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_WARN, "Não foi possível realizar cadastro!", ""));
        }

    }

    public boolean validaProjeto(Projeto projeto) {
        if (projeto.getNome().equals("")) {
            return false;
        }
        return true;

    }

    public void excluirProjeto(int id) {
        try {
            jpa.destroy(id);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Deletado com sucesso!", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_WARN, "Não foi possível deletar o Projeto!", ""));

        }
    }

    

    public int getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(int idProjeto) {
        this.idProjeto = idProjeto;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getColegiado() {
        return colegiado;
    }

    public void setColegiado(String colegiado) {
        this.colegiado = colegiado;
    }



    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }
    

    public String editarProjeto(Action submit) {

        try {
            jpa.edit(getProjeto());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Alteração realizada!", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_WARN, "Não foi possível realizar alteração!", ""));
        }

        return null;
    }
    
    public List<Projeto> allProject(){
        List<Projeto> list = new ArrayList();
        list = jpa.selectAll();
        return list;
    }

}
