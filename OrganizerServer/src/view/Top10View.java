package view;

import controller.Top10Controller;
import model.user.UserRanking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Classe encarregada de la vista de la pantalla on mostrem el top 10 d'usuaris amb més tasques pendents.
 */
public class Top10View extends JPanel {

    private JPanel jpSuperTop = new JPanel();
    private JPanel jpCenter;
    private JButton jbActulize = new JButton("Carregar");
    private JPanel[] jpArray = new JPanel[10];
    private JLabel[] jlNomUserArray = new JLabel[10];
    private JLabel[] jlNumTaskPendentsArray = new JLabel[10];
    private JLabel[] jlNumTaskTotalArray = new JLabel[10];


    /**
     * constructor. Aquí simplement iniciem els elements necessaris.
     */
    public Top10View() {
        //Posem l'action command del botó
        jbActulize.setActionCommand("Carregar");

        //Iniciem els noms, i nombre de tasques a "-", valor buit d'arrencada
        for(int i = 0; i < jlNomUserArray.length; i++){
            JLabel jl = new JLabel("-");
            jlNomUserArray[i] = jl;
        }
        for(int i = 0; i < jlNumTaskPendentsArray.length; i++){
            JLabel jl = new JLabel("-");
            jlNumTaskPendentsArray[i] = jl;
        }
        for(int i = 0; i < jlNumTaskTotalArray.length; i++){
            JLabel jl = new JLabel("-");
            jlNumTaskTotalArray[i] = jl;
        }

        //iniciem l'array de panells
        for(int i = 0 ; i < jpArray.length; i++){
            jpArray[i] = new JPanel();
        }

        //Creem els panells que contindran la secció lateral i la principal
        JPanel jpEast = new JPanel();
        jpCenter = new JPanel();

        //Definim el layout del JPanel que conté tot
        jpSuperTop.setLayout(new BoxLayout(jpSuperTop, BoxLayout.PAGE_AXIS));

        //Definim el layout del panell central
        jpCenter.setLayout(new GridLayout(10,1));

        //Afegim els panells al centre del top10
        createTop10Items();

        JScrollPane jspCenter = new JScrollPane(jpCenter);
        jspCenter.setPreferredSize(new Dimension(300,475));
        jspCenter.setBorder(BorderFactory.createTitledBorder("Top 10"));

        //Afegim el botó al lateral
        jpEast.add(jbActulize);

        //Afegim els panells al panell general
        jpSuperTop.add(jpEast);
        jpSuperTop.add(jspCenter);

        //Com la classe hereda de JPanel afegim el panell al JPanel de la classe
        this.add(jpSuperTop);
    }

    /**
     * Aquí creem els 10 "contenidors" on posarem les dades dels 10 usuaris. Tant Panels com els seus Labels.
     */
    private void createTop10Items(){
        for(int i = 0; i < jpArray.length; i++){
            JLabel jlNom = new JLabel("Username");
            jlNom.setFont(new Font("Dialog", Font.BOLD, 12));
            JLabel jlPendents = new JLabel("Pending tasks");
            jlPendents.setFont(new Font("Dialog", Font.BOLD, 12));
            JLabel jlTotals = new JLabel("Total tasks");
            jlTotals.setFont(new Font("Dialog", Font.BOLD, 12));


            JPanel jp = new JPanel();
            jp.setLayout(new BoxLayout(jp, BoxLayout.PAGE_AXIS));
            jp.add(jlNom);
            jp.add(jlNomUserArray[i]);
            jp.add(jlPendents);
            jp.add(jlNumTaskPendentsArray[i]);
            jp.add(jlTotals);
            jp.add(jlNumTaskTotalArray[i]);
            Integer aux = i + 1;
            String title = aux.toString();
            jp.setBorder(BorderFactory.createTitledBorder("Top " + title));
            jpArray[i] = jp;
            jpCenter.add(jpArray[i]);
        }

    }

    /**
     * Vinculem la vista amb el controlador
     * @param top10Controller Controlador
     */
    public void linkController(Top10Controller top10Controller){
        jbActulize.addActionListener(top10Controller);
    }


    /**
     * Procediment que ens actualitza el top 10 amb els usuaris que rebem.
     * @param top10people Top 10 d'usuaris que hem de pintar per pantalla.
     */
    public void actualizeTop10(ArrayList<UserRanking> top10people){
        Integer integer;
        if(top10people != null) {
            for (int i = 0; i < top10people.size(); i++) {
                if(top10people.get(i) != null) {
                    if(top10people.get(i).getUsername() != null){
                        jlNomUserArray[i].setText(top10people.get(i).getUsername());
                        integer = top10people.get(i).getTotalTasks();
                        jlNumTaskTotalArray[i].setText(integer.toString());
                        integer = top10people.get(i).getPendingTasks();
                        jlNumTaskPendentsArray[i].setText(integer.toString());
                    }
                }
            }
        }
    }
}
