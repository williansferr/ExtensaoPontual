package Beans;

import Controllers.PontoJpaController;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import models.Projeto;
import models.Usuario;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.primefaces.context.RequestContext;

/**
 *  *
 *  * @author andii  
 */
@ManagedBean
@ViewScoped
public class RelatorioBean implements Serializable {

    Usuario usuario;
    Projeto projeto;
    CalendarView dataInicial;
    CalendarView dataFinal;
    PontoJpaController conexao = new PontoJpaController();
    JasperPrint jasperPrint;

    public void relatorio(Usuario usuario, Projeto projeto, Calendar dataInicial, Calendar dataFinal) throws JRException, IOException {

        if (projeto != null && usuario != null) {
            try {

                Map<String, Object> parametros = new HashMap<String, Object>();
                BeanPonto controlePonto = new BeanPonto();
                String totalHoras = controlePonto.buscaHorasTotal(usuario, projeto, dataInicial, dataFinal);
                parametros.put("aluno", usuario.getMatricula());
                parametros.put("projeto", projeto.getIdProjeto());
                parametros.put("dataInicial", dataInicial.getTime());
                parametros.put("dataFinal", dataFinal.getTime());
                parametros.put("total", totalHoras);
                PDF(parametros);
            } catch (Exception e) {
                RequestContext.getCurrentInstance().execute("PF('wiz').hideBackNav();");
                e.printStackTrace();
            }
        } else {
            Map<String, Object> parametros2 = new HashMap<String, Object>();
            BeanPonto controlePonto = new BeanPonto();
            String totalHoras = controlePonto.buscaHorasTotal(usuario, projeto, dataInicial, dataFinal);
            parametros2.put("aluno", 0);
            parametros2.put("projeto", 0);
            parametros2.put("dataInicial", dataInicial.getTime());
            parametros2.put("dataFinal", dataFinal.getTime());
            parametros2.put("total", totalHoras);
            PDF(parametros2);
        }

    }

    public void PDF(Map<String, Object> parametros) throws JRException, IOException {

        String arquivo = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/META-INF/Relatorio.jasper");
        jasperPrint = JasperFillManager.fillReport(new FileInputStream(new File(arquivo)), parametros, conexao.getConnection());
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) context.getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=Relatorio.pdf");
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
        FacesContext.getCurrentInstance().responseComplete();

    }

    public static OutputStream createPDFReport(
            InputStream inputStream,
            Map<String, Object> parametros,
            Connection conexao,
            HttpServletResponse response) throws JRException, IOException {

        // configura o content type do response
        response.setContentType("application/pdf");

        // obtém o OutputStream para escrever o relatório
        OutputStream out = response.getOutputStream();

        /*
         * Cria um JasperPrint, que é a versão preenchida do relatório,
         * usando uma conexão.
         */
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                inputStream, parametros, conexao);

        // Exporta em PDF, escrevendo os dados no output stream do response.
        JRExporter exporter = new JRPdfExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT,
                jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
                out);

        // gera o relatório
        exporter.exportReport();

        // retorna o OutputStream
        return out;

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

    public CalendarView getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(CalendarView dataInicial) {
        this.dataInicial = dataInicial;
    }

    public CalendarView getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(CalendarView dataFinal) {
        this.dataFinal = dataFinal;
    }

}
