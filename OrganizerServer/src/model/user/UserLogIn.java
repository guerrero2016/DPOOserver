package model.user;

import db.DataBaseManager;

import java.io.Serializable;

public class UserLogIn implements Serializable{
        private String userName;
        private String password;

    public boolean checkLogIn() {
        return DataBaseManager.IniciarSessio(userName, password);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
