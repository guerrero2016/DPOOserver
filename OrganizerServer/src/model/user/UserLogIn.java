package model.user;

import java.io.Serializable;

public class UserLogIn implements Serializable{
        private String userName;
        private String password;

    public boolean checkLogIn() {
        //TODO Comprovar a la base de dades
        return true;
    }
}
