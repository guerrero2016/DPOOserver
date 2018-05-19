package view;

import controller.GraphController;

import javax.swing.*;
import java.awt.*;

public class SuperGraphView extends JPanel{

    private JTextField jtfUser = new JTextField();
    private JComboBox<String> jcbPeriod = new JComboBox<>();
    private JButton jbSearch = new JButton("Search");
    private GraphView graph ;

    private JPanel jpGraph = new JPanel();
    private JPanel jpSuperGraph = new JPanel();

    public SuperGraphView(){
        //Here we create all the panels and configure their layouts
        JPanel jpEast = new JPanel();
        JPanel jpAuxUserPeriod = new JPanel();

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
        jpGraph.setLayout(new BorderLayout());

        jpSuperGraph.add(jpGraph, BorderLayout.CENTER);
        this.add(jpSuperGraph);
    }

    public void configureJcbPeriod(){
        jcbPeriod.addItem("Setmanal");
        jcbPeriod.addItem("Mensual");
        jcbPeriod.addItem("Anual");
        jcbPeriod.setBorder(BorderFactory.createEmptyBorder(8,0,8,0));
        jcbPeriod.setActionCommand("jcbPeriod");
    }

    public void linkController(GraphController graphController){

        jbSearch.addActionListener(graphController);
        jcbPeriod.addActionListener(graphController);
    }

    public String getJtfUserContent(){
        return jtfUser.getText();
    }

    public void actualitzaGraph(GraphView graphView){
        revalidate();
        repaint();

        System.out.println("Prova" + graphView.getMaxScore());
    }

    public void setGraph(GraphView graph) {
        jpGraph.add(graph, BorderLayout.CENTER);
    }

    public String getPeriod(){
        return jcbPeriod.getSelectedItem().toString();
    }

}
