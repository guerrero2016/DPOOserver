package model;

import java.util.ArrayList;
import java.util.Observable;

public class DataModel extends Observable{

    private ArrayList<Double> graphPoints = new ArrayList<>();

    public ArrayList<Double> getGraphPoints() {
        return graphPoints;
    }



    public void setGraphPoints(ArrayList<Double> graphPoints) {
        this.graphPoints = graphPoints;
        setChanged();
        notifyObservers(); //Estic notificant els dos observers //????
    }
}