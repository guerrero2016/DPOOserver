import controller.SuperController;
import db.ConnectorDB;
import model.DataModel;
import network.Server;
import utils.Configuration;
import view.MainServerView;

public class Main {
    public static void main(String[] args) {
        ConnectorDB connector;

        DataModel dataModel = new DataModel();

        MainServerView mainServerView = new MainServerView();

        SuperController superController = new SuperController(mainServerView, dataModel);

        mainServerView.linkController(superController);

        Configuration.loadConfiguration();

        connector = new ConnectorDB(Configuration.getBBDDUser(), Configuration.getBBDDPass(),
        Configuration.getBBDDName(), Configuration.getBBDDPort(), Configuration.getBBDDIp());
        connector.connect();

        Server server = new Server();
        server.startServer();

    }
}