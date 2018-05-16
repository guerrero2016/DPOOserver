package model.user;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    protected String userName;

    public User() {}

    public User(String userName) {
        if(userName != null) {
            this.userName = userName;
        }
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {

        if(this == o) {
            return true;
        }

        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return Objects.equals(userName, user.userName);

    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }

}