package zxc.kyoto.server;

import zxc.kyoto.entity.Request;
import zxc.kyoto.handlers.AuthorizationHandler;
import zxc.kyoto.handlers.CandidateCreateHandler;
import zxc.kyoto.handlers.Handler;
import zxc.kyoto.handlers.RegisterHandler;

import java.util.Hashtable;

public final class Module {

    private static Hashtable<String, Handler> handlers;

    public static void init() {
        handlers.put("auth", new AuthorizationHandler());
        handlers.put("reg", new RegisterHandler());
        handlers.put("candidate_add", new CandidateCreateHandler());
    }

    public static String handle(Request request) {
        try {
            Handler handler = handlers.get(request.args[0]);
            if (handler == null) throw new IllegalArgumentException("Incorrect request");
            return handler.handle(request.user, request.args);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
