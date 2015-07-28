package Beans;

import Controllers.UsuarioJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.swing.Action;
import models.Usuario;

/**
 *
 * @author Willians
 */
@ManagedBean
@ViewScoped
public class BeanUsuario{

    private String nome;
    private String login;
    private String senha;
    private String email;
    private String tel1;
    private String tel2;


    private String tel3;
    private String endereco;
    private String colegiado;
    private Date dataNasc;
    private String tipoUsuario;
    private Usuario user = new Usuario();
    List<Usuario> lista = new ArrayList();
    

     
    UsuarioJpaController jpa = new UsuarioJpaController();

    public String novoUsuario(Action submit) {
        getUser().setEmail(getEmail());
        getUser().setLogin(getLogin());
        getUser().setNome(getNome());
        getUser().setTipoUsuario(getTipoUsuario());
        getUser().setSenha(getSenha());
        getUser().setTelefoneResidencial(getTel1());
        getUser().setTelefoneCelular(getTel2());
        getUser().setTelefoneComercial(getTel3());
        getUser().setEndereco(getEndereço());
        getUser().setColegiado(getColegiado());
        
        
        try{
            jpa.create(getUser());
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                     FacesMessage.SEVERITY_INFO, "Novo usuario cadastrado", ""));
            
            
        
        }catch(Exception e){
            e.printStackTrace();
        }
        

        return null;
    }

    public void insert() {
        if (validaUsuario(user)) {
            jpa.create(this.user);
            
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_INFO,"Cadastro realizado com sucesso!","" ));
        } else {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_WARN,"Não foi possível realizar cadastro!","" ));
        }

    }
    
    public void insert(Usuario us) {
        if (validaUsuario(us)) {
            jpa.create(us);
            
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_INFO,"Cadastro realizado com sucesso!","" ));
        } else {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_WARN,"Não foi possível realizar cadastro!","" ));
        }

    }
    
    public boolean validaUsuario(Usuario usuario) {
        if(usuario.getTipoUsuario().equals("") && usuario.getNome().equals("")) {
            return false;
        }
            return true;
        
    }
    
   public void excluirUsuario(int id){
        try{
        jpa.destroy(id);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_INFO,"Deletado com sucesso!","" ));
        }catch(Exception e){
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_WARN,"Não foi possível deletar o Usuário!","" ));
            
        }
    }

    public String editarUsuario(Action submit) {
        
        try {
            jpa.edit(getUser());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_INFO,"Alteração realizada!","" ));
        } catch (Exception e) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_WARN,"Não foi possível realizar alteração!","" ));
        }
        

        return null;
    }
    
    public List<Usuario> selecionarAll(){
        List lista;
        try{
            lista = jpa.findUsuarioEntities();
            return lista;
            
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_WARN,"Não foi possível buscar Usuarios!","" ));
        }
        return null;
    }
    

    public List<Usuario> listaUsuarios() {
       lista= jpa.selectAll();
        return lista;
    }
    
    public List<Usuario> getUsuariosProfessores(){
        List list;
        list = jpa.getUsuarioProfessor();
        return list;
    }

    public void incluirUsuarioProjeto(Usuario usuario){
        
        
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String none) {
        this.nome = none;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        System.out.println("Classe Usuario: "+user);
        this.user = user;
    }

    /**
     * @return the tel1
     */
    public String getTel1() {
        return tel1;
    }

    /**
     * @param tel1 the tel1 to set
     */
    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    /**
     * @return the tel2
     */
    public String getTel2() {
        return tel2;
    }

    /**
     * @param tel2 the tel2 to set
     */
    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    /**
     * @return the tel3
     */
    public String getTel3() {
        return tel3;
    }

    /**
     * @param tel3 the tel3 to set
     */
    public void setTel3(String tel3) {
        this.tel3 = tel3;
    }

    /**
     * @return the endereço
     */
    public String getEndereço() {
        return endereco;
    }

    /**
     * @param endereço the endereço to set
     */
    public void setEndereço(String endereço) {
        this.endereco = endereço;
    }

    /**
     * @return the colegiado
     */
    public String getColegiado() {
        return colegiado;
    }

    /**
     * @param colegiado the colegiado to set
     */
    public void setColegiado(String colegiado) {
        this.colegiado = colegiado;
    }
    
    public String getTipoUsuario() {
        return tipoUsuario;
    }
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

   
    

//    public String getNomeTipoUsuario(Usuario us) {
//        String nomeTipo = "";
//        
//        try{
//        if (us.getTipoUsuario().equals("1") || us.getTipoUsuario().equals("2") || us.getTipoUsuario().equals("3") || us.getTipoUsuario().equals("4")) {
//            switch (us.getTipoUsuario()) {
//                case "1":
//                    nomeTipo = "Coordenador";
//                    break;
//                case "2":
//                    nomeTipo = "Professor";
//                    break;
//                case "3":
//                    nomeTipo = "Aluno";
//                    break;
//                case "4":
//                    nomeTipo = "Voluntário";
//                    break;
//            }
//            return nomeTipo;
//        }
//        } catch(Exception e) {
//            FacesContext.getCurrentInstance().addMessage(null, 
//                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuário Inválido", ""));
//        }
//        return nomeTipo;
//    }
}
