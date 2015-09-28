package Beans;

import Controllers.PontoJpaController;
import Controllers.UsuarioJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.swing.Action;
import models.Ponto;
import models.Projeto;
import models.Usuario;
import models.UsuarioProjeto;

/**
 *
 * @author danielmorita
 */
@ManagedBean
@SessionScoped
public class BeanPonto implements Serializable {

    private PontoJpaController pontoControle = new PontoJpaController();
    private List<Ponto> lista = new ArrayList<Ponto>();
    private List<Ponto> allPontos = new ArrayList<Ponto>();
    @ManagedProperty(value = "#{beanLogar}")
    private BeanLogar beanLogar;
    BeanUsuario_Projeto beanUsuarioProjeto = new BeanUsuario_Projeto();
    private Integer idUsuario = 0;
    private String txtDescricao;
    private Ponto pontoAtual = new Ponto();
    Ponto ponto;
    String mes;

    UsuarioProjeto up;
    BeanConverterProjeto beanConverteProjeto = new BeanConverterProjeto();

    public BeanPonto() {
    }

    public String buscaNomeUsuario(Integer matricula) {
        if (pontoControle == null) {
            pontoControle = new PontoJpaController();
        }
        String nome = new UsuarioJpaController().findByMatricula(matricula).getNome();
        return nome;
    }

    public void iniciarPonto(Projeto p) {
        Calendar dataAtual = Calendar.getInstance();
        boolean aux = false;
        System.out.println("Projeto Selecionado: "+p.getNome());
        getListaPonto();
        up = beanUsuarioProjeto.getCadastroUsuarioProjeto(p, beanLogar.getUsuarioLogado());
        if (pontoControle == null) {
            pontoControle = new PontoJpaController();
        }

        if (!validarInicialidacao()) {
            try {
                pontoAtual.setData(dataAtual.getTime());
                pontoAtual.setDescricaoAtividade(txtDescricao);
                pontoAtual.setHoraEntrada(dataAtual.getTime());
                pontoAtual.setIdUsuarioProjeto(up);
                if (txtDescricao == null || txtDescricao.equals("")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: favor informar a atividade", ""));
                } else {

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ponto Efetuado com sucesso", ""));
                    txtDescricao = "";
                    pontoControle.create(pontoAtual);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void encerrarPonto(Ponto pont) {
        if (pontoControle == null) {
            pontoControle = new PontoJpaController();
        }

        if (pont == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Selecione o Ponto a ser Encerrado", ""));
        } else if (existePontoLista(pont)) {
            if (pont.getHoraSaida() != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ponto já consta encerrado!", ""));
            } else {
                try {
                    Calendar dataFinal = Calendar.getInstance();
                    pont.setHoraSaida(dataFinal.getTime());
                    pontoControle.edit(pont);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ponto finalizado com sucesso!", ""));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: ", ""));
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean existePontoLista(Ponto p) {
        for (int i = 0; i < getAllPontos().size(); i++) {
            if (p.getIdPonto().equals(getAllPontos().get(i).getIdPonto())) {
                return true;
            }
        }
        return false;
    }

    public String editarPonto(Action submit) {
        if (pontoControle == null) {
            pontoControle = new PontoJpaController();
        }
        try {
            pontoControle.edit(getPontoAtual());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Alteração realizada!", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_WARN, "Não foi possível realizar alteração!", ""));
        }

        return null;
    }

    public List<Ponto> getListaPonto() {
        if (pontoControle == null) {
            pontoControle = new PontoJpaController();
        }
        if (getBeanLogar().getUsuarioLogado() != null) {
            idUsuario = getBeanLogar().getUsuarioLogado().getMatricula();
        }
        setLista(pontoControle.findByUsuarioAndData(idUsuario, new Date()));
        return getLista();
    }

    //BUSCA PONTO DO USUARIO LOGADO
    public List<Ponto> getPontoUsuarioLogado(Usuario us) {
        List<Ponto> list = pontoControle.findByMatricula(us.getMatricula());
        return list;
    }

    public boolean validarInicialidacao() {
        boolean aux = false;
        for (int i = 0; i < getLista().size(); i++) {
            if (getLista().get(i).getIdUsuarioProjeto().getId().equals(up.getId())) {
                FacesContext.getCurrentInstance().addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Esse Projeto já consta iniciado Hoje!", ""));
                aux = true;
            }
        }
        return aux;
    }

    //BUSCA TODOS OS PONTO DO DIA CORRENTE DO USUARIO LOGADO
    public List<Ponto> buscaPontoDiarioDoUsuario() {
        Calendar data = Calendar.getInstance();
        
        setAllPontos(pontoControle.findByUsuarioAndData(beanLogar.getUsuarioLogado().getMatricula(), data.getTime()));
        return getAllPontos();
    }

    //BUSCA TODOS OS PONTOS DO MES CORRENTE DE DETERMINADO USUARIO (USUARIO)
    public List<Ponto> buscaPontoMensal(Usuario us) {
        Calendar m = Calendar.getInstance();
        int mes = (m.getTime().getMonth()+1);
        try {
            List<Ponto> listPonto = new ArrayList();
            listPonto = pontoControle.findByMonth(mes, us);
            return listPonto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //BUSCA TODOS OS PONTOS DO MES CORRENTE DE DETERMINADO USUARIO (Usuário, Mes)

    public List<Ponto> buscarPontoDeData(Calendar dataInicial, Calendar dataFinal, Usuario us, Projeto p) {
        CalendarView calendarView = new CalendarView();
        try {
            List<Ponto> listPonto = new ArrayList();
            listPonto = pontoControle.findByData(dataInicial, dataFinal, 
                    us.getMatricula(), p.getIdProjeto());
            return listPonto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public List<Ponto> getAllPontos() {
        return allPontos;
    }

    public void setAllPontos(List<Ponto> allPontos) {
        this.allPontos = allPontos;
    }

    public Ponto getPontoAtual() {

        return pontoAtual;
    }

    public void setPontoAtual(Ponto pontoAtual) {
        this.pontoAtual = pontoAtual;
    }

    public Ponto getPonto() {
        return ponto;
    }

    public void setPonto(Ponto ponto) {
        this.ponto = ponto;
    }

    public List<Ponto> getLista() {
        return lista;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

}
