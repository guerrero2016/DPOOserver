package db.DataManagers;

import com.mysql.jdbc.Statement;
import db.DataBaseManager;
import model.project.MemberInCharge;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MemberInChargeDBManager {
    private Statement s;

    //Funció validada
    ArrayList<MemberInCharge> getEncarregats(String id_projecte, String id_columna, String id_tasca) {
        ArrayList<MemberInCharge> encarregats = new ArrayList<>();
        ResultSet rs;
        try {
            s =(Statement) DataBaseManager.getConnection().createStatement();
            rs = s.executeQuery ("SELECT * FROM Etiqueta WHERE id_projecte = '" + id_projecte + "' AND id_columna = '" + id_columna +
                    "' AND id_tasca = '" + id_tasca + "';");
            while(rs.next()) {
                if (rs.getString("id_encarregat") != null) {
                    encarregats.add(new MemberInCharge(rs.getString("id_columna"), rs.getString("id_tasca"), rs.getString("id_encarregat"),
                            rs.getString("nom_encarregat"), rs.getString("color")));                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return encarregats;
    }

    //Funció validada
    public void addEncarregat(MemberInCharge e) {
        String query = "{CALL Organizer.AddEncarregat(?,?,?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = DataBaseManager.getConnection().prepareCall(query);
            stmt.setString(1, e.getId_task());
            stmt.setString(2, e.getId());
            stmt.setString(3, e.getName());
            stmt.setString(4, e.getColor());
            stmt.executeQuery();
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
    }

    //Funció validada
    public void deleteEncarregat(String id_encarregat) {
        try {
            s =(Statement) DataBaseManager.getConnection().createStatement();
            s.executeUpdate ("DELETE FROM Encarregat WHERE id_encarregat = '" + id_encarregat + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

}
