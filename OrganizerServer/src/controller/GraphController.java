package controller;

import db.DataBaseManager;
import model.DataModel;
import view.GraphView;
import view.SuperGraphView;

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
           //Date que passarem a la base de dades com a data mínima
           java.sql.Date sqlDate = null;
           //Array amb dates de les tasques realitzades que ens retorna la bbdd
           ArrayList<java.sql.Date> dateDots = null;
           switch (periode){
               case "Setmanal":
                   cal = Calendar.getInstance();
                   cal.add(Calendar.DATE, -7);
                   date = cal.getTime();
                   sqlDate = new java.sql.Date(date.getTime());
                   System.out.println("S: " + date);
                   System.out.println("SQL S: " + sqlDate);
                   break;
               case "Mensual":
                   cal = Calendar.getInstance();
                   cal.add(Calendar.MONTH, -1);
                   date = cal.getTime();
                   sqlDate = new java.sql.Date(date.getTime());
                   System.out.println("M: " + date);
                   System.out.println("SQL M: " + sqlDate);
                   break;
               case "Anual":
                   cal = Calendar.getInstance();
                   cal.add(Calendar.YEAR, -1);
                   date = cal.getTime();
                   sqlDate = new java.sql.Date(date.getTime());
                   System.out.println("Y: " + date);
                   System.out.println("SQL S: " + sqlDate);
                   break;
           }
           //Un cop tenim la data fem la petició a la BBDD amb l'usuari i la data mínima
           dateDots = DataBaseManager.requestUserEvolution(superGraphView.getJtfUserContent(), sqlDate);
           //TODO:Usar l'array de dates (dateDots) per fer el gràfic bé
       }

       if (e.getActionCommand().equals("jcbPeriod")){
          // graphView.definePeriodsUpdate();
           periode = superGraphView.getPeriod();
           System.out.println("Periode: " + periode);
       }
    }


}
