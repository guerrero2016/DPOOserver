/**
 * DataBaseManager és un singleton que conté tots els gestors, els quals podem recuperar per a fer tot tipus de gestions
 * a la base de dades.
 *
 * @author  Albert Ferrando
 * @version 1.0
 */
package db;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import db.DataManagers.*;

import java.sql.SQLException;

public class DataBaseManager {
    private static DataBaseManager ourInstance = new DataBaseManager();
    private static Connection connection;
    private static UserDBManager userDBManager;
    private static ProjectDBManager projectDBManager;
    private static CategoryDBManager categoryDBManager;
    private static TaskDBManager taskDBManager;
    private static MemberInChargeDBManager memberInChargeDBManager;
    private static TagDBManager tagDBManager;
    private static MemberDBManager memberDBManager;
    private static StatisticsDBManager statisticsDBManager;

    /**
     * Constructor sense paràmetres.
     */
    private DataBaseManager() {
    }

    /**
     * Getter del UserDBManager.
     */
    public static UserDBManager getUserDBManager() {
        return userDBManager;
    }

    /**
     * Getter del ProjectDBManager.
     */
    public static ProjectDBManager getProjectDBManager() {
        return projectDBManager;
    }

    /**
     * Getter del CategoryDBManager.
     */
    public static CategoryDBManager getCategoryDBManager() {
        return categoryDBManager;
    }

    /**
     * Getter del StatisticsDBManager.
     */
    public static StatisticsDBManager getStatisticsDBManager() {
        return statisticsDBManager;
    }

    /**
     * Getter del TaskDBManager.
     */
    public static TaskDBManager getTaskDBManager() {
        return taskDBManager;
    }

    /**
     * Getter del MemberInChargeDBManager.
     */
    public static MemberInChargeDBManager getMemberInChargeDBManager() {
        return memberInChargeDBManager;
    }

    /**
     * Getter del TagDBManager.
     */
    public static TagDBManager getTagDBManager() {
        return tagDBManager;
    }

    /**
     * Getter del MemberDBManager.
     */
    public static MemberDBManager getMemberDBManager() {
        return memberDBManager;
    }

    /**
     * Getter de la connexió amb la base de dades.
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Setter de la connexió i un cop tenim la connexió i per tant ja podem connectar amb la base de dades
     * creem tots els Managers.
     */
    static void setConnection(Connection connection) {
        DataBaseManager.connection = connection;
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