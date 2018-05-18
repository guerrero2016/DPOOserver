/**
 * MemberDBManager és el gestor que s'encarrega de fer totes les modificacions relacionades amb els membres.
 *
 * @author  Albert Ferrando
 * @version 1.0
 */
package db.DataManagers;

import com.mysql.jdbc.Statement;
import db.DataBaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MemberDBManager {
    private Statement s;

    /**
     * Aquesta funció s'encarrega de retornar tots els membres que estan en un mateix projecte.
     *
     * @param id_projecte Id del projecte del qual es volen recuperar els membres.
     */
    public ArrayList<String> getMembers(String id_projecte) {
        ArrayList<String> members = new ArrayList<>();
        ResultSet rs;

        try {
            s =(Statement) DataBaseManager.getInstance().getConnection().createStatement();
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

    /**
     * Aquesta funció s'encarrega de afegir un membre a un projecte. En cas que el membre ja formi part del projecte
     * no modificarà res.
     *
     * @param id_projecte Id del projecte al qual volem afegir el membre.
     * @param username Nom d'usuari del membre que volem afegir.
     */
    public void addMember(String id_projecte, String username) {
        String query = "{CALL Organizer.addMember(?,?)}";
        java.sql.CallableStatement stmt;
        try {
            stmt = DataBaseManager.getInstance().getConnection().prepareCall(query);
            stmt.setString(1, id_projecte);
            stmt.setString(2, username);
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aquesta funció s'encarrega d'esborrar un membre del projecte que li diguem.
     *
     * @param id_projecte Id del projecte del qual volem eliminar el membre.
     * @param username Nom d'usuari del membre que volem eliminar.
     */
    public void deleteMember(String id_projecte, String username) {
        try {
            s = (Statement) DataBaseManager.getInstance().getConnection().createStatement();
            s.executeUpdate ("DELETE FROM Membre WHERE id_projecte = '" + id_projecte + "'AND nom_usuari = '"
                    + username + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }
}
