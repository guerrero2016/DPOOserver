import controller.SuperController;
import db.ConnectorDB;
import model.DataModel;
import network.Server;
import view.MainServerView;
import view.PasswordDialog;

import javax.swing.*;
import java.awt.event.WindowEvent;


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
                if(connector.connect()) {
                    JOptionPane.showMessageDialog(mainServerView, "Accés a la BBDD completat!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    ok = true;
                } else {
                    JOptionPane.showMessageDialog(mainServerView, "Contrasenya incorrecta!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        }


        Server server = new Server();
        server.startServer();
    }
}