
package dat.prac3.controller;

import dat.fw.web.app.WebApp;
import dat.fw.web.handler.HandlerData;
import dat.fw.web.html.Html;

import dat.fw.util.*;

import dat.prac3.model.dao.DAOFactory;

import javax.servlet.ServletContext;


public class MsgApp
{

    /* Change to true for testing with local memory */
    public static final boolean TESTING = false;

    public final DAOFactory daoFact;

    public MsgApp(ServletContext servletContext) throws Exception {
        if (TESTING) {
            daoFact = new dat.prac3.model.memdao.MemDAOFactory();
	} else {
            daoFact = new dat.prac3.model.sqldao.SqlDAOFactory(
                servletContext.getInitParameter("message_app.db.url"),
                servletContext.getInitParameter("message_app.db.user"),
                servletContext.getInitParameter("message_app.db.password")
	    );
	}
    }


    //------------------- Type class instances ------------------------

    public static WebApp<MsgApp> WebApp = new WebApp<MsgApp>();

}

