package db.DataManagers;

import com.mysql.jdbc.Statement;
import db.DataBaseManager;
import model.project.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TaskDBManager {
    private Statement s;

    public ArrayList<Task> getTasks(String id_projecte, String id_columna) {
        ArrayList<Task> tasks = new ArrayList<>();
        ResultSet rs;

        try {
            s =(Statement) DataBaseManager.getConnection().createStatement();
            rs = s.executeQuery ("SELECT * FROM Tasca WHERE id_projecte = '" + id_projecte + "' AND id_columna = '" + id_columna + "';");
            while(rs.next()) {
                if (rs.getString("id_tasca") != null) {
                    tasks.add(new Task(rs.getString("id_columna"),
                            rs.getString("id_tasca"), rs.getString("nom_tasca"), rs.getInt("posicio"),
                            rs.getString("descripcio"), DataBaseManager.getTagDBManager().getTags(id_projecte, id_columna,
                            rs.getString("id_tasca")), DataBaseManager.getMemberInChargeDBManager().
                            getEncarregats(id_projecte, id_columna, rs.getString("id_tasca"))));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return tasks;
    }

    //Funció validada
    public void addTask(Task t) {
        String query = "{CALL Organizer.AddTask(?,?,?,?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = DataBaseManager.getConnection().prepareCall(query);
            stmt.setString(1, t.getId_category());
            stmt.setString(2, t.getId());
            stmt.setString(3, t.getName());
            stmt.setString(4, t.getDescription());
            stmt.setInt(5, t.getOrder());
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Problema al Recuperar les dades --> " + e.getSQLState());
        }
    }

    //Funció validada
    public void deleteTask(String id_tasca) {
        String query = "{CALL Organizer.deleteTask(?)}";
        java.sql.CallableStatement stmt;
        try {
            stmt = DataBaseManager.getConnection().prepareCall(query);
            stmt.setString(1, id_tasca);
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void swapTask(ArrayList<Task> tasks) {
        //TODO
    }
}
