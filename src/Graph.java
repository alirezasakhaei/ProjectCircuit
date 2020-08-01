import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.util.ArrayList;

public class Graph extends JPanel {
    static double maxTime;
    double dt, maxAmount;
    double[] fixedArray,fixedArray1;
    int[] finalArray,finalArray1;
    boolean isTwoElement;
    ArrayList<Double> protoArray,protoArray1;
    Graph (double dt, double maxAmount, ArrayList<Double> protoArray, ArrayList<Double> protoArray1){
        setSize(500,501);
        this.maxAmount = maxAmount;
        this.dt = dt;
        this.protoArray = protoArray;
        this.protoArray1 = protoArray1;
        isTwoElement = true;
        setFixedArray();
        setFinalArray();

        setFixedArray1();
        setFinalArray1();
    }

    public static double getMaxTime() {
        return maxTime;
    }

    public static void setMaxTime(double maxTimeIn) {
        maxTime = maxTimeIn;
    }

    Graph (double dt, double maxAmount, ArrayList<Double> protoArray){
        setSize(500,501);
        this.maxAmount = maxAmount;
        this.dt = dt;
        this.protoArray = protoArray;
        isTwoElement = false;
        setFixedArray();
        setFinalArray();
    }
    private void setFixedArray(){
        fixedArray = new double[500];
        double stepTime = maxTime/500;
        int stepDt = (int) (stepTime/dt);
        for (int i =0;i<500;i++){
            fixedArray[i] = protoArray.get(i*stepDt);
        }
    }

    private void setFinalArray(){
        finalArray = new int[500];
        for (int i=0;i<500;i++){
            finalArray[i] = (int)(240*(fixedArray[i]/maxAmount));
        }
    }

    private void setFixedArray1(){
        fixedArray1 = new double[500];
        double stepTime = maxTime/500;
        int stepDt = (int) (stepTime/dt);
        for (int i =0;i<500;i++){
            fixedArray1[i] = protoArray1.get(i*stepDt);
        }
    }
    private void setFinalArray1(){
        finalArray1 = new int[500];
        for (int i=0;i<500;i++){
            finalArray1[i] = (int)(240*(fixedArray1[i]/maxAmount));
        }
    }








    @Override
    public void paint(Graphics g) {
        g.drawLine(0,250,500,250);
        g.drawLine(0,0,0,500);
        Graphics2D g2d = (Graphics2D) g;


        for (int i =0;i<499;i++){
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(i,250 - finalArray[i],i+1,250 - finalArray[i+1]);
        }
        if (isTwoElement){
            g2d.setColor(Color.RED);
            for (int i =0;i<499;i++){
                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine(i,250 - finalArray1[i],i+1,250 - finalArray1[i+1]);
            }
        }
        g2d.setColor(Color.black);

        float[] dashingPattern1 = {2f, 2f};
        Stroke stroke1 = new BasicStroke(2f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);
        g2d.setStroke(stroke1);

        g2d.drawLine(0,10,500,10);
        g2d.drawLine(0,70,500,70);
        g2d.drawLine(0,130,500,130);
        g2d.drawLine(0,190,500,190);
        g2d.drawLine(0,310,500,310);
        g2d.drawLine(0,370,500,370);
        g2d.drawLine(0,430,500,430);
        g2d.drawLine(0,490,500,490);



        g2d.drawLine(70,10,70,490);
        g2d.drawLine(130,10,130,490);
        g2d.drawLine(190,10,190,490);
        g2d.drawLine(250,10,250,490);
        g2d.drawLine(310,10,310,490);
        g2d.drawLine(370,10,370,490);
        g2d.drawLine(430,10,430,490);


    }
}
