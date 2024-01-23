package com.example.vt_labs_1.handlers;

import com.example.vt_labs_1.dao.DataBaseService;
import com.example.vt_labs_1.dao.UsersService;
import com.example.vt_labs_1.entities.User;

import java.sql.SQLException;

public class CandidateCreateHandler implements Handler{

    @Override
    public boolean handle(User user, Object[] args) {
        String firstName = (String) args[0];
        String lastName = (String) args[1];
        String[] facts = (String[]) args[2];
        String[] factsWeights = (String[]) args[3];
        if (UsersService.isUserExist(user.getUsername())) {
            try {
                DataBaseService.addСandidate(firstName, lastName, facts, factsWeights);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

}
