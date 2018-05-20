package view;

import controller.GraphController;

import javax.swing.*;
import java.awt.*;

/**
 * Vista on s'engloba tant el gràfic com el panell lateral de cerca.
 */
public class SuperGraphView extends JPanel{

    private JTextField jtfUser = new JTextField();
    private JComboBox<String> jcbPeriod = new JComboBox<>();
    private JButton jbSearch = new JButton("Search");
    private GraphView graph ;

    private JPanel jpGraph = new JPanel();
    private JPanel jpSuperGraph = new JPanel();

    /**
     * Constructor ed la vista.
     */
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

    /**
     * Procediment per omplir les opcions del Combo Box
     */
    public void configureJcbPeriod(){
        jcbPeriod.addItem("Weekly");
        jcbPeriod.addItem("Monthly");
        jcbPeriod.addItem("Anual");
        jcbPeriod.setBorder(BorderFactory.createEmptyBorder(8,0,8,0));
        jcbPeriod.setActionCommand("jcbPeriod");
    }

    /**
     * Vinculació amb el controlador
     * @param graphController Controlador amb el que es vincula la vista
     */
    public void linkController(GraphController graphController){

        jbSearch.addActionListener(graphController);
        jcbPeriod.addActionListener(graphController);
    }

    /**
     * Funció per obtenir el nom de l'usuari a buscar
     * @return String amb el nom introduït per l'usuari
     */
    public String getJtfUserContent(){
        return jtfUser.getText();
    }

    /**
     * Procediment que ens afegeix a la vista el gràfic confeccionat a una altra classe.
     * @param graph Gràfic que volem afegir a la vista.
     */
    public void setGraph(GraphView graph) {
        jpGraph.add(graph, BorderLayout.CENTER);
    }

    /**
     * Funció per obtenir el periode seleccionat amb el Combo Box
     * @return String amb el periode seleccionat.
     */
    public String getPeriod(){
        return jcbPeriod.getSelectedItem().toString();
    }

}
