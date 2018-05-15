import controller.SuperController;
import db.ConnectorDB;
import db.DataBaseManager;
import model.DataModel;
import model.project.*;
import network.Server;
import view.MainServerView;
import view.PasswordDialog;
import javax.swing.*;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        boolean ok = false;
        ConnectorDB connector;

        DataModel dataModel = new DataModel();

        MainServerView mainServerView = new MainServerView();

        SuperController superController = new SuperController(mainServerView, dataModel);

        mainServerView.linkController(superController);

        while (!ok) {
            final String CANCEL = ".-^";
            String password = new PasswordDialog(mainServerView).getPass();
            if (password.equals(CANCEL)) {
                mainServerView.dispatchEvent(new WindowEvent(mainServerView, WindowEvent.WINDOW_CLOSING));
            } else {
                connector = new ConnectorDB("root", password, "Organizer", 3306);
                if (connector.connect()) {
                    JOptionPane.showMessageDialog(mainServerView, "Accés a la BBDD completat!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    ok = true;
                } else {
                    JOptionPane.showMessageDialog(mainServerView, "Contrasenya incorrecta!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        }

        Server server = new Server();
        server.startServer();

        DataBaseManager.getProjectDBManager().addProject(new Project("id_del_p", "p", null, null, true));
        DataBaseManager.getCategoryDBManager().addCategory(new Category("id_del_c", "c", 1, null), "id_del_p");
        DataBaseManager.getTaskDBManager().addTask(new Task("id_del_t1", "t1", 1, "si", null, null), "id_del_c");
        DataBaseManager.getTaskDBManager().addTask(new Task("id_del_t3", "t3", 3, "si", null, null), "id_del_c");
        DataBaseManager.getTaskDBManager().addTask(new Task("id_del_t2", "t2", 2, "si", null, null), "id_del_c");
        DataBaseManager.getUserDBManager().registrarUsuari("el nene", "si.com", "tete");
        DataBaseManager.getMemberInChargeDBManager().addMemberInCharge(new MemberInCharge("el nene"), "id_del_t1");
        DataBaseManager.getMemberInChargeDBManager().addMemberInCharge(new MemberInCharge("el nene"), "id_del_t2");
        DataBaseManager.getMemberInChargeDBManager().addMemberInCharge(new MemberInCharge("el nene"), "id_del_t3");
        DataBaseManager.getTaskDBManager().taskDone("id_del_t1");
        DataBaseManager.getTaskDBManager().taskDone("id_del_t3");

        ArrayList<Date> dates = DataBaseManager.requestUserEvolution("el nene", new Date(2005, 1, 1));
    }
}