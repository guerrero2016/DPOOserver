package model.user;

import java.util.Objects;

public class User {
    protected String userName;

    public User() {}

    public User(String userName) {
        if(userName != null) {
            this.userName = userName;
        }
    }

    public String getName() {
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
