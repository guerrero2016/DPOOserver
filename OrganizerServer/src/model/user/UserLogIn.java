package model.user;

import db.DataBaseManager;

import java.io.Serializable;

public class UserLogIn implements Serializable{
    private String userName;
    private String password;

    public UserLogIn(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public boolean checkLogIn() {
        return DataBaseManager.getUserDBManager().iniciarSessio(userName, password);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
