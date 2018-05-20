package view;

import controller.SuperController;
import javafx.scene.control.ComboBox;

import javax.swing.*;
import java.awt.*;

/**
 * Vista que engloba en un Tabbed Pane tant la vista de Top 10 com la vista del gràfic
 */
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

    /**
     * Constructor. Aquí és on afegim les dues vistes (tabs) que formen la vista de Tabbed Pane.
     */
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

    /**
     * Procediment per vincular els controladors als tabs a partir del controlador general.
     * @param superController Controlador general mitjançant el qual podem obtenir els controladors
     *                        per cada tab.
     */
    public void linkController(SuperController superController){
        superGraphContent.linkController(superController.getGraphController());
        top10View.linkController(superController.getTop10Controller());
    }

    /**
     * Funció per obtenir la vista del Tab amb el gràfic
     * @return Vista del Tab del gràfic
     */
    public SuperGraphView getSuperGraphContent() {
        return superGraphContent;
    }

    /**
     * Funció per obtenir la vista del Tab amb el Top 10
     * @return Vista del Tab del Top 10
     */
    public Top10View getTop10Content(){ return top10View; }

}
