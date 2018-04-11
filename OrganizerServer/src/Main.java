import controller.SuperController;
import model.DataModel;
import view.MainServerView;

public class Main {
    public static void main(String[] args) {
        MainServerView mainServerView = new MainServerView();
        //???????
        DataModel dataModel = new DataModel();

        SuperController superController = new SuperController(mainServerView, dataModel);

        mainServerView.linkController(superController);

    }
}
