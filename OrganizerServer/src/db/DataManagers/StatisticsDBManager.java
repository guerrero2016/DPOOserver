package db.DataManagers;

import com.mysql.jdbc.Statement;
import db.DataBaseManager;
import model.user.UserRanking;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StatisticsDBManager {
    private Statement s;

    //Funció validada
    public ArrayList<Date> requestUserEvolution(String username, Date minDate){
        String query = "{CALL Organizer.requestUserEvolution(?,?)}";
        java.sql.CallableStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Date> dates = new ArrayList<>();

        try {
            stmt = DataBaseManager.getConnection().prepareCall(query);
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
    public UserRanking[] requestTop10(){
        String query = "{CALL Organizer.requestTop10()}";
        java.sql.CallableStatement stmt = null;
        ResultSet rs = null;
        UserRanking[] ranking = new UserRanking[10];

        try {
            stmt = DataBaseManager.getConnection().prepareCall(query);
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
                    s = (Statement) DataBaseManager.getConnection().createStatement();
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
