/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class CalendarView implements Serializable{

    private Calendar date1 = Calendar.getInstance();
    private Calendar date2 = Calendar.getInstance();

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected",
                format.format(event.getObject())));
    }

    public void click() {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.update("form:display");
        requestContext.execute("PF('dlg').show()");
    }

//    public Date converterDate(Date date) throws ParseException{
//        String MonthYear = null;
//        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
//        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
//
//        String inputText = date.toString();
//        Date date1 = inputFormat.parse(inputText);
//        Date outputText = outputFormat.format(date1);
//        return outputText;
//    }
    
    public Calendar getDate1() {
        return date1;
    }

//    public Date getDateConvertida(){
//        Date d = new Date();
//        d = getDate1().getTime();
//        return d;
//    }
    

    public void setDate1(Calendar date1) {
        this.date1 = date1;
    }

    public Calendar getDate2() {
        return date2;
    }

    public void setDate2(Calendar date2) {
        this.date2 = date2;
    }

}
