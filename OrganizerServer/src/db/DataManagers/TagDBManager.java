/**
 * TagDBManager és el gestor que s'encarrega de fer totes les modificacions relacionades amb les etiquetes.
 *
 * @author  Albert Ferrando
 * @version 1.0
 */
package db.DataManagers;

import com.mysql.jdbc.Statement;
import db.DataBaseManager;
import model.project.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TagDBManager {
    private Statement s;

    /**
     * Aquesta funció s'encarrega de retornar totes les etiquetes de la tasca indicada.
     *
     * @param id_tasca Id de la tasca de la qual volem recuperar les etiquetes.
     */
    public ArrayList<Tag> getTags(String id_tasca) {
        ArrayList<Tag> tags = new ArrayList<>();
        ResultSet rs;

        try {
            s =(Statement) DataBaseManager.getInstance().getConnection().createStatement();
            rs = s.executeQuery ("SELECT * FROM Etiqueta WHERE id_tasca = '" + id_tasca + "';");
            while(rs.next()) {
                if (rs.getString("id_etiqueta") != null) {
                    tags.add(new Tag(rs.getString("id_etiqueta"), rs.getString("nom_etiqueta"),
                            rs.getString("color")));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return tags;
    }

    /**
     * Aquesta funció s'encarrega d'afegir una etiqueta a la tasca indicada.
     *
     * @param tag Etiqueta que volem afegir a la tasca en qüestió.
     * @param id_tasca Id de la tasca a la qual volem afegir la etiqueta.
     */
    public void addTag(Tag tag, String id_tasca) {
        String query = "{CALL Organizer.AddTag(?,?,?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = DataBaseManager.getInstance().getConnection().prepareCall(query);
            stmt.setString(1, id_tasca);
            stmt.setString(2, tag.getId());
            stmt.setString(3, tag.getName());
            stmt.setString(4, tag.getHexColor());
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Problema al Recuperar les dades --> " + e.getSQLState());
        }
    }

    /**
     * Aquesta funció s'encarrega d'eliminar una etiqueta concreta.
     *
     * @param id_etiqueta Id de la etiqueta que volem eliminar.
     */
    public void deleteTag(String id_etiqueta) {
        try {
            s =(Statement) DataBaseManager.getInstance().getConnection().createStatement();
            s.executeUpdate ("DELETE FROM Etiqueta WHERE id_etiqueta = '" + id_etiqueta + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    /**
     * Retorna l'id de la categoria del tag que li passem.
     */
    public String getCategoryId(Tag tag) {
        ResultSet rs = null;
        try {
            s = (Statement) DataBaseManager.getInstance().getConnection().createStatement();
            rs = s.executeQuery("SELECT * FROM Columna JOIN Tasca USING (id_columna) JOIN Etiqueta USING (id_tasca)" +
                    " WHERE id_etiqueta = '" + tag.getId() + "';");
            rs.next();
            return rs.getString("id_columna");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
        return "";
    }

}
