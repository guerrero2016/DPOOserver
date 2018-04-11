package controller;

import model.DataModel;
import view.MainServerView;

import java.util.Observable;

public class Top10Controller extends Observable {

    private SuperController superController;//???
    private DataModel dataModel;

    public Top10Controller(SuperController superController, DataModel dataModel){
        this.superController = superController;
        this.dataModel = dataModel;
    }

}