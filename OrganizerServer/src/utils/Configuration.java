package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.json.*;
import java.util.Properties;

public class Configuration {
    private static Properties config;
    private final static String path = System.getProperty("user.dir") + System.getProperty("file.separator")
            + "config.json";

    public static void loadConfiguration() {
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

    public static int getBBDDPort() {
        return Integer.parseInt(config.getProperty("bbddPORT"));
    }

    public static String getBBDDIp() {
        return config.getProperty("bbddIP");
    }

    public static String getBBDDName() {
        return config.getProperty("bbddNAME");
    }
    public static String getBBDDPass() {
        return config.getProperty("bbddPASS");
    }

    public static String getBBDDUser() {
        return config.getProperty("bbddUSER");
    }

    public static int getCommunicationPort() {
        return Integer.parseInt(config.getProperty("communicationPORT"));
    }
}
