package Beans;

import Controllers.UsuarioJpaController;
import java.io.Serializable;
import javax.persistence.Persistence;
import java.util.GregorianCalendar;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class BeanDataHora implements Serializable{

    UsuarioJpaController jpa = new UsuarioJpaController(Persistence.createEntityManagerFactory("ExtensaoPontualPU"));
    GregorianCalendar data = new GregorianCalendar();
    GregorianCalendar horaEntrada = new GregorianCalendar();
    GregorianCalendar horaSaida = new GregorianCalendar();
    GregorianCalendar publica = new GregorianCalendar();

    public GregorianCalendar getData() {
        return data;
    }

    public void setData(GregorianCalendar data) {
        this.data = data;
    }

    public GregorianCalendar getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(GregorianCalendar horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public GregorianCalendar getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(GregorianCalendar horaSaida) {
        this.horaSaida = horaSaida;
    }

    public GregorianCalendar getPublica() {
        return publica;
    }

    public void setPublica(GregorianCalendar publica) {
        this.publica = publica;
    }
    

    public GregorianCalendar getDataHoraAtual(){
        return publica;
    }
    
    
    
}
