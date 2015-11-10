/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controllers.UsuarioProjetoJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
    BeanProjeto beanProjeto = new BeanProjeto();
    UsuarioProjeto usuario_Projeto = new UsuarioProjeto();
    UsuarioProjetoJpaController jpa = new UsuarioProjetoJpaController();
    List<Projeto> listaProjetos = new ArrayList();
    List<Usuario> listaProfessores = new ArrayList();
    List<Usuario> listaAlunosPorProjeto = new ArrayList();
    List<Projeto> listaAuxProjeto = new ArrayList<>();

    boolean alterar = true;
    boolean btnProximaEtapa;

    public BeanUsuario_Projeto() {
    }

    public void insert(Usuario us, Projeto projeto) {
        UsuarioProjeto up = new UsuarioProjeto(us, projeto);
        if (!existeRegistroUsuarioNoProjeto(us, projeto)) {
            Calendar dataAtual = Calendar.getInstance();
            up.setDataCadastro(dataAtual.getTime());
            try {
                jpa.create(up);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_INFO, "Professor(a) " + us.getNome().toUpperCase() + "\n"
                        + "Com Projeto: " + projeto.getNome().toUpperCase(), ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Erro!! Professor já encontra-se cadastrado nesse Projeto!", ""));

        }
    }

    //RETORNA O NOME DO PROFESSOR DE UM PROJETO (Projeto)
    public String getNameProfessorByProjectRegistry(Projeto p) {
        String nome = "";
        List<Usuario> list = jpa.getUserProfessorByProject(p);
        if (!list.isEmpty()) {
            nome = list.get(0).getNome();
        } else {

        }
        return nome;
    }

    public List<Usuario> getListaRegistroProfessor(Projeto p) {
        return jpa.getUserProfessorByProject(p);
    }

    //RETORNA OS ALUNOS QUE ESTÃO CADASTRADOS EM DETERMINADO PROJETO (PROJETO)
    public List<Usuario> getAlunosDoProjeto(Projeto p) {
        List<Usuario> list = jpa.getUserStudentsByProjeto(p);
        return list;
    }

//RETORNA USUARIOS QUE TEM PROJETO CADASTRADO (SEM PARAMETRO)
    public List<Usuario> getTodosUsuariosComProjeto() {
        List lista = jpa.getUserWithProjectAll();
        return lista;
    }
//RETORNA PROJETOS QUE O DETERMINADO USUARIO ESTÁ CADASTRADO (USUARIO)

    public List<Projeto> getListProjetos(Usuario us) {
        try {
            List<Projeto> list = jpa.getEntityWithParameterUser(us);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // RETORNA TRUE SE USUÁRIO ESTÁ CADASTRADO EM ALGUM PROJETO, FALSE CASO CONTRÁRIO (ID USUARIO)
    public boolean usuarioRegistrado(Integer id) {
        if (jpa.isRegistered(id)) {
            return true;
        } else {
            return false;
        }
    }

    // ADICIONA DOS USUÁRIOS NO PROJETO (LISTA USUARIOS(ALUNOS), PROJETO)
    //VERIFICANDO QUAIS USUÁRIOS ESTÃO CADASTRADOS OU NÃO 
    //O MÉTODO VALIDA SE O USUÁRIO JA ESTÁ CADASTRADO NO PROJETO 
    public void incluirAlunosNoProjeto(List<Usuario> listaAlunos, Projeto projeto) {
        UsuarioProjeto up = new UsuarioProjeto();
        List<Usuario> listUsersWithoutProject;
        Calendar dataAtual = Calendar.getInstance();
        up.setDataCadastro(dataAtual.getTime());
        try {
            listUsersWithoutProject = jpa.getUserNotThereIsThisProject(listaAlunos, projeto); // Retorna lista Validada
            if (!listUsersWithoutProject.isEmpty()) {
//                for (int i = 0; i < listUsersWithoutProject.size(); i++) {
//
//                }
                for (int i = 0; i < listUsersWithoutProject.size(); i++) {
                    if (listUsersWithoutProject.get(i) != null) {
                        up.setIdProjeto(projeto);
                        up.setMatricula(listUsersWithoutProject.get(i));
                        jpa.create(up);

                    }
                }
                BeanProjeto.dropLista.clear();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_INFO, "Alunos Cadastrados no Projeto " + projeto.getNome().toUpperCase(), ""));

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_INFO, "Lista Vazia! Favor Adicionar Aluno/Voluntário", ""));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//REALIZA A EXCLUSÃO DO CADASTRO DO USUARIO E PROJETO ESPECIFICO

    public void excluirCadastro(Usuario us, Projeto p) {
        
        if (us != null && p != null) {
            jpa.removeUserOfProject(p,us);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Usuário Excluido do Projeto!", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Não foi possível excluir Usuário do Projeto!!!", ""));
        }
    }

    //RETORNA A LISTA DE PROJETOS DO USUARIO QUE ESTÁ LOGADO
    public List<Projeto> getProjetosUsuarioLogado(Usuario us) {
        listaAuxProjeto = new ArrayList();
        if (us.getTipoUsuario().equals("Professor")) {
            setListaAuxProjeto(jpa.getEntityWithParameterUser(us));
            return getListaAuxProjeto();

        } else if (us.getTipoUsuario().equals("Coordenador")) {
            setListaAuxProjeto(jpa.getAllProjects());
            return getListaAuxProjeto();
        } else {
            return null;
        }
    }

    public List<String> getNomeProjetosUsuarioAlunos(Usuario us) {
        List<Projeto> list = getProjetosUsuarioLogado(us);
        List<String> listaNome = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            if (!list.isEmpty()) {
                listaNome.add(i, list.get(i).getNome());
            }
        }
        return listaNome;

    }

    //REALIZA A EXCLUSÃO DE QUALQUER TIPO DE VINCULO QUE O USUÁRIO 
    //ESTIVER CADASTRADO OU O PROJETO (USUARIO/PROJETO)
    public void removeRegistroCadastro(Object obj) {
        try {
            jpa.destroyAllRegistered(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //BUSCA O REGISTRO DE  PROJETO E USUARIO
    public UsuarioProjeto getCadastroUsuarioProjeto(Projeto p, Usuario us) {
        if (p != null & us != null) {
            UsuarioProjeto up = jpa.getRegistryByProjectAndUsuario(p, us);
            return up;
        } else {
            return null;
        }
    }

    public boolean existeRegistroUsuarioNoProjeto(Usuario us, Projeto p) {
        UsuarioProjeto up = jpa.getRegistryByProjectAndUsuario(p, us);
        if (up != null) {
            return true;
        } else {
            return false;
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

    public List<Projeto> getListaProjetos() {
        return listaProjetos;
    }

    public void setListaProjetos(List<Projeto> lista) {
        this.listaProjetos = lista;
    }

    public List<Usuario> getListaProfessores() {
        return listaProfessores;
    }

    public void setListaProfessores(List<Usuario> listaProfessores) {
        this.listaProfessores = listaProfessores;
    }

    public boolean getAlterar() {
        return alterar;
    }

    public void setAlterar(boolean alterar) {
        this.alterar = alterar;
    }

    public boolean isProximaEtapa() {
        return btnProximaEtapa;
    }

    public void setProximaEtapa(boolean btnProximaEtapa) {
        this.btnProximaEtapa = btnProximaEtapa;
    }

    public List<Projeto> getListaProjetosPorUsuario(Usuario us) {
        List<Projeto> list = jpa.getEntityWithParameterUser(us);
        return list;
    }

    public List<Usuario> getListaAlunosPorProjeto() {
        return listaAlunosPorProjeto;
    }

    public void setListaAlunosPorProjeto(List<Usuario> listaAlunosPorProjeto) {
        this.listaAlunosPorProjeto = listaAlunosPorProjeto;
    }

    public List<Projeto> getListaAuxProjeto() {
        return listaAuxProjeto;
    }

    public void setListaAuxProjeto(List<Projeto> listaAuxProjeto) {
        this.listaAuxProjeto = listaAuxProjeto;
    }

}
