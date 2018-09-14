
package webprofe.webfw.demos.slate2;

import javax.servlet.ServletContext;
import java.util.Date;


/**
 * SlateService representa el modelo de una aplicación simple de pizarra remota.
 * Los 2 métodos get/setValue del interfaz permiten consultar/modificar el contenido
 * de la pizarra.
 */
public class SlateApp
{

    protected SlateValue last_value;


    public SlateService newService(String user)
    {
	if (user == null) {
	    throw new IllegalArgumentException("User is null");
	}
	return new SlateService(this,user);
    }


    /** Obtiene el contenido de la pizarra. */
    protected SlateValue getValue()
    {
	return last_value;
    }


    /** Modifica el contenido de la pizarra */
    protected void setValue(String user, String msg)
    {
	last_value = new SlateValue(user, msg, new Date());
    }

}

