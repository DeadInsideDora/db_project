package com.example.vt_labs_1.handlers;

import com.example.vt_labs_1.entities.User;

public interface Handler {

    boolean handle(User user, Object[] args);

}
