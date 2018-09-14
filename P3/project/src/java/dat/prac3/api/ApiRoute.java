
package dat.prac3.api;

import dat.fw.web.app.Route;

import dat.fw.util.Pair;
import static dat.fw.util.Pair.*;
import dat.fw.util.IList;
import static dat.fw.util.IList.*;
import dat.fw.util.Maybe;
import static dat.fw.util.Maybe.*;


/**
 * @author Jordi Forga
 */
public abstract class ApiRoute
{

    public abstract Pair<IList<String>,IList<Pair<String,String>>> render();


    private static final IList<Pair<String,String>> emptyParams = IList.Nil();

    public static final ApiRoute UserR = new ApiRoute() {
        public Pair<IList<String>,IList<Pair<String,String>>> render() { return Pair(mkIList("user"), emptyParams); }
    };

    public static class AccountR extends ApiRoute {
        public final String userName;
        public AccountR(String userName) {
            this.userName = userName;
        }
        public Pair<IList<String>,IList<Pair<String,String>>> render() {
            return Pair(mkIList("accounts", userName), emptyParams);
        }
    }
    public static ApiRoute AccountR(String userName) { return new AccountR(userName); }

    public static class AccReceivedR extends ApiRoute {
        public final String userName;
        public final int id;
        public AccReceivedR(String userName, int id) {
            this.userName = userName;
            this.id = id;
        }
        public Pair<IList<String>,IList<Pair<String,String>>> render() {
            return Pair(mkIList("accounts", userName, "received", Integer.toString(id)), emptyParams);
        }
    }
    public static ApiRoute AccReceivedR(String userName, int id) { return new AccReceivedR(userName, id); }

    public static class AccSentR extends ApiRoute {
        public final String userName;
        public AccSentR(String userName) {
            this.userName = userName;
        }
        public Pair<IList<String>,IList<Pair<String,String>>> render() {
            return Pair(mkIList("accounts", userName, "sent"), emptyParams);
        }
    }
    public static ApiRoute AccSentR(String userName) { return new AccSentR(userName); }


    public static Maybe<ApiRoute> parseRoute(IList<String> segments, IList<Pair<String,String>> params)
    {
        if (segments.isNil()) {
            return Nothing();
        } else {
            String s1 = segments.head();
            IList<String> t1 = segments.tail();
            if (s1.equals("user") && t1.isNil()) {
                return Just(UserR);
            }
            if (s1.equals("accounts") && ! t1.isNil()) {
                String s2 = t1.head();
                IList<String> t2 = t1.tail();
                if (t2.isNil()) {
                    return Just(AccountR(s2));
                } else {
                    String s3 = t2.head();
                    IList<String> t3 = t2.tail();
                    if (s3.equals("received") && ! t3.isNil()) {
                        String s4 = t3.head();
                        IList<String> t4 = t3.tail();
                        if (t4.isNil()) {
                            return Just(AccReceivedR(s2, Integer.parseInt(s4)));
                        }
                    } else if (s3.equals("sent") && t3.isNil()) {
                        return Just(AccSentR(s2));
                    }
                }
            }
            return Nothing();
        }
    }

}


