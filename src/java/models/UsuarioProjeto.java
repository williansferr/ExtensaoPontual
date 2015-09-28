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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Willians
 */
@Entity
@Table(name = "usuario_projeto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioProjeto.findAll",
            query = "SELECT up FROM UsuarioProjeto up"),

    @NamedQuery(name = "UsuarioProjeto.usuarioPorProjeto",
            query = "SELECT up.idProjeto FROM UsuarioProjeto up "
            + "INNER JOIN up.idProjeto p"),

    @NamedQuery(name = "UsuarioProjeto.getProjectUser",
            query = "SELECT up.idProjeto FROM UsuarioProjeto up "
            + "INNER JOIN up.matricula u "
            + "INNER JOIN up.idProjeto p where up.matricula.matricula = :matricula ORDER BY up.idProjeto.nome"),

    @NamedQuery(name = "UsuarioProjeto.TodoUsuariosComProjeto",
            query = "SELECT up.matricula FROM UsuarioProjeto up"),

    @NamedQuery(name = "UsuarioProjeto.isRegisteredInSomeProject",
            query = "SELECT up.matricula FROM UsuarioProjeto up where up.matricula.matricula = :valor"),

    @NamedQuery(name = "UsuarioProjeto.findRegisteredUsuarioByProject",
            query = "SELECT up FROM UsuarioProjeto up "
            + "INNER JOIN up.matricula u "
            + "where up.matricula.matricula =:matricula and up.idProjeto.idProjeto =:idProjeto"),

    @NamedQuery(name = "UsuarioProjeto.getRegistryByProjectAndProfessor",
            query = "SELECT up FROM UsuarioProjeto up "
            + "INNER JOIN up.matricula u "
            + "where up.matricula.tipoUsuario = 'Professor' and up.idProjeto.idProjeto =:idProjeto"),

    @NamedQuery(name = "UsuarioProjeto.buscaUserProfessorByProject",
            query = "SELECT up.matricula FROM UsuarioProjeto up "
            + "INNER JOIN up.matricula u "
            + "where up.matricula.tipoUsuario = 'Professor' and up.idProjeto.idProjeto =:idProjeto"),

    @NamedQuery(name = "UsuarioProjeto.thereIsThisProject",
            query = "SELECT up.matricula FROM UsuarioProjeto up "
            + "INNER JOIN up.matricula u "
            + "INNER JOIN up.idProjeto p "
            + "where up.matricula.matricula =:matricula and up.idProjeto.idProjeto =:idProjeto"),

    @NamedQuery(name = "UsuarioProjeto.StudentsOfProject",
            query = "SELECT up.matricula FROM UsuarioProjeto up "
            + "INNER JOIN up.matricula u "
            + "INNER JOIN up.idProjeto p "
            + "where up.idProjeto.idProjeto =:idProjeto and "
            + "(up.matricula.tipoUsuario = 'Aluno' or up.matricula.tipoUsuario = 'Voluntario') "
            + "ORDER BY up.matricula.nome"),

    @NamedQuery(name = "UsuarioProjeto.getProjectOfProfessor",
            query = "SELECT up.idProjeto FROM UsuarioProjeto up "
            + "INNER JOIN up.idProjeto u "
            + "INNER JOIN up.idProjeto p "
            + "where up.matricula.tipoUsuario = 'Professor' and "
            + "up.matricula.matricula =:matricula ORDER BY up.idProjeto.nome"),

    @NamedQuery(name = "UsuarioProjeto.getAllProjects",
            query = "SELECT p FROM Projeto p "
            + "ORDER BY p.nome"),

    @NamedQuery(name = "UsuarioProjeto.getPontoByUserByPresenteDay",
            query = "SELECT up FROM UsuarioProjeto up "
            + "INNER JOIN Ponto p "
            + "where p.data =:data and p.horaEntrada IS null "
            + "and p.idUsuarioProjeto.matricula.matricula =:matricula"),

    @NamedQuery(name = "UsuarioProjeto.getProjectByUserByPresenteDay2",
            query = "select p.nome, p.idProjeto from UsuarioProjeto up "
            + "inner join up.idProjeto p "
            + "where up.matricula.matricula = :matricula and up.id not in "
            + "(select up2.id from Ponto po2 "
            + "inner join po2.idUsuarioProjeto up2 "
            + "inner join up2.idProjeto p2 "
            + "where up2.matricula.matricula = :matricula and po2.data = :data)"),

    @NamedQuery(name = "UsuarioProjeto.getProjectByUser",
            query = "SELECT DISTINCT(up.idProjeto) FROM UsuarioProjeto up "
            + "INNER JOIN up.idProjeto proj "
            + "INNER JOIN up.matricula us where  us.matricula = :matricula")

})

public class UsuarioProjeto implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioProjeto")
    private List<Ponto> pontoList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "dataCadastro")
    @Temporal(TemporalType.DATE)
    private Date dataCadastro;
    @JoinColumn(name = "matricula", referencedColumnName = "matricula")
    @ManyToOne(optional = false)
    private Usuario matricula;
    @JoinColumn(name = "idProjeto", referencedColumnName = "idProjeto")
    @ManyToOne(optional = false)
    private Projeto idProjeto;

    public UsuarioProjeto() {
    }

    public UsuarioProjeto(Integer id) {
        this.id = id;
    }

    public UsuarioProjeto(Usuario matricula, Projeto idProjeto) {
        this.matricula = matricula;
        this.idProjeto = idProjeto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Usuario getMatricula() {
        return matricula;
    }

    public void setMatricula(Usuario matricula) {
        this.matricula = matricula;
    }

    public Projeto getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(Projeto idProjeto) {
        this.idProjeto = idProjeto;
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

    public List<Ponto> getPontoList() {
        return pontoList;
    }

    @Override
    public String toString() {
        return "models.UsuarioProjeto[ id=" + id + " ]";
    }

}
