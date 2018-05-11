package db.DataManagers;

import com.mysql.jdbc.Statement;
import db.DataBaseManager;
import model.project.MemberInCharge;

import java.sql.SQLException;
import java.util.ArrayList;

public class MemberInChargeDBManager {
    private Statement s;

    public void addMemberInCharge(MemberInCharge memberInCharge, String id_tasca) {
        String query = "{CALL Organizer.AddEncarregat(?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = DataBaseManager.getConnection().prepareCall(query);
            stmt.setString(1, memberInCharge.getName());
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

    public ArrayList<MemberInCharge> getMembersInCharge(String id_projecte, String id_columna, String id_tasca) {
        ArrayList<MemberInCharge> membersInCharge = new ArrayList<>();
        return membersInCharge;
    }
}
