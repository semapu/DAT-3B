
package webprofe.webfw.demos.slate2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * @author Jordi Forga
 * Comando de entrada en la pizarra remota
 */
public class LoginCommand extends SlateBaseCommand
{

    public String execute(HttpServletRequest request)
    {
	String user = request.getParameter("user");

	SlateApp app = (SlateApp)context.getAttribute("demos.slate2");
	SlateService srv = app.newService(user);

	HttpSession session = request.getSession();
	session.setAttribute("demos.slate2",srv);

	return "success";
    }


}

