package controller;

import model.DataModel;
import view.GraphView;
import view.SuperGraphView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GraphController implements ActionListener{

    private SuperController superController; //????
    private DataModel dataModel;

    private GraphView graphView = new GraphView();
    private SuperGraphView superGraphView;
    private String periode = "Setmanal";

    public GraphController(SuperController superController, DataModel dataModel, SuperGraphView superGraphView){
        this.superController = superController;
        this.dataModel = dataModel;
        this.superGraphView = superGraphView;
        superGraphView.setGraph(graphView);
    }


    public void actualizeGraph(ArrayList<Double> scores){
        graphView.setScores(scores);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
       if(e.getActionCommand().equals("jbSearch")) {
           System.out.println("CLICKED");
           //TEST -- Veiem com si canvien les dades canvia el gràfic
           ArrayList<Double> aux = new ArrayList<>();
           aux.add(1.0);
           aux.add(1.4);
           aux.add(5.0);
           aux.add(5.7);
           aux.add(8.8);
           aux.add(10.5);
           aux.add(12.7);
           aux.add(13.75);
           dataModel.setGraphPoints(aux);
           //TREURE FINS AQUI
           
           //Demanem les dades
           //Primer necessitem saber des de quina data necessitem les dades
           //Això ho fem en funció del periode seleccionat
           Calendar cal;
           Date date;
           switch (periode){
               case "Setmanal":
                   cal = Calendar.getInstance();
                   cal.add(Calendar.DATE, -7);
                   date = cal.getTime();
                   System.out.println("S: " + date);
                   break;
               case "Mensual":
                   cal = Calendar.getInstance();
                   cal.add(Calendar.MONTH, -1);
                   date = cal.getTime();
                   System.out.println("M: " + date);
                   break;
               case "Anual":
                   cal = Calendar.getInstance();
                   cal.add(Calendar.YEAR, -1);
                   date = cal.getTime();
                   System.out.println("Y: " + date);
                   break;
           }
           //Un cop tenim la data fem la petició a la BBDD amb l'usuari i la data mínima
           //TODO:Crida a la bbdd

       }

       if (e.getActionCommand().equals("jcbPeriod")){
          // graphView.definePeriodsUpdate();
           periode = superGraphView.getPeriod();
           System.out.println("Periode: " + periode);
       }
    }


}
