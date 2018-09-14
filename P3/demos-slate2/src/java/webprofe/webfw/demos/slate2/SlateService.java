
package webprofe.webfw.demos.slate2;

import javax.servlet.ServletContext;
import java.util.Date;


/**
 * SlateService representa el modelo de una sesión de pizarra remota (asociada a un usuario).
 * Los 2 métodos get/setValue del interfaz permiten consultar/modificar el contenido
 * de la pizarra.
 */
public class SlateService
{

    protected SlateApp application;
    protected String user;


    protected SlateService(SlateApp app, String usr)
    {
	application = app;
	user = usr;
    }


    /** Obtiene el usuario asociado a esta sesión. */
    public String getUser()
    {
	return user;
    }


    /** Obtiene el contenido de la pizarra. */
    public SlateValue getValue()
    {
	return application.getValue();
    }


    /** Modifica el contenido de la pizarra */
    public void setValue(String msg)
    {
	if (msg == null) {
	    throw new IllegalArgumentException("Message is null");
	}
	application.setValue(user,msg);
    }

}

