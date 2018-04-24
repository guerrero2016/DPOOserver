import controller.SuperController;
import db.ConnectorDB;
import model.DataBaseManager;
import model.DataModel;
import model.project.Category;
import model.project.Project;
import model.project.Task;
import network.Server;
import view.MainServerView;
import view.PasswordDialog;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        boolean ok = false;
        ConnectorDB connector;
        Object test = new Server();


        System.out.println(test.getClass().getName());

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
                if(connector.connect()) {
                    JOptionPane.showMessageDialog(mainServerView, "Accés a la BBDD completat!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    ok = true;
                } else {
                    JOptionPane.showMessageDialog(mainServerView, "Contrasenya incorrecta!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        DataBaseManager.deleteCategory("", "");
        DataBaseManager.deleteEncarregat("","","", "");
        DataBaseManager.deleteProject("");
        DataBaseManager.deleteTag("","","", "");
        DataBaseManager.deleteTask("","","");
        DataBaseManager.deleteEncarregat("","","", "");
        DataBaseManager.addProject(new Project());
    }
}