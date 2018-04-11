package view;

import controller.SuperController;
import javafx.scene.control.ComboBox;

import javax.swing.*;
import java.awt.*;

public class MainServerView extends JFrame {

    private JPanel jpSuperGraph = new JPanel();
    private JTextField jtfUser = new JTextField();
    private JComboBox<String> jcbPeriod = new JComboBox<>();
    private JButton jbSearch = new JButton("Search");

    public MainServerView(){
        setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server Control Window");
        setLocationRelativeTo(null);

        //Here we create all the panels and configure their layouts
        // JPanel jpCenter = new JPanel();
        JPanel jpEast = new JPanel();
        JPanel jpAuxUserPeriod = new JPanel();

        //jpCenter.setLayout(new BorderLayout());
        jpEast.setLayout(new BorderLayout());
        jpSuperGraph.setLayout(new BorderLayout());
        jpAuxUserPeriod.setLayout(new BoxLayout(jpAuxUserPeriod, BoxLayout.PAGE_AXIS));
        configureJcbPeriod();


        jpAuxUserPeriod.add(jtfUser);
        jpAuxUserPeriod.add(jcbPeriod);
        jbSearch.setActionCommand("jbSearch");
        jcbPeriod.setBorder(BorderFactory.createEmptyBorder(8,0,8,0));
        jpAuxUserPeriod.add(jbSearch);
        jpEast.add(jpAuxUserPeriod, BorderLayout.PAGE_START);
        jpEast.setBorder(BorderFactory.createTitledBorder("Controls"));
        jpSuperGraph.add(jpEast, BorderLayout.LINE_END);

        getContentPane().add(jpSuperGraph);
        setVisible(true);
    }

    //?? Cridada des del constructor del controller
    public void fillJpSuperGraph(JPanel jpGraph){
        //Here we fill the panel
        jpSuperGraph.add(jpGraph, BorderLayout.CENTER);
        jpSuperGraph.revalidate();
        jpSuperGraph.repaint();


    }

    public void configureJcbPeriod(){
        jcbPeriod.addItem("Diari");
        jcbPeriod.addItem("Mensual");
        jcbPeriod.addItem("Anual");
        jcbPeriod.setBorder(BorderFactory.createEmptyBorder(8,0,8,0));
    }

    public void linkController(SuperController superController){
        //Quan vulgui linkar haure de fer per exemple superController.getGraphController
        jbSearch.addActionListener(superController.getGraphController());
    }
}
