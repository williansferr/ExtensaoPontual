/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Willians
 */
@Entity
@Table(name = "usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll",
            query = "SELECT u FROM Usuario u where u.tipoUsuario not like 'Coordenador' ORDER BY u.nome"),
    
    @NamedQuery(name = "Usuario.findAllWithAdmin",
            query = "SELECT u FROM Usuario u ORDER BY u.nome"),

    @NamedQuery(name = "Usuario.findByMatricula",
            query = "SELECT u FROM Usuario u where u.matricula = :matricula ORDER BY u.nome"),

    @NamedQuery(name = "Usuario.findUsersProfessor",
            query = "SELECT u FROM Usuario u where u.tipoUsuario = 'Professor' ORDER BY u.nome"),

    @NamedQuery(name = "Usuario.findAllUserStudentsAndVolunteers",
            query = "SELECT u FROM Usuario u where u.tipoUsuario = 'Aluno' or u.tipoUsuario = 'Voluntario' ORDER BY u.nome"),

    @NamedQuery(name = "Usuario.findAllEmails",
            query = "SELECT u.email FROM Usuario u"),

    @NamedQuery(name = "Usuario.findEmail",
            query = "SELECT u FROM Usuario u "
                    + "where u.email = :email")})

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "matricula")
    private Integer matricula;
    @Basic(optional = false)
    @Column(name = "tipoUsuario")
    private String tipoUsuario;
    @Column(name = "colegiado")
    private String colegiado;
    @Column(name = "dataNasc")
    @Temporal(TemporalType.DATE)
    private Date dataNasc;
    @Column(name = "endereco")
    private String endereco;
    @Column(name = "senha")
    private String senha;
    @Column(name = "login")
    private String login;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Column(name = "telefoneResidencial")
    private String telefoneResidencial;
    @Column(name = "telefoneComercial")
    private String telefoneComercial;
    @Column(name = "telefoneCelular")
    private String telefoneCelular;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matricula")
    private List<UsuarioProjeto> usuarioProjetoList;

    public Usuario() {
    }

    public Usuario(Integer matricula) {
        this.matricula = matricula;
    }

    public Usuario(Integer matricula, String nome, String tipoUsuario, String email) {
        this.matricula = matricula;
        this.nome = nome;
        this.tipoUsuario = tipoUsuario;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getMatricula() {
        return matricula;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getColegiado() {
        return colegiado;
    }

    public void setColegiado(String colegiado) {
        this.colegiado = colegiado;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefoneResidencial() {
        return telefoneResidencial;
    }

    public void setTelefoneResidencial(String telefoneResidencial) {
        this.telefoneResidencial = telefoneResidencial;
    }

    public String getTelefoneComercial() {
        return telefoneComercial;
    }

    public void setTelefoneComercial(String telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }

    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    @XmlTransient
    public List<UsuarioProjeto> getUsuarioProjetoList() {
        return usuarioProjetoList;
    }

    public void setUsuarioProjetoList(List<UsuarioProjeto> usuarioProjetoList) {
        this.usuarioProjetoList = usuarioProjetoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matricula != null ? matricula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.matricula == null && other.matricula != null) || (this.matricula != null && !this.matricula.equals(other.matricula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Usuario[ matricula=" + matricula + " ]";
    }
    
}
