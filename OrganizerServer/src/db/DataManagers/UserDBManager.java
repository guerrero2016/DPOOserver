package db.DataManagers;

import com.mysql.jdbc.Statement;
import db.DataBaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDBManager {
    private static Statement s;

    //Funció validada
    public int RegistrarUsuari(String nom_usuari, String correu, String contrasenya) {
        ResultSet rs;

        try {
            String query = "{CALL Organizer.RegistrarUsuari(?,?,?)}";
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

    //Funció validada
    public boolean IniciarSessio(String nom_correu, String password) {
        ResultSet rs;

        try {
            String query = "{CALL Organizer.IniciarSessio(?,?)}";
            java.sql.CallableStatement stmt = DataBaseManager.getConnection().prepareCall(query);
            stmt.setString(1, nom_correu);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getString(1).equals("NO")) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Funció validada
    public String getUser(String userName) {
        ResultSet rs = null;
        String query = "{CALL Organizer.GetUser(?)}";
        try {
            java.sql.CallableStatement stmt = DataBaseManager.getConnection().prepareCall(query);
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            System.out.println("Problema al Recuperar les dades --> " + e.getSQLState());
        }
        return "";
    }
}
