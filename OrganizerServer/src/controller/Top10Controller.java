package controller;

import db.DataBaseManager;
import model.DataModel;
import model.user.UserRanking;
import view.Top10View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Top10Controller extends Observable implements ActionListener {

    private SuperController superController;//???
    private DataModel dataModel;
    private Top10View top10View;

    public Top10Controller(SuperController superController, DataModel dataModel, Top10View top10View){
        this.superController = superController;
        this.dataModel = dataModel;
        this.top10View = top10View;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Carregar")){
            UserRanking[] top10People = DataBaseManager.getInstance().getStatisticsDBManager().requestTop10();
            /*SEGÜENT LINIA NOMES PER TEST
            UserRanking[] top10People = new UserRanking[10];
            //DADES NOMES DE TEST
            //ESBORRAR FINS ON S'INDIQUI
            top10People[0] = new UserRanking();
            top10People[1] = new UserRanking();
            top10People[2] = new UserRanking();
            top10People[3] = new UserRanking();
            top10People[4] = new UserRanking();
            top10People[5] = new UserRanking();
            top10People[6] = new UserRanking();
            top10People[7] = new UserRanking();
            top10People[8] = new UserRanking();
            top10People[9] = new UserRanking();

            top10People[0].setUsername("a");
            top10People[0].setTotalTasks(10);
            top10People[0].setPendingTasks(2);
            top10People[1].setUsername("b");
            top10People[1].setTotalTasks(410);
            top10People[1].setPendingTasks(122);
            top10People[2].setUsername("c");
            top10People[2].setTotalTasks(3);
            top10People[2].setPendingTasks(2);
            top10People[3].setUsername("d");
            top10People[3].setTotalTasks(130);
            top10People[3].setPendingTasks(123);
            top10People[4].setUsername("e");
            top10People[4].setTotalTasks(140);
            top10People[4].setPendingTasks(44);
            top10People[5].setUsername("f");
            top10People[5].setTotalTasks(220);
            top10People[5].setPendingTasks(122);
            top10People[6].setUsername("g");
            top10People[6].setTotalTasks(3);
            top10People[6].setPendingTasks(2222);
            top10People[7].setUsername("h");
            top10People[7].setTotalTasks(130);
            top10People[7].setPendingTasks(23);
            top10People[8].setUsername("i");
            top10People[8].setTotalTasks(10);
            top10People[8].setPendingTasks(44);
            top10People[9].setUsername("j");
            top10People[9].setTotalTasks(2220);
            top10People[9].setPendingTasks(1122);
            */
            //Ordenem per tasques pendents
            ArrayList<UserRanking> arrayOrdenat = new ArrayList<>();
            arrayOrdenat.addAll(Arrays.asList(top10People));
            for(int i = 0; i < arrayOrdenat.size(); i++){
                if(arrayOrdenat.get(i) != null) {
                    System.out.println(arrayOrdenat.get(i).getUsername());
                }
            }
            //arrayOrdenat.sort(Comparator.comparingInt(UserRanking::getPendingTasks));
            top10View.actualizeTop10(arrayOrdenat);
        }
    }
}