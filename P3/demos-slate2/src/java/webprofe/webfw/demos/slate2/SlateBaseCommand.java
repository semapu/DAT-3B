
package webprofe.webfw.demos.slate2;

import webprofe.webfw.controller.CommandSupport;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Jordi Forga
 * Base de los comandos de pizarra remota
 */
public abstract class SlateBaseCommand extends CommandSupport
{

    public void init()
    {
	SlateApp app = (SlateApp)context.getAttribute("demos.slate2");
	if (app == null) {
	    app = new SlateApp();
	    context.setAttribute("demos.slate2",app);
	}
    }


}

