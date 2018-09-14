
package dat.prac3.controller;

import dat.fw.web.servlet.DispatchServlet;
import dat.fw.web.dispatch.*;

import javax.servlet.ServletContext;


/**
 * @author Jordi Forga
 */
public class MsgServlet extends DispatchServlet<MsgApp> {

    @Override
    public MsgApp makeApp(ServletContext servletContext) throws Exception
    {
        return new MsgApp(servletContext);
    }

    @Override
    public MsgDispatch getDispatch() {
        return new MsgDispatch();
    }

}


