package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class GraphView extends JPanel{

    private static final Stroke LINIAGRAFIC = new BasicStroke(2f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);
    private List<Integer> scores;
    private static final Color COLORLINIAGRAFIC = new Color(255, 38, 38, 200);
    private static final Color COLORDIVISIONS = new Color(44, 102, 230, 100);


    public GraphView(){
        ArrayList<Integer> aux = new ArrayList<>();
        //Al principi deixem els intervals a 0
        aux.add(0);
        this.scores = aux;
    }

    //Aquí és on pintem el gràfic en sí
    @Override
    protected void paintComponent(Graphics g) {
        //Repintem el gràfic anterior, si no cridem 'super' se'ns superposen els gràfics
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //Hem trobat que utilitzant RenderngHints fem que la línia del gràfic quedi més neta. Sinó era esglaonada.
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Definim que el marge pel gràfic sigui de 5 i per les etiquetes de 15
        int marge = 5;
        int margeEtiqueta = 15;
        double distanciaX = ((double) getWidth() - (2 * marge) - margeEtiqueta) / (scores.size() - 1);
        double distanciaY = ((double) getHeight() - 2 * marge - margeEtiqueta) / (getMaxScore() - getMinScore());

        //A partir de Scores ens farem una llista de punts. Per fer això tenim en compte distanciaX i distanciaY perquè quadrin el
        //gràfic i la quadrícula.
        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            int x1 = (int) (i * distanciaX + marge + margeEtiqueta);
            int y1 = (int) ((getMaxScore() - scores.get(i)) * distanciaY + marge);
            graphPoints.add(new Point(x1, y1));
        }

        //Creem els eixos X i Y del gràfic
        g2.drawLine(marge + margeEtiqueta, getHeight() - marge - margeEtiqueta, marge + margeEtiqueta, marge);
        g2.drawLine(marge + margeEtiqueta, getHeight() - marge - margeEtiqueta, getWidth() - marge, getHeight() - marge - margeEtiqueta);

        //Creem les divisions en Y
        //Inicialment posem 10 divisions perquè és un nombre coherent i gràficament queda agradable
        int numDivY = 10;
        for (int i = 0; i < numDivY + 1; i++) {
            int x0 = marge + margeEtiqueta;
            int x1 =  marge + margeEtiqueta;
            int y0 = getHeight() - ((i * (getHeight() - marge * 2 - margeEtiqueta)) / numDivY + marge + margeEtiqueta);
            if (scores.size() > 0) {
                //Dibuixem les divisions de Y que formen la graella
                g2.setColor(COLORDIVISIONS);
                g2.drawLine(marge + margeEtiqueta + 1 , y0, getWidth() - marge, y0);
                //col·loquem les etiquetes amb els valors de l'eix Y
                g2.setColor(Color.BLACK);
                String etiquetaY = ((int) ((getMinScore() + (getMaxScore() - getMinScore()) * ((i * 1.0) / numDivY)) * 100)) / 100.0 + "";
                int ampladaEtiqueta = g2.getFontMetrics().stringWidth(etiquetaY);
                g2.drawString(etiquetaY, x0 - ampladaEtiqueta - 5, y0 + (g2.getFontMetrics().getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y0);
        }

        //Creem les divisions per X
        for (int i = 0; i < scores.size(); i++) {
            if (scores.size() > 1) {
                //Dibuixem les divisions de X que formen la graella
                int x0 = i * (getWidth() - marge * 2 - margeEtiqueta) / (scores.size() - 1) + marge + margeEtiqueta;
                int y0 = getHeight() - marge - margeEtiqueta;
                if ((i % ((int) ((scores.size() / 20.0)) + 1)) == 0) {
                    g2.setColor(COLORDIVISIONS);
                    g2.drawLine(x0, getHeight() - marge - margeEtiqueta - 1, x0, marge);
                    //col·loquem les etiquetes amb els valors de l'eix X
                    g2.setColor(Color.BLACK);
                    String etiquetaX = i + "";
                    int ampladaEtiqueta = g2.getFontMetrics().stringWidth(etiquetaX);
                    g2.drawString(etiquetaX, x0 - ampladaEtiqueta / 2, y0 + g2.getFontMetrics().getHeight() + 3);
                }
                g2.drawLine(x0, y0, x0, y0);
            }
        }

        //Dibuixem la línia del gràfic (la que ens dóna informació)
        g2.setColor(COLORLINIAGRAFIC);
        g2.setStroke(LINIAGRAFIC);
        for (int i = 0; i < graphPoints.size()-1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        int width = 600;
        int heigth = 500;
        return new Dimension(width, heigth);
    }

    //Funció que ens retorna el mínim valor dins de scores
    private double getMinScore() {
        double minScore = Double.MAX_VALUE;
        for (Integer score : scores) {
            minScore = Math.min(minScore, score);
        }
        return minScore;
    }

    //Funció que ens retorna el màxim valor dins de scores
    public double getMaxScore() {
        double maxScore = Double.MIN_VALUE;
        for (Integer score : scores) {
            maxScore = Math.max(maxScore, score);
        }
        return maxScore;
    }

    public void setScores(ArrayList<Integer> scores) {
        this.scores = scores;
        invalidate();
        this.repaint();
    }


}
