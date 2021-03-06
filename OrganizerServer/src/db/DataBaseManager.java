/**
 * DataBaseManager és un singleton que conté tots els gestors, els quals podem recuperar per a fer tot tipus de gestions
 * a la base de dades.
 *
 * @author  Albert Ferrando
 * @version 1.0    private static Connection connection;
 */
package db;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import db.DataManagers.*;

import java.sql.SQLException;

public class DataBaseManager {
    private static DataBaseManager ourInstance = new DataBaseManager();
    private UserDBManager userDBManager;
    private Connection connection;
    private ProjectDBManager projectDBManager;
    private CategoryDBManager categoryDBManager;
    private TaskDBManager taskDBManager;
    private MemberInChargeDBManager memberInChargeDBManager;
    private TagDBManager tagDBManager;
    private MemberDBManager memberDBManager;
    private StatisticsDBManager statisticsDBManager;

    /**
     * Getter de la instància singleton.
     * @return Instància
     */
    public static DataBaseManager getInstance() {
        return ourInstance;
    }

    /**
     * Constructor sense paràmetres.
     */
    private DataBaseManager() {
    }

    /**
     * Getter del UserDBManager.
     * @return UserDBManager
     */
    public UserDBManager getUserDBManager() {
        return userDBManager;
    }

    /**
     * Getter del ProjectDBManager.
     * @return ProjectDBManager
     */
    public ProjectDBManager getProjectDBManager() {
        return projectDBManager;
    }

    /**
     * Getter del CategoryDBManager.
     * @return CategoryDBManager
     */
    public CategoryDBManager getCategoryDBManager() {
        return categoryDBManager;
    }

    /**
     * Getter del StatisticsDBManager.
     * @return StatisticsDBManager
     */
    public StatisticsDBManager getStatisticsDBManager() {
        return statisticsDBManager;
    }

    /**
     * Getter del TaskDBManager.
     * @return TaskDBManager
     */
    public TaskDBManager getTaskDBManager() {
        return taskDBManager;
    }

    /**
     * Getter del MemberInChargeDBManager.
     * @return MemberInChargeDBManager
     */
    public MemberInChargeDBManager getMemberInChargeDBManager() {
        return memberInChargeDBManager;
    }

    /**
     * Getter del TagDBManager.
     * @return TagDBManager
     */
    public TagDBManager getTagDBManager() {
        return tagDBManager;
    }

    /**
     * Getter del MemberDBManager.
     * @return MemberDBManager
     */
    public MemberDBManager getMemberDBManager() {
        return memberDBManager;
    }

    /**
     * Getter de la connexió amb la base de dades.
     * @return Connexió
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Setter de la connexió i un cop tenim la connexió i per tant ja podem connectar amb la base de dades
     * creem tots els Managers.
     * @param connection Connexió
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
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