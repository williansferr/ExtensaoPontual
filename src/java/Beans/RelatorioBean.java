/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.view.JasperViewer;

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

    public void visualizarRelatorio() {
        RelatorioBean r = new RelatorioBean();
//        r.gerarRelatorio();
    }

//    public void gerarRelatorio(Usuario usuario, Projeto projeto, Calendar dataInicial, Calendar dataFinal) {
//
//        ServletOutputStream servletOutputStream = null;
//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
//        try {
//            servletOutputStream = response.getOutputStream();
//            String arquivo = "D:\\UNIFIL\\PrimeFacesSipow\\web\\relatorio\\Relatorio.jasper";
//            Map<String, Object> parametros = new HashMap<String, Object>();
//            parametros.put("aluno", usuario);
//            parametros.put("projeto", projeto);
//            parametros.put("dataInicial", dataInicial);
//            parametros.put("dataFinal", dataFinal);
//            JasperRunManager.runReportToPdfStream(new FileInputStream(new File(arquivo)), response.getOutputStream(), parametros, conexao.getConnection());
//            response.setContentType("D:\\UNIFIL\\PrimeFacesSipow\\web\\META-INF\\relatorio\\Relatorio.jasper");
//            servletOutputStream.flush();
//            servletOutputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void relatorio(Usuario usuario, Projeto projeto, Calendar dataInicial, Calendar dataFinal) throws JRException, IOException {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("aluno", usuario);
        parametros.put("projeto", projeto);
        parametros.put("dataInicial", dataInicial);
        parametros.put("dataFinal", dataFinal);
        PDF(parametros);

    }

    //metodo da net
    public void PDF(Map<String, Object> parametros) throws JRException, IOException {
        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("Relatorio.jasper");
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(null);
        String arquivo = "D:\\UNIFIL\\PrimeFacesSipow\\web\\META-INF\\Relatorio.jasper";
        jasperPrint = JasperFillManager.fillReport(new FileInputStream(new File(arquivo)), parametros, conexao.getConnection());
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) context.getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=Relatorio.pdf");
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        FacesContext.getCurrentInstance().responseComplete();
        JasperViewer view = new JasperViewer(jasperPrint, false);
        view.show();

    }

//    private void gerarRelatorioWeb(JRDataSource jrds, Map<String, Object> parametros, String arquivo) {
//
//        ServletOutputStream servletOutputStream = null;
//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
//        try {
//            servletOutputStream = response.getOutputStream();
//            JasperRunManager.runReportToPdfStream(new FileInputStream(new File(arquivo)), response.getOutputStream(), parametros, jrds);
//            response.setContentType("D:\\UNIFIL\\PrimeFacesSipow\\web\\META-INF\\relatorio\\Relatorio.jasper");
//            servletOutputStream.flush();
//            servletOutputStream.close();
//            context.renderResponse();
//            context.responseComplete();
//        } catch (Exception e) {
//        }
//
//    }
//    private void gerarRelatorio(JRDataSource jrds, Map<String, Object> parametros, String arquivo) {
//
////        System.out.println("Entrou no 2º");
////        System.out.println("Parametros: " + parametros);
//        try {
//            jasperPrint = JasperFillManager.fillReport(arquivo, parametros, conexao.getConnection());
//            JasperViewer view = new JasperViewer(jasperPrint, false);
//            view.setExtendedState(Frame.MAXIMIZED_BOTH);
//            view.setVisible(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    private void gerarRelatorio(JRDataSource jrds, Map<String, Object> parametros, HttpServletResponse response) throws IOException {
//        OutputStream out = null;
//        // obtém o relatório compilado
//        InputStream inputStream = getClass().getResourceAsStream("D:\\UNIFIL\\PrimeFacesSipow\\web\\META-INF\\relatorio\\Relatorio.jasper");
//        // preenche o mapa de parâmetros
////        Map<String, Object> parametros = new HashMap<String, Object>();
//        parametros.put("primeiroNome", "D%");
//        try {
//            // gera o relatório e atribui o OutputStream gerado
//            out = createPDFReport(inputStream, parametros,
//                    conexao.getConnection(), response);
//        } catch (JRException exc) {
//            exc.printStackTrace();
//        } finally {
//            // se não aconteceu nenhum problema, fecha o output stream
//            if (out != null) {
//                out.close();
//            }
//
//        }
//    }
//    private List<ObjetoTeste> retornaListaPontos(Usuario usuario, Projeto projeto, Calendar dataInicial, Calendar dataFinal) {
//        List<ObjetoTeste> lista = new ArrayList<>();
//
//        ObjetoTeste ob1 = new ObjetoTeste();
//        ob1.setDataFinal(dataFinal);
//        ob1.setDataInicial(dataInicial);
//        ob1.setProjeto(projeto);
//        ob1.setUsuario(usuario);
//        lista.add(ob1);
//
//        return lista;
//
//    }
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
