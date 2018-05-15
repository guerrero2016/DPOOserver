package db.DataManagers;

import com.mysql.jdbc.Statement;
import db.DataBaseManager;
import model.project.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MemberInChargeDBManager {
    private Statement s;

    //Funció validada
    public void addMemberInCharge(User user, String id_tasca) {
        String query = "{CALL Organizer.AddEncarregat(?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = DataBaseManager.getConnection().prepareCall(query);
            stmt.setString(1, user.getName());
            stmt.setString(2, id_tasca);
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Problema al Recuperar les dades --> " + e.getSQLState());
        }
    }

    //Funció validada
    public void deleteMemberInCharge(String nom_usuari, String id_tasca) {
        try {
            s =(Statement) DataBaseManager.getConnection().createStatement();
            s.executeUpdate ("DELETE FROM Tasca_Usuari WHERE nom_usuari = '" + nom_usuari +
                    "' AND id_tasca = '" + id_tasca + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    //Funció validada
    public ArrayList<User> getMembersInCharge(String id_tasca) {
        ArrayList<User> membersInCharge = new ArrayList<>();
        ResultSet rs;
        try {
            s =(Statement) DataBaseManager.getConnection().createStatement();
            rs = s.executeQuery ("SELECT * FROM Tasca_Usuari WHERE id_tasca = '" + id_tasca + "';");
            while(rs.next()) {
                if (rs.getString("id_tasca") != null) {
                    membersInCharge.add(new User(rs.getString("nom_usuari")));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return membersInCharge;
    }
}