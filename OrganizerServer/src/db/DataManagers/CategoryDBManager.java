package db.DataManagers;

import com.mysql.jdbc.Statement;
import db.DataBaseManager;
import model.project.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryDBManager {
    private Statement s;

    /** documentation */
    public  ArrayList<Category> getCategories(String id_projecte) {
        ArrayList<Category> categories = new ArrayList<>();
        ResultSet rs;
        try {
            s =(Statement) DataBaseManager.getConnection().createStatement();
            rs = s.executeQuery ("SELECT * FROM Columna WHERE id_projecte = '" + id_projecte + "';");
            while(rs.next()) {
                if (rs.getString("id_columna") != null) {
                    categories.add(new Category(rs.getString("id_columna"),
                            rs.getString("nom_columna"), rs.getInt("posicio"),
                            DataBaseManager.getTaskDBManager().getTasks(rs.getString("id_columna"))));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return categories;
    }

    //Funció validada
    public void addCategory(Category c, String id_projecte) {
        String query = "{CALL Organizer.AddCategory(?,?,?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = DataBaseManager.getConnection().prepareCall(query);
            stmt.setString(1, id_projecte);
            stmt.setString(2, c.getId());
            stmt.setString(3, c.getName());
            stmt.setInt(4, c.getOrder());
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Funció validada
    public void deleteCategory(String id_columna) {
        String query = "{CALL Organizer.deleteCategory(?)}";
        java.sql.CallableStatement stmt;
        try {
            stmt = DataBaseManager.getConnection().prepareCall(query);
            stmt.setString(1, id_columna);
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Funció validada
    public void swapCategory(String id_projecte, Category category1, Category category2) {
        if(Math.abs(category1.getOrder() - category2.getOrder()) == 1) {
            String query = "{CALL Organizer.SwapCategory(?,?,?,?,?)}";
            java.sql.CallableStatement stmt;
            try {
                stmt = DataBaseManager.getConnection().prepareCall(query);
                stmt.setString(1, id_projecte);
                stmt.setString(2, category1.getId());
                stmt.setString(3, category2.getId());
                stmt.setInt(4, category1.getOrder());
                stmt.setInt(5, category2.getOrder());
                stmt.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Per fer swap han de ser columnes col·lindants.");
        }
    }
}
