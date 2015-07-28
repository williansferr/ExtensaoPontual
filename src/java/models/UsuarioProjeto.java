/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Willians
 */
@Entity
@Table(name = "usuario_projeto")
@NamedQueries({
    @NamedQuery(name = "UsuarioProjeto.findAll", query = "SELECT up FROM UsuarioProjeto up"),
    @NamedQuery(name = "UsuarioProjeto.usuarioPorProjeto",
            query = "SELECT up.projeto FROM UsuarioProjeto up "
            + "INNER JOIN up.projeto p"),
    @NamedQuery(name = "UsuarioProjeto.getUserProject", query = "SELECT up.usuario FROM UsuarioProjeto up "
            + "INNER JOIN up.usuario u "
            + "INNER JOIN up.projeto p where p.idProjeto = :valor"),
    @NamedQuery(name = "UsuarioProjeto.getProjectUser", query = "SELECT up.projeto FROM UsuarioProjeto up "
            + "INNER JOIN up.usuario u "
            + "INNER JOIN up.projeto p where u.matricula = :valor"),
    @NamedQuery(name = "UsuarioProjeto.TodoUsuariosComProjeto",
            query = "SELECT up.usuario FROM UsuarioProjeto up")})

public class UsuarioProjeto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "matricula", referencedColumnName = "matricula")
    @ManyToOne(optional = false)
    private Usuario usuario;
    @JoinColumn(name = "idProjeto", referencedColumnName = "idProjeto")
    @ManyToOne(optional = false)
    private Projeto projeto;

//    public UsuarioProjeto(Integer idUsuario, Integer idProjeto) {
//        this.projeto
//    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioProjeto)) {
            return false;
        }
        UsuarioProjeto other = (UsuarioProjeto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.UsuarioProjeto[ id=" + id + " ]";
    }

}
