/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Willians
 */
@Entity
@Table(name = "ponto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ponto.findAll",
            query = "SELECT p FROM Ponto p"),
    @NamedQuery(name = "Ponto.findByIdPonto",
            query = "SELECT p FROM Ponto p WHERE p.idPonto = :idPonto"),

    @NamedQuery(name = "Ponto.findByMatricula",
            query = "SELECT p FROM Ponto p WHERE p.idUsuarioProjeto = :matricula"),

    @NamedQuery(name = "Ponto.findByHoraEntrada",
            query = "SELECT p FROM Ponto p WHERE p.horaEntrada = :horaEntrada"),

    @NamedQuery(name = "Ponto.findByHoraSaida",
            query = "SELECT p FROM Ponto p WHERE p.horaSaida = :horaSaida"),

    @NamedQuery(name = "Ponto.findByMonth",
            query = "SELECT p FROM Ponto p "
            + "INNER JOIN p.idUsuarioProjeto up "
            + "INNER JOIN up.matricula us "
            + "WHERE FUNC('MONTH', p.data) = :mes and us.matricula = :matricula ORDER BY p.data"),

    @NamedQuery(name = "Ponto.findByDescricaoAtividade",
            query = "SELECT p FROM Ponto p WHERE p.descricaoAtividade = :descricaoAtividade"),

    @NamedQuery(name = "Ponto.findByTotalHoras",
            query = "SELECT p FROM Ponto p WHERE p.totalHoras = :totalHoras"),

    @NamedQuery(name = "Ponto.findByUsuarioAndData",
            query = "SELECT p FROM Ponto p "
            + "where p.idUsuarioProjeto.matricula.matricula= :matricula and p.data = :data"),

    @NamedQuery(name = "Ponto.findByUsuarioWithEmptyExit",
            query = "SELECT p FROM Ponto p "
            + "where p.horaSaida IS null and p.idUsuarioProjeto.matricula = :matricula"),

    @NamedQuery(name = "Ponto.findByDateByUserAndProject",
            query = "SELECT p FROM Ponto p "
            + "INNER JOIN p.idUsuarioProjeto up "
            + "INNER JOIN up.matricula us "
            + "WHERE p.data >= :dataInicial and p.data <= :dataFinal and "
            + "p.idUsuarioProjeto.matricula.matricula = :matricula and "
            + "up.idProjeto.idProjeto = :idProjeto "
            + "ORDER BY p.data"),

    @NamedQuery(name = "Ponto.findByDateByUserAndProject",
            query = "SELECT p FROM Ponto p "
            + "INNER JOIN p.idUsuarioProjeto up "
            + "INNER JOIN up.matricula us "
            + "WHERE p.data >= :dataInicial and p.data <= :dataFinal and "
            + "p.idUsuarioProjeto.matricula.matricula = :matricula and "
            + "up.idProjeto.idProjeto = :idProjeto "
            + "ORDER BY p.data"),
    
    @NamedQuery(name = "Ponto.findAmountTotalOfHours",
            query = "SELECT sum(p.horaSaida - p.horaEntrada) FROM Ponto p "
            + "INNER JOIN p.idUsuarioProjeto up "
            + "INNER JOIN up.matricula us "
            + "where p.idUsuarioProjeto.matricula.matricula= :matricula "
            + "and up.idProjeto.idProjeto= :projeto")})

public class Ponto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPonto")
    private Integer idPonto;
    @Column(name = "horaEntrada")
    @Temporal(TemporalType.TIME)
    private Date horaEntrada;
    @Column(name = "horaSaida")
    @Temporal(TemporalType.TIME)
    private Date horaSaida;
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @Column(name = "descricaoAtividade")
    private String descricaoAtividade;
    @Column(name = "totalHoras")
    private Integer totalHoras;
    @Column(name = "justificativa")
    private String justificativa;
    @JoinColumn(name = "idUsuarioProjeto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UsuarioProjeto idUsuarioProjeto;

    public Ponto() {
    }

    public Ponto(Integer idPonto) {
        this.idPonto = idPonto;
    }

    public Integer getIdPonto() {
        return idPonto;
    }

    public void setIdPonto(Integer idPonto) {
        this.idPonto = idPonto;
    }

    public Date getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Date horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Date getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(Date horaSaida) {
        this.horaSaida = horaSaida;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricaoAtividade() {
        return descricaoAtividade;
    }

    public void setDescricaoAtividade(String descricaoAtividade) {
        this.descricaoAtividade = descricaoAtividade;
    }

    public Integer getTotalHoras() {
        return totalHoras;
    }

    public void setTotalHoras(Integer totalHoras) {
        this.totalHoras = totalHoras;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public UsuarioProjeto getIdUsuarioProjeto() {
        return idUsuarioProjeto;
    }

    public void setIdUsuarioProjeto(UsuarioProjeto idUsuarioProjeto) {
        this.idUsuarioProjeto = idUsuarioProjeto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPonto != null ? idPonto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ponto)) {
            return false;
        }
        Ponto other = (Ponto) object;
        if ((this.idPonto == null && other.idPonto != null) || (this.idPonto != null && !this.idPonto.equals(other.idPonto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Ponto[ idPonto=" + idPonto + " ]";
    }

}
