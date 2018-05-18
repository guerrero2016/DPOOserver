/**
 * TaskDBManager és el gestor que s'encarrega de fer totes les modificacions relacionades amb les tasques.
 *
 * @author  Albert Ferrando
 * @version 1.0
 */
package db.DataManagers;

import com.mysql.jdbc.Statement;
import db.DataBaseManager;
import model.project.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TaskDBManager {
    private Statement s;

    /**
     * Aquesta funció s'encarrega de retornar totes les tasques que es troben dins d'una categoria.
     * Les tasques retornades contindran tots els elements: Etiquetes i usuaris.
     *
     * @param id_columna: Id de la columna de la qual volem recuperar totes les seves tasques.
     */
    public ArrayList<Task> getTasks(String id_columna) {
        ArrayList<Task> tasks = new ArrayList<>();
        ResultSet rs;

        try {
            s =(Statement) DataBaseManager.getInstance().getConnection().createStatement();
            rs = s.executeQuery ("SELECT * FROM Tasca WHERE id_columna = '" + id_columna + "';");
            while(rs.next()) {
                if (rs.getString("id_tasca") != null) {
                    tasks.add(new Task(rs.getString("id_tasca"), rs.getString("nom_tasca"), rs.getInt("posicio"),
                            rs.getString("descripcio"), DataBaseManager.getInstance().getTagDBManager().getTags(rs.getString("id_tasca")),
                            DataBaseManager.getInstance().getMemberInChargeDBManager().getMembersInCharge(rs.getString("id_tasca"))));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return tasks;
    }

    /**
     * Aquesta funció s'encarrega d'afegir una tasca a una categoria concreta.
     *
     * @param t: Tasca en concret que volem afegir.
     * @param id_category: Categoria a la qual volem afegir la tasca en qüestió.
     */
    public void addTask(Task t, String id_category) {
        String query = "{CALL Organizer.AddTask(?,?,?,?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = DataBaseManager.getInstance().getConnection().prepareCall(query);
            stmt.setString(1, id_category);
            stmt.setString(2, t.getId());
            stmt.setString(3, t.getName());
            stmt.setString(4, t.getDescription());
            stmt.setInt(5, t.getOrder());
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Problema al Recuperar les dades --> " + e.getSQLState());
        }
    }

    /**
     * Aquesta funció s'encarrega d'eliminar la tasca indicada.
     *
     * @param id_tasca: Id de la tasca que volem eliminar.
     */
    public void deleteTask(String id_tasca) {
        String query = "{CALL Organizer.deleteTask(?)}";
        java.sql.CallableStatement stmt;
        try {
            stmt = DataBaseManager.getInstance().getConnection().prepareCall(query);
            stmt.setString(1, id_tasca);
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aquesta funció s'encarrega d'actualitzar l'ordre de les tasques.
     *
     * @param tasks: Tasques de les quals volem actualitzar l'ordre.
     */
    public void swapTask(ArrayList<Task> tasks) {
        for(Task t: tasks) {
            String query = "{CALL Organizer.swapTask(?,?)}";
            java.sql.CallableStatement stmt;
            try {
                stmt = DataBaseManager.getInstance().getConnection().prepareCall(query);
                stmt.setString(1, t.getId());
                stmt.setInt(2, t.getOrder());
                stmt.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Aquesta funció s'encarrega de marcar una tasca com a feta, alhora apuntant la data del dia en que s'ha completat.
     *
     * @param id_task: Id de la tasca que volem marcar com a feta.
     */
    public void taskDone(String id_task) {
        String query = "{CALL Organizer.taskDone(?)}";
        java.sql.CallableStatement stmt;
        try {
            stmt = DataBaseManager.getInstance().getConnection().prepareCall(query);
            stmt.setString(1, id_task);
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aquesta funció s'encarrega de desmarcar una tasca com a feta.
     *
     * @param id_task: Id de la tasca que volem marcar com a no feta.
     */
    public void taskNotDone(String id_task) {
        String query = "{CALL Organizer.taskNotDone(?)}";
        java.sql.CallableStatement stmt;
        try {
            stmt = DataBaseManager.getInstance().getConnection().prepareCall(query);
            stmt.setString(1, id_task);
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
