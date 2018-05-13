package db.DataManagers;

import com.mysql.jdbc.Statement;
import db.DataBaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MemberDBManager {
    private Statement s;

    public ArrayList<String> getMembers(String id_projecte) {
        ArrayList<String> members = new ArrayList<>();
        ResultSet rs;

        try {
            s =(Statement) DataBaseManager.getConnection().createStatement();
            rs = s.executeQuery ("SELECT * FROM Membre WHERE id_projecte = '" + id_projecte + "';");
            while(rs.next()) {
                if(rs.getString("nom_usuari") != null) {
                    members.add(rs.getString("nom_usuari"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return members;
    }

    //Funció validada
    public void addMember(String id_projecte, String username) {
        String query = "{CALL Organizer.addMember(?,?)}";
        java.sql.CallableStatement stmt;
        try {
            stmt = DataBaseManager.getConnection().prepareCall(query);
            stmt.setString(1, id_projecte);
            stmt.setString(2, username);
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMember(String id_projecte, String username) {
        try {
            s = (Statement) DataBaseManager.getConnection().createStatement();
            s.executeUpdate ("DELETE FROM Membre WHERE id_projecte = '" + id_projecte + "'AND nom_usuari = '"
                    + username + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }
}
