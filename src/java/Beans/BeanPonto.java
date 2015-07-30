package Beans;

import Controllers.PontoJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import models.Ponto;

/**
 *
 * @author danielmorita
 */
@ManagedBean
@SessionScoped
public class BeanPonto implements Serializable {

    private PontoJpaController pontoControle;
    private List<Ponto> lista = new ArrayList<Ponto>();
    @ManagedProperty(value = "#{beanLogar}")
    private BeanLogar beanLogar;
    private Integer idUsuario = 0;
    private String txtDescricao;
    private Util util = new Util();
    private Ponto pontoAtual;

    public BeanPonto() {
    }

    public String iniciarPonto() {
        if (pontoControle == null) {
            pontoControle = new PontoJpaController();
        }
        if (lista.size() > 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Ponto ja foi iniciado", ""));
        } else {
            try {
                pontoAtual = new Ponto();
                Date dataAtual = new Date();
                pontoAtual.setData(dataAtual);
                pontoAtual.setDescricaoAtividade(txtDescricao);
                pontoAtual.setHoraEntrada(dataAtual);
                pontoAtual.setMatricula(getBeanLogar().getUsuarioLogado().getMatricula());
                if (txtDescricao == null || txtDescricao.equals("")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: favor informar a atividade", ""));
                } else {
                    getLista();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ponto Efetuado com sucesso", ""));
                    txtDescricao = "";
                    pontoControle.create(pontoAtual);
                    return "PF('pontoDialog').hide()";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void encerrarPonto() {
        if (pontoControle == null) {
            pontoControle = new PontoJpaController();
        }
        getLista();
        if (lista.get(lista.size() - 1).getHoraSaida() == null) {
            if (lista.size() == 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Ponto ainda nao foi iniciado para ser encerrado", ""));
            } else {
                try {
                    Date dataAtual = new Date();
                    pontoAtual = lista.get(lista.size() - 1);
                    pontoAtual.setHoraSaida(dataAtual);
                    pontoAtual.setIdPonto(lista.get(lista.size() - 1).getIdPonto());
                    pontoControle.edit(pontoAtual);
                    getLista();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ponto encerrado com sucesso", ""));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: ", ""));
                    e.printStackTrace();
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Ponto ja foi encerrado", ""));
        }
    }

    public String disableIniciar() {
        System.out.println("lista.size() " + getLista());
        if (lista.size() > 0) {
            return "true";
        }
        return "false";
    }

    public String disableEncerrar() {
        getLista();
        if (lista.size() > 0) {
            if (lista.get(lista.size() - 1).getHoraSaida() == null) {
                return "false";
            }
        }
        return "true";
    }

    public List<Ponto> getLista() {
        if (pontoControle == null) {
            pontoControle = new PontoJpaController();
        }
        if (getBeanLogar().getUsuarioLogado() != null) {
            idUsuario = getBeanLogar().getUsuarioLogado().getMatricula();
        }
        lista = pontoControle.findByUsuarioAndData(idUsuario, new Date());
        return lista;
    }

    public void setLista(List<Ponto> lista) {
        this.lista = lista;
    }

    public BeanLogar getBeanLogar() {
        return beanLogar;
    }

    public void setBeanLogar(BeanLogar beanLogar) {
        this.beanLogar = beanLogar;
    }

    public String getTxtDescricao() {
        return txtDescricao;
    }

    public void setTxtDescricao(String txtDescricao) {
        this.txtDescricao = txtDescricao;
    }

}
