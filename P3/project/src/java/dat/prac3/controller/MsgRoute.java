
package dat.prac3.controller;

import dat.fw.web.app.Route;

import dat.fw.util.Pair;
import static dat.fw.util.Pair.*;
import dat.fw.util.IList;
import static dat.fw.util.IList.*;
import dat.fw.util.Maybe;
import static dat.fw.util.Maybe.*;

import dat.prac3.api.ApiRoute;


/**
 * @author Jordi Forga
 */
public abstract class MsgRoute extends Route<MsgApp>
{

    public static final MsgRoute HomeR = new MsgRoute() {
        public Pair<IList<String>,IList<Pair<String,String>>> render() { return Pair(IList.Nil(String.class), emptyParams); }
    };

    public static final MsgRoute InboxR = new MsgRoute() {
        public Pair<IList<String>,IList<Pair<String,String>>> render() { return Pair(mkIList("inbox"), emptyParams); }
    };

    public static final MsgRoute ComposeR = new MsgRoute() {
        public Pair<IList<String>,IList<Pair<String,String>>> render() { return Pair(mkIList("compose"), emptyParams); }
    };

    public static class MessageR extends MsgRoute {
        public final int id;
        public MessageR(int id) { this.id = id; }
        public Pair<IList<String>,IList<Pair<String,String>>> render() { return Pair(mkIList("message", Integer.toString(id)), emptyParams); }
    }
    public static MsgRoute MessageR(int id) { return new MessageR(id); }

    public static final MsgRoute LoginR = new MsgRoute() {
        public Pair<IList<String>,IList<Pair<String,String>>> render() { return Pair(mkIList("login"), emptyParams); }
    };

    public static final MsgRoute LogoutR = new MsgRoute() {
        public Pair<IList<String>,IList<Pair<String,String>>> render() { return Pair(mkIList("logout"), emptyParams); }
    };

    public static class StaticR extends MsgRoute {
        public final IList<String> path;
        public StaticR(IList<String> path) { this.path = path; }
        public Pair<IList<String>,IList<Pair<String,String>>> render() { return Pair(Cons("recursos", path), emptyParams); }
    }
    public static MsgRoute StaticR(IList<String> path) { return new StaticR(path); }
    public static MsgRoute StaticR(String... path) { return new StaticR(mkIList(path)); }

    public static class ApiR extends MsgRoute {
        public final ApiRoute apiR;
        public ApiR(ApiRoute apiR) { this.apiR = apiR; }
        public Pair<IList<String>,IList<Pair<String,String>>> render() {
            Pair<IList<String>,IList<Pair<String,String>>> r = apiR.render();
            return Pair(Cons("api", r.c1), r.c2);
        }
    }
    public static MsgRoute ApiR(ApiRoute apiR) { return new ApiR(apiR); }


    public static Maybe<MsgRoute> parseRoute(IList<String> segments, IList<Pair<String,String>> params)
    {
        if (segments.isNil()) {
            return Just(HomeR);
        } else {
            String s1 = segments.head();
            IList<String> t1 = segments.tail();
            if (s1.equals("login") && t1.isNil()) {
                return Just(LoginR);
            }
            if (s1.equals("logout") && t1.isNil()) {
                return Just(LogoutR);
            }
            if (s1.equals("recursos") && ! t1.isNil()) {
                return Just(StaticR(t1));
            }
            if (s1.equals("inbox") && t1.isNil()) {
                return Just(InboxR);
            }
            if (s1.equals("compose") && t1.isNil()) {
                return Just(ComposeR);
            }
            if (s1.equals("message") && ! t1.isNil()) {
                String s2 = t1.head();
                IList<String> t2 = t1.tail();
                if (t2.isNil()) {
                    return Just(MessageR(Integer.parseInt(s2)));
                }
            }
            if (s1.equals("api")) {
                Maybe<ApiRoute> apiR = ApiRoute.parseRoute(t1, params);
                if (apiR.isJust()) {
                    return Just(ApiR(apiR.fromJust()));
                } else {
                    return Nothing();
                }
            }
            return Nothing();
        }
    }

}


