
package dat.prac3.controller;

import dat.prac3.model.dao.*;

import dat.fw.web.handler.HandlerData;
import dat.fw.web.content.TypedContent;
import dat.fw.web.html.Html;
import dat.fw.web.widget.Widget;
import dat.fw.util.*;

import java.util.*;

import dat.fw.web.html.template.HtmlTempl;


/**
 * @author Jordi Forga
 */
public abstract class MsgHandler extends Fun1E<HandlerData<MsgApp>,TypedContent> {

    public static Maybe<Entity<User>> getSessionUser(HandlerData<MsgApp> henv) throws Exception {
	Maybe<String> mbUserId = henv.lookupSession("userId");
	if (mbUserId.isNothing()) {
	    return Maybe.Nothing();
	} else {
            int userId = Integer.parseInt(mbUserId.fromJust());
	    Maybe<User> mbUser = henv.getApp().daoFact.getUserDAO().get(userId);
	    if (mbUser.isNothing()) {
	        return Maybe.Nothing();
            } else {
                return Maybe.Just(Entity.Entity(userId, mbUser.fromJust()));
            }
        }
    }

    // Remove this handler when all functionalities are implemented
    private static final MsgHandler handleTODO = new MsgHandler(){
        @Override
        public TypedContent call(HandlerData<MsgApp> henv) throws Exception {
            Html html = Html.PreEscaped(
                "<!doctype html>\n" +
                "<html>\n" +
                "<head><title>Pràctica 3</title></head>\n" +
                "<body><h1>TODO</h1></body>\n" +
                "</html>\n"
            );
            return Html.ToTypedContent_Html.toTypedContent(html);
        }
    };

    public static final MsgHandler getHomeR = new MsgHandler(){
        @Override
        public TypedContent call(HandlerData<MsgApp> henv) throws Exception {
            final Maybe<Html> mbErrMessage = henv.getMessage();
            final Maybe<Entity<User>> mbUser = getSessionUser(henv);
            Html html = henv.withUrlRenderer( $(HtmlTempl.templateFile("MsgApp", "home.html")) );
            return Html.ToTypedContent_Html.toTypedContent(html);
        }
    };

    public static final MsgHandler postLoginR = new MsgHandler(){
        @Override
        public TypedContent call(HandlerData<MsgApp> henv) throws Exception {
            Maybe<String> mbName = henv.lookupParam("name");
            Maybe<String> mbPasswd = henv.lookupParam("password");
            if (mbName.isJust() && mbPasswd.isJust()) {
                // Autentifica un usuari.
                Maybe<Entity<User>> selected = henv.getApp().daoFact.getUserDAO().selectByName(mbName.fromJust());
                if (selected.isNothing() || !selected.fromJust().value.password.equals(mbPasswd.fromJust())) {
                    henv.setMessage("Error d'autenticació");
                    return henv.redirect(MsgRoute.HomeR);
                } else {
                    henv.setSession("userId", Integer.toString(selected.fromJust().id));
                    return henv.redirect(MsgRoute.HomeR);
                }
            } else {
                return henv.invalidArgs(IList.mkIList("name", "password"));
            }
        }
    };

    public static final MsgHandler postLogoutR = new MsgHandler(){
        @Override
        public TypedContent call(HandlerData<MsgApp> henv) throws Exception {
            henv.deleteSession("userId");
            return henv.redirect(MsgRoute.HomeR);
        }
    };

    public static final MsgHandler getInboxR = new MsgHandler(){
        @Override
        public TypedContent call(HandlerData<MsgApp> henv) throws Exception {
            final Maybe<Html> mbErrMessage = henv.getMessage();
            final Maybe<Entity<User>> mbUser = getSessionUser(henv);
            if (mbUser.isNothing()) {
                return henv.redirect(MsgRoute.HomeR);
            } else {
                final int userId = mbUser.fromJust().id;
                final String userName = mbUser.fromJust().value.name;
				Entity<Message> received = henv.getApp().daoFact.getMessageDAO().selectByTo(userId);
				
		//Per mostrar el nom del receptor                
		final Pair<Entity<Message>, String> receivedWithName = new Pair<Entity<Message>, String> (received, henv.getApp().daoFact.getUserDAO().get(received.id).fromJust().name);

				final String remitent = 
                Html html = henv.withUrlRenderer( $(HtmlTempl.templateFile("MsgApp", "inbox.html")) );
                return Html.ToTypedContent_Html.toTypedContent(html);
            }
        }
    };


    public static final MsgHandler postInboxR = new MsgHandler(){

		//agafem el valor del botó
		Maybe<string> mb_button = LookupParam("delete");

		//gestió dels botons
		if(!isNothing(mb_button){
		
			//agafem els id dels ckeckbox seleccionats, està dins del camp valor de l'html del checkbox	
			IList<string> seleccionats = LookupParams("checkbox"); 	
		
			for string id :: seleccionats{

				henv.getApp().daoFact.getMessageDAO().delete(id));	
			}	
		}

		//recarreguem la pàgina
			            return henv.redirect(MsgRoute.InboxR);
	};



	//mostra pàgina d'escriure el missatge
    public static final MsgHandler getComposeR = new MsgHandler(){

					final String remitent = 
                Html html = henv.withUrlRenderer( $(HtmlTempl.templateFile("MsgApp", "compose.html")) );
                return Html.ToTypedContent_Html.toTypedContent(html);

	};




	//envia el missatge (ho afegeix a la bbdd)
    public static final MsgHandler postComposeR = new MsgHandler(){

		//gestió dels botons
		if(!isNothing(mb_send){

			//agafem el valor del botó
			Maybe<string> mb_send = LookupParam("ok");
			Maybe<string> mb_to = LookupParam("To");
			Maybe<string> mb_subject = LookupParam("Subject");
			Maybe<string> mb_content = LookupParam("Content");
			int userId = mbUser.fromJust().id;
			//obtenir data		
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 0);
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String date=cal.getTime());

			//formato de un message: Message(int fromId, int toId, java.util.Date date, java.lang.String subject, java.lang.String text) 
				Message msg = new Message(userId, mb_to, date, mb_subject, mb_content);
			//inserim a la base de dades
				henv.getApp().daoFact.getMessageDAO().insert(msg));
		
		}

		//recarreguem la pàgina
			            return henv.redirect(MsgRoute.ComposeR);
	};


    //mostra els missatges quan li donem al subratllat de l'asunto
    public static MsgHandler getMessageR(int mid) { 
		return new MsgHandler(){
        @Override
        public TypedContent call(HandlerData<MsgApp> henv) throws Exception {
            final Maybe<Html> mbErrMessage = henv.getMessage();
            final Maybe<Entity<User>> mbUser = getSessionUser(henv);
            if (mbUser.isNothing()) {
                return henv.redirect(MsgRoute.HomeR);
            } else {
                final int userId = mbUser.fromJust().id;
                final String userName = mbUser.fromJust().value.name;
	
				final Maybe<Message> received = henv.getApp().daoFact.getMessageDAO().get(mid);
			}

			 if (received.isNothing()) {
                return henv.redirect(MsgRoute.HomeR);
			}else{
				final String remitent = received.fromJust().fromId;
                Html html = henv.withUrlRenderer( $(HtmlTempl.templateFile("MsgApp", "message.html")) );
                return Html.ToTypedContent_Html.toTypedContent(html);
            }
        }
    };

	 }

    //dentro del mensaje un botón de delete para borrar el mensaje concreto 
    public static MsgHandler postMessageR(int mid) { 
	return new MsgHandler(){
        @Override
        public TypedContent call(HandlerData<MsgApp> henv) throws Exception {
            final Maybe<Html> mbErrMessage = henv.getMessage();
            final Maybe<Entity<User>> mbUser = getSessionUser(henv);
            if (mbUser.isNothing()) {
                return henv.redirect(MsgRoute.HomeR);
            } else {
                final int userId = mbUser.fromJust().id;
                final String userName = mbUser.fromJust().value.name;
	
				final Maybe<Message> received = henv.getApp().daoFact.getMessageDAO().get(mid);
			}
//agafem el valor del botó
		Maybe<string> mb_ko = LookupParam("ko");

		//gestió dels botons
		if(!isNothing(mb_ko){

				henv.getApp().daoFact.getMessageDAO().delete(mid));	
			}	
		}



Html html = henv.withUrlRenderer( $(HtmlTempl.templateFile("MsgApp", "message.html")) );
                return Html.ToTypedContent_Html.toTypedContent(html);

}

