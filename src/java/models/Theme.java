/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import Beans.BeanConverterProjeto;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Willians
 */
@FacesConverter(value = "projetoConverter")
public class Theme implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (Projeto) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof Projeto) {
            Projeto entity = (Projeto) value;
            if (entity != null && entity instanceof Projeto && entity.getIdProjeto() != null) {
                uiComponent.getAttributes().put(entity.getIdProjeto().toString(), entity);
                return entity.getIdProjeto().toString();
            }
        }
        return "";
    }

}
