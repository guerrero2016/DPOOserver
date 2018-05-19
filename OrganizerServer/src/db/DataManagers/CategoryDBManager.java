/**
 * CategoryDBManager és el gestor que s'encarrega de fer totes les modificacions relacionades amb les categories.
 *
 * @author  Albert Ferrando
 * @version 1.0
 */
package db.DataManagers;

import com.mysql.jdbc.Statement;
import db.DataBaseManager;
import model.project.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryDBManager {
    private Statement s;

    /**
     * Aquesta funció retorna totes les categories d'un projecte, amb totes les seves tasques i alhora les tasques
     * amb els seus respectius tags i usuaris.
     *
     * @param id_projecte Id del projecte del qual es vol recuperar les seves categories.
     */
    public  ArrayList<Category> getCategories(String id_projecte) {
        ArrayList<Category> categories = new ArrayList<>();
        ResultSet rs;
        try {
            s =(Statement) DataBaseManager.getInstance().getConnection().createStatement();
            rs = s.executeQuery ("SELECT * FROM Columna WHERE id_projecte = '" + id_projecte + "' ORDER BY posicio ASC;");
            while(rs.next()) {
                if (rs.getString("id_columna") != null) {
                    Category category = new Category(rs.getString("nom_columna"), rs.getInt("posicio"),
                            DataBaseManager.getInstance().getTaskDBManager().getTasks(rs.getString("id_columna")));
                    category.setId(rs.getString("id_columna"));
                    categories.add(category);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return categories;
    }

    /**
     * Aquesta funció serveix per afegir o editar una categoria. En cas que la categoria no existeixi crearà la
     * categoria i en cas que si que existeix editarà els camps propis de la categoria.
     *
     * @param c Columna que es vol afegir al projecte.
     * @param id_projecte Id del projecte del qual es vol afegir la categoria.
     */
    public void addCategory(Category c, String id_projecte) {
        String query = "{CALL Organizer.AddCategory(?,?,?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = DataBaseManager.getInstance().getConnection().prepareCall(query);
            stmt.setString(1, id_projecte);
            stmt.setString(2, c.getId());
            stmt.setString(3, c.getName());
            stmt.setInt(4, c.getOrder());
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aquesta funció s'encarrega d'eliminar una categoria, esborrant també totes les tasques, etiquetes i usuaris
     * que es trobin dins d'aquesta.
     *
     * @param id_projecte Projecte en el que es troba la categoria a eliminar
     * @param category Categoria que volem eliminar
     */
    public void deleteCategory(Category category, String id_projecte) {
        String query = "{CALL Organizer.deleteCategory(?,?,?)}";
        java.sql.CallableStatement stmt;
        try {
            stmt = DataBaseManager.getInstance().getConnection().prepareCall(query);
            stmt.setString(1, category.getId());
            stmt.setString(2, id_projecte);
            stmt.setInt(3, category.getOrder());
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aquesta funció s'encarrega de canviar l'ordre entre dos columnes col·lindants. Funciona independentment
     * de quin sigui l'ordre de les columnes proporcionades. No funciona en cas que les columnes no siguin col·lindants.
     *
     * @param category1 Categoria a la qual se li assignarà la posició de la categoria 2.
     * @param category2 Categoria a la qual se li assignarà la posició de la categoria 1.
     */
    public void swapCategory(Category category1, Category category2) {
        if(Math.abs(category1.getOrder() - category2.getOrder()) == 1) {
            String query = "{CALL Organizer.SwapCategory(?,?,?,?)}";
            java.sql.CallableStatement stmt;
            try {
                stmt = DataBaseManager.getInstance().getConnection().prepareCall(query);
                stmt.setString(1, category1.getId());
                stmt.setString(2, category2.getId());
                stmt.setInt(3, category1.getOrder());
                stmt.setInt(4, category2.getOrder());
                stmt.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Per fer swap han de ser columnes col·lindants.");
        }
    }
}
