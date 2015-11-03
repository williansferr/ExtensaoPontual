package Beans;

import Controllers.UsuarioJpaController;
import models.Sha;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import models.Usuario;

/**
 *
 * @author danielmorita
 */
@ManagedBean
@SessionScoped
public class BeanLogar implements Serializable {

    private Usuario usuarioLogado;
    private String senha;
    private String email;
    private HttpSession session;
    private FacesContext fc;

    private UsuarioJpaController usuarioController;

    public String verificar() {

        boolean emailSenha = false;

        fc = FacesContext.getCurrentInstance();
        session = (HttpSession) fc.getExternalContext().
                getSession(true);
        if (usuarioController == null) {
            usuarioController = new UsuarioJpaController();
        }
        codificar();
        if (getLogin() != null && getSenha() != null) {
            List<Usuario> listaUsuario = usuarioController.selectAllWithAdmin();

            for (Usuario us : listaUsuario) {
                
                    if (us.getSenha() == null) {
                    
                } else {
                    if (us.getEmail().trim().equals(getLogin())
                            && us.getSenha().trim().equals(getSenha().trim())) {
                        emailSenha = true;
                        this.usuarioLogado = us;
                    } else {

                    }
                }
            }
        } else {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "", "Preencher todos os campos!"));
        }
        if (emailSenha == true) {
            return "/AdministradorPaginas/Ponto/EscolherProjetoAcessarPonto.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "", "Login ou Senha incorretos"));
        }
        return null;
    }

    public void codificar() {
        String aux = getSenha();
        try {
            setSenha(Sha.generateHash(aux));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean loggedUserType(String type) {
        return usuarioLogado.getTipoUsuario().equalsIgnoreCase(type);
    }

    public boolean loggedUserType(String u1, String u2) {
        boolean acesso = false;

        if (usuarioLogado.getTipoUsuario().equalsIgnoreCase(u1) || usuarioLogado.getTipoUsuario().equalsIgnoreCase(u2)) {
            acesso = true;
        } else {
            acesso = false;
        }
        return acesso;
    }

    public void interceptor() throws IOException {

        if (this.session.getAttribute("USUARIO_LOGADO") == null) {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/401.xhtml");
        }

    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogin() {
        return email;
    }

    public void setLogin(String email) {
        this.email = email;
    }

    

}
