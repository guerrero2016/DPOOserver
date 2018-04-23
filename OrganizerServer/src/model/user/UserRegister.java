package model.user;

import java.io.Serializable;

public class UserRegister implements Serializable{
    private final int MIN_LENGTH = 8;

    private String userName;
    private String email;
    private String password;
    private String confirm;

    public boolean checkSignIn() throws Exception{

        if (userName == null){
            return false;
        }
        for(int i = 0; i < userName.length(); i++) {
            if(!Character.isLetterOrDigit(userName.charAt(i)) && !(userName.charAt(i) == '_')) {
                return false;
            }
        }
        //TODO Comprovar a la base de dades

        if (email == null) {
            return false;
        }for(int i = 0; i < email.length(); i++) {
            if(!Character.isLetterOrDigit(email.charAt(i)) && !(email.charAt(i) == '@') &&
                    !(email.charAt(i) == '_') && !(email.charAt(i) == '.')) {
                return false;
            }
        }
        boolean arroba = false;
        boolean dot = false;
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') {
                arroba = true;
            }
            if (email.charAt(i) == '.') {
                dot = true;
            }
        }
        if (!arroba || !dot) {
            return false;
        }

        //TODO Comprovar a la bbdd

        if (!password.equals(confirm)) {
            return false;
        }

        if (password.length() < MIN_LENGTH) {
            return false;
        }

        boolean majus = false;
        boolean minus = false;
        boolean num = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(password.charAt(i))) {
                minus = true;
            }
            if (Character.isUpperCase(password.charAt(i))) {
                majus = true;
            }
            if (Character.isDigit(password.charAt(i))) {
                num = true;
            }
        }

        if (!minus || !majus || !num) {
            return false;
        }

        return true;
    }
}
