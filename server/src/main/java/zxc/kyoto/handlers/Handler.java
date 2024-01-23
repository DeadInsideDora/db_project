package zxc.kyoto.handlers;

import zxc.kyoto.entity.User;

public interface Handler {

    boolean handle(User user, Object[] args);

}
