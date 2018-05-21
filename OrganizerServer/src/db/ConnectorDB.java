/**
 * ConnectorDB és l'encarregat de duur a terme la connexió amb la base de dades MySQL.
 *
 * @author  Albert Ferrando
 * @version 1.0
 */
package db;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectorDB {
    private static ConnectorDB ourInstance = new ConnectorDB();

    /**
     * Aquesta funció s'encarrega de crear la instància del nostre singleton.
     * @return Instància
     */
    public static ConnectorDB getInstance() {
        return ourInstance;
    }

    /**
     * Constructor sense paràmetres.
     */
    private ConnectorDB() {
    }

    private String userName;
    private String password;
    private String url = "jdbc:mysql://";
    private Connection conn = null;
    private Statement s;

    /**
     * Constructor amb paràmetres.
     *
     * @param usr Usuari amb el qual volem connectar-nos a la bbdd.
     * @param pass Contrasenya de l'usuari.
     * @param db Nom de la base de dades.
     * @param port Port en el que es troba la base de dades (o el gestor).
     * @param ip IP
     */
    public ConnectorDB(String usr, String pass, String db, int port, String ip) {
        this.userName = usr;
        this.password = pass;
        this.url += ip;
        this.url += ":" + port + "/";
        this.url += db;
    }

    /** Funció que s'encarrega de connectar amb la base de dades que els atributs del propi connector indiquen.
     * @return Retorna cert si la connexió ha estat satisfactoria i fals si no.
     */
    public boolean connect() {
        try {
            conn = (Connection) DriverManager.getConnection(url + "?useSSL=false", userName, password);
            if (conn != null) {
                System.out.println("Conexió a base de dades " + url + " ... Ok");
            }
            DataBaseManager.getInstance().setConnection(conn);
            return true;
        } catch (SQLException ex) {
            System.out.println("Problema al connecta-nos a la BBDD --> " + url);
        }
        return false;
    }
}