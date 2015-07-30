package Beans;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author danielmorita
 */
public class Util {

    /**
     * Metodo formata a data conforme o parametro passado ex: dd/MM/yyyy
     * HH:mm:ss
     *
     * @param formato tipo da formatação
     * @param data data a ser formatada
     * @return valor em String formatado
     */
    public String formatarDatePara(String formato, Date data) {
        SimpleDateFormat formatas = new SimpleDateFormat(formato);
        return formatas.format(data);
    }

}
