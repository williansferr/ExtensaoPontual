/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Beans.SessionBeans;

//import CustomAnnotations.ControllerInjecter;
//import CustomAnnotations.GenericController;
//import CustomAnnotations.InjectedBean;
import Beans.BeanUsuario;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import models.Usuario;

/**
 *
 * @author Rauiz
 */
@ManagedBean(name = "loginBean")
@RequestScoped
public class SessionBean  {

//    @ControllerInjecter(clazz = Usuario.class)
    private BeanUsuario usuario_controller;
    
    private String txtEmail;
    private String txtSenha;
    
    
    /**
     * Chamado quando o usuário tenta realizar o login.
     * @return A página p/ a qual ele será direcionado.
     */
    public String doLoginAction(){
        List<Usuario> user_list = usuario_controller.selecionarAll();
        Usuario login_attempt = null;
        for (Usuario usuario : user_list) {
            if(usuario.getEmail().equals(this.txtEmail) && usuario.getSenha().equals(txtSenha))
                login_attempt = usuario;                              
        }
        if(login_attempt == null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_WARN,"E-Mail ou Senha inválido!","" ));
            return "";
        }
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("LOGGED_USER", login_attempt);
        
        if(login_attempt.getTipoUsuario().equals("Coordenador"))
            return "AdminPages/Usuarios/CriarUsuario.xhtml?faces-redirect=true";
        if(login_attempt.getTipoUsuario().equals("Professor"))
            return "ConsultantPages/MeusAtendimentos.xhtml?faces-redirect=true";
        if(login_attempt.getTipoUsuario().equals("Aluno"))
            return "OtherPages/CriarAtendimento.xhtml?faces-redirect=true";
        if(login_attempt.getTipoUsuario().equals("Voluntario"))
            return "OtherPages/CriarAtendimento.xhtml?faces-redirect=true";
        else            
            return "";        
    }
    
    public String doLogoutAction(){        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("LOGGED_USER", null);
        HttpSession session = (HttpSession) context.getSession(false);
        session.invalidate();
        return "/index.xhtml?faces-redirect=true";
    }
    
    public boolean matchLoggedUserType(String type){
        Usuario logged_user = (Usuario) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("LOGGED_USER");
        
        return logged_user.getTipoUsuario().equalsIgnoreCase(type);
    }

    public String getTxtEmail() {
        return txtEmail;
    }

    public void setTxtEmail(String txtEmail) {
        this.txtEmail = txtEmail;
    }

    public String getTxtSenha() {
        return txtSenha;
    }

    public void setTxtSenha(String txtSenha) {
        this.txtSenha = txtSenha;
    }
        
}


