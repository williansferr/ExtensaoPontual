package Beans;

import Controllers.UsuarioJpaController;
import hash.Sha;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.swing.Action;
import models.Usuario;
import org.primefaces.context.RequestContext;
import org.primefaces.event.DragDropEvent;

/**
 *
 * @author Willians
 */
@ManagedBean
@ViewScoped
public class BeanUsuario implements Serializable {

    private String nome;
    private String login;
    private String senha;
    private String email;
    private String tel1;
    private String tel2;
    private String tel3;
    private String endereco;
    private String colegiado;
    private String dataNasc;
    private String tipoUsuario;
    private Usuario user = new Usuario();
    List<Usuario> lista = new ArrayList();
    static List<Usuario> listaUsuarios;
    static List<Usuario> dropList;
    private String senhaConfirm;
    private String senhaAntiga;
    private String renhaReset;

    BeanUsuario_Projeto beanUsuarioProjeto = new BeanUsuario_Projeto();
    static UsuarioJpaController usuarioControlerJpa = new UsuarioJpaController();
    BeanUsuario_Projeto usuarioProjeto = new BeanUsuario_Projeto();

    @PostConstruct
    static public void init() {
        listaUsuarios = usuarioControlerJpa.getAllUserStudentsAndVolunteers();
        dropList = new ArrayList<Usuario>();
    }

    public void insert() {
        System.out.println("");
        try {
            user.setDataNasc(converterStringParaDate(getDataNasc()));
        } catch (Exception e) {

        }
        if (validaUsuario(user)) {
            if (!user.getSenha().equals("")) {
                try {
                    String aux = user.getSenha();
                    String senhaHash = Sha.generateHash(aux);
                    user.setSenha(senhaHash);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    String senhaNull = Sha.generateHash("unifil");
                    user.setSenha(senhaNull);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            usuarioControlerJpa.create(this.user);
            setUser(new Usuario());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Cadastro realizado com sucesso!", ""));
        }
    }

    public void esqueceuSenha(String senha, String senhaConfirm, String email) {
        Usuario us = new Usuario();
        if (senha.equals("") || senhaConfirm.equals("") || email.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Existem campos vazios!", ""));
        } else if (!senha.equals(senhaConfirm)) {
        } else {
            us = usuarioControlerJpa.getFindEmail(email.trim());
            if (us != null) {
                us.setSenha(senha);
                try {
                    usuarioControlerJpa.edit(us);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_INFO, "Senha Alterada com sucesso!", ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Email não Localizado!", ""));
            }
        }
    }

    public void alterarSenha(String senhaAntiga, String senhaNova, String senhaConfirm, Usuario usuarioLogado) {
        if (!senhaAntiga.equals("") || !senhaNova.equals("") || !senhaConfirm.equals("")) {
            String senhaAntigaHash = getSenhaHash(senhaAntiga);
            String senhaNovaHash = getSenhaHash(senhaNova);
            String senhaConfirmHash = getSenhaHash(senhaConfirm);
            Usuario us = usuarioControlerJpa.findUsuario(usuarioLogado.getMatricula());
            if (!us.getSenha().equals("")) {
                if (!us.getSenha().equals(senhaAntigaHash)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Senha Atual Incorreta!", ""));
                } else if (!senhaNovaHash.equals(senhaConfirmHash)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Confirmação de Senha inválido!", ""));
                } else {
                    try {
                        us.setSenha(senhaNovaHash);
                        usuarioControlerJpa.edit(us);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                                FacesMessage.SEVERITY_INFO, "Alteração Realizada!", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (!senhaNovaHash.equals(senhaConfirmHash)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Confirmação de Senha inválido!", ""));
                } else {
                    try {
                        us.setSenha(senhaNovaHash);
                        usuarioControlerJpa.edit(us);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                                FacesMessage.SEVERITY_INFO, "Alteração Realizada!", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Todos os campos devem ser preenchidos!", ""));

        }
    }

    // PROFESSOR OU COORDENADOR REALIZA RESET DA SENHA PARA DATA DE ANIVERSARIO
    public void resetSenha(Usuario us) {
        String senhaDataNasc = "";
        Usuario usuario = usuarioControlerJpa.findUsuario(us.getMatricula());
        if (usuario.getDataNasc() != null) {
            senhaDataNasc = converterDateParaString(usuario.getDataNasc());
            System.out.println("SenhaDataNasc: " + senhaDataNasc);
        }
        if (usuario != null) {
            String senha = getSenhaHash(senhaDataNasc);
            usuario.setSenha(senha);
            try {
                usuarioControlerJpa.edit(usuario);
                setRenhaReset(usuario.getSenha());
                RequestContext.getCurrentInstance().execute("PF('DetailSenhaNova').show()");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getSenhaHash(String senha) {
        try {
            return Sha.generateHash(senha);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean validaUsuario(Usuario usuario) {
        List<Usuario> listaUser = listaUsuarios();
        if (!listaUser.isEmpty()) {
            for (int i = 0; i < listaUser.size(); i++) {
                if (usuario.getEmail().equals(listaUser.get(i).getEmail())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Email já Cadastrado!", ""));

                    return false;

                } else if (usuario.getEmail().equals("")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Incluir o Nome", ""));
                    return false;
                }
            }
        }
        try {
            if (usuario.getTipoUsuario().equals("")
                    || usuario.getNome().equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_INFO, "Necessário incluir o nome!", ""));
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void excluirUsuario(Usuario us) {
        beanUsuarioProjeto.removeRegistroCadastro(us);
        try {
            usuarioControlerJpa.destroy(us.getMatricula());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Usuário Deletado!", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_WARN, "Não foi possível deletar o Usuário" + "\n" + "Contate o Administrador!", ""));

        }
    }

    public String editarUsuario(Action submit) {
        
        try {
            usuarioControlerJpa.edit(getUser());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Alteração realizada!", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_WARN, "Não foi possível realizar alteração!", ""));
        }

        return null;
    }

//BUSCAR TODOS OS USUARIOS CADASTRADOS SEM EXCEÇÃO
    public List<Usuario> listaUsuarios() {
        lista = usuarioControlerJpa.selectAll();
        return lista;
    }
//BUSCAR TODOS OS PROFESSORES CADASTRADOS DA TABELA USUARIO

    public List<Usuario> usuariosProfessores() {
        List list = new ArrayList();
        list = usuarioControlerJpa.getUserProfessor();
        if (list == null) {
            return null;
        } else {
            return list;
        }
    }
//BUSCAR TODOS OS ALUNOS E VOLUNTÁRIOS CADASTRADOS NA TABELA USUARIO

    public List<Usuario> getBuscarAlunosEVoluntarios() {
        return listaUsuarios;
    }

    //BUSCAR USUÁRIOS CADASTRADOS CONFORME O PERFIL LOGADO (USUARIO_LOGADO)
    public List<Usuario> getUsuarioCadastrados(Usuario us) {
        if (us.getTipoUsuario().equals("Coordenador")) {
            return usuarioControlerJpa.selectAll();
        } else if (us.getTipoUsuario().equals("Professor")) {
            return usuarioControlerJpa.getAllUserStudentsAndVolunteers();
        }
        return null;
    }

    //EVENTO DA DRAGDROP DE ARRASTAR E SOLTAR
    public void onUsuarioDrop(DragDropEvent ddEvent) {
        Usuario us = ((Usuario) ddEvent.getData());
        System.out.println("us: " + us);
        dropList.add(us);
        listaUsuarios.remove(us);
    }

    public void removerAlunoDaLista(Usuario us) {
        dropList.remove(us);
    }
//Converter String para Date

    public java.util.Date converterStringParaDate(String data) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = (java.util.Date) formatter.parse(data);
        System.out.println("Date: " + date);
        return date;
    }

    //Converter Date para String
    public String converterDateParaString(Date data) {
        DateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        String newDate = formatter.format(data.getTime());
        return newDate;
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

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }

    public List<Usuario> getLista() {
        return lista;
    }

    public void setLista(List<Usuario> lista) {
        this.lista = lista;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public List<Usuario> getDropList() {
        return dropList;
    }

    public void setDropList(List<Usuario> dropList) {
        this.dropList = dropList;
    }

    public String getSenhaConfirm() {
        return senhaConfirm;
    }

    public void setSenhaConfirm(String senhaConfirm) {
        this.senhaConfirm = senhaConfirm;
    }

    public String getSenhaAntiga() {
        return senhaAntiga;
    }

    public void setSenhaAntiga(String senhaAntiga) {
        this.senhaAntiga = senhaAntiga;
    }

    public String getRenhaReset() {
        return renhaReset;
    }

    public void setRenhaReset(String renhaReset) {
        this.renhaReset = renhaReset;
    }

}
