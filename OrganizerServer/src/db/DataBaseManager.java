package db;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import db.DataManagers.*;

import java.sql.SQLException;

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
    private static StatisticsDBManager statisticsDBManager;

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

    public static StatisticsDBManager getStatisticsDBManager() {
        return statisticsDBManager;
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
        statisticsDBManager = new StatisticsDBManager();
    }
}