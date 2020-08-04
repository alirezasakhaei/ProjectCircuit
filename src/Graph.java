import javax.swing.*;
import java.awt.Graphics;
import java.awt.*;
import java.util.ArrayList;

public class Graph extends JPanel {
    final int elementsNumber;
    static double maxTime;
    final double dt;
    final double maxAmount;
    final int[][] finalArrays;
    final Color[] colors = {Color.red, Color.green, Color.yellow, Color.pink, Color.ORANGE, Color.magenta, Color.cyan, Color.darkGray, Color.lightGray, Color.gray};
    ArrayList<Double> protoArray, protoArray1;

    Graph(double dt, double maxAmount, Element[] chosenElements, int elementsNumber, char graphType) {
        setSize(500, 501);
        this.elementsNumber = elementsNumber;
        this.maxAmount = maxAmount;
        this.dt = dt;
        finalArrays = new int[10][];
        double[] fixedArray;
        int[] finalArray;
        for (int i = 0; i < elementsNumber; i++) {
            fixedArray = new double[500];
            double stepTime = maxTime / 500;
            int stepDt = (int) (stepTime / dt);
            switch (graphType) {
                case 'V':
                    for (int j = 0; j < 500; j++) {
                        double sum = 0;
                        for (int k = 0; k < stepDt; k++) {
                            sum += chosenElements[i].getVoltagesArray().get(j * stepDt + k);
                        }
                        System.out.println(sum + " " + stepDt + " " + (sum / stepDt));
                        fixedArray[j] = sum / stepDt;

                    }
                    break;
                case 'A':
                    for (int j = 0; j < 500; j++) {
                        double sum = 0;
                        for (int k = 0; k < stepDt; k++) {
                            sum += chosenElements[i].getCurrentsArray().get(j * stepDt + k);
                        }
                        fixedArray[j] = sum / stepDt;
                    }
                    break;
                case 'W':
                    for (int j = 0; j < 500; j++) {
                        double sum = 0;
                        for (int k = 0; k < stepDt; k++) {
                            sum += chosenElements[i].getPowersArray().get(j * stepDt + k);
                        }
                        fixedArray[j] = sum / stepDt;
                    }
                    break;
            }
            finalArray = new int[500];
            for (int j = 0; j < 500; j++) {
                finalArray[j] = (int) (240 * (fixedArray[j] / maxAmount));
            }
            finalArrays[i] = finalArray;
        }

    }

    public static double getMaxTime() {
        return maxTime;
    }

    public static void setMaxTime(double maxTimeIn) {
        maxTime = maxTimeIn;
    }


    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        g.drawLine(0, 250, 500, 250);
        g.drawLine(0, 0, 0, 500);

        for (int i = 0; i <elementsNumber; i++) {
            g2d.setColor(colors[i]);
            for (int j = 0; j < 499; j++) {
                g2d.drawLine(j, 250 - finalArrays[i][j], j + 1, 250 - finalArrays[i][j + 1]);
            }
        }

        // dashed lines
        g2d.setColor(Color.black);

        float[] dashingPattern1 = {2f, 2f};
        Stroke stroke1 = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);
        g2d.setStroke(stroke1);

        g2d.drawLine(0, 10, 500, 10);
        g2d.drawLine(0, 70, 500, 70);
        g2d.drawLine(0, 130, 500, 130);
        g2d.drawLine(0, 190, 500, 190);
        g2d.drawLine(0, 310, 500, 310);
        g2d.drawLine(0, 370, 500, 370);
        g2d.drawLine(0, 430, 500, 430);
        g2d.drawLine(0, 490, 500, 490);


        g2d.drawLine(70, 10, 70, 490);
        g2d.drawLine(130, 10, 130, 490);
        g2d.drawLine(190, 10, 190, 490);
        g2d.drawLine(250, 10, 250, 490);
        g2d.drawLine(310, 10, 310, 490);
        g2d.drawLine(370, 10, 370, 490);
        g2d.drawLine(430, 10, 430, 490);


    }
}
