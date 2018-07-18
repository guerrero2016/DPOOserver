package model.user;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * Objecte que defineix com es un usuari
 */
public class User implements Serializable {
    public final static int serialVersionUID = 1238;

    protected String userName;

    /**
     * Crea un <code>User</code> buit
     */
    public User() {}

    /**
     * Crea un <code>User</code> i l'inicialitza amb el nom d'usuari especificat
     * @param userName Nom d'usuari
     */
    public User(String userName) {
        if(userName != null) {
            this.userName = userName;
        }
    }

    /**
     * Recuperes el nom d'usuari
     * @return Nom d'usuari
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Encripta un <code>String</code>.
     * @param entry <code>String</code> a encriptar
     * @return <code>String</code> d'entrada encriptat
     */
    public static String getMD5(String entry) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(entry.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Comprova si un <code>String</code> te alguna majuscula
     * @param entry <code>String</code> a comprovar
     * @return <code>true</code> si en te alguna, <code>false</code> si no en te
     */
    public static boolean containsUpperCase(String entry) {
        for (char c : entry.toCharArray()) {
            if (Character.isUpperCase(c)) return true;
        }
        return false;
    }

    /**
     * Equals
     * @param o Objecte
     * @return Si equival
     */
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

    /**
     * Hashcode
     * @return Hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }

}