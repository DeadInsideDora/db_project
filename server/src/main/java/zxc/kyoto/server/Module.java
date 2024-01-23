package zxc.kyoto.server;

import zxc.kyoto.entity.Request;
import zxc.kyoto.handlers.AuthorizationHandler;

public class Module {
    public String handle(Request request) {
        if (request.args[0].equals("auth")) {
            if (new AuthorizationHandler().handle(request.user, request.args)) {
                return "true";
            } else {
                return "false";
            }
        }
        return "false";
    }
}
