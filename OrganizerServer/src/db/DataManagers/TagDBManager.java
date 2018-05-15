package db.DataManagers;

import com.mysql.jdbc.Statement;
import db.DataBaseManager;
import model.project.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TagDBManager {
    private Statement s;

    //Funció validada
    public ArrayList<Tag> getTags(String id_tasca) {
        ArrayList<Tag> tags = new ArrayList<>();
        ResultSet rs;

        try {
            s =(Statement) DataBaseManager.getConnection().createStatement();
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

    //Funció validada
    public void addTag(Tag tag, String id_tasca) {
        String query = "{CALL Organizer.AddTag(?,?,?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = DataBaseManager.getConnection().prepareCall(query);
            stmt.setString(1, id_tasca);
            stmt.setString(2, tag.getId());
            stmt.setString(3, tag.getName());
            stmt.setString(4, tag.getHexColor());
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Problema al Recuperar les dades --> " + e.getSQLState());
        }
    }

    //Funció validada
    public void deleteTag(String id_etiqueta) {
        try {
            s =(Statement) DataBaseManager.getConnection().createStatement();
            s.executeUpdate ("DELETE FROM Etiqueta WHERE id_etiqueta = '" + id_etiqueta + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }
}
