package model;

import com.mysql.jdbc.CallableStatement;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseManager {
    private static DataBaseManager ourInstance = new DataBaseManager();
    private static Connection connection;
    private static Statement s;

    public static DataBaseManager getInstance() {
        return ourInstance;
    }

    private DataBaseManager() {
    }

    public static void setConnection(Connection connection) {
        DataBaseManager.connection = connection;
        try {
            s = (Statement) connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Problema al Inserir --> " + e.getSQLState());
        }
    }

    //Funció validada.
    public static int RegistrarUsuari(String nom_usuari, String correu, String contrasenya) {
            ResultSet rs;
            try {
                String query = "{CALL Organizer.RegistrarUsuari(?,?,?)}";
                java.sql.CallableStatement stmt = connection.prepareCall(query);
                stmt.setNString(1, nom_usuari);
                stmt.setNString(2, correu);
                stmt.setNString(3, contrasenya);
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
    public static boolean IniciarSessio(String nom_correu, String password) {
        ResultSet rs;
        try {
            String query = "{CALL Organizer.IniciarSessio(?,?)}";
            java.sql.CallableStatement stmt = connection.prepareCall(query);
            stmt.setNString(1, nom_correu);
            stmt.setNString(2, password);
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
}