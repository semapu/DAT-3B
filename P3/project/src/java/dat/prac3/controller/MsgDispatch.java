
package dat.prac3.controller;

import dat.fw.web.dispatch.Dispatch;
import dat.fw.web.dispatch.Request;
import dat.fw.web.dispatch.Response;
import dat.fw.web.handler.HandlerData;
import dat.fw.web.content.TypedContent;
import dat.fw.web.servlet.ServletResponse;

import dat.fw.util.IList;
import dat.fw.util.Maybe;
import dat.fw.util.Fun1E;

import static dat.prac3.controller.MsgRoute.*;
import static dat.prac3.controller.MsgHandler.*;
import dat.prac3.api.ApiRoute;
import dat.prac3.api.MsgApi;


/**
 * @author Jordi Forga
 */
public class MsgDispatch extends Dispatch<MsgApp>
{

    public MsgDispatch() { super(MsgApp.WebApp); }

    @Override
    public Response dispatch(Dispatch.Env<MsgApp> dispEnv, Request request) throws Exception
    {
        String method = request.method();
        IList<String> segments = request.pathInfo();
        Maybe<MsgRoute> mbRoute = parseRoute(segments, request.queryString());
        Fun1E<HandlerData<MsgApp>,TypedContent> handler;
        if (mbRoute.isJust()) {
            MsgRoute route = mbRoute.fromJust();
            if (route == HomeR) {
                if (method.equals("GET")) handler = getHomeR;
                else handler = badMethodHandler;
            } else if (route == InboxR) {
                if (method.equals("GET")) handler = getInboxR;
                else if (method.equals("POST")) handler = postInboxR;
                else handler = badMethodHandler;
            } else if (route == ComposeR) {
                if (method.equals("GET")) handler = getComposeR;
                else if (method.equals("POST")) handler = postComposeR;
                else handler = badMethodHandler;
            } else if (route instanceof MessageR) {
                int mid = ((MessageR)route).id;
                if (method.equals("GET")) handler = getMessageR(mid);
                else if (method.equals("POST")) handler = postMessageR(mid);
                else handler = badMethodHandler;
            } else if (LoginR == route) {
                if (method.equals("POST")) handler = postLoginR;
                else handler = badMethodHandler;
            } else if (LogoutR == route) {
                if (method.equals("POST")) handler = postLogoutR;
                else handler = badMethodHandler;
            } else if (route instanceof ApiR) {
                ApiRoute apiR = ((ApiR)route).apiR;
                handler = MsgApi.handleApiR(apiR, method);
            } else {
                throw new RuntimeException("Case exception: " + route);
            }
        } else {
            handler = notFoundHandler;
        }
        return dispatchHandler(dispEnv, handler, request);
    }

}

