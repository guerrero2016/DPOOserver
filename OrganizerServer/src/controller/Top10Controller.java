package controller;

import model.DataBaseManager;
import model.DataModel;
import model.user.Top10NeededData;
import view.MainServerView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

public class Top10Controller extends Observable implements ActionListener {

    private SuperController superController;//???
    private DataModel dataModel;

    public Top10Controller(SuperController superController, DataModel dataModel){
        this.superController = superController;
        this.dataModel = dataModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Carregar")){
            Top10NeededData[] top10People = DataBaseManager.requestTop10();
            //TODO: Actualitzar el top 10 amb les dades de top10People
        }
    }
}