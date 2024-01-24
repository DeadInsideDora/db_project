package zxc.kyoto.entity;

import java.io.Serializable;

public class Request implements Serializable {
    public final User user;
    public final Object[] args;

    public Request(User user, Object[] args) {
        this.user = user;
        this.args = args;
    }
}
