package Beans;

import Controllers.UsuarioJpaController;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import models.Usuario;

/**
 *
 * @author danielmorita
 */
@ManagedBean
@SessionScoped
public class BeanLogar implements Serializable{

    private Usuario usuarioLogado;

    private String usuario; //logar por email ou usuario
    private String senha;

    private UsuarioJpaController usuarioController;

    public String verificar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().
                getSession(true);
        if (usuarioController == null) {
            usuarioController = new UsuarioJpaController();
        }
        List<Usuario> listaUsuario = usuarioController.selectAll();
        for (Usuario us : listaUsuario) {
            if (us.getLogin().equals(this.usuario) && us.getSenha().equals(this.senha)
                    || us.getEmail().equals(this.usuario) && us.getSenha().equals(this.senha)) {
                this.usuarioLogado = us;
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Logado com sucesso", ""));
                return "/AdministradorPaginas/Ponto/AcessarPonto.xhtml?faces-redirect=true";
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login ou Senha incorretos", ""));
        return null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
