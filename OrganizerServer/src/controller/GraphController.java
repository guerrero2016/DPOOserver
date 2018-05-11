package controller;

import db.DataBaseManager;
import model.DataModel;
import view.GraphView;
import view.SuperGraphView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
           //Ordenem les dates que ens retornen
           //NOMES UTILITZAT PER TEST
           //NO FER CAS
           /*
           SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
           Date parsed = null;
           try {
               parsed = format.parse("20180210");
           } catch (ParseException e1) {
               e1.printStackTrace();
           }
           java.sql.Date sql = new java.sql.Date(parsed.getTime());
           dateDots.add(sql);
           try {
               parsed = format.parse("20170210");
           } catch (ParseException e1) {
               e1.printStackTrace();
           }
           java.sql.Date sql2 = new java.sql.Date(parsed.getTime());
           dateDots.add(sql2);
           try {
               parsed = format.parse("20190210");
           } catch (ParseException e1) {
               e1.printStackTrace();
           }
           java.sql.Date sql3 = new java.sql.Date(parsed.getTime());
           dateDots.add(sql3);
           dateDots.sort(Comparator.naturalOrder());
           for(java.sql.Date d: dateDots){
               System.out.println("DATA: " + d.toString());
           }
           */
           //Ara comptem quants cops es repeteix una data
           //TODO: COMPTAR LES REPETICIONS D'UNA DATA I MUNTAR EL GRÀFIC
       }

       if (e.getActionCommand().equals("jcbPeriod")){
          // graphView.definePeriodsUpdate();
           periode = superGraphView.getPeriod();
           System.out.println("Periode: " + periode);
       }
    }


}
