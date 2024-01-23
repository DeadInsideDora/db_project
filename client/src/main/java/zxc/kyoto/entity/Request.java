package zxc.kyoto.entity;

import java.io.Serializable;

public class Request implements Serializable {
    public final User user;
    public final String[] args;

    public Request(User user, String[] args) {
        this.user = user;
        this.args = args;
    }
}
