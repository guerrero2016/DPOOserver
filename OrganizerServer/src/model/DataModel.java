package model;

import java.util.ArrayList;
import java.util.Observable;

public class DataModel extends Observable{

    private ArrayList<Integer> graphPoints = new ArrayList<>();

    public ArrayList<Integer> getGraphPoints() {
        return graphPoints;
    }

    public void setGraphPoints(ArrayList<Integer> graphPoints) {
        this.graphPoints = graphPoints;
        setChanged();
        notifyObservers();
    }
}