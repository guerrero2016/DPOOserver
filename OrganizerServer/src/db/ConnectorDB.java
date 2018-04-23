package db;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import model.DataBaseManager;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectorDB {
    private String userName;
    private String password;
    private String db;
    private int port;
    private String url = "jdbc:mysql://localhost";
    private Connection conn = null;
    private Statement s;

    public ConnectorDB(String usr, String pass, String db, int port) {
        this.userName = usr;
        this.password = pass;
        this.db = db;
        this.port = port;
        this.url += ":" + port + "/";
        this.url += db;
    }

    public boolean connect() {
        try {
            conn = (Connection) DriverManager.getConnection(url + "?useSSL=false", userName, password);
            if (conn != null) {
                System.out.println("Conexió a base de dades " + url + " ... Ok");
            }
            DataBaseManager.setConnection(conn);
            return true;
        } catch (SQLException ex) {
            System.out.println("Problema al connecta-nos a la BBDD --> " + url);
        }
        return false;
    }
}