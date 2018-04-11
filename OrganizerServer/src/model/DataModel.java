package model;

import java.util.ArrayList;
import java.util.Observable;

public class DataModel extends Observable{

    private ArrayList<Double> graphPoints = new ArrayList<>();

    public ArrayList<Double> getGraphPoints() {
        return graphPoints;
    }

    public void setGraphPoints(ArrayList<Double> graphPoints) {
        // this.graphPoints = graphPoints;
        this.graphPoints.clear();
        for(int i = 0; i < graphPoints.size(); i++){
            this.graphPoints.add(graphPoints.get(i));
        }
        setChanged();
        notifyObservers(); //Estic notificant els dos observers //????
    }
}