/**
 * MemberInChargeDBManager és el gestor que s'encarrega de fer totes les modificacions relacionades amb
 * els usuaris relacionats a les tasques.
 *
 * @author  Albert Ferrando
 * @version 1.0
 */
package db.DataManagers;

import com.mysql.jdbc.Statement;
import db.DataBaseManager;
import model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MemberInChargeDBManager {
    private Statement s;

    /**
     * Aquesta funció s'encarrega d'afegir un usuari a una tasca.
     *
     * @param user L'usuari en si que volem afegir.
     * @param id_tasca Id de la tasca a la qual volem afegir aquest usuari.
     */
    public void addMemberInCharge(User user, String id_tasca) {
        String query = "{CALL Organizer.AddEncarregat(?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = DataBaseManager.getInstance().getConnection().prepareCall(query);
            stmt.setString(1, user.getUserName());
            stmt.setString(2, id_tasca);
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Problema al Recuperar les dades --> " + e.getSQLState());
        }
    }

    /**
     * Aquesta funció s'encarrega d'esborrar un usuari de la tasca que li diguem.
     *
     * @param nom_usuari Nom d'usuari de l'usuari que volem eliminar.
     * @param id_tasca Id de la tasca de la qual volem eliminar l'usuari.
     */
    public void deleteMemberInCharge(String nom_usuari, String id_tasca) {
        try {
            s =(Statement) DataBaseManager.getInstance().getConnection().createStatement();
            s.executeUpdate ("DELETE FROM Tasca_Usuari WHERE nom_usuari = '" + nom_usuari +
                    "' AND id_tasca = '" + id_tasca + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    /**
     * Aquesta funció s'encarrega de retornar tots els usuaris d'una tasca.
     *
     * @param id_tasca Id de la tasca de la qual volem recuperar els usuaris.
     */
    public ArrayList<User> getMembersInCharge(String id_tasca) {
        ArrayList<User> membersInCharge = new ArrayList<>();
        ResultSet rs;
        try {
            s =(Statement) DataBaseManager.getInstance().getConnection().createStatement();
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