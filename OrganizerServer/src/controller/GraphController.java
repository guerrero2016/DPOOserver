package controller;

import model.DataModel;
import view.GraphView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GraphController implements ActionListener{

    private SuperController superController; //????
    private DataModel dataModel;

    private GraphView graphView = new GraphView();

    public GraphController(SuperController superController, DataModel dataModel){
        this.superController = superController;
        this.dataModel = dataModel;
    }

    public JPanel getPanelVista(){
        return graphView.getGraphView();
    }

    public void actualizeGraph(ArrayList<Double> scores){
        graphView.setScores(scores);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("CLICKED");
        //TEST -- Veiem com si canvien les dades canvia el gràfic
        ArrayList<Double> aux = new ArrayList<>();
        aux.add(0.0);
        aux.add(1.4);
        aux.add(5.0);
        aux.add(5.7);
        aux.add(8.8);
        aux.add(10.5);
        aux.add(12.7);
        aux.add(13.75);
        dataModel.setGraphPoints(aux);
    }
}
