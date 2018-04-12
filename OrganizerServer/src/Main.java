import controller.SuperController;
import model.DataModel;
import network.Server;
import view.MainServerView;

public class Main {
    public static void main(String[] args) {

        Object test = new Server();

        System.out.println(test.getClass().getName());

        /*MainServerView mainServerView = new MainServerView();
        //???????
        DataModel dataModel = new DataModel();

        SuperController superController = new SuperController(mainServerView, dataModel);

        mainServerView.linkController(superController);*/

    }
}
