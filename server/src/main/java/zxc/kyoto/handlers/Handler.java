package zxc.kyoto.handlers;

import zxc.kyoto.entity.User;

public interface Handler {

    String handle(User user, Object[] args);

}
