package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.json.*;
import java.util.Properties;

/**
 * Classe singleton encarregada de llegir la configuració.
 */
public class Configuration {
    private static Configuration ourInstance = new Configuration();
    private Properties config;
    private final String path = System.getProperty("user.dir") + System.getProperty("file.separator")
            + "config.json";

    /**
     * Constructor sense paràmetres.
     */
    private Configuration() {}

    /**
     * Getter del singleton Configuration.
     * @return Instància
     */
    public static Configuration getInstance() {
        return ourInstance;
    }

    /**
     * Carrega diversos paràmetres de configuració a partir de l'arxiu config.json.
     */
    public void loadConfiguration() {
        BufferedReader br = null;
        config = new Properties();

        try {
            br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            JSONObject jsonObject = new JSONObject(sb.toString());
            config.setProperty("bbddPORT", jsonObject.getString("bbddPORT"));
            config.setProperty("bbddIP", jsonObject.getString("bbddIP"));
            config.setProperty("bbddNAME", jsonObject.getString("bbddNAME"));
            config.setProperty("bbddUSER", jsonObject.getString("bbddUSER"));
            config.setProperty("bbddPASS", jsonObject.getString("bbddPASS"));
            config.setProperty("communicationPORT", jsonObject.getString("communicationPORT"));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Getter del port de la base de dades Mysql.
     * @return Port
     */
    public int getBBDDPort() {
        return Integer.parseInt(config.getProperty("bbddPORT"));
    }

    /**
     * Getter de la IP de la base de dades Mysql.
     * @return IP
     */
    public String getBBDDIp() {
        return config.getProperty("bbddIP");
    }

    /**
     * Getter del nom de la base de dades Mysql.
     * @return Nom de la base de dades
     */
    public String getBBDDName() {
        return config.getProperty("bbddNAME");
    }

    /**
     * Getter de la contrasenya de l'usuari amb el qual ens connectem a la base de dades.
     * @return Contrasenya
     */
    public String getBBDDPass() {
        return config.getProperty("bbddPASS");
    }

    /**
     * Getter del nom de l'usuari amb el qual ens connectem a la base de dades.
     * @return Nom de l'usuari
     */
    public String getBBDDUser() {
        return config.getProperty("bbddUSER");
    }

    /**
     * Getter del port amb el qual ens connectarem amb el client.
     * @return Port
     */
    public int getCommunicationPort() {
        return Integer.parseInt(config.getProperty("communicationPORT"));
    }
}
