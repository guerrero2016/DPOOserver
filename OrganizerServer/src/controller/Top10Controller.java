package controller;

import db.DataBaseManager;
import model.DataModel;
import model.user.UserRanking;
import view.Top10View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Controller del Tab del Top 10
 */
public class Top10Controller extends Observable implements ActionListener {

    private Top10View top10View;

    /**
     * COnstructor
     * @param top10View Vista vinculada amb el controller
     */
    public Top10Controller(Top10View top10View){
        this.top10View = top10View;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Si ens premen carregar, actualitzem el Top 10 d'usuaris
        if(e.getActionCommand().equals("Carregar")){
            UserRanking[] top10People = DataBaseManager.getInstance().getStatisticsDBManager().requestTop10();
            ArrayList<UserRanking> arrayOrdenat = new ArrayList<>();
            arrayOrdenat.addAll(Arrays.asList(top10People));
            for(int i = 0; i < arrayOrdenat.size(); i++){
                if(arrayOrdenat.get(i) != null) {
                    System.out.println(arrayOrdenat.get(i).getUsername());
                }
            }
            top10View.actualizeTop10(arrayOrdenat);
        }
    }
}