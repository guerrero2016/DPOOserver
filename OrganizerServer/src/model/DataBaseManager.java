package model;

import com.mysql.jdbc.Connection;

public class DataBaseManager {
    private static DataBaseManager ourInstance = new DataBaseManager();
    private static Connection connection;

    public static DataBaseManager getInstance() {
        return ourInstance;
    }

    private DataBaseManager() {
    }

    public static void setConnection(Connection connection) {
        DataBaseManager.connection = connection;
    }
}
