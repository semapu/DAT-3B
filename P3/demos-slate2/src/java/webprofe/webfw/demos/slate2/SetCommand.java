
package webprofe.webfw.demos.slate2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * @author Jordi Forga
 * Comando de modificación de la pizarra remota
 */
public class SetCommand extends SlateBaseCommand
{

    public String execute(HttpServletRequest request)
    {
	String message = request.getParameter("message");

	HttpSession session = request.getSession();
	SlateService srv = (SlateService)session.getAttribute("demos.slate2");

	if (srv == null) {
	    return "error";
	} else {
	    srv.setValue(message);
	    return "success";
	}
    }


}

