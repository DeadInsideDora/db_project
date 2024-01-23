package com.example.vt_labs_1.entities;

public class Request {
    private final User user;
    private final String[] args;

    public Request(User user, String[] args) {
        this.user = user;
        this.args = args;
    }
}
