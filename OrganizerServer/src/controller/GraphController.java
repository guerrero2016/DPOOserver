package controller;

import db.DataBaseManager;
import model.DataModel;
import view.GraphView;
import view.SuperGraphView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller corresponent al Tab on es pinta el gràfic
 */
public class GraphController implements ActionListener{

    private SuperController superController;
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


    /**
     * Procediment per actualitzar els valors del gràfic
     * @param scores Valors a pintar al gràfic
     */
    public void actualizeGraph(ArrayList<Integer> scores){
        graphView.setScores(scores);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //Si s'ha apretat el botó de cerca
        if(e.getActionCommand().equals("jbSearch")) {

            if(superGraphView.getJtfUserContent().equals("")){
                JOptionPane.showMessageDialog(superGraphView, "Can't leave username blank!");
            }
            else {
                //Demanem les dades
                //Primer necessitem saber des de quina data necessitem les dades
                //Això ho fem en funció del periode seleccionat
                Calendar cal;
                Date date;
                //Date que passarem a la base de dades com a data mínima
                java.sql.Date sqlDate = null;
                //Array amb dates de les tasques realitzades que ens retorna la bbdd
                ArrayList<java.sql.Date> dateDots = null;
                switch (periode) {
                    case "Setmanal":
                        cal = Calendar.getInstance();
                        cal.add(Calendar.DATE, -7);
                        date = cal.getTime();
                        sqlDate = new java.sql.Date(date.getTime());
                        break;
                    case "Mensual":
                        cal = Calendar.getInstance();
                        cal.add(Calendar.MONTH, -1);
                        date = cal.getTime();
                        sqlDate = new java.sql.Date(date.getTime());
                        break;
                    case "Anual":
                        cal = Calendar.getInstance();
                        cal.add(Calendar.YEAR, -1);
                        date = cal.getTime();
                        sqlDate = new java.sql.Date(date.getTime());
                        break;
                }
                //Un cop tenim la data fem la petició a la BBDD amb l'usuari i la data mínima
                dateDots = DataBaseManager.getInstance().getStatisticsDBManager().requestUserEvolution(superGraphView.getJtfUserContent(), sqlDate);
                dateDots.sort(Comparator.naturalOrder());
                //Ara comptem quants cops es repeteix una data
                ArrayList<Integer> repes = countDates(dateDots, periode);
                dataModel.setGraphPoints(repes);
            }
        }

        //Si l'event ve del Combo Box, actualitzem el període
        if (e.getActionCommand().equals("jcbPeriod")){
            // graphView.definePeriodsUpdate();
            periode = superGraphView.getPeriod();
        }
    }

    /**
     * Funció per obtenir els punts a pintar al gràfic. Retornem el valor per cada dia en el periode
     * @param dateDots Conjunt de dates en que l'usuari ha acabat una tasca
     * @param periode Periode pel qual es vol mirar l'evolució
     * @return Retornem un arrayList amb el nombre de tasques finalitzades per cada dia dins del periode establert
     */
    private ArrayList<Integer> countDates(ArrayList<java.sql.Date> dateDots, String periode){
        ArrayList<Integer> arrayDates = new ArrayList<>();
        Calendar calAux = Calendar.getInstance();
        Calendar calAuxActual = Calendar.getInstance();
        int i = 0;
        if(periode.equals("Setmanal")){
            calAux.add(Calendar.DATE, -7);
            arrayDates = auxCountDateInArray(calAux, calAuxActual, dateDots);
        }
        if(periode.equals("Mensual")){
            calAux.add(Calendar.MONTH, -1);
            arrayDates = auxCountDateInArray(calAux, calAuxActual, dateDots);
        }
        if(periode.equals("Anual")){
            calAux.add(Calendar.YEAR, -1);
            arrayDates = auxCountDateInArray(calAux, calAuxActual, dateDots);
        }
        return arrayDates;
    }

    /**
     * Funció que compta per cada dia al periode quantes tasques ha fet l'usuari
     * @param calAux Data d'inici del periode
     * @param calAuxActual Data actual, finalització del periode
     * @param dateDots Conjunt de dates on ha acabat tasques
     * @return Retornem l'ArrayList amb la quantitat de tasques fetes cada dia del periode
     */
    private ArrayList<Integer> auxCountDateInArray(Calendar calAux, Calendar calAuxActual,
                                                   ArrayList<java.sql.Date> dateDots){
        ArrayList<Integer> arrayDates = new ArrayList<>();
        int i = 0;
        for (Date date = calAux.getTime(); calAux.before(calAuxActual) || calAux.equals(calAuxActual); calAux.add(Calendar.DATE, 1), date = calAux.getTime()) {
            int numRepe = 0;
            java.sql.Date dateAux = new java.sql.Date(date.getTime());
            for(java.sql.Date d : dateDots) {
                if (dateAux.toString().equals(d.toString())) {
                    numRepe++;
                }
            }
            arrayDates.add(i,numRepe);
            i++;
        }

        return arrayDates;
    }


}

