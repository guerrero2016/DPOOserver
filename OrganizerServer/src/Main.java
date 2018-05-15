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


        //Testing (Es pot borrar pero el deixo per si en un futur cal).
        /*
        DataBaseManager.getProjectDBManager().addProject(new Project("id_del_p", "posando", null, null, true));
        Category c1 = new Category("id_del_c", "cacatua", 1, null);
        Category c2 = new Category("id_del_c2", "cister", 2, null);
        DataBaseManager.getCategoryDBManager().addCategory(c1, "id_del_p");
        DataBaseManager.getCategoryDBManager().addCategory(c2, "id_del_p");
        Task t1 = new Task("id_del_t1", "t1", 1, "siasd", null, null);
        Task t2 = new Task("id_del_t3", "t3", 3, "siasd", null, null);
        Task t3 = new Task("id_del_t2", "t2", 2, "siasd", null, null);
        DataBaseManager.getTaskDBManager().addTask(t1, "id_del_c");
        DataBaseManager.getTaskDBManager().addTask(t2, "id_del_c");
        DataBaseManager.getTaskDBManager().addTask(t3, "id_del_c");
        DataBaseManager.getUserDBManager().registrarUsuari("el nene", "si.com", "tete");
        DataBaseManager.getUserDBManager().registrarUsuari("el nense", "si.csom", "tete");
        DataBaseManager.getUserDBManager().registrarUsuari("el nensse", "sis.com", "tete");


        DataBaseManager.getMemberInChargeDBManager().addMemberInCharge(new User("el nene"), "id_del_t1");
        DataBaseManager.getMemberInChargeDBManager().addMemberInCharge(new User("el nene"), "id_del_t2");
        DataBaseManager.getMemberInChargeDBManager().addMemberInCharge(new User("el nene"), "id_del_t3");
        DataBaseManager.getTaskDBManager().taskDone("id_del_t1");
        DataBaseManager.getTaskDBManager().taskDone("id_del_t3");
        DataBaseManager.getTagDBManager().addTag(new Tag("id_del_tag", "tita", "color"), "id_del_t1");
        DataBaseManager.getTagDBManager().addTag(new Tag("id_del_tag2", "tita", "color"), "id_del_t2");

        ArrayList<User> users = DataBaseManager.getMemberInChargeDBManager().getMembersInCharge("id_del_t1");
        ArrayList<Tag> tags = DataBaseManager.getTagDBManager().getTags("id_del_t1");
        ArrayList<Task> tasks = DataBaseManager.getTaskDBManager().getTasks("id_del_c");
        ArrayList<Category> categories = DataBaseManager.getCategoryDBManager().getCategories("id_del_p");
        Project p = DataBaseManager.getProjectDBManager().getProject("id_del_p");
        DataBaseManager.getCategoryDBManager().swapCategory("id_del_p", c2, c1);

        DataBaseManager.getMemberDBManager().addMember("id_del_p", "el nene");
        DataBaseManager.getMemberDBManager().addMember("id_del_p", "el nense");
        DataBaseManager.getMemberDBManager().addMember("id_del_p", "el nensse");
        DataBaseManager.getMemberDBManager().addMember("id_del_p", "el nene");
        DataBaseManager.getMemberDBManager().deleteMember("id_del_p", "el nene");
        ArrayList<String> members = DataBaseManager.getMemberDBManager().getMembers("id_del_p");
        DataBaseManager.getProjectDBManager().addProjectOwner("id_del_p", "el nene");

        ArrayList<Project> projects = DataBaseManager.getProjectDBManager().getProjectsOwner("el nene");
        String nom = DataBaseManager.getUserDBManager().getUsername("si.com");
        t1.setOrder(3);
        t2.setOrder(2);
        t3.setOrder(1);
        ArrayList<Task> tasques = new ArrayList<>();
        tasques.add(t1);
        tasques.add(t2);
        tasques.add(t3);
        DataBaseManager.getTaskDBManager().swapTask(tasques); */
    }
}