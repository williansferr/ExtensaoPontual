/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controllers.PontoJpaController;
import Controllers.UsuarioJpaController;
import Controllers.UsuarioProjetoJpaController;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author Willians
 */
public class teste {

    public static void main(String[] args) throws ParseException, JRException {

        UsuarioJpaController controlerUsuario = new UsuarioJpaController();
        BeanUsuario beanUsuario = new BeanUsuario();
        UsuarioProjetoJpaController jpa = new UsuarioProjetoJpaController();
        PontoJpaController controlePonto = new PontoJpaController();
        BeanConverterProjeto a = new BeanConverterProjeto();

        InputStream relatorio = null;

        try {

            String pdfFile = "C:\\sampleReport.pdf";

            ByteArrayOutputStream Teste = new ByteArrayOutputStream();

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(teste.class.getClassLoader().getResourceAsStream("Report/sampleReport.jasper"));
            jasperReport.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);

            Map<String, Integer> params = new HashMap<>();
            JasperPrint print = JasperFillManager.fillReport(jasperReport, null);

            JRExporter exporter = new net.sf.jasperreports.engine.export.JRPdfExporter();
     //JRExporter exporter = new net.sf.jasperreports.engine.export.JRHtmlExporter();
            //JRExporter exporter = new net.sf.jasperreports.engine.export.JRXlsExporter();
            //JRExporter exporter = new net.sf.jasperreports.engine.export.JRXmlExporter();
            //JRExporter exporter = new net.sf.jasperreports.engine.export.JRCsvExporter();

            //exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, pdfFile);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, Teste);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
            exporter.exportReport();

            relatorio = new ByteArrayInputStream(Teste.toByteArray());

        } catch (JRException ex) {
            Logger.getLogger(teste.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
