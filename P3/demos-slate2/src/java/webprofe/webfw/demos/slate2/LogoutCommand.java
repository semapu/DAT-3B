
package webprofe.webfw.demos.slate2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * @author Jordi Forga
 * Comando de salida de la pizarra remota
 */
public class LogoutCommand extends SlateBaseCommand
{

    public String execute(HttpServletRequest request)
    {
	HttpSession session = request.getSession();
	session.removeAttribute("demos.slate2");

	return "success";
    }


}

