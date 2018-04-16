package controller;

import model.DataModel;
import view.MainServerView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class SuperController implements Observer{

    private MainServerView mainServerView;
    private GraphController graphController;
    private Top10Controller top10Controller;
    private DataModel dataModel;// = new DataModel();

    public SuperController(MainServerView mainServerView, DataModel dataModel){
        this.dataModel = dataModel;
        //?????
        dataModel.addObserver(this); //ELS DOS VIEWS SACTUALITZEN QUNA HI HA UPDATE
        this.mainServerView = mainServerView;
        this.graphController = new GraphController(this, dataModel, mainServerView.getSuperGraphContent());
        this.top10Controller = new Top10Controller(this,dataModel);
        //TEST
        //FINS A ON ES DIU S'HAURA DE TREURE
        /*
        ArrayList<Double> aux = new ArrayList<>();
        aux.add(0.0);
        aux.add(1.4);
        aux.add(5.0);
        aux.add(6.7);
        aux.add(9.8);
        aux.add(10.5);
        aux.add(15.7);
        aux.add(16.75);
        dataModel.setGraphPoints(aux);
        */
        //TREURE FINS AQUI

    }

    public GraphController getGraphController() {
        return graphController;
    }

    public Top10Controller getTop10Controller() {
        return top10Controller;
    }


    @Override
    public void update(Observable o, Object arg) {
        //PASSEM ELS DE ?USUARI EN QÜESTIÓ DE TANTS DIES COM TINGUEM
        graphController.actualizeGraph(dataModel.getGraphPoints());
    }
}
