package view;

import controller.SuperController;
import javafx.scene.control.ComboBox;

import javax.swing.*;
import java.awt.*;

public class MainServerView extends JFrame {

    //Constants
    private static final int MIN_WIDTH = 550;
    private static final int MIN_HEIGHT = 400;

    private JPanel jpSuperGraph = new JPanel();
    private JPanel jpSuperTop10 = new JPanel();
    private JTabbedPane jtpMain = new JTabbedPane();

    //Internal Views
    private SuperGraphView superGraphContent = new SuperGraphView();
    private Top10View top10View = new Top10View();

    public MainServerView(){
        setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server Control Window");
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));

        jpSuperGraph.add(superGraphContent, BorderLayout.CENTER);
        jpSuperTop10.add(top10View, BorderLayout.CENTER);

        jtpMain.addTab("Graph", null, jpSuperGraph, "User progression graph");
        jtpMain.addTab("Top 10", null, jpSuperTop10, "Top 10 users with most pending tasks");
        getContentPane().add(jtpMain);
        setVisible(true);
    }

    public void linkController(SuperController superController){
        superGraphContent.linkController(superController.getGraphController());
        top10View.linkController(superController.getTop10Controller());
    }

    public SuperGraphView getSuperGraphContent() {
        return superGraphContent;
    }

    public Top10View getTop10Content(){ return top10View; }

}
