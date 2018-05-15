/**
 * UserDBManager és el gestor que s'encarrega de fer totes les modificacions relacionades amb els usuaris.
 *
 * @author  Albert Ferrando
 * @version 1.0
 */
package db.DataManagers;

import com.mysql.jdbc.Statement;
import db.DataBaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDBManager {
    private static Statement s;

    /**
     * Aquesta funció s'encarrega de registrar un usuari en cas que les dades de registre siguin correctes.
     * Si el nom d'usuari ja existeix a la base de dades retornarà un 1, si l'adreça de correu electrònic ja existeix
     * a la base de dades retornarà un 2 i si el registre s'ha completat correctament retornarà un 0.
     *
     * @param nom_usuari Nom d'usuari a registrar.
     * @param correu Correu electrònic a registrar.
     * @param contrasenya Contrasenya a registrar.
     */
    public int registrarUsuari(String nom_usuari, String correu, String contrasenya) {
        ResultSet rs;

        try {
            String query = "{CALL Organizer.registrarUsuari(?,?,?)}";
            java.sql.CallableStatement stmt = DataBaseManager.getConnection().prepareCall(query);
            stmt.setString(1, nom_usuari);
            stmt.setString(2, correu);
            stmt.setString(3, contrasenya);
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getString(1).equals("NO1")) {
                return 1;
            } else if(rs.getString(1).equals("NO2")) {
                return 2;
            } else  {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * Aquesta funció s'encarrega de retornar el nom d'usuari d'un usuari indistintament de si li passem el correu
     * electrònic o el nom d'usuari del mateix. Retorna "fail" si el nom d'usuari o correu no es troba registrat.
     *
     * @param nom_or_correu Nom d'usuari o correu electrònic de l'usuari del qual volem recuperar el seu nom d'usuari.
     */
    public String getUsername(String nom_or_correu) {
        ResultSet rs;

        try {
            s =(Statement) DataBaseManager.getConnection().createStatement();
            rs = s.executeQuery ("SELECT nom_usuari FROM Usuari " +
                    "WHERE correu = '" + nom_or_correu + "' OR nom_usuari = '" + nom_or_correu + "';");
            while(rs.next()) {
                if(rs.getString("nom_usuari") != null) {
                    return rs.getString("nom_usuari");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return "Fail";
    }

    /**
     * Aquesta funció s'encarrega de mirar si les credencials d'un usuari per entrar al programa son correctes o no.
     * En cas que les credencials siguin correctes retornarà un 1 i en cas contrari un 0.
     *
     * @param nom_correu Nom o correu de l'usuari que vol iniciar sessió.
     * @param password Contrasenya de l'usuari que vol iniciar sessió.
     */
    public int iniciarSessio(String nom_correu, String password) {
        ResultSet rs;

        try {
            String query = "{CALL Organizer.iniciarSessio(?,?)}";
            java.sql.CallableStatement stmt = DataBaseManager.getConnection().prepareCall(query);
            stmt.setString(1, nom_correu);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getString(1).equals("NO")) {
                return 0;
            } else {
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
