package db;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import db.DataManagers.*;
import model.user.UserRanking;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataBaseManager {
    private static DataBaseManager ourInstance = new DataBaseManager();
    private static Connection connection;
    private static Statement s;
    private static UserDBManager userDBManager;
    private static ProjectDBManager projectDBManager;
    private static CategoryDBManager categoryDBManager;
    private static TaskDBManager taskDBManager;
    private static MemberInChargeDBManager memberInChargeDBManager;
    private static TagDBManager tagDBManager;
    private static MemberDBManager memberDBManager;

    private DataBaseManager() {
    }

    public static UserDBManager getUserDBManager() {
        return userDBManager;
    }

    public static ProjectDBManager getProjectDBManager() {
        return projectDBManager;
    }

    public static CategoryDBManager getCategoryDBManager() {
        return categoryDBManager;
    }

    public static TaskDBManager getTaskDBManager() {
        return taskDBManager;
    }

    public static MemberInChargeDBManager getMemberInChargeDBManager() {
        return memberInChargeDBManager;
    }

    public static TagDBManager getTagDBManager() {
        return tagDBManager;
    }

    public static MemberDBManager getMemberDBManager() {
        return memberDBManager;
    }

    public static Connection getConnection() {
        return connection;
    }

    static void setConnection(Connection connection) {
        DataBaseManager.connection = connection;
        try {
            s = (Statement) connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Problema al Inserir --> " + e.getSQLState());
        }
        userDBManager = new UserDBManager();
        projectDBManager = new ProjectDBManager();
        categoryDBManager = new CategoryDBManager();
        taskDBManager = new TaskDBManager();
        memberInChargeDBManager = new MemberInChargeDBManager();
        tagDBManager = new TagDBManager();
        memberDBManager = new MemberDBManager();
    }

    public static ArrayList<Date> requestUserEvolution(String username, Date minDate){
        String query = "{CALL Organizer.requestUserEvolution(?,?)}";
        java.sql.CallableStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Date> dates = new ArrayList<>();

        try {
            stmt = connection.prepareCall(query);
            stmt.setString(1, username);
            stmt.setDate(2, minDate);
            rs = stmt.executeQuery();

            while(rs.next()) {

                    Date data = rs.getDate("data_done");
                    dates.add(data);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dates;
    }

    //Funció validada
    public static UserRanking[] requestTop10(){
        String query = "{CALL Organizer.requestTop10()}";
        java.sql.CallableStatement stmt = null;
        ResultSet rs = null;
        UserRanking[] ranking = new UserRanking[10];

        try {
            stmt = connection.prepareCall(query);
            rs = stmt.executeQuery();

            int i = 0;
            while(rs.next() && rs.getString("nom_usuari") != null && i < 10) {
                ranking[i] = new UserRanking();
                ranking[i].setUsername(rs.getString("nom_usuari"));
                ranking[i].setPendingTasks(rs.getInt("tasques_per_fer"));
                i++;
            }
            for(UserRanking u: ranking) {
                if(u != null) {
                    s = (Statement) connection.createStatement();
                    rs = s.executeQuery("SELECT COUNT(*) as tasques_fetes FROM Tasca as t JOIN Tasca_Usuari as tu ON t.id_tasca = tu.id_tasca " +
                            "WHERE data_done IS NOT null AND nom_usuari = '" + u.getUsername() + "' GROUP BY nom_usuari;");
                    rs.next();
                    u.setTotalTasks(u.getPendingTasks() + rs.getInt("tasques_fetes"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ranking;
    }
}