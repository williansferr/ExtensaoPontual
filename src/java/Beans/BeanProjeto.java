package Beans;

import Controllers.ProjetoJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import models.Projeto;
import models.Usuario;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author Willians
 */
@ManagedBean
@ViewScoped
public class BeanProjeto implements Serializable {

    private int idProjeto, matricula;
    private Date data;

    private String nome, estado, colegiado;
    private Projeto projeto = new Projeto();
    List<Projeto> listaProjeto;
    List<Projeto> listaProjetoAux;
    ProjetoJpaController jpa;
    static List<Usuario> dropLista = new ArrayList();
    List<Usuario> lista = new ArrayList();
    Date dataAtual = new Date();
    private boolean skip;

    public void inserir() {
        if (jpa == null) {
            jpa = new ProjetoJpaController();
        }

        jpa.create(projeto);
        setProjeto(new Projeto());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_INFO, "Cadastro realizado com sucesso!", ""));

    }

    public void excluirProjeto(int id) {
        if (jpa == null) {
            jpa = new ProjetoJpaController();
        }
        try {
            jpa.destroy(id);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Deletado com sucesso!", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_WARN, "Não foi possível deletar o Projeto!", ""));

        }
    }

    public String onFlowProcess(FlowEvent event) {

        String current = event.getOldStep();
        String next = event.getNewStep();
        boolean proceed = true;

        if (current.equals("projeto") && next.equals("alunos") && (getProjeto() == null)) {

            proceed = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "", "Necessário Escolher Projeto!"));
        }
        if (proceed) {
            return next;
        } else {

            return current;
        }
    }

//    public String onFlowProcess(FlowEvent event) {
//
//        String current = event.getOldStep();
//        String next = event.getNewStep();
//        boolean proceed = true;
//        if (skip) {
//            skip = false;   //reset in case user goes back
//            return event.getOldStep();
//        } else {
//            return event.getNewStep();
//        }
//    }

    //EVENTO DA DRAGDROP DE ARRASTAR E SOLTAR
    public void onUsuarioDrop(DragDropEvent ddEvent) {
        Usuario us = ((Usuario) ddEvent.getData());
        dropLista.add(us);
        lista.remove(us);
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

    public String editarProjeto() {
        if (jpa == null) {
            jpa = new ProjetoJpaController();
        }

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

    //Busca Todos os Projetos Cadastrados
    public List<Projeto> buscaTodosProjetos() {
        if (jpa == null) {
            jpa = new ProjetoJpaController();
        }
        List<Projeto> list = new ArrayList();
        list = jpa.selectAll();
        return list;
    }

    public void removerAlunoDaLista(Usuario us) {
        dropLista.remove(us);
    }

    public void limparDropLista() {
        this.dropLista = new ArrayList<>();
    }

    public List<Usuario> getDropLista() {
        return dropLista;
    }

    public void setDropLista(List<Usuario> dropLista) {
        this.dropLista = dropLista;
    }

    public Date getDataAtual() {
        return dataAtual;
    }

    public void setDataAtual(Date dataAtual) {
        this.dataAtual = dataAtual;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

}
