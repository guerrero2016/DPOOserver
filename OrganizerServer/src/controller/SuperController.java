package controller;

import model.DataModel;
import view.MainServerView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Classe que engloba els dos controllers que seran per cada Tab.
 */
public class SuperController implements Observer{

    private MainServerView mainServerView;
    private GraphController graphController;
    private Top10Controller top10Controller;
    private DataModel dataModel;

    /**
     * Constructor sense paràmetres.
     * @param mainServerView Vista
     * @param dataModel Model
     */
    public SuperController(MainServerView mainServerView, DataModel dataModel){
        this.dataModel = dataModel;
        dataModel.addObserver(this);
        this.mainServerView = mainServerView;
        this.graphController = new GraphController(this, dataModel, mainServerView.getSuperGraphContent());
        this.top10Controller = new Top10Controller(mainServerView.getTop10Content());
    }

    /**
     * Funció per obtenir el controller del Tab del gràfic
     * @return Controller del Tab del gràfic
     */
    public GraphController getGraphController() {
        return graphController;
    }

    /**
     * Funció per obtenir el controller del Tab del Top10
     * @return Controller del Tab del Top10
     */
    public Top10Controller getTop10Controller() {
        return top10Controller;
    }


    /**
     * Procediment propi del patro observer
     * @param o Classe que és observable
     * @param arg Objecte on s'ha fet l'update
     */
    @Override
    public void update(Observable o, Object arg) {
        graphController.actualizeGraph(dataModel.getGraphPoints());
    }
}
