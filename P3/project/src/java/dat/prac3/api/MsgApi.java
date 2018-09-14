
package dat.prac3.api;

import dat.prac3.controller.MsgHandler;
import dat.prac3.controller.MsgApp;
import dat.prac3.model.dao.*;

import dat.fw.web.handler.HandlerData;
import dat.fw.web.content.TypedContent;

import dat.prac3.json.JSONValue;

import dat.fw.util.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;


/**
 * @author Jordi Forga
 */
public class MsgApi
{

    public static MsgHandler handleApiR(final ApiRoute route, final String method) {
        return new MsgHandler(){
        @Override
        public TypedContent call(HandlerData<MsgApp> henv) throws Exception {
            Fun1E<HandlerData<MsgApp>,TypedContent> handler;
            if (route == ApiRoute.UserR) {
                if (method.equals("GET")) return getUserR(henv);
                else return henv.badMethod();
            } else if (route instanceof ApiRoute.AccountR) {
                String userName = ((ApiRoute.AccountR)route).userName;
                if (method.equals("GET")) return getAccountR(userName, henv);
                else return henv.badMethod();
            } else if (route instanceof ApiRoute.AccReceivedR) {
                String userName = ((ApiRoute.AccReceivedR)route).userName;
                int mid = ((ApiRoute.AccReceivedR)route).id;
                if (method.equals("GET")) return getReceivedR(userName, mid, henv);
                else if (method.equals("DELETE")) return deleteReceivedR(userName, mid, henv);
                else return henv.badMethod();
            } else if (route instanceof ApiRoute.AccSentR) {
                String userName = ((ApiRoute.AccSentR)route).userName;
                if (method.equals("POST")) return postSentR(userName, henv);
                else return henv.badMethod();
            } else {
                throw new RuntimeException("Case exception: " + route);
            }
        }
        };
    }

    public static TypedContent getUserR(HandlerData<MsgApp> henv) throws Exception {
            final Maybe<Entity<User>> mbUser = MsgHandler.getSessionUser(henv);
            if (mbUser.isNothing()) {
                return henv.notAuthenticated();
            } else {
                Entity<User> user = mbUser.fromJust();
                JSONValue resp = JSONValue.object(
                    Pair.Pair( "id",   JSONValue.JNumber(user.id) ),
                    Pair.Pair( "name", JSONValue.JString(user.value.name) )
                );
                return JSONValue.ToTypedContent_JSONValue.toTypedContent(resp);
            }
    }

    public static TypedContent getAccountR(String userName, HandlerData<MsgApp> henv) throws Exception {
            final Maybe<Entity<User>> mbUser = MsgHandler.getSessionUser(henv);
            if (mbUser.isNothing()) {
                return henv.notAuthenticated();
            } else {
                Entity<User> user = mbUser.fromJust();
                if (! userName.equals(user.value.name)) {
                    return henv.permissionDenied(userName);
                } else {
                    DAOFactory daoFact = henv.getApp().daoFact;
                    List<Entity<Message>> received = daoFact.getMessageDAO().selectByTo(user.id);
                    ArrayList<JSONValue> jms = new ArrayList<JSONValue>();
                    for (Entity<Message> m : received) {
                        Maybe<User> mbFrom = daoFact.getUserDAO().get(m.value.fromId);
                        String fromName;
                        if (mbFrom.isJust()) { fromName = mbFrom.fromJust().name; }
                        else { fromName = "???"; }
                        jms.add(
                            JSONValue.object(
                                Pair.Pair( "id",   JSONValue.JNumber(m.id) ),
                                Pair.Pair( "from", JSONValue.JString(fromName) ),
                                Pair.Pair( "date", JSONValue.JNumber(m.value.date.getTime()) ),
                                Pair.Pair( "subject", JSONValue.JString(m.value.subject) )
                            )
                        );
                    }
                    JSONValue resp = JSONValue.object(
                        Pair.Pair( "name", JSONValue.JString(user.value.name) ),
                        Pair.Pair( "received", JSONValue.JArray(jms) )
                    );
                    return JSONValue.ToTypedContent_JSONValue.toTypedContent(resp);
                }
            }
    }

    public static TypedContent getReceivedR(String userName, int mid, HandlerData<MsgApp> henv) throws Exception {
            final Maybe<Entity<User>> mbUser = MsgHandler.getSessionUser(henv);
            if (mbUser.isNothing()) {
                return henv.notAuthenticated();
            } else {
                Entity<User> user = mbUser.fromJust();
                if (! userName.equals(user.value.name)) {
                    return henv.permissionDenied(userName);
                } else {
                    DAOFactory daoFact = henv.getApp().daoFact;
                    Maybe<Message> mbMsg = daoFact.getMessageDAO().get(mid);
                    if (mbMsg.isJust() && mbMsg.fromJust().toId == user.id) {
                        Message msg = mbMsg.fromJust();
                        Maybe<User> mbFrom = daoFact.getUserDAO().get(msg.fromId);
                        String fromName;
                        if (mbFrom.isJust()) { fromName = mbFrom.fromJust().name; }
                        else { fromName = "???"; }
                        JSONValue resp = JSONValue.object(
                                Pair.Pair( "id",   JSONValue.JNumber(mid) ),
                                Pair.Pair( "from", JSONValue.JString(fromName) ),
                                Pair.Pair( "to",   JSONValue.JString(user.value.name) ),
                                Pair.Pair( "date", JSONValue.JNumber(msg.date.getTime()) ),
                                Pair.Pair( "subject", JSONValue.JString(msg.subject) ),
                                Pair.Pair( "content", JSONValue.JString(msg.text) )
                        );
                        return JSONValue.ToTypedContent_JSONValue.toTypedContent(resp);
                    } else {
                        return henv.notFound();
                    }
                }
            }
    }

    public static TypedContent deleteReceivedR(String userName, int mid, HandlerData<MsgApp> henv) throws Exception {
            final Maybe<Entity<User>> mbUser = MsgHandler.getSessionUser(henv);
            if (mbUser.isNothing()) {
                return henv.notAuthenticated();
            } else {
                Entity<User> user = mbUser.fromJust();
                if (! userName.equals(user.value.name)) {
                    return henv.permissionDenied(userName);
                } else {
                    DAOFactory daoFact = henv.getApp().daoFact;
                    Maybe<Message> mbMsg = daoFact.getMessageDAO().get(mid);
                    if (mbMsg.isJust() && mbMsg.fromJust().toId == user.id) {
                        daoFact.getMessageDAO().delete(mid);
                    }
                    JSONValue resp = JSONValue.object( IList.<Pair<String,JSONValue>>Nil() );
                    return JSONValue.ToTypedContent_JSONValue.toTypedContent(resp);
                }
            }
    }

    public static TypedContent postSentR(String userName, HandlerData<MsgApp> henv) throws Exception {
        final Maybe<Entity<User>> mbUser = MsgHandler.getSessionUser(henv);
        if (mbUser.isNothing()) {
            return henv.notAuthenticated();
        } else {
            Entity<User> user = mbUser.fromJust();
            if (! userName.equals(user.value.name)) {
                return henv.permissionDenied(userName);
            } else {
                Maybe<String> mbTo = henv.lookupParam("to");
                Maybe<String> mbSubject = henv.lookupParam("subject");
                Maybe<String> mbText = henv.lookupParam("message");
                if (mbTo.isNothing() || mbSubject.isNothing() || mbText.isNothing()) {
                    return henv.invalidArgs(IList.mkIList("to", "subject", "message"));
                } else {
                    Maybe<Entity<User>> mbToEnt = henv.getApp().daoFact.getUserDAO().selectByName(mbTo.fromJust());
                    if (mbToEnt.isNothing()) {
                        return henv.invalidArgs(IList.mkIList("to"));
                    } else {
                        Message msg = new Message(user.id, mbToEnt.fromJust().id, new Date(), mbSubject.fromJust(), mbText.fromJust());
                        int mid = henv.getApp().daoFact.getMessageDAO().insert(msg);
                        JSONValue resp = JSONValue.object(
                                Pair.Pair( "id",   JSONValue.JNumber(mid) )
                        );
                        return JSONValue.ToTypedContent_JSONValue.toTypedContent(resp);
                    }
                }
            }
        }
    }

}


