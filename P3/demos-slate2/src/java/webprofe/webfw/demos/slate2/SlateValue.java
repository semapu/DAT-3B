
package webprofe.webfw.demos.slate2;

import java.util.Date;


/**
 * SlateValue representa el contenido de la pizarra remota.
 * Dispone de 3 propiedades: who (usuario que modificó el contenido), text (contenido de la pizarra)
 * y date (fecha en que se modificó el contenido).
 */
public class SlateValue
{

    private String who;

    private String text;

    private Date date;


    public SlateValue(String u, String t, Date d)
    {
	who = u;
	text = t;
	date = d;
    }


    public String getWho()
    {
	return who;
    }


    public String getText()
    {
	return text;
    }


    public Date getDate()
    {
	return date;
    }


}

